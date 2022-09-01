package org.exporecerca.planner.data.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.exporecerca.planner.data.entity.Jury;
import org.exporecerca.planner.data.entity.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TopicService {

    private final TopicRepository repository;

    @Autowired
    public TopicService(TopicRepository repository) {
        this.repository = repository;
    }

    public Optional<Topic> get(Integer id) {
        return repository.findById(id);
    }

    public Topic update(Topic entity) {
        return repository.save(entity);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public void delete(Topic topic) {
        repository.delete(topic);
    }
    
    public Page<Topic> list(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    public  List<Topic> findAll() {
        return repository.findAll();
    }
    
    public int count() {
        return (int) repository.count();
    }

	public void save(Topic topic) {
		repository.save(topic);
		
	}

	public Topic findByName(String topic) {
		return repository.findByName(topic);
	}

}
