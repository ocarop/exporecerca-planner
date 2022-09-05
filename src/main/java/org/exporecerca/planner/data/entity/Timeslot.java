package org.exporecerca.planner.data.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Timeslot extends AbstractEntity {

	@Column(nullable=false)
	@NotNull
    private LocalDateTime startTime;
	
	@NotNull
	@Column(nullable=false)
	private LocalDateTime endTime;
    


}
