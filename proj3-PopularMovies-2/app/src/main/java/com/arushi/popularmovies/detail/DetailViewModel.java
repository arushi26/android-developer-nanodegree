package com.arushi.popularmovies.detail;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.arushi.popularmovies.data.local.AppDatabase;
import com.arushi.popularmovies.data.local.FavouriteDao;
import com.arushi.popularmovies.data.local.entity.FavouriteEntity;
import com.arushi.popularmovies.data.model.MovieDetail;

public class DetailViewModel extends ViewModel {
    private LiveData<FavouriteEntity> favouriteEntity;
    private MovieDetail movieDetail;

    public DetailViewModel(AppDatabase database, MovieDetail movieDetail) {
        this.movieDetail = movieDetail;
        favouriteEntity = database.favouriteDao().getFavouriteById(this.movieDetail.getId());
    }

    public LiveData<FavouriteEntity> getFavouriteEntity() {
        return favouriteEntity;
    }

    public void addFavourite(FavouriteDao dao, FavouriteEntity favouriteEntity){
        if(favouriteEntity!=null){
            AddAsFavourite addAsFavourite = new AddAsFavourite(dao);
            addAsFavourite.execute(favouriteEntity);
        }
    }

    private static class AddAsFavourite extends AsyncTask<FavouriteEntity, Void, Void> {
        private FavouriteDao mDao;

        AddAsFavourite(FavouriteDao dao){
            mDao = dao;
        }

        @Override
        protected Void doInBackground(FavouriteEntity... favouriteEntities) {
            mDao.insertFavourite(favouriteEntities[0]);
            return null;
        }
    }

    public void deleteFavourite(FavouriteDao dao){
        if(movieDetail!=null){
            DeleteFromFavourite deleteFromFavourite = new DeleteFromFavourite(dao);
            deleteFromFavourite.execute(movieDetail.getId());
        }
    }

    private static class DeleteFromFavourite extends AsyncTask<Integer, Void, Void> {
        private FavouriteDao mDao;

        DeleteFromFavourite(FavouriteDao dao){
            mDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            mDao.deleteFavourite(integers[0]);
            return null;
        }
    }

}
