package org.exporecerca.planner.data.entity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.optaplanner.core.api.domain.lookup.PlanningId;

import lombok.Data;

@Entity
@Data
public class Jury extends AbstractEntity {

	@Column(nullable=false, length=50)
	@NotNull
	@Size(min=2,max=50,message="Size must be between 2 an 50 characters")
	private String firstName;
    
	@Column(nullable=false, length=50)
	@NotNull
	@Size(min=2,max=50,message="Size must be between 2 an 50 characters")	
	private String lastName;
	
	@Column(nullable=true, length=100)
	@Email(message="email format incorrect")
	@Size(min=2,max=50,message="Size must be between 2 an 100 characters")	
    private String email;
	
	@Column(nullable=true, length=30)
    private String phone;
    
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			  name = "jury_topic", 
			  joinColumns = @JoinColumn(name = "jury_id"), 
			  inverseJoinColumns = @JoinColumn(name = "topic_id"))
	private Set<Topic> topics;
    
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			  name = "jury_timeslot", 
			  joinColumns = @JoinColumn(name = "jury_id"), 
			  inverseJoinColumns = @JoinColumn(name = "timeslot_id"))
	private Set<Timeslot> timeslots;
 

}
