package org.exporecerca.planner.data.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Timeslot extends AbstractEntity {

	@Column(nullable=false)
	@NotNull
    private LocalDateTime startTime;
	
	@NotNull
	@Column(nullable=false)
	private LocalDateTime endTime;

	@Override
	public String toString() {
		if (startTime.getDayOfMonth()==endTime.getDayOfMonth() && startTime.getMonth()==endTime.getMonth())
			//same day
			return startTime.format(DateTimeFormatter.ofPattern("dd-MM")) + " from " + startTime.format(DateTimeFormatter.ofPattern("hh:mm")) + " to " + endTime.format(DateTimeFormatter.ofPattern("hh:mm"));
		else	
			return "From " + startTime.format(DateTimeFormatter.ofPattern("dd-MM hh:mm")) + " to " + endTime.format(DateTimeFormatter.ofPattern("dd-MM hh:mm"));
	}
    


}
