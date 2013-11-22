package edu.cmu.lti.msbic.bioauto.rawdata;

import java.util.ArrayList;
import java.util.List;

public class RnaPair {

    private String miRna;
    private String gene;
    private List<String> pairs;
    private int label;

    public RnaPair(String miRna, String gene, int label) {
	this.miRna = miRna;
	this.gene = gene;
	this.label = label;
	pairs = new ArrayList<String>();
    }

    public void createPairs(String miRnaSequence, String alignment,
	    String mRnaSequence) throws UnequalLengthException {
	if (miRnaSequence.length() != alignment.length()
		|| miRnaSequence.length() != mRnaSequence.length()) {
	    throw new UnequalLengthException("unequal lengths");
	}

	for (int i = 0; i < miRnaSequence.length(); i++) {
	    pairs.add(new String(miRnaSequence.charAt(i) + ""
		    + alignment.charAt(i) + "" + mRnaSequence.charAt(i)));
	}
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append(miRna + " " + gene + "\n");
	for (String pair : pairs) {
	    builder.append(pair);
	    builder.append("\n");
	}
	return builder.toString();
    }

    public int getLabel() {
        return label;
    }
}
