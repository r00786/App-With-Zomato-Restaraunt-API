package com.example.restraunt_search.activities.restaurant;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.restraunt_search.R;
import com.example.restraunt_search.activities.bookmarks.BookmarksActivity;
import com.example.restraunt_search.activities.displaydetails.DisplayDetailsActivity;
import com.example.restraunt_search.models.Restaurant;
import com.example.restraunt_search.models.RestaurantsResponse;
import com.example.restraunt_search.utils.Constants;
import com.example.restraunt_search.utils.PaginationScrollListener;
import com.example.restraunt_search.utils.TextWatcherHelper;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * author Rohit Sharma Date - 27-05-2019
 * Activity to show the Restaurants
 */
public class RestaurantActivity extends AppCompatActivity implements AdapterRestaurant.AdapterRestaurantCallbacks {
    @BindView(R.id.tb_restaurant)
    Toolbar tbRestaurant;
    @BindView(R.id.rv_restaurants)
    RecyclerView rvRestaurants;
    @Inject
    ViewModelProvider.Factory factory;
    @Inject
    AdapterRestaurant adapter;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.pb_loading)
    ProgressBar progressBar;
    private ViewModelRestaurant viewModelRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initGlobVariables();
        setUpRecycler();
        initObservers();
        setSearchListener();
        mockEmptyQuery();
    }

    /**
     * mocking empty query to get Data for the first time
     */
    private void mockEmptyQuery() {
        progressBar.setVisibility(View.VISIBLE);
        viewModelRestaurant.getRestaurants("");
    }

    /**
     * setting search listener on edit text to query after some input by user
     */
    @SuppressWarnings("CheckResult")
    private void setSearchListener() {
        TextWatcherHelper.getObservable(etSearch).debounce(800, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe((String query) -> {
            progressBar.setVisibility(View.VISIBLE);
            viewModelRestaurant.setHaveToPaginate(false);
            viewModelRestaurant.getRestaurants(query);
        }, err -> {/*Handle Error */});
    }

    /**
     * Initializing the Observers For Live Data
     */
    private void initObservers() {
        viewModelRestaurant.getLiveData().observe(this, restaurants -> {
            progressBar.setVisibility(View.GONE);
            adapter.addData(checkForBookMarks(restaurants));
        });
    }

    /**
     * Check If User has some bookmarks already
     * @param restaurants to set the bookmarks which are available in the response
     * @return list with updated bookmarks
     */
    private RestaurantsResponse checkForBookMarks(RestaurantsResponse restaurants) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        return viewModelRestaurant.checkForBookmarks(sharedPreferences.getStringSet(Constants.BOOK_MARKED_ITEMS, new HashSet<>()), restaurants);
    }
    /**
     * Setting up RecyclerView
     */
    private void setUpRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        rvRestaurants.setLayoutManager(linearLayoutManager);
        rvRestaurants.addItemDecoration(dividerItemDecoration);
        rvRestaurants.setAdapter(adapter);
        rvRestaurants.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItems() {
                progressBar.setVisibility(View.VISIBLE);
                viewModelRestaurant.paginate(etSearch.getText().toString());
            }

            @Override
            public boolean isLoading() {
                return viewModelRestaurant.isLoading();
            }

            @Override
            public boolean isLastPage() {
                return viewModelRestaurant.getIsLastPage();
            }
        });
    }

    /**
     * Initializing Global Variables
     */
    private void initGlobVariables() {
        viewModelRestaurant = ViewModelProviders.of(this, factory).get(ViewModelRestaurant.class);
    }

    /**
     * Adapter Callback for saving or Removing the Bookmark
     * @param restaurant Item to saved in the database
     * @param save       true to save false to remove
     */
    @Override
    public void saveItem(Restaurant restaurant, boolean save) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        Set<String> bookMarkedItems = new HashSet<>(sharedPreferences.getStringSet(Constants.BOOK_MARKED_ITEMS, new HashSet<>()));
        viewModelRestaurant.addOrRemoveFromSet(restaurant.getRestaurant().getId(), bookMarkedItems, save);
        sharedPreferences.edit().putStringSet(Constants.BOOK_MARKED_ITEMS, bookMarkedItems).apply();
        viewModelRestaurant.saveOrDeleteFromDB(restaurant, save);
    }

    /**
     * Handling Click on RecyclerView Item
     * @param restaurant object to be marshaled and send to the activity
     */
    @Override
    public void onClickRecycler(Restaurant restaurant) {
        Intent intent = new Intent(this, DisplayDetailsActivity.class);
        intent.putExtra(Constants.DETAIL_ITEM, restaurant.getRestaurant());
        startActivity(intent);
    }

    @OnClick({R.id.iv_bookmarks})
    void onClickBookMarks() {
        Intent intent = new Intent(this, BookmarksActivity.class);
        startActivityForResult(intent, Constants.BOOKMARKS_REMOVED);
    }

    @OnClick(R.id.iv_back)
    void onBackClicked() {
        onBackPressed();
    }

    /**
     * Handling the Removed Items from Bookmarks Section if any of them is removed
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.BOOKMARKS_REMOVED: {
                if (data != null) {
                    Set<String> changedItems = (Set<String>) data.getSerializableExtra(Constants.CHANGED_ITEMS);
                    adapter.checkForRemovedItems(changedItems);
                }
            }
        }
    }
}
