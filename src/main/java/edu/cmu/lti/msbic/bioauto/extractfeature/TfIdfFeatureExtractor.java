package edu.cmu.lti.msbic.bioauto.extractfeature;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Properties;

import edu.cmu.lti.msbic.bioauto.util.MiscHelper;

public class TfIdfFeatureExtractor {

	private static final String FEATURE_FILE_NAME = "tfidf-features.txt";

	public void extractFeatures(String formatedDataFilePath,
			String featureFolderPath) throws IOException {
		InvertedList invList = buildIndex(formatedDataFilePath);
		FileReader fin;
		BufferedReader bin = null;
		PrintWriter out = null;
		try {
			fin = new FileReader(formatedDataFilePath);
			bin = new BufferedReader(fin);
			out = new PrintWriter(featureFolderPath + "/" + FEATURE_FILE_NAME);
			String line = bin.readLine(); // skip first line headers
			int docId;
			int tokenId;
			double tfIdf;
			HashSet<Integer> tokensInArticle = new HashSet<Integer>();
			while ((line = bin.readLine()) != null) {
				String[] strArr = line.split(",\\s*");
				docId = Integer.parseInt(strArr[1]);
				tokensInArticle.clear();
				for (int i = 2; i < strArr.length; i++) {
					// for each token id
					tokenId = Integer.parseInt(strArr[i]);
					if (tokensInArticle.contains(tokenId) == false) {
						tfIdf = invList.getTfIdf(tokenId, docId);
						String outline = tokenId + ":" + tfIdf;
						out.print(outline + " ");
						tokensInArticle.add(tokenId);
					}
				}
				out.println();
			}
		} finally {
			MiscHelper.closeReader(bin);
			MiscHelper.closeWriter(out);
		}
	}

	/**
	 * Scan the Bag of Word file and build an index
	 * 
	 * @param bowFilePath
	 *            Path to the bag of word file from which the program will read
	 * 
	 * @throws IOException
	 * 
	 */
	private InvertedList buildIndex(String bowFilePath) throws IOException {
		InvertedList invList = new InvertedList();

		FileReader fin;
		BufferedReader bin = null;
		try {
			fin = new FileReader(bowFilePath);
			bin = new BufferedReader(fin);
			String line = bin.readLine(); // skip first line headers
			int docId;
			int tokenId;

			while ((line = bin.readLine()) != null) {
				String[] strArr = line.split(",\\s*");
				docId = Integer.parseInt(strArr[1]);
				for (int i = 2; i < strArr.length; i++) {
					// for each token id
					tokenId = Integer.parseInt(strArr[i]);
					invList.addToken(tokenId, docId, i - 1);
				}
			}
		} finally {
			MiscHelper.closeReader(bin);
		}
		return invList;
	}

	public static void main(String[] args) throws IOException {
		Properties prop = new Properties();
		prop.load(TfIdfFeatureExtractor.class
				.getResourceAsStream("/extractor.properties"));
		String formatedDataFilePath = prop.getProperty("formated_data_file");
		String featureFolderPath = prop.getProperty("feature_folder");

		TfIdfFeatureExtractor tfidfExtractor = new TfIdfFeatureExtractor();
		tfidfExtractor.extractFeatures(formatedDataFilePath, featureFolderPath);
	}

}
