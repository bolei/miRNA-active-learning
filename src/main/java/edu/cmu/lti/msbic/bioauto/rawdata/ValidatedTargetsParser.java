package edu.cmu.lti.msbic.bioauto.rawdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ValidatedTargetsParser {

    private File validatedTargetsFile;
    private String miRna;
    
    public ValidatedTargetsParser(File validatedTargetsFile, String miRna) {
	this.validatedTargetsFile = validatedTargetsFile;
	this.miRna = miRna;
    }
    
    public Set<String> parse() throws FileNotFoundException {
	Set<String> genes = new HashSet<String>();
	Scanner scanner = new Scanner(validatedTargetsFile);
	while(scanner.hasNextLine()) {
	    String line = scanner.nextLine();
	    if (line.startsWith(miRna)) {
		String[] parts = line.split("\t");
		genes.add(parts[3]);
	    }
	}
	scanner.close();
	return genes;
    }
}
