package com.example.restraunt_search.activities.displaydetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.restraunt_search.R;
import com.example.restraunt_search.models.RestaurantInner;
import com.example.restraunt_search.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author Rohit 27-05-2019
 * Activity to show details of the restaurant
 */
public class DisplayDetailsActivity extends AppCompatActivity {

    @BindView(R.id.iv_detail)
    ImageView ivDetail;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_cuisines)
    TextView tvCuisines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_details);
        ButterKnife.bind(this);
        setDetails();
    }

    /**
     * To Set the Details In the Text Views and Image
     */
    private void setDetails() {
        RestaurantInner restaurantInner = getIntent().getParcelableExtra(Constants.DETAIL_ITEM);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        requestOptions.error(R.mipmap.ic_launcher);
        Glide.with(this).setDefaultRequestOptions(requestOptions).load(restaurantInner.getFeatured_image()).into(ivDetail);
        tvName.setText(String.format("Name : \n%s", restaurantInner.getName()));
        tvAddress.setText(String.format("Address : \n%s", restaurantInner.getLocation().getAddress()));
        tvCuisines.setText(String.format("Cuisines : \n%s", restaurantInner.getCuisines()));

    }
}
