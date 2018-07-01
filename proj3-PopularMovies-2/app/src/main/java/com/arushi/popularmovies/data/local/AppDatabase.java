package com.arushi.popularmovies.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.arushi.popularmovies.data.local.entity.FavouriteEntity;

@Database(entities = {FavouriteEntity.class},
        version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "popularmovies";
    private static AppDatabase sINSTANCE;

    public abstract FavouriteDao favouriteDao();

    public static AppDatabase getInstance(Context context) {
        if (sINSTANCE == null) {
            synchronized (LOCK) {
                sINSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                AppDatabase.class, DATABASE_NAME)
                                .build();
            }
        }
        return sINSTANCE;
    }

    public static void destroyInstance() {
        sINSTANCE = null;
    }
}
