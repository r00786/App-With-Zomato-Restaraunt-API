package com.example.restraunt_search.di;

import android.arch.lifecycle.ViewModelProvider;

import com.example.restraunt_search.activities.bookmarks.AdapterBookMarks;
import com.example.restraunt_search.activities.bookmarks.BookmarksActivity;
import com.example.restraunt_search.activities.restaurant.RepositoryRestaurant;
import com.example.restraunt_search.activities.restaurant.RepositoryRestaurantCallbacks;
import com.example.restraunt_search.activities.restaurant.ViewModelRestaurant;
import com.example.restraunt_search.utils.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ModuleBookmarks {
    @Provides
    public static ViewModelProvider.Factory providesViewModel(ViewModelRestaurant viewModelRestaurant) {
        return new ViewModelProviderFactory<>(viewModelRestaurant);
    }

    @Binds
    abstract AdapterBookMarks.AdapterRestaurantCallbacks providesAdapterCallbacks(BookmarksActivity restaurantActivity);

    @Binds
    abstract RepositoryRestaurantCallbacks providesRepoCallbacks(RepositoryRestaurant repositoryRestaurant);
}
