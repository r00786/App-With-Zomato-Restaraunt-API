package com.example.restraunt_search.activities.bookmarks;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.restraunt_search.R;
import com.example.restraunt_search.activities.displaydetails.DisplayDetailsActivity;
import com.example.restraunt_search.activities.restaurant.ViewModelRestaurant;
import com.example.restraunt_search.models.Restaurant;
import com.example.restraunt_search.utils.Constants;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

/**
 * author Rohit Date 27-05-2019
 * Activity for the Bookmarks
 */
public class BookmarksActivity extends AppCompatActivity implements AdapterBookMarks.AdapterRestaurantCallbacks {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tb_restaurant)
    Toolbar tbRestaurant;
    @BindView(R.id.rv_restaurants)
    RecyclerView rvRestaurants;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @Inject
    AdapterBookMarks adapter;
    @Inject
    ViewModelProvider.Factory factory;
    ViewModelRestaurant viewModelRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        ButterKnife.bind(this);
        initGlobalVariables();
        setUpRecycler();
        initObservers();
        getBookMarks();
    }

    /**
     * Initializing the Observers For Live Data
     */
    private void initObservers() {
        viewModelRestaurant.getBookmarks().observe(this, restaurants -> adapter.addData(restaurants));
    }

    /**
     * Initializing the Global Variables
     */
    private void initGlobalVariables() {
        viewModelRestaurant = ViewModelProviders.of(this, factory).get(ViewModelRestaurant.class);
    }

    /**
     * Getting the saved Book Marks
     */
    private void getBookMarks() {
        viewModelRestaurant.getBookMarkedRestaurants();
    }

    /**
     * Setting Up the Recycler
     */
    private void setUpRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        rvRestaurants.setLayoutManager(linearLayoutManager);
        rvRestaurants.addItemDecoration(dividerItemDecoration);
        rvRestaurants.setAdapter(adapter);

    }

    /**
     * Handling Items Removal From the RecyclerItems
     *
     * @param restaurant item to be removed
     * @param save       here it comes false always to remove item
     */
    @Override
    public void saveItem(Restaurant restaurant, boolean save) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        Set<String> bookMarkedItems = new HashSet<>(sharedPreferences.getStringSet(Constants.BOOK_MARKED_ITEMS, new HashSet<>()));
        viewModelRestaurant.addOrRemoveFromSet(restaurant.getRestaurant().getId(), bookMarkedItems, save);
        sharedPreferences.edit().putStringSet(Constants.BOOK_MARKED_ITEMS, bookMarkedItems).apply();
        viewModelRestaurant.addToRemovedBookMarks(restaurant.getRestaurant().getId());
        viewModelRestaurant.saveOrDeleteFromDB(restaurant, save);
    }

    /**
     * Setting the items which are removed in the intent
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Constants.CHANGED_ITEMS, (Serializable) viewModelRestaurant.getRemovedItems());
        setResult(Constants.BOOKMARKS_REMOVED, intent);
        finish();
    }

    @OnClick(R.id.iv_back)
    void onBackClicked() {
        onBackPressed();
    }

    /**
     * Handling Click on RecyclerView Item
     *
     * @param restaurant object to be marshaled and send to the activity
     */
    @Override
    public void onClickRecycler(Restaurant restaurant) {
        Intent intent = new Intent(this, DisplayDetailsActivity.class);
        intent.putExtra(Constants.DETAIL_ITEM, restaurant.getRestaurant());
        startActivity(intent);
    }
}
