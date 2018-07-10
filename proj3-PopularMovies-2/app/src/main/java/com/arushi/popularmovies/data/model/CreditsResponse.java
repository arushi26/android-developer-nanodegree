package com.arushi.popularmovies.data.model;
/*
 * This project was submitted by Arushi Pant as part of the Android Developer Nanodegree at Udacity.
 *
 * As part of Udacity Honor code, your submissions must be your own work, hence
 * submitting this project as yours will cause you to break the Udacity Honor Code
 * and the suspension of your account.
 *
 * I, the author of the project, allow you to check the code as a reference, but if
 * you submit it, it's your own responsibility if you get expelled.
 *
 * Besides the above notice, the MIT license applies and this license notice
 * must be included in all works derived from this project
 *
 * Copyright (c) 2018 Arushi Pant
 */

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