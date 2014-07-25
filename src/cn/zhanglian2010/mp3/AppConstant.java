package cn.zhanglian2010.mp3;

import java.io.File;

public interface AppConstant {
	
	public class ActionMsg {
		public static final int MSG_PREV = 10;
		public static final int MSG_PLAY = 20;
		public static final int MSG_PAUSE = 30;
		public static final int MSG_STOP = 40;
		public static final int MSG_NEXT = 50;
		public static final int MSG_PROGRESS_JUMP = 60;
		public static final int MSG_BIND_LISTENER = 100;
		
	}
	
	public class Params {
		public static final String PARAM_MP3_INDEX = "MP3_INDEX";
		public static final String PARAM_MP3_PLAY_LIST = "MP3_PLAY_LIST";
		public static final String PARAM_MP3_PROGRESS = "MP3_PROGRESS";
		public static final String PARAM_ACTION_MSG = "ACTION_MSG";
		public static final String PARAM_SERVICE_STOP = "cn.zhanglian2010.mp3.util.SERVICE_STOP";
	}
	
	public class Path {
		public static final String BASE_PATH = "mp3" + File.separator;
	}
}
