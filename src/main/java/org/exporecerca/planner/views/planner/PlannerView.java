package org.exporecerca.planner.views.planner;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.exporecerca.planner.data.entity.Contestant;
import org.exporecerca.planner.data.entity.Evaluation;
import org.exporecerca.planner.data.entity.Jury;
import org.exporecerca.planner.data.entity.TimeTable;
import org.exporecerca.planner.data.entity.Timeslot;
import org.exporecerca.planner.data.service.ContestantService;
import org.exporecerca.planner.data.service.EvaluationService;
import org.exporecerca.planner.data.service.JuryService;
import org.exporecerca.planner.data.service.TimeslotService;
import org.exporecerca.planner.excel.ExcelService;
import org.exporecerca.planner.solver.TimeTableConstraintProvider;
import org.exporecerca.planner.views.MainLayout;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.vaadin.stefan.fullcalendar.CalendarLocale;
import org.vaadin.stefan.fullcalendar.CalendarViewImpl;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import org.vaadin.stefan.fullcalendar.Scheduler;
import org.vaadin.stefan.fullcalendar.Timezone;
import org.vaadin.stefan.fullcalendar.Entry.RenderingMode;
import org.vaadin.stefan.fullcalendar.dataprovider.CallbackEntryProvider;
import org.vaadin.stefan.fullcalendar.dataprovider.EntryProvider;
import org.vaadin.stefan.fullcalendar.dataprovider.LazyInMemoryEntryProvider;

@PageTitle("Planner")
@Route(value = "planner", layout = MainLayout.class)
@AnonymousAllowed
public class PlannerView extends VerticalLayout {

	private TimeslotService timeslotService;
	private ContestantService contestantService;
	private JuryService juryService;
	private FullCalendar calendar;
	private PlannerViewToolbar toolbar;
    private EvaluationService evaluationService;
    private EntryProvider<Entry> entryProvider;
    private TimeTable solution;	

