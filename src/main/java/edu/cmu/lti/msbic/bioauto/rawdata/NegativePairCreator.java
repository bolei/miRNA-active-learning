package edu.cmu.lti.msbic.bioauto.rawdata;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class NegativePairCreator {

    private int dictionarySize;
    private Random rand = new Random(Calendar.getInstance().getTimeInMillis());

    public NegativePairCreator(int dictionarySize) {
	this.dictionarySize = dictionarySize;
    }

    public void createNegativePairsFile(File negativePairsFile, int n) {
	BufferedOutputStream os = null;
	try {
	    os = new BufferedOutputStream(new FileOutputStream(
		    negativePairsFile));
	    for (int i = 0; i < n; i++) {
		os.write(String.format("-1,%d", i + 1).getBytes());
		for (Integer pair : createRandomPairs()) {
		    for (int j = 0; j < dictionarySize; j++) {
			os.write(",".getBytes());
			int val = 0;
			if (pair == j) {
			    val = 1;
			}
			os.write(Integer.toString(val).getBytes());
		    }
		}
		os.write("\n".getBytes());
	    }
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
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

    private List<Integer> createRandomPairs() {
	List<Integer> randomPairs = new ArrayList<Integer>();
	for (int i = 0; i < 8; i++) {
	    randomPairs.add(rand.nextInt(dictionarySize));
	}
	return randomPairs;
    }
}
