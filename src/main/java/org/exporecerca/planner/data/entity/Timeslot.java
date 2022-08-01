package org.exporecerca.planner.data.entity;

import java.time.LocalTime;

import javax.persistence.Entity;

@Entity
public class Timeslot extends AbstractEntity {

    private LocalTime startTime;
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
