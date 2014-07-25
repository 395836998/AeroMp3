package cn.zhanglian2010.mp3.core;

import cn.zhanglian2010.mp3.model.Mp3Info;

public interface PlayEngineListener {

	void onStart(int millstime);
	
	void onPause();
	
	void onStop();
	
	void onMp3Changed(Mp3Info info);
	
	void onProgressChanged(int progress);
	
}
