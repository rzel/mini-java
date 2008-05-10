package fa.legacy;

public enum DFAState {
	INIT, RUNNING, DEAD, ACCEPTED,
	ACCT_DEAD	/* DEAD after ACCEPTED */
}
