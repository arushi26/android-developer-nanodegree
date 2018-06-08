package com.arushi.popularmovies.data.local;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.arushi.popularmovies.data.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arushi on 04/06/18.
 */

public class MainActivitySaveInstance implements Parcelable {
    private int position;
    private List<Movie> movieList;
    private int nextPage;
    private int totalPages;
    private int sorting;
    private String title;

    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }

    @NonNull
    public List<Movie> getMovieList() {
        if(movieList==null) return new ArrayList<>();
        return movieList;
    }
    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public int getNextPage() {
        return nextPage;
    }
    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getTotalPages() {
        return totalPages;
    }
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getSorting() {
        return sorting;
    }
    public void setSorting(int sorting) {
        this.sorting = sorting;
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
        dest.writeList(this.movieList);
        dest.writeInt(this.nextPage);
        dest.writeInt(this.totalPages);
        dest.writeInt(this.sorting);
        dest.writeString(this.title);
    }

    public MainActivitySaveInstance() {
    }

    protected MainActivitySaveInstance(Parcel in) {
        this.position = in.readInt();
        this.movieList = new ArrayList<>();
        in.readList(this.movieList, Movie.class.getClassLoader());
        this.nextPage = in.readInt();
        this.totalPages = in.readInt();
        this.sorting = in.readInt();
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
                ", movieList=" + movieList +
                ", nextPage=" + nextPage +
                ", totalPages=" + totalPages +
                ", sorting=" + sorting +
                ", title=" + title +
                '}';
    }
}
