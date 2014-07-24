package cn.zhanglian2010.mp3;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import cn.zhanglian2010.mp3.ctx.Mp3Application;
import cn.zhanglian2010.mp3.model.Mp3Info;
import cn.zhanglian2010.mp3.util.AppConstant;
import cn.zhanglian2010.mp3.util.FileUtils;

public class Mp3ListActivity extends ListActivity {

	private List<Mp3Info> mp3List = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp3_list);
		
		scanMp3Files();
		fillListData();
	}

	private void scanMp3Files() {

		File mp3Path = new File(FileUtils.SD_ROOT + AppConstant.Path.BASE_PATH);

		File[] files = mp3Path.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if (filename.endsWith(".mp3")) {
					return true;
				}
				return false;
			}
		});

		if(files == null){
			mp3List = Collections.emptyList();
			return;
		}

		List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
		for (File file : files) {
			Mp3Info mp3Info = new Mp3Info();
			mp3Info.setMp3Name(file.getName());
			mp3Info.setMp3Size(file.length() + "");
			mp3Infos.add(mp3Info);
		}
		mp3List = mp3Infos;

		Mp3Application app = (Mp3Application) getApplication();
		app.setMp3List(mp3List);

	}

	private void fillListData() {
		List<HashMap<String, String> > data = new ArrayList<HashMap<String, String>>();
		for(Mp3Info info : mp3List){
			HashMap<String, String> item1 = new HashMap<String, String>();
			item1.put("mp3_name", info.getMp3Name());
			item1.put("mp3_size", info.getMp3Size());
			data.add(item1);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, data, 
				R.layout.mp3_list_item, new String[]{"mp3_name", "mp3_size"}, new int[]{R.id.mp3NameId, R.id.mp3SizeId });

		setListAdapter(adapter);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Intent intent = new Intent();
		intent.putExtra(AppConstant.Params.PARAM_MP3_INDEX, position);
		intent.setClass(this, Mp3SingleActivity.class);

		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			showTips();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showTips() {

		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("是否退出ZeroMp3?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(AppConstant.Params.PARAM_SERVICE_STOP);
						sendBroadcast(intent);
						
						Mp3ListActivity.this.finish();
					}

				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).create(); // 创建对话框
		
		// 显示对话框
		alertDialog.show(); 
	}

}
