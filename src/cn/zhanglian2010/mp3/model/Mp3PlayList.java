package cn.zhanglian2010.mp3.model;

import java.util.ArrayList;
import java.util.List;

public class Mp3PlayList {

	private List<Mp3Info> playList;
	
	private int currentIndex = 0;
	
	public Mp3PlayList(){
		this.playList = new ArrayList<Mp3Info>();
	}
	
	public Mp3PlayList(List<Mp3Info> playList){
		if(playList == null){
			playList = new ArrayList<Mp3Info>();
		}
		this.playList = playList;
	}
	
	public Mp3PlayList(List<Mp3Info> playList, int index){
		this(playList);
		this.currentIndex = index;
	}

	public List<Mp3Info> getPlayList() {
		return playList;
	}

	public void setPlayList(List<Mp3Info> playList) {
		this.playList = playList;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void move(int index) {
		if(playList.isEmpty()){
			return;
		}
		if(index >= 0 && index < playList.size()){
			this.currentIndex = index;
		}
	}
	
	public void movePrev(){
		if(playList.isEmpty()){
			return;
		}
		if(currentIndex > 0){
			currentIndex--;
		}
	}
	
	public Mp3Info curr(){
		if(playList.isEmpty()){
			return null;
		}
		return playList.get(currentIndex);
	}
	
	public String currName(){
		if(playList.isEmpty()){
			return "";
		}
		return curr().getMp3Name();
	}
	
	public void moveNext(){
		if(playList.isEmpty()){
			return;
		}
		if(currentIndex < playList.size()-1){
			currentIndex++;
		}
	}
}
