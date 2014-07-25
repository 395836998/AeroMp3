package cn.zhanglian2010.mp3.ui;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import cn.zhanglian2010.mp3.AppConstant;
import cn.zhanglian2010.mp3.Mp3Application;
import cn.zhanglian2010.mp3.model.Mp3Info;
import cn.zhanglian2010.mp3.model.Mp3PlayList;
import cn.zhanglian2010.mp3.service.PlayService;
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
		int id = 1;
		for (File file : files) {
			Mp3Info mp3Info = new Mp3Info();
			mp3Info.setMp3Id(id++ + "");
			mp3Info.setMp3Name(file.getName());
			mp3Info.setMp3Size(file.length() + "");
			mp3Infos.add(mp3Info);
		}
		mp3List = mp3Infos;

		Mp3PlayList playList = new Mp3PlayList(mp3List);
		Mp3Application.getInstance().setPlayList(playList);
		
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
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "关于").setIcon(android.R.drawable.ic_dialog_info); 
        menu.add(Menu.NONE, Menu.FIRST + 2, 2,"退出").setIcon(android.R.drawable.ic_lock_power_off);
        return true; 
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == Menu.FIRST + 1){ 
			Toast.makeText(this, "关于ZeroMp3", Toast.LENGTH_LONG).show();
        }if(item.getItemId() == Menu.FIRST + 2){ 
        	showTips();
        }else{ 
            return super.onOptionsItemSelected(item); 
        } 
        return true; 
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Mp3Application.getInstance().getPlayList().move(position);
		Intent intent = new Intent();
		intent.setClass(this, Mp3SingleActivity.class);
		startActivity(intent);
	}

	private void showTips() {

		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("是否退出ZeroMp3?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent();
						intent.setAction(PlayService.STOP_RECEIVED);
						sendBroadcast(intent);  
						
						System.out.println("sadasdasd");
						
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
