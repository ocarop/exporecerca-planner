package org.exporecerca.planner.data.entity;

import java.time.LocalDate;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.optaplanner.core.api.domain.lookup.PlanningId;

@Entity
public class Jury extends AbstractEntity {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    
    @OneToMany
    private Collection<Topic> topics;
    
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


}
