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
public class JuryService {

    private final JuryRepository repository;

    @Autowired
    public JuryService(JuryRepository repository) {
        this.repository = repository;
    }

    public Optional<Jury> get(Integer id) {
        return repository.findById(id);
    }

    public Jury update(Jury entity) {
        return repository.save(entity);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public Page<Jury> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }
    public void delete(Jury jury) {
        repository.delete(jury);
    }
   
    
    public  List<Jury> findAll() {
        return repository.findAll();
    }
   
}
