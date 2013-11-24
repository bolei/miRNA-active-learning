package edu.cmu.lti.msbic.bioauto.util;

import java.io.Reader;
import java.io.Writer;

public class MiscHelper {
	public static void closeReader(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
				reader = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeWriter(Writer writer) {
		if (writer != null) {
			try {
				writer.close();
				writer = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
