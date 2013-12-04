package edu.cmu.lti.msbic.bioauto.rawdata;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Aggregator {
    private Map<Integer, String> miRnaGeneIds;
    private Map<String, Integer> dictionary;
    
    private Aggregator() {
	this.miRnaGeneIds = new HashMap<Integer, String>();
	this.dictionary = new HashMap<String, Integer>();
    }
    
    public static Aggregator aggregate(Map<String, Set<String>> dictionaries) {
	Aggregator aggregator = new Aggregator();
	
	// Combine dictionaries together and create conversion from pair to integer value
	Iterator<String> iter = dictionaries.keySet().iterator();
	while (iter.hasNext()) {
	    String miRna = iter.next();
	    Set<String> pairs = dictionaries.get(miRna);
	    for (String pair : pairs) {
		if (aggregator.dictionary.get(pair) == null) {
		    aggregator.dictionary.put(pair, aggregator.dictionary.size() + 1);
		}
	    }
	}
	return aggregator;
    }
    
    public void createFormattedDataFile(File formattedDataFile, Map<String, List<MiRnaGenePair>> pairs) {
	BufferedOutputStream os = null;
	try {
	    os = new BufferedOutputStream(new FileOutputStream(formattedDataFile));
	    os.write("Label,miRnaGeneId,Features\n".getBytes());
	    int miRnaGeneId = 1;
	    Iterator<String> iter = pairs.keySet().iterator();
	    while (iter.hasNext()) {
		String miRna = iter.next();
		List<MiRnaGenePair> miRnaGenePairs = pairs.get(miRna);
		for (MiRnaGenePair miRnaGenePair : miRnaGenePairs) {
		    String miRnaGene = miRna + ":" + miRnaGenePair.getGene();
		    miRnaGeneIds.put(miRnaGeneId, miRnaGene);
		    
		    // Write miRna Gene Pair to output file
		    os.write(String.format("%d,%d", miRnaGenePair.getLabel(), miRnaGeneId++).getBytes());
		    List<String> pair = miRnaGenePair.getPairs();
		    for (int i = 0; i < 8; i++) {
			int feature = dictionary.get(pair.get(i));
			for (int j = 0; j < dictionary.size(); j++) {
			    os.write(",".getBytes());
			    int val = 0;
			    if (feature == j) {
				val = 1;
			    }
			    os.write(Integer.toString(val).getBytes());
			}
		    }
//		    for (String pair : miRnaGenePair.getPairs()) {
//			os.write(",".getBytes());
//			
//			os.write(dictionary.get(pair).toString().getBytes());
//		    }
		    os.write("\n".getBytes());
		}
		
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    if (os != null) {
		try {
		    os.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		os = null;
	    }
	}
    }
    
    public void writeMiRnaGeneIdsFile(File miRnaGeneIdsFile) {
	BufferedOutputStream os = null;
	try {
	    os = new BufferedOutputStream(new FileOutputStream(miRnaGeneIdsFile));
	    Iterator<Integer> iter = miRnaGeneIds.keySet().iterator();
	    while (iter.hasNext()) {
		Integer id = iter.next();
		String miRnaGene = miRnaGeneIds.get(id);
		os.write(String.format("%s,%d\n", miRnaGene, id).getBytes());
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    if (os != null) {
		try {
		    os.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		os = null;
	    }
	}
    }
    
    public void writeDictionaryFile(File dictionaryFile) {
	BufferedOutputStream os = null;
	try {
	    os = new BufferedOutputStream(new FileOutputStream(dictionaryFile));
	    Iterator<String> iter = dictionary.keySet().iterator();
	    while (iter.hasNext()) {
		String pair = iter.next();
		Integer id = dictionary.get(pair);
		os.write(String.format("%s,%d\n", pair, id).getBytes());
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    if (os != null) {
		try {
		    os.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		os = null;
	    }
	}
    }
}
