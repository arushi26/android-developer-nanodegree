/*
 *
 *  *
 *  *  This project was submitted by Arushi Pant as part of the Android Developer Nanodegree at Udacity.
 *  *
 *  *  As part of Udacity Honor code, your submissions must be your own work, hence
 *  *  submitting this project as yours will cause you to break the Udacity Honor Code
 *  *  and the suspension of your account.
 *  *
 *  *  I, the author of the project, allow you to check the code as a reference, but if
 *  *  you submit it, it's your own responsibility if you get expelled.
 *  *
 *  *  Besides the above notice, the MIT license applies and this license notice
 *  *  must be included in all works derived from this project
 *  *
 *  *  Copyright (c) 2018 Arushi Pant
 *  *
 *
 */

package com.arushi.bakingapp.data.local.entity;

import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "steps",
        primaryKeys = {"id","dessertId"})
public class StepEntity
    implements Parcelable {

    private int id;
    private String videoURL;
    private String description;
    private String shortDescription;
    private String thumbnailURL;
    private int dessertId;

    public StepEntity(int id, String videoURL, String description,
                         String shortDescription, String thumbnailURL, int dessertId){
        this.id = id;
        this.videoURL = videoURL;
        this.description = description;
        this.shortDescription = shortDescription;
        this.thumbnailURL = thumbnailURL;
        this.dessertId =dessertId;
    }

    public int getId() {
        return id;
    }

    public int getDessertId() {
        return dessertId;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.id);
        parcel.writeInt(this.dessertId);
        parcel.writeString(this.videoURL);
        parcel.writeString(this.description);
        parcel.writeString(this.shortDescription);
        parcel.writeString(this.thumbnailURL);
    }

    public static final Parcelable.Creator<StepEntity> CREATOR
            = new Parcelable.Creator<StepEntity>() {
        public StepEntity createFromParcel(Parcel in) {
            return new StepEntity(in);
        }

        public StepEntity[] newArray(int size) {
            return new StepEntity[size];
        }
    };

    private StepEntity(Parcel in) {
        this.id = in.readInt();
        this.dessertId = in.readInt();
        this.videoURL = in.readString();
        this.description = in.readString();
        this.shortDescription = in.readString();
        this.thumbnailURL = in.readString();
    }

}
