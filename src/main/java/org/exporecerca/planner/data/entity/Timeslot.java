package org.exporecerca.planner.data.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Timeslot extends AbstractEntity {

	@Column(nullable=false)
	@NotNull
    private LocalDateTime startTime;
	
	@NotNull
	@Column(nullable=false)
	private LocalDateTime endTime;
    
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

}
