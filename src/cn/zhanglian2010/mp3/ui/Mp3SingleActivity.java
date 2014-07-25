package cn.zhanglian2010.mp3.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import cn.zhanglian2010.mp3.AppConstant;
import cn.zhanglian2010.mp3.Mp3Application;
import cn.zhanglian2010.mp3.core.PlayEngineListener;
import cn.zhanglian2010.mp3.model.Mp3Info;
import cn.zhanglian2010.mp3.service.PlayService;

public class Mp3SingleActivity extends Activity {
	
	private ImageButton prevButton;
	private ImageButton playButton;
	private ImageButton nextButton;
	
	private TextView titleView;
	private SeekBar progressBar;
	
	private int playStatus = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		System.out.println("create...");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp3_single);
		
		playButton = (ImageButton) findViewById(R.id.playPauseId);
		playButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(playStatus == 0){
					//播放
					play();
				}else if(playStatus == 1){
					//暂停
					pause();
				}
			}
		});
		
		prevButton = (ImageButton) findViewById(R.id.prevId);
		prevButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				prev();
			}
		});
		
		nextButton = (ImageButton) findViewById(R.id.nextId);
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				next();
			}
		});
		
		Mp3Application.getInstance().setPlayEngineListener(playEngineListener);
		Intent intent = new Intent();
		intent.putExtra(AppConstant.Params.PARAM_ACTION_MSG, AppConstant.ActionMsg.MSG_BIND_LISTENER);
		intent.setClass(this, PlayService.class);
		startService(intent);
		
		titleView = (TextView) findViewById(R.id.mp3InfoId);
		titleView.setText(Mp3Application.getInstance().getPlayList().currName());
		
		progressBar = (SeekBar) findViewById(R.id.mp3ProgressId);
		progressBar.setOnSeekBarChangeListener(new ProgressChangeListener());
		
		play();
	}
	
	private void play() {
		sendServiceMsg(AppConstant.ActionMsg.MSG_PLAY);
		playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_media_pause));
		playStatus = 1;
		
	}
	
	private void sendServiceMsg(int msgType){
		Intent intent = new Intent();
		intent.putExtra(AppConstant.Params.PARAM_ACTION_MSG, msgType);
		intent.setClass(this, PlayService.class);
		startService(intent);
	}
	
	private void pause(){
		sendServiceMsg(AppConstant.ActionMsg.MSG_PAUSE);
		playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_media_play));
		playStatus = 0;
	}
	
	private void prev() {
		sendServiceMsg(AppConstant.ActionMsg.MSG_PREV);
	}
	
	private void next() {
		sendServiceMsg(AppConstant.ActionMsg.MSG_NEXT);
	}
	
	
	private class ProgressChangeListener implements SeekBar.OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			Intent intent = new Intent();
			intent.putExtra(AppConstant.Params.PARAM_ACTION_MSG, AppConstant.ActionMsg.MSG_PROGRESS_JUMP);
			intent.putExtra(AppConstant.Params.PARAM_MP3_PROGRESS, progressBar.getProgress());
			intent.setClass(Mp3SingleActivity.this, PlayService.class);
			
			startService(intent);
		}
		
	}
	
	private PlayEngineListener playEngineListener = new PlayEngineListener(){

		@Override
		public void onStart(int millstime) {
			progressBar.setMax(millstime / 1000);
		}

		@Override
		public void onPause() {
			
		}

		@Override
		public void onStop() {
			
		}

		@Override
		public void onMp3Changed(Mp3Info info) {
			titleView.setText( info.getMp3Name() );
			progressBar.setProgress(0);
		}

		@Override
		public void onProgressChanged(int progress) {
			progressBar.setProgress(progress);
		}
		
	};
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mp3_single, menu);
		return true;
	}

}
