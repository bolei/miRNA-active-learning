package edu.cmu.lti.msbic.bioauto.rawdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
    private Map<String, List<MiRnaGenePair>> miRnaGenePairs;
    private Map<String, Set<String>> dictionary;
    
    public MicroRnaOrgParser(File microRnaOrgFile, Map<String, Set<String>> validatedPairs) {
	this.microRnaOrgFile = microRnaOrgFile;
	this.validatedPairs = validatedPairs;
	miRnaGenePairs = new HashMap<String, List<MiRnaGenePair>>();
	dictionary = new HashMap<String, Set<String>>();
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
		MiRnaGenePair pair = new MiRnaGenePair(miRna, gene, label);
		pair.createPairs(miRnaSequence, alignment, mRnaSequence);
		
		// Add RNA Pair
		List<MiRnaGenePair> rnaPairs = miRnaGenePairs.get(miRna);
		if (rnaPairs == null) {
		    rnaPairs = new ArrayList<MiRnaGenePair>();
		    miRnaGenePairs.put(miRna, rnaPairs);
		}
		rnaPairs.add(pair);
		
		// Add all pairs to vocab list
		Set<String> pairsList = dictionary.get(miRna);
		if (pairsList == null) {
		    pairsList = new HashSet<String>();
		    dictionary.put(miRna, pairsList);
		}
		pairsList.addAll(pair.getPairs());
	    }
	}
	scanner.close();
    }

    public Map<String, Set<String>> getDictionary() {
        return dictionary;
    }

    public Map<String, List<MiRnaGenePair>> getPairs() {
        return miRnaGenePairs;
    }   
}
