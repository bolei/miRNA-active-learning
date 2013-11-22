package edu.cmu.lti.msbic.bioauto.rawdata;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class RunParsing {

    /**
     * @param args
     * @throws IOException 
     * @throws UnequalLengthException 
     */
    public static void main(String[] args) throws IOException, UnequalLengthException {
	Properties prop = new Properties();
	prop.load(MicroRnaOrgParser.class.getResourceAsStream("/parser.properties"));
	
	// Load validated pairs
	Map<String, Set<String>> validatedPairs = new HashMap<String, Set<String>>();
	String targetsFolderName = prop.getProperty("validated_targets_location");
	File targetsFolder = new File(targetsFolderName);
	for (File targetFile : targetsFolder.listFiles()) {
	    String name = targetFile.getName();
	    int extensionLoc = name.indexOf(".");
	    String miRna = name.substring(0, extensionLoc);
	    ValidatedTargetsParser validatedtargetsParser = new ValidatedTargetsParser(targetFile, miRna); 
	    validatedPairs.put(miRna, validatedtargetsParser.parse());
	}
	
	String filename = prop.getProperty("sequence_file");
	MicroRnaOrgParser microRnaOrgParser = new MicroRnaOrgParser(new File(filename), validatedPairs);
	microRnaOrgParser.parse();
    }

}
