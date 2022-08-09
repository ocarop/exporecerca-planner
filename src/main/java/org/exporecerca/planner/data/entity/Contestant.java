package org.exporecerca.planner.data.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.optaplanner.core.api.domain.lookup.PlanningId;

@Entity
public class Contestant extends AbstractEntity {

	@Column(nullable=false, length=50)
	@NotNull
	@Size(min=2,max=50,message="Size must be between 2 an 50 characters")
    private String firstName;
	
	
	@Column(nullable=false, length=50)
	@NotNull
	@Size(min=2,max=50,message="Size must be between 2 an 50 characters")
    private String lastName;

	@Column(nullable=true, length=100)
	@Email
	@Size(min=2,max=50,message="Size must be between 2 an 100 characters")
	private String email;
	
	@Column(nullable=true, length=30)
    private String phone;

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
	@Override
	public String toString() {
		return "Contestant [firstName=" + firstName + ", lastName=" + lastName + "]";
	}


}
