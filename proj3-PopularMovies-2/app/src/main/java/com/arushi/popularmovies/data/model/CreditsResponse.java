package com.arushi.popularmovies.data.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CreditsResponse{

	@SerializedName("cast")
	private List<CastItem> cast;

	@SerializedName("id")
	private int id;

	public void setCast(List<CastItem> cast){
		this.cast = cast;
	}

	public List<CastItem> getCast(){
		return cast;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"CreditsResponse{" + 
			"cast = '" + cast + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}