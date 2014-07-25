package cn.zhanglian2010.mp3;

import android.app.Application;
import cn.zhanglian2010.mp3.core.PlayEngineListener;
import cn.zhanglian2010.mp3.model.Mp3PlayList;

public class Mp3Application extends Application {

	private static Mp3Application instance;
	
	private Mp3PlayList playList;
	
	private PlayEngineListener playEngineListener;
	
	@Override
	public void onCreate() {
		System.out.println("app create...");
		super.onCreate();
		instance = this;
	}

	public static Mp3Application getInstance() {
		return instance;
	}

	public Mp3PlayList getPlayList() {
		return playList;
	}

	public void setPlayList(Mp3PlayList playList) {
		this.playList = playList;
	}

	public PlayEngineListener getPlayEngineListener() {
		return playEngineListener;
	}

	public void setPlayEngineListener(PlayEngineListener playEngineListener) {
		this.playEngineListener = playEngineListener;
	}
	
}
