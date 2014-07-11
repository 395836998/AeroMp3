package cn.zhanglian2010.mp3;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import cn.zhanglian2010.mp3.model.Mp3Info;
import cn.zhanglian2010.mp3.service.DownloadService;
import cn.zhanglian2010.mp3.util.HttpDownloader;
import cn.zhanglian2010.mp3.util.Mp3ListContentHandler;

public class DownloadActivity extends ListActivity {

	private List<Mp3Info> mp3InfoList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		
		//在线获取可用列表
		queryMp3List();
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			System.out.println(msg.obj);
			mp3InfoList = parse(msg.obj + "");
			
			List<HashMap<String, String> > data = new ArrayList<HashMap<String, String>>();
			for(Mp3Info info : mp3InfoList){
				HashMap<String, String> item1 = new HashMap<String, String>();
				item1.put("mp3_name", info.getMp3Name());
				item1.put("mp3_size", info.getMp3Size());
				data.add(item1);
			}
			
			SimpleAdapter adapter = new SimpleAdapter(DownloadActivity.this, data, R.layout.mp3_list_item,
					new String[]{"mp3_name", "mp3_size"}, new int[]{R.id.mp3NameId, R.id.mp3SizeId });
			
			setListAdapter(adapter);
		}
	};
	
	private List<Mp3Info> parse(String xmlStr) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		List<Mp3Info> infos = new ArrayList<Mp3Info>();
		try {
			XMLReader xmlReader = saxParserFactory.newSAXParser()
					.getXMLReader();
			Mp3ListContentHandler mp3ListContentHandler = new Mp3ListContentHandler(
					infos);
			xmlReader.setContentHandler(mp3ListContentHandler);
			xmlReader.parse(new InputSource(new StringReader(xmlStr)));
			for (Iterator<Mp3Info> iterator = infos.iterator(); iterator.hasNext();) {
				Mp3Info mp3Info = (Mp3Info) iterator.next();
				System.out.println(mp3Info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}
	
	
	private void queryMp3List(){
		new Thread(new Mp3LoaderThread()).start();
	}
	
	class Mp3LoaderThread implements Runnable {
		@Override
		public void run() {
			
			HttpDownloader httpDownloader = new HttpDownloader();
			String result = httpDownloader.download("http://192.168.1.104:8080/mp3/resources.xml");
			
			Message msg = handler.obtainMessage();
			msg.obj = result;
			handler.sendMessage(msg);
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if(mp3InfoList != null){
			Mp3Info info = mp3InfoList.get(position);
			//下载该MP3
			Intent intent = new Intent();
			intent.putExtra("info", info);
			intent.setClass(this, DownloadService.class);
			startService(intent);
		}
	}

}
