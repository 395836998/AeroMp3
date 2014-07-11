package cn.zhanglian2010.mp3.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import cn.zhanglian2010.mp3.model.Mp3Info;
import cn.zhanglian2010.mp3.util.AppConstant;
import cn.zhanglian2010.mp3.util.HttpDownloader;

public class DownloadService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Mp3Info info = (Mp3Info)intent.getSerializableExtra("info");
		System.out.println(info.getMp3Name());
		
		new DownloadThread(info.getMp3Name()).start();
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private class DownloadThread extends Thread {
		
		private String mp3Name;
		
		public DownloadThread(String mp3Name) {
			this.mp3Name = mp3Name;
		}
		@Override
		public void run() {
			//下载
			
			String mp3Url = AppConstant.URL.BASE_URL + mp3Name;
			HttpDownloader httpDownloader = new HttpDownloader();
			int result = httpDownloader.downFile(mp3Url, "mp3/", mp3Name);
			
			if(result == -1){
				System.out.println("失败。。。");
			}else if(result == 0){
				System.out.println("已下载。。。");
			}else if(result == 1){
				System.out.println("下载成功。。。");
			}
			
		}
	}

}
