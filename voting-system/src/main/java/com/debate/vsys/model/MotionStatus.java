package com.debate.vsys.model;

/**
 * 
 * RUNNABLE:
 *   - before startTime
 * RUNNING:
 *   - during active time between startTime and endTime
 * PENDING:
 *   - during grace period for vote worker process votes
 * TIED:
 *   - waiting for VP make decision
 * BEFORETERM:
 *   - waiting for output the result
 * TERMINATED
 *   - motion result come out
 */
public enum MotionStatus {

	RUNNABLE,
	RUNNING,
	PENDING,
	TIED,
	BEFORETERM,
	TERMINATED
}
