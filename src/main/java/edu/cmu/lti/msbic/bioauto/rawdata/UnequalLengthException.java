package edu.cmu.lti.msbic.bioauto.rawdata;

public class UnequalLengthException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public UnequalLengthException(String description) {
	super(description);
    }
}
