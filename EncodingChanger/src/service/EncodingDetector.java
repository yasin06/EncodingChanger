package service;

import java.io.File;
import java.io.FileInputStream;

import org.mozilla.universalchardet.UniversalDetector;

public class EncodingDetector {

	/***********************************************************
	 * Use Mozilla juniversalchardet.jar utility to auto detect file encoding.
	 * https://code.google.com/archive/p/juniversalchardet/
	 * 
	 * @param file - File
	 * @return success: file encoding type, failure: null
	 ***********************************************************/
	public static String detectEncoding(File file) {
		String encoding = null;
		UniversalDetector detector = new UniversalDetector(null);
		FileInputStream fis = null;
		int nread;
		byte[] buf = new byte[4096];

		try {
			fis = new FileInputStream(file);

			while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
				detector.handleData(buf, 0, nread);
			}

			detector.dataEnd();
			encoding = detector.getDetectedCharset();
			detector.reset();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return encoding;
	}

}
