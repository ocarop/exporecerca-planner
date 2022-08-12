package org.exporecerca.planner.data.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
@Entity
public class Evaluation extends AbstractEntity {

	
	
	@PlanningVariable(valueRangeProviderRefs = "juryRange",nullable=true)
	@ManyToOne
	private Jury jury;
	
	@ManyToOne
	private Contestant contestant;
	
	@ManyToOne
	private Timeslot timeslot;

    // No-arg constructor required for Hibernate and OptaPlanner
    public Evaluation() {
    }
    
    @Override
    @PlanningId
	public Integer getId() {
		return super.getId();
	}

    
	public Evaluation(Integer id, Contestant contestant, Timeslot timeslot) {
		this.setId(id);
		this.contestant=contestant;
		this.timeslot=timeslot;
	}
	public Jury getJury() {
		return jury;
	}
	public void setJury(Jury jury) {
		this.jury = jury;
	}
	
	public Contestant getContestant() {
		return contestant;
	}
	public void setContestant(Contestant contestant) {
		this.contestant = contestant;
	}
	
	public Timeslot getTimeslot() {
		return timeslot;
	}
	public void setTimeslot(Timeslot timeslot) {
		this.timeslot = timeslot;
	}
	
	public  boolean isUnassigned(){
		return jury==null;
	}
}
