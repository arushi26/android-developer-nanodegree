package com.arushi.popularmovies.data.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class VideoResponse{

	@SerializedName("id")
	private int id;

	@SerializedName("results")
	private List<YoutubeItem> results;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setResults(List<YoutubeItem> results){
		this.results = results;
	}

	public List<YoutubeItem> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"VideoResponse{" + 
			"id = '" + id + '\'' + 
			",results = '" + results + '\'' + 
			"}";
		}
}