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

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Topic extends AbstractEntity {


	@Column(nullable=false, length=150)
	@NotNull
	@Size(min = 1, max = 150, message="Size must be between 1 an 150 characters")
	private String name;

    /**
     * The color of this topic. Any valid css color is allowed (e.g. #f00, #ff0000, rgb(255,0,0), or red).
     */
	@Column(nullable=true, length=20)
	private String color;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			  name = "jury_topic", 
			  joinColumns = @JoinColumn(name = "topic_id"), 
			  inverseJoinColumns = @JoinColumn(name = "jury_id"))
	List<Jury> possibleJuryList;

	public Topic(String name) {
		this.name=name;
	}

	public Topic() {
		super();
	}

	@Override
	public String toString() {
		return name ;
	}
	
	
}
