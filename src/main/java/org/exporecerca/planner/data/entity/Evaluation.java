package org.exporecerca.planner.data.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@PlanningEntity
@Entity
@Getter @Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Evaluation extends AbstractEntity {
	
	@PlanningVariable(valueRangeProviderRefs = "juryRange",nullable=true)
	@ManyToOne
	private Jury jury;
	
	@ManyToOne
	@NonNull
	private Contestant contestant;
	
	@ManyToOne
	@NonNull
	private Timeslot timeslot;

    @Override
    @PlanningId
	public Integer getId() {
		return super.getId();
	}

    


	public  boolean isUnassigned(){
		return jury==null;
	}
	
    @ValueRangeProvider(id = "juryRange")
    public List<Jury> getPossibleJuryList() {
        return getContestant().getTopic().getPossibleJuryList();
    }
}
