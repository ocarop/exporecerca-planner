/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

package org.exporecerca.planner.solver;

import java.time.Duration;

import org.exporecerca.planner.data.entity.Evaluation;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

public class TimeTableConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                // Hard constraints
        		juryConflictTimeslot(constraintFactory),
                contestantConflictTimeslot(constraintFactory),
                alreadyEvaluatedByJuryConflict(constraintFactory),
                notAssigned(constraintFactory)
                
        };
    }

    Constraint juryConflictTimeslot(ConstraintFactory constraintFactory) {
        // A jury can evaluate 1 contestant at same time
        return constraintFactory
                // Select each pair of 2 different evaluations ...
                .forEachUniquePair(Evaluation.class,
                        // ... in the same timeslot ...
                        Joiners.equal(Evaluation::getTimeslot),
                        // ... the same jury ...
                        Joiners.equal(Evaluation::getJury))
                // ... and penalize each pair with a hard weight.
                .penalize("Jury conflict", HardMediumSoftScore.ONE_HARD);
    }

    Constraint contestantConflictTimeslot(ConstraintFactory constraintFactory) {
        // A contestant shows their project to one Judge at the same time.
        return constraintFactory
                .forEachUniquePair(Evaluation.class,
                		// ... in the same timeslot ...
                        Joiners.equal(Evaluation::getTimeslot),
                        // ... same contestant ...
                        Joiners.equal(Evaluation::getContestant))
                .penalize("Contestant conflict", HardMediumSoftScore.ONE_HARD);
    }

    Constraint alreadyEvaluatedByJuryConflict(ConstraintFactory constraintFactory) {
        // A jury can evaluate 1 contestant just once
        return constraintFactory
                // Select each pair of 2 different evaluations ...
                .forEachUniquePair(Evaluation.class,
                        // ... in the same timeslot ...
                        Joiners.equal(Evaluation::getContestant),
                        // ... the same jury ...
                        Joiners.equal(Evaluation::getJury))
                // ... and penalize each pair with a hard weight.
                .penalize("already Evaluated", HardMediumSoftScore.ONE_HARD);
    }
 
    private Constraint notAssigned(ConstraintFactory factory) {
        return factory.forEachIncludingNullVars(Evaluation.class)
                .filter(Evaluation::isUnassigned)
                .penalize("Slot unassigned", HardMediumSoftScore.ONE_MEDIUM);
    }
}
