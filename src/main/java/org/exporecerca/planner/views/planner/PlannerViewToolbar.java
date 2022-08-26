package org.exporecerca.planner.views.planner;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.select.Select;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.stefan.fullcalendar.*;
import org.vaadin.stefan.fullcalendar.dataprovider.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Stefan Uebe
 */
public class PlannerViewToolbar extends MenuBar {
	public static final List<Timezone> SOME_TIMEZONES = Arrays.asList(Timezone.UTC,
			new Timezone(ZoneId.of("Europe/Berlin")), new Timezone(ZoneId.of("America/Los_Angeles")),
			new Timezone(ZoneId.of("Japan")));

	private final FullCalendar calendar;
	private final boolean allTimezones;
	private final boolean allLocales;
	private final boolean editable;
	private final boolean viewChangeable;
	private final boolean dateChangeable;

	private CalendarView selectedView = CalendarViewImpl.DAY_GRID_MONTH;
	private Button buttonDatePicker;
	private MenuItem viewSelector;
	private Select<Timezone> timezoneSelector;
	private HasComponents calendarParent;

	public PlannerViewToolbar(FullCalendar calendar, boolean allTimezones, boolean allLocales, boolean editable,
			boolean viewChangeable, boolean dateChangeable) {

		this.calendar = calendar;
		this.allTimezones = allTimezones;
		this.allLocales = allLocales;
		this.editable = editable;
		this.viewChangeable = viewChangeable;
		this.dateChangeable = dateChangeable;

		addThemeVariants(MenuBarVariant.LUMO_SMALL);

		initMenuBar();
	}

	protected void initMenuBar() {
		if (dateChangeable) {
			initDateItems();
		}

		if (viewChangeable) {
			initViewSelector();
		}

		if (editable) {
			initEditItems();
		}


	}

	private void initDateItems() {
		addItem(VaadinIcon.ANGLE_LEFT.create(), e -> calendar.previous());

		// simulate the date picker light that we can use in polymer
		DatePicker gotoDate = new DatePicker();
		gotoDate.addValueChangeListener(event1 -> calendar.gotoDate(event1.getValue()));
		gotoDate.getElement().getStyle().set("visibility", "hidden");
		gotoDate.getElement().getStyle().set("position", "fixed");
		gotoDate.setWidth("0px");
		gotoDate.setHeight("0px");
		gotoDate.setWeekNumbersVisible(true);
		buttonDatePicker = new Button(VaadinIcon.CALENDAR.create());
		buttonDatePicker.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
		buttonDatePicker.getElement().appendChild(gotoDate.getElement());
		buttonDatePicker.addClickListener(event -> gotoDate.open());
		buttonDatePicker.setWidthFull();
		addItem(buttonDatePicker);
		addItem(VaadinIcon.ANGLE_RIGHT.create(), e -> calendar.next());
		addItem("Today", e -> calendar.today());
	}

	protected SubMenu initEditItems() {
		SubMenu calendarItems = addItem("Entries").getSubMenu();

		MenuItem addSingleItem;
		addSingleItem = calendarItems.addItem("Add single entry", event -> {
			event.getSource().setEnabled(false);
			Entry entry = new Entry();
			entry.setStart(LocalDate.now().atTime(10, 0));
			entry.setEnd(LocalDate.now().atTime(11, 0));
			entry.setTitle("Single entry");
			((LazyInMemoryEntryProvider<Entry>) calendar.getEntryProvider()).addEntries(entry);

			Notification.show("Added a single entry for today");
		});

		calendarItems.addItem("Remove all entries", e -> {
			calendar.getEntryProvider().fetchAll().collect(Collectors.toSet());
			if (addSingleItem != null) {
				addSingleItem.setEnabled(true);
			}
			Notification.show(
					"All entries removed. Reload this page to create a new set of samples or use the Add sample entries buttons.");
		});

		return calendarItems;
	}


