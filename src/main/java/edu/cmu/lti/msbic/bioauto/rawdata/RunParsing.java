package edu.cmu.lti.msbic.bioauto.rawdata;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
	System.out.println("Loading validated pairs");
	Map<String, Set<String>> validatedPairs = new HashMap<String, Set<String>>();
	String targetsFolderName = prop.getProperty("validated_targets_location");
	File targetsFolder = new File(targetsFolderName);
	for (File targetFile : targetsFolder.listFiles()) {
	    System.out.println("Parsing " + targetFile.getName());
	    String name = targetFile.getName();
	    int extensionLoc = name.indexOf(".");
	    String miRna = name.substring(0, extensionLoc);
	    ValidatedTargetsParser validatedtargetsParser = new ValidatedTargetsParser(targetFile, miRna); 
	    validatedPairs.put(miRna, validatedtargetsParser.parse());
	}
	
	// Parse sequence file
	System.out.println("Parsing sequence file");
	String sequenceFile = prop.getProperty("sequence_file");
	MicroRnaOrgParser microRnaOrgParser = new MicroRnaOrgParser(new File(sequenceFile), validatedPairs);
	microRnaOrgParser.parse();
	Map<String, List<MiRnaGenePair>> miRnaGenePairs = microRnaOrgParser.getMiRnaGenePairs();
	Map<String, Set<String>> dictionaries = microRnaOrgParser.getDictionaries();
	
	// Aggregate files
	System.out.println("Aggregating files");
	String formattedDataFile = prop.getProperty("formatted_data_file");
	String dictionaryFile = prop.getProperty("dictionary_file");
	String miRnaGeneIdsFile = prop.getProperty("mirna_gene_ids_file");
	Aggregator aggregator = Aggregator.aggregate(dictionaries);
	aggregator.createFormattedDataFile(new File(formattedDataFile), miRnaGenePairs);
	aggregator.writeDictionaryFile(new File(dictionaryFile));
	aggregator.writeMiRnaGeneIdsFile(new File(miRnaGeneIdsFile));
	
//	// Negative pairs file
//	System.out.println("Creating negative pairs file");
//	String negativePairsFile = prop.getProperty("negative_pairs_file");
//	NegativePairCreator negativePairCreator = new NegativePairCreator(aggregator.getDictionarySize());
//	negativePairCreator.createNegativePairsFile(new File(negativePairsFile), 3000);
    }

}
