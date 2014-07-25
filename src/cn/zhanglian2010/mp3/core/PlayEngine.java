package cn.zhanglian2010.mp3.core;

import cn.zhanglian2010.mp3.model.Mp3PlayList;

public interface PlayEngine {

	boolean isPlaying();
	
	void play();
	
	void pause();
	
	void prev();
	
	void next();
	
	void stop();
	
	void release();
	
	void progressTo(int progress);
	
	void setPlayList(Mp3PlayList playList);
	
	void setPlayEngineListener(PlayEngineListener listener);
	
}
