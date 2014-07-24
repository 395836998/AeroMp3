package cn.zhanglian2010.mp3.ctx;

import java.util.List;

import cn.zhanglian2010.mp3.model.Mp3Info;
import android.app.Application;

public class Mp3Application extends Application {

	private List<Mp3Info> mp3List;

	public List<Mp3Info> getMp3List() {
		return mp3List;
	}

	public void setMp3List(List<Mp3Info> mp3List) {
		this.mp3List = mp3List;
	}
	
}
