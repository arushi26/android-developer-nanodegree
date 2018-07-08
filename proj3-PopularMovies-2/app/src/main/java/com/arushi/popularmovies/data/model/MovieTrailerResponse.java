package com.arushi.popularmovies.data.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MovieTrailerResponse {

	@SerializedName("youtube")
	private List<YoutubeItem> youtube;

	@SerializedName("id")
	private int id;

	@SerializedName("quicktime")
	private List<Object> quicktime;

	public void setYoutube(List<YoutubeItem> youtube){
		this.youtube = youtube;
	}

	public List<YoutubeItem> getYoutube(){
		return youtube;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setQuicktime(List<Object> quicktime){
		this.quicktime = quicktime;
	}

	public List<Object> getQuicktime(){
		return quicktime;
	}

	@Override
 	public String toString(){
		return 
			"MovieTrailerResponse{" +
			"youtube = '" + youtube + '\'' + 
			",id = '" + id + '\'' + 
			",quicktime = '" + quicktime + '\'' + 
			"}";
		}
}