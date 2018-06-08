package com.arushi.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.arushi.popularmovies.data.model.Movie;
import com.arushi.popularmovies.utils.Constants;
import com.arushi.popularmovies.utils.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arushi on 30/05/18.
 */

public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Movie> mMovieList;
    private Context mContext;

    private static final int VIEW_TYPE_POSTER = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    private boolean mShowLoadingLayout = false;

    public MovieListAdapter(Context context){
        this.mContext = context;
        this.mMovieList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (viewType == VIEW_TYPE_LOADING){
            View view = inflater.inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);

        } else { // Movie Poster
            View view = inflater.inflate(R.layout.item_movie_poster, parent, false);
            return new MovieListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType){
            case VIEW_TYPE_POSTER:
                MovieListViewHolder movieHolder = (MovieListViewHolder) holder;
                Movie movie = mMovieList.get(position);

                movieHolder.itemView.setTag(movie.getId());

                GlideApp.with(mContext)
                        .load(movie.getPosterPath())
                        .thumbnail(0.1f)
                        .placeholder(R.drawable.ic_image)
                        .error(R.drawable.ic_broken_image)
                        .centerCrop()
                        .into(movieHolder.moviePosterView);
                break;
            case VIEW_TYPE_LOADING:
                break;
            default:
                Log.e("MovieAdapter","Illegal View type");
        }
    }

    @Override
    public int getItemCount() {
        if(mMovieList==null || mMovieList.size()==0) {
            return 0;
        } else if(mShowLoadingLayout && Constants.isOnline(mContext)) {
            return mMovieList.size() + 1;
        }

        return mMovieList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mShowLoadingLayout && position == mMovieList.size()){
            return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_POSTER;
        }
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{
        ImageView moviePosterView;

        public MovieListViewHolder(View itemView) {
            super(itemView);
            moviePosterView = itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Navigate to movie detail screen on poster click, if online
            if(!Constants.isOnline(mContext)){
                Toast.makeText(mContext,
                        mContext.getString(R.string.error_network_connection),
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(Constants.KEY_ID, view.getTag().toString());
                mContext.startActivity(intent);
            }
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder{
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void addMovieList(List<Movie> movieList)
    {
        this.mMovieList.addAll(movieList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash
        notifyDataSetChanged();
    }

    public void clearMovieList(){
        this.mMovieList.clear();
        notifyDataSetChanged();
    }

    public void showLoadingMore(boolean showLoading){
        this.mShowLoadingLayout = showLoading;
    }

    public void removeLoadingMore(){
        mShowLoadingLayout = false;
        notifyDataSetChanged();
    }

    public List<Movie> getMovieList(){
        return mMovieList;
    }
}
