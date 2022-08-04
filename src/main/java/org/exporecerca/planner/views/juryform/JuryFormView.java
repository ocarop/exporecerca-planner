package org.exporecerca.planner.views.juryform;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.security.PermitAll;

import org.exporecerca.planner.data.entity.Jury;
import org.exporecerca.planner.data.entity.Topic;
import org.exporecerca.planner.data.service.JuryService;
import org.exporecerca.planner.data.service.TopicService;
import org.exporecerca.planner.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.CheckBoxGroupProvider;

@PageTitle("Jury Form")
@Route(value = "jury-form/:samplePersonID?/:action?(edit)", layout = MainLayout.class)
@RouteAlias(value = "jury", layout = MainLayout.class)
@Uses(Icon.class)
@PermitAll
public class JuryFormView extends Div implements BeforeEnterObserver {


 
    @Autowired
    public JuryFormView(JuryService juryService,TopicService topicService) {
        // crud instance
        GridCrud<Jury> crud = new GridCrud<>(Jury.class);

        // grid configuration
        crud.getGrid().setColumns ("firstName", "lastName","email");
        crud.getGrid().setColumnReorderingAllowed(true);

        // form configuration
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties(
                "firstName", "lastName", "email", "phone", "topics");
        crud.getCrudFormFactory().setFieldProvider("topics",
                new CheckBoxGroupProvider<>("Groups",topicService.findAll(),Topic::getName));
 
        // layout configuration
        setSizeFull();
        
		crud.getCrudFormFactory().setUseBeanValidation(true);
		crud.setFindAllOperation(() -> juryService.findAll());
		crud.setAddOperation(juryService::update);
		crud.setUpdateOperation(juryService::update);
		crud.setDeleteOperation(juryService::delete);	
        add(crud);
//        crud.setFindAllOperationVisible(false);

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
    	/*
    	Optional<UUID> samplePersonId = event.getRouteParameters().get(SAMPLEPERSON_ID).map(UUID::fromString);
        if (samplePersonId.isPresent()) {
            Optional<Jury> samplePersonFromBackend = samplePersonService.get(samplePersonId.get());
            if (samplePersonFromBackend.isPresent()) {
                populateForm(samplePersonFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested samplePerson was not found, ID = %s", samplePersonId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(JuryFormView.class);
            }
        }
        */
    }

}
