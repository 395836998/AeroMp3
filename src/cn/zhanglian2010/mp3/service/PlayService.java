package cn.zhanglian2010.mp3.service;

import java.io.File;
import java.io.IOException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import cn.zhanglian2010.mp3.model.Mp3Info;
import cn.zhanglian2010.mp3.util.AppConstant;
import cn.zhanglian2010.mp3.util.FileUtils;

public class PlayService extends Service {

	private MediaPlayer player = null;
	private Mp3Info info = null;
	private StopServiceBroadcastReceiver stopReceiver;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		//初始化媒体播放器
		player = new MediaPlayer();
		
		stopReceiver = new StopServiceBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppConstant.Params.PARAM_SERVICE_STOP);
		this.registerReceiver(stopReceiver, filter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Mp3Info info1 = (Mp3Info) intent.getSerializableExtra(AppConstant.Params.PARAM_MP3_INFO);
		int actionMsg = intent.getIntExtra(AppConstant.Params.PARAM_ACTION_MSG, 0);
		
		switch (actionMsg) {
			case AppConstant.ActionMsg.MSG_PLAY:
				play(info1);
				break;
			case AppConstant.ActionMsg.MSG_PAUSE:
				pause();
				break;
			case AppConstant.ActionMsg.MSG_STOP:
				stop();
				break;
			default:
				stop();
				break;
		}
		return START_NOT_STICKY;
	}
	
	private void play(Mp3Info info1){

		if(this.info == null || !this.info.getMp3Name().equals(info1.getMp3Name())){
			//第一次播放 或者 换播不同的歌
			this.info = info1;
			String path = getMp3Path(this.info);
			try {
				//重置MediaPlayer
				player.reset();
				//设置要播放的文件的路径
				player.setDataSource(path);
				//准备播放
				player.prepare();
				//不循环
				player.setLooping(false);
				//开始播放
				player.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(!player.isPlaying()){
			player.start();
		}
	}
	
	private void pause(){
		player.pause();
	}
	
	private void stop(){
		player.stop();
		player.release();
		this.info = null;
	}
	
	@Override
	public void onDestroy() {
		player.release();

		if (stopReceiver != null) {
			this.unregisterReceiver(stopReceiver);
			stopReceiver = null;
		}
		super.onDestroy();
		System.out.println("destroy...");
	}
	
	private String getMp3Path(Mp3Info mp3Info) {
		String path = FileUtils.SD_ROOT + AppConstant.Path.BASE_PATH + File.separator + mp3Info.getMp3Name();
		return path;
	}
	
	
	private class StopServiceBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("stopping...");
			PlayService.this.stopSelf();
//			PlayService.this.onDestroy();
		}
		
	}
}