	private void initViewSelector() {
		List<CalendarView> calendarViews;
		calendarViews = new ArrayList<>(Arrays.asList(CalendarViewImpl.values()));
		// if (calendar instanceof Scheduler) {
		// calendarViews.addAll(Arrays.asList(SchedulerView.values()));
		// }

		calendarViews.sort(Comparator.comparing(CalendarView::getName));

		viewSelector = addItem("View: " + getViewName(selectedView));
		SubMenu subMenu = viewSelector.getSubMenu();
		calendarViews.stream().sorted(Comparator.comparing(this::getViewName)).forEach(view -> {
			String viewName = getViewName(view);
			subMenu.addItem(viewName, event -> {
				calendar.changeView(view);
				viewSelector.setText("View: " + viewName);
				selectedView = view;
			});
		});
	}

	private String getViewName(CalendarView view) {
		String name = null /* customViewNames.get(view) */;
		if (name == null) {
			name = StringUtils
					.capitalize(String.join(" ", StringUtils.splitByCharacterTypeCamelCase(view.getClientSideValue())));
		}

		return name;
	}

	public void updateInterval(LocalDate intervalStart) {
		if (buttonDatePicker != null && selectedView != null) {
			updateIntervalLabel(buttonDatePicker, selectedView, intervalStart);
		}
	}

	void updateIntervalLabel(HasText intervalLabel, CalendarView view, LocalDate intervalStart) {
		String text = "--";
		Locale locale = calendar.getLocale();

		if (view instanceof CalendarViewImpl) {
			switch ((CalendarViewImpl) view) {
			default:
			case DAY_GRID_MONTH:
			case LIST_MONTH:
				text = intervalStart.format(DateTimeFormatter.ofPattern("MMMM yyyy").withLocale(locale));
				break;
			case TIME_GRID_DAY:
			case DAY_GRID_DAY:
			case LIST_DAY:
				text = intervalStart.format(DateTimeFormatter.ofPattern("dd.MM.yyyy").withLocale(locale));
				break;
			case TIME_GRID_WEEK:
			case DAY_GRID_WEEK:
			case LIST_WEEK:
				text = intervalStart.format(DateTimeFormatter.ofPattern("dd.MM.yy").withLocale(locale)) + " - "
						+ intervalStart.plusDays(6).format(DateTimeFormatter.ofPattern("dd.MM.yy").withLocale(locale))
						+ " (cw " + intervalStart.format(DateTimeFormatter.ofPattern("ww").withLocale(locale)) + ")";
				break;
			case LIST_YEAR:
				text = intervalStart.format(DateTimeFormatter.ofPattern("yyyy").withLocale(locale));
				break;
			}
		} else if (view instanceof SchedulerView) {
			switch ((SchedulerView) view) {
			case TIMELINE_DAY:
			case RESOURCE_TIMELINE_DAY:
			case RESOURCE_TIME_GRID_DAY:
				text = intervalStart.format(DateTimeFormatter.ofPattern("dd.MM.yyyy").withLocale(locale));
				break;
			case TIMELINE_WEEK:
			case RESOURCE_TIMELINE_WEEK:
			case RESOURCE_TIME_GRID_WEEK:
				text = intervalStart.format(DateTimeFormatter.ofPattern("dd.MM.yy").withLocale(locale)) + " - "
						+ intervalStart.plusDays(6).format(DateTimeFormatter.ofPattern("dd.MM.yy").withLocale(locale))
						+ " (cw " + intervalStart.format(DateTimeFormatter.ofPattern("ww").withLocale(locale)) + ")";
				break;
			case TIMELINE_MONTH:
			case RESOURCE_TIMELINE_MONTH:
				text = intervalStart.format(DateTimeFormatter.ofPattern("MMMM yyyy").withLocale(locale));
				break;
			case TIMELINE_YEAR:
			case RESOURCE_TIMELINE_YEAR:
				text = intervalStart.format(DateTimeFormatter.ofPattern("yyyy").withLocale(locale));
				break;
			}
		} else {
			String pattern = view != null && view.getDateTimeFormatPattern() != null ? view.getDateTimeFormatPattern()
					: "MMMM yyyy";
			text = intervalStart.format(DateTimeFormatter.ofPattern(pattern).withLocale(locale));

		}

		intervalLabel.setText(text);
	}

	/**
	 * Sets the timezone in the timezone selector. May lead to client side updates.
	 * 
	 * @param timezone timezone
	 */
	public void setTimezone(Timezone timezone) {
		if (timezoneSelector != null) {
			timezoneSelector.setValue(timezone);
		}
	}
}
