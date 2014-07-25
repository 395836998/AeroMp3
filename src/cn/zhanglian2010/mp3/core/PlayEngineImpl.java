package cn.zhanglian2010.mp3.core;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import cn.zhanglian2010.mp3.AppConstant;
import cn.zhanglian2010.mp3.Mp3Application;
import cn.zhanglian2010.mp3.model.Mp3Info;
import cn.zhanglian2010.mp3.model.Mp3PlayList;
import cn.zhanglian2010.mp3.util.FileUtils;

public class PlayEngineImpl implements PlayEngine {

	private MyMediaPlayer mediaPlayer;

	private Mp3PlayList playList;

	private PlayEngineListener listener;
	
	private Handler handler;

	public PlayEngineImpl(){
		handler = new Handler();
	}
	
	private class MyMediaPlayer extends MediaPlayer {
		public Mp3Info playingMp3Info;
	}
	
	private Runnable updateProgressTask = new Runnable() {
		public void run() {
			if(listener != null){
				if(mediaPlayer != null){
					listener.onProgressChanged(mediaPlayer.getCurrentPosition() / 1000);
				}
				handler.postDelayed(this, 1000);
			}
		}
	};
 

	@Override
	public boolean isPlaying() {
		if(mediaPlayer == null){
			return false;
		}
		return mediaPlayer.isPlaying();
	}

	@Override
	public void play() {
		if(playList == null){
			return;
		}
		Mp3Info info = playList.curr();
		if(info == null){
			return;
		}

		if(mediaPlayer == null){
			//第一次播放初始化播放器
			mediaPlayer = createPlayer(info);
			if(listener != null){
				listener.onMp3Changed(info);
			}
		}else if( !mediaPlayer.playingMp3Info.getMp3Id().equals(info.getMp3Id()) ){
			//换了另外一首歌播放
			mediaPlayer = resetPlayer(info);
		}
		if(!mediaPlayer.isPlaying()){
			handler.removeCallbacks(updateProgressTask);
			handler.postDelayed(updateProgressTask, 1000);
			mediaPlayer.start();
		}
		
		System.out.println(mediaPlayer.playingMp3Info);
		System.out.println(info);
		
		if(listener != null){
			listener.onStart(mediaPlayer.getDuration());
		}
	}

	private MyMediaPlayer createPlayer(Mp3Info info){
		MyMediaPlayer player = new MyMediaPlayer();
		return preparePlayer(player, info);
	}

	private MyMediaPlayer resetPlayer(Mp3Info info){
		mediaPlayer.reset();
		return preparePlayer(mediaPlayer, info);
	}

	private MyMediaPlayer preparePlayer(MyMediaPlayer player, Mp3Info info){
		String path = getMp3Path(info);
		try {
			//设置要播放的文件的路径
			player.setDataSource(path);
			//设置当前播放文件为指定文件
			player.playingMp3Info = info;
			//准备播放
			player.prepare();
			//不循环
			player.setLooping(false);
			//播放完毕后，设置监听事件
			player.setOnCompletionListener(new OnCompletionListener(){
				@Override
				public void onCompletion(MediaPlayer mediaPlayer) {
					next();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return player;
	}

	private String getMp3Path(Mp3Info mp3Info) {
		String path = FileUtils.SD_ROOT + AppConstant.Path.BASE_PATH + File.separator + mp3Info.getMp3Name();
		return path;
	}

	@Override
	public void pause() {
		if(mediaPlayer == null){
			return;
		}
		mediaPlayer.pause();
		handler.removeCallbacks(updateProgressTask);
		if(listener != null){
			listener.onPause();
		}
	}

	@Override
	public void prev() {
		playList.movePrev();
		play();
		if(listener != null){
			listener.onMp3Changed(playList.curr());
		}
	}

	@Override
	public void next() {
		playList.moveNext();
		play();
		if(listener != null){
			listener.onMp3Changed(playList.curr());
		}
	}

	@Override
	public void stop() {
		cleanUp();
		if(listener != null){
			listener.onStop();
		}
	}

	private void cleanUp(){
		if(mediaPlayer != null){
			try{
				mediaPlayer.stop();
			} catch (IllegalStateException e){
				// this may happen sometimes
			} finally {
				mediaPlayer.release();
				mediaPlayer = null;
			}
		}
	}

	@Override
	public void setPlayList(Mp3PlayList playList) {
		this.playList = playList;
	}

	@Override
	public void setPlayEngineListener(PlayEngineListener listener) {
		this.listener = listener;
		playList = Mp3Application.getInstance().getPlayList();
	}


	@Override
	public void progressTo(int progress) {
		if(mediaPlayer == null){
			return;
		}
		Mp3Info info = playList.curr();
		if(info == null){
			return;
		}
		
		if(progress >= 0){
			mediaPlayer.seekTo(progress * 1000);
		}
	}

	@Override
	public void release() {
		cleanUp();
	}

}
