package org.exporecerca.planner.data.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
@Entity
public class Evaluation extends AbstractEntity {

	@PlanningVariable
	@ManyToOne
	private Jury jury;
	
	@PlanningVariable
	@ManyToOne
	private Contestant contestant;
	
	@ManyToOne
	private Timeslot timeslot;

	
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
	
	
}
