package org.exporecerca.planner.data.service;

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

    public Optional<Contestant> get(UUID id) {
        return repository.findById(id);
    }

    public Contestant update(Contestant entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Contestant> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }


}
