package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import model.MyFile;

public class EncodingChanger {

	/******************************************************************
	 * Changes given files encoding to UTF-8. Also repairs TURKCE chars
	 * 'ý'->ı,'ð'->ğ,'þ'->ş,'Þ'->Ş,'â'->a,'Ý'->İ
	 * 
	 * @param myFile - input file to change UTF-8
	 * @return true if success, else false
	 * @throws Exception
	 ******************************************************************/
	public static boolean changeEncoding(MyFile myFile) throws Exception {
		String path = myFile.getFile().getParent();
		String fileName = myFile.getFile().getName();
		File outFile = new File(path, fileName + ".tmp");
		File finalFile = null;

		Reader in = new InputStreamReader(new FileInputStream(myFile.getFile().getAbsoluteFile()),
				myFile.getSourceEncoding());
		Writer out = new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8");

		char[] cbuf = new char[2048];
		int len;

		while ((len = in.read(cbuf, 0, cbuf.length)) != -1) {
//			 TURKCE KARAKTER DUZELTME
//			'ý'->ı,'ð'->ğ,'þ'->ş,'Þ'->Ş,'â'->a,'Ý'->İ
//			-3, -16, -2, -34, -30, -35
			for (int i = 0; i < len; i++) {
				switch (cbuf[i]) {
				case ('ý'):
					cbuf[i] = 'ı';
					break;
				case ('ð'):
					cbuf[i] = 'ğ';
					break;
				case ('þ'):
					cbuf[i] = 'ş';
					break;
				case ('Þ'):
					cbuf[i] = 'Ş';
					break;
				case ('â'):
					cbuf[i] = 'a';
					break;
				case ('Ý'):
					cbuf[i] = 'I';
					break;
				default:
					break;
				}
			}

			out.write(cbuf, 0, len);
		}

		try {
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		myFile.getFile().delete();
		finalFile = new File(path, fileName);
		outFile.renameTo(finalFile);
		outFile.delete();
		myFile.setFile(finalFile);

		return true;
	}

}
