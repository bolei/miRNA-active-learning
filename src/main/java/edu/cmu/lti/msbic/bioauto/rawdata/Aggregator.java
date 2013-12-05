package edu.cmu.lti.msbic.bioauto.rawdata;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Aggregator {
    private Map<Integer, String> miRnaGeneIds;
    private Map<String, Integer> dictionary;
    private List<String> dictionaryKeys;
    private Random rand;
    
    private Aggregator() {
	this.miRnaGeneIds = new HashMap<Integer, String>();
	this.dictionary = new HashMap<String, Integer>();
	dictionaryKeys = new ArrayList<String>();
	rand = new Random(Calendar.getInstance().getTimeInMillis());
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
	aggregator.dictionaryKeys.addAll(aggregator.dictionary.keySet());
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
		    
		    // Negative pair, so need to create true negative by removing positive binding
		    if (miRnaGenePair.getLabel() == 0) {
			createNegativePair(pair);
		    }
		    
		    for (int i = 0; i < 8; i++) {
//			int feature = dictionary.get(pair.get(i));
//			for (int j = 0; j < dictionary.size(); j++) {
//			    os.write(",".getBytes());
//			    int val = 0;
//			    if (feature == (j+1)) {
//				val = 1;
//			    }
//			    os.write(Integer.toString(val).getBytes());
//			}
		    }
		    for (int i = 0; i < pair.size(); i++) {
			os.write(",".getBytes());
			os.write(dictionary.get(pair.get(i)).toString().getBytes());
		    }
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
    
    private void createNegativePair(List<String> pair) {
	List<Integer> positiveBindPosition = new ArrayList<Integer>();
	// Get locations of positive bind positions
	for (int i = 0; i < 8; i++) {
	    if (pair.get(i).contains("|")) {
		positiveBindPosition.add(i);
	    }
	}
	
	// No positive binding positions then can return
	if (positiveBindPosition.size() < 1) {
	    return;
	}
	
	// Randomly change positive bind pair
	int i = 0;
	while (i < positiveBindPosition.size()) {
	    int negativePosition = rand.nextInt(dictionaryKeys.size());
	    if (!dictionaryKeys.get(negativePosition).contains("|")) {
		int position = positiveBindPosition.get(i);
		pair.set(position, dictionaryKeys.get(negativePosition));
		i++;
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
    
    public int getDictionarySize() {
	return dictionary.size();
    }
}
