package model;

import java.io.File;

public class MyFile {

	private File file;
	private String sourceEncoding;
	private String targetEncoding;

	public MyFile(File file, String sourceEncoding, String targetEncoding) {
		super();
		this.file = file;
		this.sourceEncoding = sourceEncoding;
		this.targetEncoding = targetEncoding;
	}

	public MyFile() {
		super();
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getSourceEncoding() {
		return sourceEncoding;
	}

	public void setSourceEncoding(String sourceEncoding) {
		this.sourceEncoding = sourceEncoding;
	}

	public String getTargetEncoding() {
		return targetEncoding;
	}

	public void setTargetEncoding(String targetEncoding) {
		this.targetEncoding = targetEncoding;
	}

}
