package org.exporecerca.planner;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.exporecerca.planner.Application;
import org.exporecerca.planner.data.entity.Topic;
import org.exporecerca.planner.data.service.TopicRepository;
import org.exporecerca.planner.excel.ExcelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ExporecercaPlannerTest {

	@Autowired 
	TopicRepository topicRepository;
	
	
	@Test
	public void givenTopicRepository_whenSaveAndRetreiveEntity_thenOK() {
		Topic newTopic = topicRepository.save(new Topic("Sciences")); 
		Optional<Topic> foundTopic = topicRepository.findById(newTopic.getId());
        assertNotNull(foundTopic.get());
	}

	/*
    @Test
    public void exportToExcel_readFromExcel_thenOK() throws IOException {
    	ByteArrayOutputStream output = new ByteArrayOutputStream();
    	excelGenerator.generate(output,topicRepository.findAll());
    	String stringOutput=output.toString();
    	System.out.println(stringOutput);
    }
    */
}
