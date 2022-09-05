package org.exporecerca.planner.views.contestantform;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.security.PermitAll;

import org.exporecerca.planner.data.service.ContestantService;
import org.exporecerca.planner.data.service.JuryService;
import org.exporecerca.planner.data.service.TopicService;
import org.exporecerca.planner.excel.ExcelService;
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
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

@PageTitle("Contestant Form")
@Route(value = "contestant-form/:samplePersonID?/:action?(edit)", layout = MainLayout.class)
//@PermitAll
@AnonymousAllowed
public class ContestantFormView extends Div implements BeforeEnterObserver {

    private final String SAMPLEPERSON_ID = "samplePersonID";
    private final String SAMPLEPERSON_EDIT_ROUTE_TEMPLATE = "contestant-form/%s/edit";

    private Grid<Jury> grid = new Grid<>(Jury.class, false);

    private TextField code;
    private TextField title;
    private TextField center;
    private TextField names;
    
    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Jury> binder;

    private Jury samplePerson;

    
    @Autowired
    public ContestantFormView(ContestantService contestantService,TopicService topicService, ExcelService excelService) {
        // crud instance
        GridCrud<Contestant> crud = new GridCrud<>(Contestant.class);

        // grid configuration
        crud.getGrid().setColumns ("code", "title","center","names","topic");
        crud.getGrid().setColumnReorderingAllowed(true);

        // form configuration
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties(
                "code", "title", "topic");
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
		MemoryBuffer memoryBuffer = new MemoryBuffer();
		Upload singleFileUpload = new Upload(memoryBuffer);

		singleFileUpload.addSucceededListener(event -> {
			// Get information about the uploaded file
			InputStream fileData = memoryBuffer.getInputStream();
			String fileName = event.getFileName();
			long contentLength = event.getContentLength();
			String mimeType = event.getMIMEType();

			String log = excelService.importContestants(fileData, contestantService,topicService);
			if (log.equals("")) {
				Notification.show("Juries imported succesfully", 10000, Position.TOP_CENTER);
				crud.refreshGrid();
			} else
				Notification.show(log, 10000, Position.TOP_CENTER);
		});
		add(singleFileUpload);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
    }

 }
