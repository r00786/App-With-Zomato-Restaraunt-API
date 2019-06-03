package com.example.restraunt_search.di;

import com.example.restraunt_search.RestaurantApplication;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {AndroidSupportInjectionModule.class, AppModule.class, ActivityBuilder.class})
public interface AppComponent {

    void inject(RestaurantApplication networkApplication);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(RestaurantApplication application);

        AppComponent build();
    }

}