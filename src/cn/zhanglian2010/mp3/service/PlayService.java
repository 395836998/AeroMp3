package cn.zhanglian2010.mp3.service;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import cn.zhanglian2010.mp3.model.Mp3Info;
import cn.zhanglian2010.mp3.util.AppConstant;

public class PlayService extends Service {

	private MediaPlayer player = null;
	private Mp3Info info = null;
	
	private boolean hasPlayer = false;
	private boolean isPlaying = false;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Mp3Info info1 = (Mp3Info)intent.getSerializableExtra("info");
		int MSG = intent.getIntExtra("MSG", 0);
		System.out.println(MSG);
		
		if(MSG == AppConstant.PlayerMsg.PLAY_MSG){
			if(isPlaying){
				if(info1.getMp3Name().equals(info.getMp3Name())){
					pause();
				}else{
					stop();
					play(info1);
				}
			}else{
				play(info1);
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void play(Mp3Info info){
		this.info = info;
		if(!hasPlayer){
			String path = getMp3Path(info);
			System.out.println(path);
			player = MediaPlayer.create(this, Uri.parse("file://"+path) );
			player.setLooping(false);
			player.start();
			hasPlayer = true;
		}else{
			player.start();
		}
		isPlaying = true;
	}
	
	private void pause(){
		player.pause();
		isPlaying = false;
	}
	
	private void stop(){
		player.stop();
		player.release();
		hasPlayer = false;
		isPlaying = false;
	}
	
	private String getMp3Path(Mp3Info mp3Info) {
		String SDCardRoot=Environment.getExternalStorageDirectory().getAbsolutePath();
		String path=SDCardRoot+File.separator+"mp3"+File.separator+mp3Info.getMp3Name();
		return path;
	}
	
	
}
