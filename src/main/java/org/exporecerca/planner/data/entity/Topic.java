package org.exporecerca.planner.data.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Topic extends AbstractEntity {

	@NotNull
	@Size(min = 1, max = 50, message="Size must be between 1 an 50 characters")
	private String name;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			  name = "jury_topics", 
			  joinColumns = @JoinColumn(name = "topic_id"), 
			  inverseJoinColumns = @JoinColumn(name = "jury_id"))
	List<Jury> possibleJuryList;

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

	public List<Jury> getPossibleJuryList() {
		return possibleJuryList;
	}	
}
