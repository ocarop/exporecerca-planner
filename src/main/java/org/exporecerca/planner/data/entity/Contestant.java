package org.exporecerca.planner.data.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.optaplanner.core.api.domain.lookup.PlanningId;

import lombok.Data;

@Entity
@Data
public class Contestant extends AbstractEntity {

	@Column(nullable=false, length=3)
	@NotNull
    private String code;

	@Column(nullable=false, length=250)
	@NotNull
	@Size(min=2,max=250,message="Size must be between 2 an 250 characters")
    private String title;
	
	
	@Column(nullable=false, length=50)
	@Size(min=2,max=500,message="Size must be between 2 an 500 characters")
    private String names;

	@Column(nullable=true, length=100)
	@Size(min=2,max=50,message="Size must be between 2 an 100 characters")
	private String center;
	
	@ManyToOne
	private Topic topic;


}
