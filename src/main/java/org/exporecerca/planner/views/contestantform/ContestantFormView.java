package org.exporecerca.planner.views.contestantform;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.security.PermitAll;

import org.exporecerca.planner.data.service.ContestantService;
import org.exporecerca.planner.data.service.JuryService;
import org.exporecerca.planner.data.service.TopicService;
import org.exporecerca.planner.data.entity.Contestant;
import org.exporecerca.planner.data.entity.Jury;
import org.exporecerca.planner.data.entity.Topic;
import org.exporecerca.planner.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.CheckBoxGroupProvider;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;

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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

@PageTitle("Contestant Form")
@Route(value = "contestant-form/:samplePersonID?/:action?(edit)", layout = MainLayout.class)
@PermitAll
public class ContestantFormView extends Div implements BeforeEnterObserver {

    private final String SAMPLEPERSON_ID = "samplePersonID";
    private final String SAMPLEPERSON_EDIT_ROUTE_TEMPLATE = "contestant-form/%s/edit";

    private Grid<Jury> grid = new Grid<>(Jury.class, false);

    private TextField firstName;
    private TextField lastName;
    private TextField email;
    private TextField phone;
    private DatePicker dateOfBirth;
    private TextField occupation;
    private Checkbox important;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Jury> binder;

    private Jury samplePerson;

    
    @Autowired
    public ContestantFormView(ContestantService contestantService,TopicService topicService) {
        // crud instance
        GridCrud<Contestant> crud = new GridCrud<>(Contestant.class);

        // grid configuration
        crud.getGrid().setColumns ("firstName", "lastName","email");
        crud.getGrid().setColumnReorderingAllowed(true);

        // form configuration
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties(
                "firstName", "lastName", "email", "phone", "topic");
        crud.getCrudFormFactory().setFieldProvider("topic",
                new ComboBoxProvider<Topic>("Topic", topicService.findAll()));
 
        // layout configuration
        setSizeFull();
        
		crud.getCrudFormFactory().setUseBeanValidation(true);
		crud.setFindAllOperation(() -> contestantService.findAll());
		crud.setAddOperation(contestantService::update);
		crud.setUpdateOperation(contestantService::update);
		crud.setDeleteOperation(contestantService::delete);	
        add(crud);

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
    }

 }
