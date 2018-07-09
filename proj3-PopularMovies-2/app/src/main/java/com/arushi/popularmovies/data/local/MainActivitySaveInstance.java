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

package com.arushi.popularmovies.data.local;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arushi on 04/06/18.
 */

public class MainActivitySaveInstance implements Parcelable {
    private int position;
    private int totalPages;
    private String title;

    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }

    public int getTotalPages() {
        return totalPages;
    }
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.position);
        dest.writeInt(this.totalPages);
        dest.writeString(this.title);
    }

    public MainActivitySaveInstance() {
    }

    protected MainActivitySaveInstance(Parcel in) {
        this.position = in.readInt();
        this.totalPages = in.readInt();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<MainActivitySaveInstance> CREATOR = new Parcelable.Creator<MainActivitySaveInstance>() {
        @Override
        public MainActivitySaveInstance createFromParcel(Parcel source) {
            return new MainActivitySaveInstance(source);
        }

        @Override
        public MainActivitySaveInstance[] newArray(int size) {
            return new MainActivitySaveInstance[size];
        }
    };

    @Override
    public String toString() {
        return "MainActivitySaveInstance{" +
                "position=" + position +
                ", totalPages=" + totalPages +
                ", title=" + title +
                '}';
    }
}
