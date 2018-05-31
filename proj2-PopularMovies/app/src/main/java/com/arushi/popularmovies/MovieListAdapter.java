package com.arushi.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arushi.popularmovies.data.model.Movie;
import com.arushi.popularmovies.utils.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arushi on 30/05/18.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder> {
    private List<Movie> mMovieList;
    private Context mContext;

    public MovieListAdapter(Context context){
        this.mContext = context;
        this.mMovieList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.item_movie_poster, parent, false);
        return new MovieListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);

        GlideApp.with(mContext)
                .load(movie.getPosterPath())
                .thumbnail(0.1f)
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .centerCrop()
                .into(holder.moviePosterView);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder{
        ImageView moviePosterView;

        public MovieListViewHolder(View itemView) {
            super(itemView);
            moviePosterView = itemView.findViewById(R.id.iv_movie_poster);
        }
    }

    public void setMovieList(List<Movie> movieList)
    {
        this.mMovieList.clear();
        this.mMovieList.addAll(movieList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash
        notifyDataSetChanged();
    }

}
