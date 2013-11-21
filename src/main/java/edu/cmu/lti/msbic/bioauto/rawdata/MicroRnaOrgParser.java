package edu.cmu.lti.msbic.bioauto.rawdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Parses file from microrna.org
 *
 */
public class MicroRnaOrgParser {

    private File microRnaOrgFile;
    
    public MicroRnaOrgParser(File microRnaOrgFile) {
	this.microRnaOrgFile = microRnaOrgFile;
    }
    
    public void parse() throws FileNotFoundException, UnequalLengthException {
	Scanner scanner = new Scanner(microRnaOrgFile);
	scanner.nextLine();
	while (scanner.hasNextLine()) {
	    String line = scanner.nextLine();
	    String[] parts = line.split("\t");
	    String miRna = parts[1]; // miRNA name
	    String gene = parts[3]; // gene symbol
	    String miRnaSequence = parts[6]; // miRNA sequence
	    String alignment = parts[7]; // alignment
	    String mRnaSequence = parts[8]; // mRNA sequence
	    
	    RnaPair pair = new RnaPair(miRna, gene);
	    pair.createPairs(miRnaSequence, alignment, mRnaSequence);
	    System.out.println(pair.toString());
	}
	scanner.close();
    }
    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
	// TODO Auto-generated method stub
	String filename = "";
	MicroRnaOrgParser parser = new MicroRnaOrgParser(new File(filename));
	parser.parse();
    }

}
