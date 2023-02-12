/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.exporecerca.planner.data.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.solver.SolverStatus;

import lombok.Getter;
import lombok.Setter;

@PlanningSolution
@Getter
@Setter
@Entity
public class TimeTable extends AbstractEntity{

	@Column 
	Date generationTime;
	
	@Transient
    @ProblemFactCollectionProperty
    private List<Timeslot> timeslotList;

	@Transient
    @ProblemFactCollectionProperty
    private List<Jury> juryList;

	@Transient
    @ProblemFactCollectionProperty
    private List<Contestant> contestantList;
    
	@OneToMany(mappedBy = "timeTable", fetch = FetchType.EAGER) 
    @PlanningEntityCollectionProperty
    private List<Evaluation> evaluationList;

	@Transient
    @PlanningScore
    private HardMediumSoftScore score;

    // Ignored by OptaPlanner, used by the UI to display solve or stop solving button
	@Transient
	private SolverStatus solverStatus;

    // No-arg constructor required for OptaPlanner
    public TimeTable() {
    }



	public HardMediumSoftScore getScore() {
        return score;
    }

    public SolverStatus getSolverStatus() {
        return solverStatus;
    }

    public void setSolverStatus(SolverStatus solverStatus) {
        this.solverStatus = solverStatus;
    }

}
