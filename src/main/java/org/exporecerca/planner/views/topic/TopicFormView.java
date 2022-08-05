package org.exporecerca.planner.views.topic;

import javax.annotation.security.RolesAllowed;

import org.exporecerca.planner.data.entity.Timeslot;
import org.exporecerca.planner.data.entity.Topic;
import org.exporecerca.planner.data.service.TimeslotService;
import org.exporecerca.planner.data.service.TopicService;
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

@PageTitle("Topic")
@Route(value = "topic/:topicID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@Uses(Icon.class)
public class TopicFormView extends Div implements BeforeEnterObserver{
	@Autowired
	public TopicFormView(TopicService topicService) {
		super();
		GridCrud<Topic> crud = new GridCrud<>(Topic.class);
		

        // form configuration
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties("name");
        
		crud.getCrudFormFactory().setUseBeanValidation(true);
		crud.setFindAllOperation(() -> topicService.findAll());
		crud.setAddOperation(topicService::update);
		crud.setUpdateOperation(topicService::update);
		crud.setDeleteOperation(topicService::delete);		
		add(crud);
		}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		// TODO Auto-generated method stub

	}
}
