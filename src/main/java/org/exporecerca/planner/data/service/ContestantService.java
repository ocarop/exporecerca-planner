package org.exporecerca.planner.data.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.exporecerca.planner.data.entity.Contestant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContestantService {

    private final ContestantRepository repository;

    @Autowired
    public ContestantService(ContestantRepository repository) {
        this.repository = repository;
    }

    public Optional<Contestant> get(Integer id) {
        return repository.findById(id);
    }

    public Contestant update(Contestant entity) {
        return repository.save(entity);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public void delete(Contestant contestant) {
        repository.delete(contestant);
    }
    
    public Page<Contestant> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

	public  List<Contestant> findAll() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	public void save(Contestant contestant) {
		repository.save(contestant);
		
	}


}
