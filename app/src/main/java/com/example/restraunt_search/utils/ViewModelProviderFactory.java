package com.example.restraunt_search.utils;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

/**
 * ViewModel Provider Factory to Save the items and Disable recreation of a ViewMOdel Object
 * @param <V> ViewModel
 */
public class ViewModelProviderFactory<V> implements ViewModelProvider.Factory {

    private V viewModel;
    @Inject
    public ViewModelProviderFactory( V viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(viewModel.getClass())) {
            return (T) viewModel;
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}