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

import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.solver.SolverStatus;

@PlanningSolution
public class TimeTable {

    @ValueRangeProvider(id = "timeslotRange")
    @ProblemFactCollectionProperty
    private List<Timeslot> timeslotList;

    @ValueRangeProvider(id = "juryRange")
    @ProblemFactCollectionProperty
    private List<Jury> juryList;

    @ValueRangeProvider(id = "contestantRange")
    @ProblemFactCollectionProperty
    private List<Contestant> contestantList;
    
    @PlanningEntityCollectionProperty
    private List<Evaluation> evaluationList;

    @PlanningScore
    private HardSoftScore score;

    // Ignored by OptaPlanner, used by the UI to display solve or stop solving button
    private SolverStatus solverStatus;

    // No-arg constructor required for OptaPlanner
    public TimeTable() {
    }


    // ************************************************************************
    // Getters and setters
    // ************************************************************************


    
    public List<Timeslot> getTimeslotList() {
        return timeslotList;
    }

    public List<Jury> getJuryList() {
		return juryList;
	}


	public void setJuryList(List<Jury> juryList) {
		this.juryList = juryList;
	}


	public List<Contestant> getContestantList() {
		return contestantList;
	}


	public void setContestantList(List<Contestant> list) {
		this.contestantList = list;
	}


	public List<Evaluation> getEvaluationList() {
		return evaluationList;
	}


	public void setEvaluationList(List<Evaluation> evaluationList) {
		this.evaluationList = evaluationList;
	}


	public void setTimeslotList(List<Timeslot> timeslotList) {
		this.timeslotList = timeslotList;
	}


	public HardSoftScore getScore() {
        return score;
    }

    public SolverStatus getSolverStatus() {
        return solverStatus;
    }

    public void setSolverStatus(SolverStatus solverStatus) {
        this.solverStatus = solverStatus;
    }

}
