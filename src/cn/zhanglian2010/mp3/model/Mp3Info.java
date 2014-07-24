package cn.zhanglian2010.mp3.model;

import java.io.Serializable;

public class Mp3Info implements Serializable {

	private static final long serialVersionUID = 6737380669853664271L;

	private String mp3Id;
	
	private String mp3Name;
	
	private String mp3Size;


	public String getMp3Id() {
		return mp3Id;
	}

	public void setMp3Id(String mp3Id) {
		this.mp3Id = mp3Id;
	}

	public String getMp3Name() {
		return mp3Name;
	}

	public void setMp3Name(String mp3Name) {
		this.mp3Name = mp3Name;
	}

	public String getMp3Size() {
		return mp3Size;
	}

	public void setMp3Size(String mp3Size) {
		this.mp3Size = mp3Size;
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append(this.getMp3Id()).append(";")
				.append(this.getMp3Name()).append(";")
				.append(this.getMp3Size()).append(";")
				.toString();
	}
}
