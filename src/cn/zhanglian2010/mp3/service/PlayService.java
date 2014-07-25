package cn.zhanglian2010.mp3.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import cn.zhanglian2010.mp3.AppConstant;
import cn.zhanglian2010.mp3.Mp3Application;
import cn.zhanglian2010.mp3.core.PlayEngine;
import cn.zhanglian2010.mp3.core.PlayEngineImpl;

public class PlayService extends Service {

	private PlayEngine playEngine;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		//初始化媒体播放器引擎
		playEngine = new PlayEngineImpl();
		
		System.out.println("service create...");
		IntentFilter intentFilter = new IntentFilter( );
		intentFilter.addAction(STOP_RECEIVED);
		registerReceiver(receiver , intentFilter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		int actionMsg = intent.getIntExtra(AppConstant.Params.PARAM_ACTION_MSG, 0);
		
		switch (actionMsg) {
			case AppConstant.ActionMsg.MSG_BIND_LISTENER:
				playEngine.setPlayEngineListener(Mp3Application.getInstance().getPlayEngineListener());
				break;
			case AppConstant.ActionMsg.MSG_PLAY:
				playEngine.play();
				break;
			case AppConstant.ActionMsg.MSG_PAUSE:
				playEngine.pause();
				break;
			case AppConstant.ActionMsg.MSG_PROGRESS_JUMP:
				int progress = intent.getIntExtra(AppConstant.Params.PARAM_MP3_PROGRESS, 0);
				playEngine.progressTo(progress);
				break;
			case AppConstant.ActionMsg.MSG_PREV:
				playEngine.prev();
				break;
			case AppConstant.ActionMsg.MSG_NEXT:
				playEngine.next();
				break;
			case AppConstant.ActionMsg.MSG_STOP:
				playEngine.stop();
				break;
			default:
				
				break;
		}
		return START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy() {
		System.out.println("destr");
		playEngine.release();
		unregisterReceiver(receiver);
		super.onDestroy();
	}
	
	
	public static final String STOP_RECEIVED = "CN.ZHANGLIAN2010.MP3.SERVICE.STOPPLAYSERVICE";
	  
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("stop...");
			stopSelf();
		}
		
	};
	
}
