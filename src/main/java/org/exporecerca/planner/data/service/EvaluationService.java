package org.exporecerca.planner.data.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.exporecerca.planner.data.entity.Evaluation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.dataprovider.EntryQuery;

@Service
public class EvaluationService {

    private final EvaluationRepository repository;

    @Autowired
    public EvaluationService(EvaluationRepository repository) {
        this.repository = repository;
    }

    public Optional<Evaluation> get(Integer id) {
        return repository.findById(id);
    }

    public Evaluation update(Evaluation entity) {
        return repository.save(entity);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public Page<Evaluation> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }
    public void delete(Evaluation evaluation) {
        repository.delete(evaluation);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
    
	public Evaluation save(Evaluation evaluation) {
		return repository.save(evaluation);
	}
	
    public  List<Evaluation> findAll() {
        return repository.findAll();
    }

	public Stream<Entry> streamEntries(EntryQuery query) {
		// TODO apply query filter
		return findAll().stream().map(e->toEntry(e));
	}

	private Entry toEntry(Evaluation evaluation) {
		Entry entry = new Entry();
		entry.setStart(evaluation.getTimeslot().getStartTime());
		entry.setEnd(evaluation.getTimeslot().getEndTime());
		if (evaluation.getJury()!=null)
			entry.setTitle(evaluation.getContestant().getCode() + "\\" + evaluation.getJury().getLastName());
		else
			entry.setTitle("not assigned");
		return entry;
	}

	public Entry getEntry(String entryId) {
		Optional<Evaluation> evaluation = get(Integer.parseInt(entryId));
		if (evaluation.isEmpty())return null;
		else return toEntry(evaluation.get());
	}


   
}
