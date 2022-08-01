package org.exporecerca.planner.data.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.exporecerca.planner.data.entity.Jury;
import org.exporecerca.planner.data.entity.Timeslot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TimeslotService {

    private final TimeslotRepository repository;

    @Autowired
    public TimeslotService(TimeslotRepository repository) {
        this.repository = repository;
    }

    public Optional<Timeslot> get(UUID id) {
        return repository.findById(id);
    }

    public Timeslot update(Timeslot entity) {
        return repository.save(entity);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
    
    public void delete(Timeslot timeslot) {
        repository.deleteById(timeslot.getId());
    }

    public Page<Timeslot> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

	public  List<Timeslot> findAll() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

}
