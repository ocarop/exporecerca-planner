import java.util.Optional;

import org.exporecerca.planner.Application;
import org.exporecerca.planner.data.entity.Topic;
import org.exporecerca.planner.data.service.TopicRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ExporecercaPlannerTest {

	@Autowired 
	TopicRepository topicRepository;
	
	@Test
	public void givenTopicRepository_whenSaveAndRetreiveEntity_thenOK() {
		Topic newTopic = topicRepository.save(new Topic("Sciences")); 
		Optional<Topic> foundTopic = topicRepository.findById(newTopic.getId());
        assertNotNull(foundTopic.get());
	}
}
