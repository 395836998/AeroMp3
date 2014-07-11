package cn.zhanglian2010.mp3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import cn.zhanglian2010.mp3.model.Mp3Info;
import cn.zhanglian2010.mp3.service.PlayService;
import cn.zhanglian2010.mp3.util.AppConstant;
import cn.zhanglian2010.mp3.util.FileUtils;

public class MainActivity extends ListActivity {
	
	private List<Mp3Info> mp3List = null;
	private String startedName = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		FileUtils fileUtil = new FileUtils();
		mp3List = fileUtil.getMp3Files("mp3/");
		
		List<HashMap<String, String> > data = new ArrayList<HashMap<String, String>>();
		if(mp3List != null){
			for(Mp3Info info : mp3List){
				HashMap<String, String> item1 = new HashMap<String, String>();
				item1.put("mp3_name", info.getMp3Name());
				item1.put("mp3_size", info.getMp3Size());
				data.add(item1);
			}
		}else{
			HashMap<String, String> item1 = new HashMap<String, String>();
			item1.put("mp3_name", "没有本地mp3文件");
			item1.put("mp3_size", "0");
			data.add(item1);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, data, 
				R.layout.mp3_list_item, new String[]{"mp3_name", "mp3_size"}, new int[]{R.id.mp3NameId, R.id.mp3SizeId });

		setListAdapter(adapter);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0, 1, 1, "在线下载");
		menu.add(0, 2, 2, "关于");
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == 1){
			System.out.println("在线下载");
			
			Intent intent = new Intent();
			intent.setClass(this, DownloadActivity.class);
			startActivity(intent);
			
		}else{
			System.out.println("关于");
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if(mp3List != null){
			Mp3Info info = mp3List.get(position);
			System.out.println("播放"+info.getMp3Name());
			
			Intent intent = new Intent();
			intent.putExtra("info", info);
			intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
			intent.setClass(this, PlayService.class);
			
			startService(intent);
		}
		
		super.onListItemClick(l, v, position, id);
	}

}
