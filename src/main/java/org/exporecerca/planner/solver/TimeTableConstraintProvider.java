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
import org.optaplanner.core.api.score.stream.ConstraintCollectors;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

public class TimeTableConstraintProvider implements ConstraintProvider {

	private final int DESIRED_NUM_EVALS_PER_CONTESTANT = 5;
	private final int NUM_EVALS_PER_JURY = 10;

	@Override
	public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
		return new Constraint[] {
				// Hard constraints
				juryConflictTimeslot(constraintFactory), 
				contestantConflictTimeslot(constraintFactory),
				alreadyEvaluatedByJuryConflict(constraintFactory),
				numberEvaluationsPerJuryEqualsExactNumber(constraintFactory),
				// medium constraints
				minNumberofEvaluations(constraintFactory),
				notAssigned(constraintFactory),
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
				.penalize(HardMediumSoftScore.ONE_HARD).asConstraint("Jury conflict timeslot");
	}

	Constraint contestantConflictTimeslot(ConstraintFactory constraintFactory) {
		// A contestant shows their project to one Judge at the same time.
		return constraintFactory.forEachUniquePair(Evaluation.class,
				// ... in the same timeslot ...
				Joiners.equal(Evaluation::getTimeslot),
				// ... same contestant ...
				Joiners.equal(Evaluation::getContestant)).penalize(HardMediumSoftScore.ONE_HARD)
				.asConstraint("Contestant conflict");
	}

	Constraint alreadyEvaluatedByJuryConflict(ConstraintFactory constraintFactory) {
		// A jury can evaluate 1 contestant just once
		return constraintFactory
				// Select each pair of 2 different evaluations ...
				.forEachUniquePair(Evaluation.class,
						// ... in same contestant ...
						Joiners.equal(Evaluation::getContestant),
						// ... the same jury ...
						Joiners.equal(Evaluation::getJury))
				// ... and penalize each pair with a hard weight.
				.penalize(HardMediumSoftScore.ONE_HARD).asConstraint("already Evaluated");
	}

	private Constraint numberEvaluationsPerJuryEqualsExactNumber(ConstraintFactory factory) {
		return factory.forEach(Evaluation.class).groupBy(Evaluation::getJury, ConstraintCollectors.count())
				.filter((jury, juryCount) -> juryCount > this.NUM_EVALS_PER_JURY)
				.penalize(HardMediumSoftScore.ONE_HARD).asConstraint("Number evaluations per jury");
	}

	private Constraint minNumberofEvaluations(ConstraintFactory factory) {
		return factory.forEach(Evaluation.class).groupBy(Evaluation::getContestant, ConstraintCollectors.count())
				.filter((contestant, contestantCount) -> contestantCount < this.DESIRED_NUM_EVALS_PER_CONTESTANT)
				.penalize(HardMediumSoftScore.ONE_MEDIUM)
				//.penalize(HardMediumSoftScore.ONE_MEDIUM,(contestant, contestantCount)->this.DESIRED_NUM_EVALS_PER_CONTESTANT-contestantCount)
				.asConstraint("Desired number of evaluations per contestant");
	}
	
	private Constraint notAssigned(ConstraintFactory factory) {
		return factory.forEachIncludingNullVars(Evaluation.class).filter(Evaluation::isUnassigned)
				.penalize(HardMediumSoftScore.ONE_MEDIUM).asConstraint("Slot unassigned" );
	}

	
}
