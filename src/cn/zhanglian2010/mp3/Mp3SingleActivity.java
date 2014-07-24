package cn.zhanglian2010.mp3;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.zhanglian2010.mp3.ctx.Mp3Application;
import cn.zhanglian2010.mp3.model.Mp3Info;
import cn.zhanglian2010.mp3.service.PlayService;
import cn.zhanglian2010.mp3.util.AppConstant;

public class Mp3SingleActivity extends Activity {
	
	private ImageButton prevButton;
	private ImageButton playButton;
	private ImageButton nextButton;
	
	private TextView titleView;
	
	private List<Mp3Info> mp3List;
	
	private int index;
	private int playStatus = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		System.out.println("oncreate...");
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
		
		Mp3Application app = (Mp3Application) getApplication();
		mp3List = app.getMp3List();
		
		Intent actIntent = getIntent();
		index = actIntent.getIntExtra(AppConstant.Params.PARAM_MP3_INDEX, 0);
		
		titleView = (TextView) findViewById(R.id.mp3TitleId);
		titleView.setText(mp3List.get(index).getMp3Name());
		
		play();
	}
	

	private void play() {
		sendServiceMsg(AppConstant.ActionMsg.MSG_PLAY);
		playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_media_pause));
		playStatus = 1;
	}
	
	private void sendServiceMsg(int msgType){
		Intent intent = new Intent();
		intent.putExtra(AppConstant.Params.PARAM_MP3_INFO, mp3List.get(index));
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
		if(index > 0){
			index--;
		}
		titleView.setText(mp3List.get(index).getMp3Name());
		play();
	}
	
	private void next() {
		if(index < mp3List.size()-1){
			index++;
		}
		titleView.setText(mp3List.get(index).getMp3Name());
		play();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mp3_single, menu);
		return true;
	}

}
