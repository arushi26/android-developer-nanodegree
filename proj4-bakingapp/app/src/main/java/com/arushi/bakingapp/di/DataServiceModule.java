package com.arushi.bakingapp.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.arushi.bakingapp.data.DessertRepository;
import com.arushi.bakingapp.data.local.AppDatabase;
import com.arushi.bakingapp.data.local.DessertDao;
import com.arushi.bakingapp.data.remote.ApiRequestInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {NetworkServiceModule.class, AppModule.class})
public class DataServiceModule {

    @Provides
    @Singleton
    DessertRepository provideRepository(DessertDao dessertDao,
                                        ApiRequestInterface apiRequestInterface){
        return new DessertRepository(dessertDao, apiRequestInterface);
    }

    @Provides
    @Singleton
    DessertDao provideDessertDao(AppDatabase database){
        return database.dessertDao();
    }

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(Application application){
        return Room.databaseBuilder(
                application,
                AppDatabase.class,
                "redhotoven.db"
                ).build();
    }

}