	public PlannerView(TimeslotService timeslotService, ContestantService contestantService, JuryService juryService, EvaluationService evaluationService, ExcelService excelService) {
		this.timeslotService = timeslotService;
		this.contestantService = contestantService;
		this.juryService = juryService;
		this.evaluationService=evaluationService;

		setSpacing(false);
		Button cmdAsignarTimslots = new Button("Asignar Timeslots");
		cmdAsignarTimslots.addClickListener(clickEvent -> {
			List<Jury> juryList = juryService.findAll();
			List<Timeslot> allTimeSlots = timeslotService.findAll();
			juryList.forEach(jury->{
				Set<Timeslot> timeSlotSet = new  HashSet<Timeslot>(allTimeSlots);
				jury.setTimeslots(timeSlotSet);
				juryService.save(jury);
			});
		});
		
		Button cmdExportJuryCalendar = new Button("Export juries calendar");
		cmdExportJuryCalendar.addClickListener(clickEvent -> {
			try {
				excelService.exportJuriesCalendar(solution);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		Button cmdExportContestantCalendar = new Button("Export contestants calendar");
		cmdExportContestantCalendar.addClickListener(clickEvent -> {
			try {
				excelService.exportContestantsCalendar(solution);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		Button cmdSolve = new Button("Solve");
		cmdSolve.addClickListener(clickEvent -> {
			// solver
			SolverFactory<TimeTable> solverFactory = SolverFactory
					.create(new SolverConfig().withSolutionClass(TimeTable.class).withEntityClasses(Evaluation.class)
							.withConstraintProviderClass(TimeTableConstraintProvider.class)
							// The solver runs only for 5 seconds on this small dataset.
							// It's recommended to run for at least 5 minutes ("5m") otherwise.
							.withTerminationSpentLimit(Duration.ofMinutes(5)));

			// Load the problem
			TimeTable problem = generatePlanningSolution();

			// Solve the problem
			Solver<TimeTable> solver = solverFactory.buildSolver();
			solution = solver.solve(problem);

			// Visualize the solution
			printTimetable(solution);
		});
		calendar = createCalendar();
		
        toolbar = new PlannerViewToolbar(calendar,true,true,true,true,true);

        calendar.setHeightByParent();

        calendar.addEntryClickedListener(event -> {
            if (event.getEntry().getRenderingMode() != RenderingMode.BACKGROUND && event.getEntry().getRenderingMode() != RenderingMode.INVERSE_BACKGROUND) {
                DemoDialog dialog = new DemoDialog(event.getEntry(), false);
                dialog.open();
            }
        });

            add(toolbar);
            setHorizontalComponentAlignment(Alignment.CENTER, toolbar);

        setFlexGrow(1, calendar);
        setHorizontalComponentAlignment(Alignment.STRETCH, calendar);
		
		
		
		HorizontalLayout menu = new HorizontalLayout();
		menu.setWidthFull();
		add(menu);
		menu.add(cmdSolve,cmdAsignarTimslots, cmdExportJuryCalendar,cmdExportContestantCalendar);
		add(calendar);
		// this.setFlexGrow(1, calendar);
		calendar.setHeightByParent(); // calculate the height by parent
		calendar.getElement().getStyle().set("flex-grow", "1");


		setSizeFull();
		setJustifyContentMode(JustifyContentMode.CENTER);
		setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
		getStyle().set("text-align", "center");

	}

	protected FullCalendar createCalendar() {

		// Create a new calendar instance and attach it to our layout
		FullCalendar calendar = FullCalendarBuilder.create().withScheduler().build();
		((Scheduler) calendar).setSchedulerLicenseKey("CC-Attribution-NonCommercial-NoDerivatives");
		calendar.changeView(CalendarViewImpl.DAY_GRID_WEEK);
		//Timezone timezoneMadrid=new Timezone(ZoneId.of("Europe/Madrid"));
		//calendar.setTimezone(timezoneMadrid);
		calendar.setLocale(CalendarLocale.SPANISH);

		CallbackEntryProvider<Entry> entryProvider = createEntryProvider();

		// set entry provider
		calendar.setEntryProvider(entryProvider);

		//entryProvider.refreshAll(); // call refresh to inform the client about the data change and trigger a refetch

		return calendar;
	}

    protected CallbackEntryProvider<Entry> createEntryProvider() {
    	//Creates entryprovider from backend. EvaluationService returns filtered enry streams 
        CallbackEntryProvider<Entry> entryProvider = EntryProvider.fromCallbacks(
                query -> evaluationService.streamEntries(query),
                entryId -> evaluationService.getEntry(entryId)
        );

        return entryProvider;
    }
    
	public TimeTable generatePlanningSolution() {
		evaluationService.deleteAll();
		TimeTable timeTable = new TimeTable();

		List<Timeslot> timeslotList = timeslotService.findAllByOrderByStartTime();

		List<org.exporecerca.planner.data.entity.Contestant> contestantList = contestantService.findAll();

		ArrayList<Evaluation> evaluationList = new ArrayList<Evaluation>();

		Integer id = 0;
		for (Timeslot timeslot : timeslotList) {
			for (Contestant contestant : contestantList) {
				Evaluation evaluation = new Evaluation(contestant, timeslot);
				evaluation = evaluationService.save(evaluation);
				evaluationList.add(evaluation);
			}
		};

		timeTable.setJuryList(juryService.findAll());
		timeTable.setContestantList(contestantList);
		timeTable.setTimeslotList(timeslotList);
		timeTable.setEvaluationList(evaluationList);

		return timeTable;
	}

	private void printTimetable(TimeTable timeTable) {
		// load items from backend
		List<Entry> entryList = new ArrayList<Entry>();
		// List<Entry> entryList = backend.streamEntries().collect(Collectors.toList());

		LocalDateTime minTime = null;
		for (Evaluation evaluation : timeTable.getEvaluationList()) {
			Entry entry = new Entry();
			if (minTime == null)
				minTime = evaluation.getTimeslot().getStartTime();
			else if (minTime.isAfter(evaluation.getTimeslot().getStartTime()))
				minTime = evaluation.getTimeslot().getStartTime();
			// the given times will be interpreted as utc based - useful when the times are
			// fetched from your database
			entry.setStart(evaluation.getTimeslot().getStartTime());
			entry.setEnd(evaluation.getTimeslot().getEndTime());
			String color=null;
			color=evaluation.getContestant().getTopic().getColor();
			entry.setColor(color);
			if (evaluation.getJury()!=null)
				entry.setTitle(evaluation.getContestant().getCode() + "\\" + evaluation.getJury().getLastName());
			else 
				entry.setTitle(evaluation.getContestant().getCode() + "\\" +"not assigned");
			//todo: configure show/hide not assigned
			if (evaluation.getJury()!=null)				
				entryList.add(entry);

			
		}
		// init lazy loading provider based on given collection - does NOT use the
		// collection as backend as ListDataProvider does
/*test entry
        Entry entry = new Entry();
        LocalDate now = LocalDate.now();
        entry.setTitle("Meeting 12");
        entry.setStart(now.withDayOfMonth(17).atTime(11, 30));
        entry.setEnd(entry.getStart().plus(30, ChronoUnit.MINUTES));
        entry.setAllDay(false);
        entry.setColor("mediumseagreen");
        entry.setCustomProperty("description", "Description of meeting 12" );
        entry.setEditable(true);
        entryList.add(entry);
        */
        LazyInMemoryEntryProvider<Entry> entryProvider = EntryProvider.lazyInMemoryFromItems(entryList);
		calendar.gotoDate(minTime.toLocalDate());
		// set entry provider
		calendar.setEntryProvider(entryProvider);
		//entryProvider.refreshAll();

	}


}
