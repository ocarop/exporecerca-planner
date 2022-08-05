package org.exporecerca.planner.data.entity;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Timeslot extends AbstractEntity {

	@Column(nullable=false)
	@NotNull
    private LocalTime startTime;
	
	@NotNull
	@Column(nullable=false)
	private LocalTime endTime;
    
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	public LocalTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

}
