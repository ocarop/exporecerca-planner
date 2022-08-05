package org.exporecerca.planner.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Topic extends AbstractEntity {

	@NotNull
	@Size(min = 1, max = 50, message="Size must be between 1 an 50 characters")
	private String name;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	@Override
	public String toString() {
		return  name ;
	}	
}
