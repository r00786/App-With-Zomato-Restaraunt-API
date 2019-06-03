package com.example.restraunt_search.di;

import com.example.restraunt_search.activities.bookmarks.BookmarksActivity;
import com.example.restraunt_search.activities.restaurant.RestaurantActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = ModuleMainActivity.class)
    abstract RestaurantActivity bindMoviesActivity();

    @ContributesAndroidInjector(modules = ModuleBookmarks.class)
    abstract BookmarksActivity bindBookMarksActivity();


}