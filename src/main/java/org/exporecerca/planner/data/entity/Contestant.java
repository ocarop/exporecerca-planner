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
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Contestant extends AbstractEntity {

	@Column(nullable=false, length=3)
	@NotNull
    private String code;

	@Column(nullable=false, length=1000)
	@NotNull
	@Size(min=2,max=1000,message="Size must be between 2 an 1000 characters")
    private String title;
	
	
	@Column(nullable=true, length=500)
    private String names;

	@Column(nullable=true, length=500)
	private String center;
	
	@ManyToOne
	private Topic topic;

	@Override
	public String toString() {
		return  code ;
	}


}
