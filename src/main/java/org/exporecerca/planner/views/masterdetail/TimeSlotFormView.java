package org.exporecerca.planner.views.masterdetail;

import javax.annotation.security.RolesAllowed;

import org.exporecerca.planner.data.entity.Timeslot;
import org.exporecerca.planner.data.service.TimeslotService;
import org.exporecerca.planner.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.crudui.crud.impl.GridCrud;

import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Time slots")
@Route(value = "timeslot/:stimeslotID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@Uses(Icon.class)
public class TimeSlotFormView extends Div implements BeforeEnterObserver{
	@Autowired
	public TimeSlotFormView(TimeslotService timeSlotService) {
		super();
		GridCrud<Timeslot> crud = new GridCrud<>(Timeslot.class);
		crud.getCrudFormFactory().setUseBeanValidation(true);
		crud.setFindAllOperation(() -> timeSlotService.findAll());
		crud.setAddOperation(timeSlotService::update);
		crud.setUpdateOperation(timeSlotService::update);
		crud.setDeleteOperation(timeSlotService::delete);		
		add(crud);
		}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		// TODO Auto-generated method stub

	}
}
