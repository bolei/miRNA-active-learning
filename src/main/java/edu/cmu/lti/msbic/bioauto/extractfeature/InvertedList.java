package edu.cmu.lti.msbic.bioauto.extractfeature;

import java.util.HashMap;
import java.util.HashSet;

public class InvertedList {

	// <tokenId, <docId, positions> >
	private HashMap<Integer, HashMap<Integer, HashSet<Integer>>> invertedList = new HashMap<Integer, HashMap<Integer, HashSet<Integer>>>();

	// <tokenId, term_freq>
	private HashMap<Integer, Integer> collectionTermFrequency = new HashMap<Integer, Integer>();

	private HashSet<Integer> docIdSet = new HashSet<Integer>();

	public void addToken(int tokenId, int docId, int position) {

		// add token into inverted list
		if (invertedList.containsKey(tokenId) == false) {
			invertedList.put(tokenId, new HashMap<Integer, HashSet<Integer>>());
		}
		if (invertedList.get(tokenId).containsKey(docId) == false) {
			invertedList.get(tokenId).put(docId, new HashSet<Integer>());
		}
		invertedList.get(tokenId).get(docId).add(position);

		// update term frequency
		if (collectionTermFrequency.containsKey(tokenId) == false) {
			collectionTermFrequency.put(tokenId, 1);
		} else {
			collectionTermFrequency.put(tokenId,
					collectionTermFrequency.get(tokenId) + 1);
		}

		// update docId set
		docIdSet.add(docId);
	}

	public int getDf(int tokenId) {
		if (invertedList.containsKey(tokenId) == false) {
			return 0;
		}
		return invertedList.get(tokenId).size();
	}

	public int getCollectionTermFrequency(int tokenId) {
		if (collectionTermFrequency.containsKey(tokenId) == false) {
			return 0;
		}
		return collectionTermFrequency.get(tokenId);
	}

	public int getTermFrequency(int tokenId, int docId) {
		if (invertedList.containsKey(tokenId) == false) {
			return 0;
		}
		if (invertedList.get(tokenId).containsKey(docId) == false) {
			return 0;
		}
		return invertedList.get(tokenId).get(docId).size();
	}

	public double getTfIdf(int tokenId, int docId) {
		double tf = Math.log(getTermFrequency(tokenId, docId) + 1);
		double idf = Math.log(docIdSet.size() / ((double) getDf(tokenId)) + 1d);
		return tf * idf;
	}
}
