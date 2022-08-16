package org.exporecerca.planner.views.about;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.exporecerca.planner.data.entity.Contestant;
import org.exporecerca.planner.data.entity.Evaluation;
import org.exporecerca.planner.data.entity.TimeTable;
import org.exporecerca.planner.data.entity.Timeslot;
import org.exporecerca.planner.data.service.ContestantService;
import org.exporecerca.planner.data.service.JuryService;
import org.exporecerca.planner.data.service.TimeslotService;
import org.exporecerca.planner.solver.TimeTableConstraintProvider;
import org.exporecerca.planner.views.MainLayout;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.vaadin.stefan.fullcalendar.CalendarView;
import org.vaadin.stefan.fullcalendar.CalendarViewImpl;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import org.vaadin.stefan.fullcalendar.FullCalendarScheduler;
import org.vaadin.stefan.fullcalendar.SchedulerView;
import org.vaadin.stefan.fullcalendar.dataprovider.EntryProvider;
import org.vaadin.stefan.fullcalendar.dataprovider.LazyInMemoryEntryProvider;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
@AnonymousAllowed
public class AboutView extends VerticalLayout {

	TimeslotService timeslotService;
	ContestantService contestantService;
	JuryService juryService;
	FullCalendar calendar;

	public AboutView(TimeslotService timeslotService, ContestantService contestantService, JuryService juryService) {
		this.timeslotService = timeslotService;
		this.contestantService = contestantService;
		this.juryService = juryService;

		setSpacing(false);
		Button cmdSolve = new Button("Solve");
		cmdSolve.addClickListener(clickEvent -> {
			// test solver
			SolverFactory<TimeTable> solverFactory = SolverFactory
					.create(new SolverConfig().withSolutionClass(TimeTable.class).withEntityClasses(Evaluation.class)
							.withConstraintProviderClass(TimeTableConstraintProvider.class)
							// The solver runs only for 5 seconds on this small dataset.
							// It's recommended to run for at least 5 minutes ("5m") otherwise.
							.withTerminationSpentLimit(Duration.ofSeconds(15)));

			// Load the problem
			TimeTable problem = generateDemoData();

			// Solve the problem
			Solver<TimeTable> solver = solverFactory.buildSolver();
			TimeTable solution = solver.solve(problem);

			// Visualize the solution
			printTimetable(solution);
		});
		calendar = createCalendar();
		HorizontalLayout menu = new HorizontalLayout();
		menu.setWidthFull();
		add(menu);
		menu.add(cmdSolve);
		add(calendar);
		// this.setFlexGrow(1, calendar);
		calendar.setHeightByParent(); // calculate the height by parent
		calendar.getElement().getStyle().set("flex-grow", "1");


		setSizeFull();
		setJustifyContentMode(JustifyContentMode.CENTER);
		setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
		getStyle().set("text-align", "center");
		;

	}

	protected FullCalendar createCalendar() {

		// Create a new calendar instance and attach it to our layout
		FullCalendar calendar = FullCalendarBuilder.create().build();
		calendar.changeView(CalendarViewImpl.DAY_GRID_WEEK);
		// Create a initial sample entry
		Entry entry = new Entry();
		entry.setTitle("Contestant <br/> Jury <br/>");
		entry.setColor("#ff3333");

		// the given times will be interpreted as utc based - useful when the times are
		// fetched from your database
		entry.setStart(LocalDate.now().withDayOfMonth(3).atTime(10, 0));
		entry.setEnd(entry.getStart().plusHours(2));
		// load items from backend
		List<Entry> entryList = new ArrayList<Entry>();
		// List<Entry> entryList = backend.streamEntries().collect(Collectors.toList());

		// init lazy loading provider based on given collection - does NOT use the
		// collection as backend as ListDataProvider does
		LazyInMemoryEntryProvider<Entry> entryProvider = EntryProvider.lazyInMemoryFromItems(entryList);

		// set entry provider
		calendar.setEntryProvider(entryProvider);

		// CRUD operations
		// to add
		entryProvider.addEntries(entry); // register in data provider
		entryProvider.refreshAll(); // call refresh to inform the client about the data change and trigger a refetch

		return calendar;
	}

	public TimeTable generateDemoData() {
		TimeTable timeTable = new TimeTable();

		List<Timeslot> timeslotList = timeslotService.findAll();

		List<org.exporecerca.planner.data.entity.Contestant> contestantList = contestantService.findAll();

		ArrayList<Evaluation> evaluationList = new ArrayList<Evaluation>();

		Integer id = 0;
		for (Timeslot timeslot : timeslotList) {
			for (Contestant contestant : contestantList) {
				evaluationList.add(new Evaluation(id++, contestant, timeslot));
			}
		}
		;

		timeTable.setJuryList(juryService.findAll());
		timeTable.setContestantList(contestantService.findAll());
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
			if (evaluation.getJury()!=null)
				entry.setTitle(evaluation.getContestant().getLastName() + "\\" + evaluation.getJury().getLastName());
			else
				entry.setTitle("not assigned");
			entryList.add(entry);
		}
		// init lazy loading provider based on given collection - does NOT use the
		// collection as backend as ListDataProvider does
		LazyInMemoryEntryProvider<Entry> entryProvider = EntryProvider.lazyInMemoryFromItems(entryList);
		calendar.gotoDate(minTime.toLocalDate());
		// set entry provider
		calendar.setEntryProvider(entryProvider);
		entryProvider.refreshAll();
	}

}
