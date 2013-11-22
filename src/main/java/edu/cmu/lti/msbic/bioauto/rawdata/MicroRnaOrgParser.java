package edu.cmu.lti.msbic.bioauto.rawdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Parses file from microrna.org
 *
 */
public class MicroRnaOrgParser {

    private File microRnaOrgFile;
    private Map<String, Set<String>> validatedPairs;
    private Map<String, List<RnaPair>> pairs;
    
    public MicroRnaOrgParser(File microRnaOrgFile, Map<String, Set<String>> validatedPairs) {
	this.microRnaOrgFile = microRnaOrgFile;
	this.validatedPairs = validatedPairs;
	pairs = new HashMap<String, List<RnaPair>>();
    }
    
    public void parse() throws FileNotFoundException, UnequalLengthException {
	Scanner scanner = new Scanner(microRnaOrgFile);
	while (scanner.hasNextLine()) {
	    String line = scanner.nextLine();
	    String[] parts = line.split("\t");
	    String miRna = parts[1]; // miRNA name
	    String gene = parts[3]; // gene symbol
	    String miRnaSequence = parts[6]; // miRNA sequence
	    String alignment = parts[7]; // alignment
	    String mRnaSequence = parts[8]; // mRNA sequence
	    
	    Set<String> genes = validatedPairs.get(miRna);
	    if (genes != null) {
		int label = 0;
		if (genes.contains(gene)) {
		    label = 1;
		}
		RnaPair pair = new RnaPair(miRna, gene, label);
		pair.createPairs(miRnaSequence, alignment, mRnaSequence);
		
		List<RnaPair> rnaPairs = pairs.get(miRna);
		if (rnaPairs == null) {
		    rnaPairs = new ArrayList<RnaPair>();
		    pairs.put(miRna, rnaPairs);
		}
		rnaPairs.add(pair);
	    }
	}
	scanner.close();
    }
}
