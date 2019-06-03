package com.example.restraunt_search.di;

import android.arch.lifecycle.ViewModelProvider;

import com.example.restraunt_search.activities.restaurant.AdapterRestaurant;
import com.example.restraunt_search.activities.restaurant.RepositoryRestaurant;
import com.example.restraunt_search.activities.restaurant.RepositoryRestaurantCallbacks;
import com.example.restraunt_search.activities.restaurant.RestaurantActivity;
import com.example.restraunt_search.activities.restaurant.ViewModelRestaurant;
import com.example.restraunt_search.utils.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ModuleMainActivity {

    @Provides
    public static ViewModelProvider.Factory providesViewModel(ViewModelRestaurant viewModelRestaurant) {
        return new ViewModelProviderFactory<>(viewModelRestaurant);
    }

    @Binds
    abstract AdapterRestaurant.AdapterRestaurantCallbacks providesAdapterCallbacks(RestaurantActivity restaurantActivity);

    @Binds
    abstract RepositoryRestaurantCallbacks providesRepoCallbacks(RepositoryRestaurant repositoryRestaurant);
}
