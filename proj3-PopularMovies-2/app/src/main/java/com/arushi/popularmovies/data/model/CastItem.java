package com.arushi.popularmovies.data.model;

import com.arushi.popularmovies.utils.Constants;
import com.google.gson.annotations.SerializedName;

public class CastItem{

	@SerializedName("cast_id")
	private int castId;

	@SerializedName("character")
	private String character;

	@SerializedName("name")
	private String name;

	@SerializedName("profile_path")
	private String profilePath;

	@SerializedName("order")
	private int order;

	public void setCastId(int castId){
		this.castId = castId;
	}

	public int getCastId(){
		return castId;
	}

	public void setCharacter(String character){
		this.character = character;
	}

	public String getCharacter(){
		return character;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setProfilePath(String profilePath){
		this.profilePath = profilePath;
	}

	public String getProfilePath(){
		return Constants.IMAGE_BASE_URL + Constants.IMAGE_SIZE + profilePath;
	}

	public void setOrder(int order){
		this.order = order;
	}

	public int getOrder(){
		return order;
	}

	@Override
 	public String toString(){
		return 
			"CastItem{" + 
			"cast_id = '" + castId + '\'' + 
			",character = '" + character + '\'' + 
			",name = '" + name + '\'' +
			",profile_path = '" + profilePath + '\'' + 
			",order = '" + order + '\'' +
			"}";
		}
}