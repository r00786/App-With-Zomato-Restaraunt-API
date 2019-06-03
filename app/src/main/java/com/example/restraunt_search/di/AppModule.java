package com.example.restraunt_search.di;

import android.content.Context;

import com.example.restraunt_search.RestaurantApplication;
import com.example.restraunt_search.data.DataManager;
import com.example.restraunt_search.data.NetworkRequestManager;
import com.example.restraunt_search.utils.Constants;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Provides
    Context providesApplicationContext(RestaurantApplication daggerApplication) {
        return daggerApplication;
    }

    @Provides
    DataManager providesDataManager(NetworkRequestManager networkRequestManager) {
        return new DataManager(networkRequestManager);
    }

    private OkHttpClient initOkHttp() {
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(interceptor);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("user-key", "5daf4c1792fa72c96123a2c935054767");
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        return httpClient.build();
    }

    @Provides
    public NetworkRequestManager providesNetworkService() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(initOkHttp())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(NetworkRequestManager.class);

    }


}