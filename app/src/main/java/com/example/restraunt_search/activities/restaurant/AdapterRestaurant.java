package com.example.restraunt_search.activities.restaurant;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.restraunt_search.R;
import com.example.restraunt_search.models.Restaurant;
import com.example.restraunt_search.models.RestaurantInner;
import com.example.restraunt_search.models.RestaurantsResponse;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * Adapter for the Restaurants Recycler in the activity
 */
public class AdapterRestaurant extends RecyclerView.Adapter<AdapterRestaurant.ViewHolder> {
    public AdapterRestaurantCallbacks adapterRestaurantCallbacks;
    private List<Restaurant> items;

    public AdapterRestaurantCallbacks getAdapterRestaurantCallbacks() {
        return adapterRestaurantCallbacks;
    }

    public List<Restaurant> getItems() {
        return items;
    }


    @Inject
    public AdapterRestaurant(AdapterRestaurantCallbacks adapterRestaurantCallbacks) {
        this.adapterRestaurantCallbacks = adapterRestaurantCallbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_rv_restaurants, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindItems(items.get(i));

    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    /**
     * Checking for the Removed items And Updating in the Recycler
     *
     * @param changedItems id's of the removed items
     */
    @SuppressWarnings("CheckResult")
    public void checkForRemovedItems(Set<String> changedItems) {
        if (changedItems.isEmpty()) return;
        Observable.fromIterable(items).subscribe(success -> {
            if (changedItems.contains(success.getRestaurant().getId())) {
                success.getRestaurant().setBookMarked(false);
            }
        }, error -> {
        });
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_featured_image)
        ImageView ivFeaturedImage;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_bookmark)
        public ImageView ivBookmark;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindItems(Restaurant obj) {
            if (obj == null) return;
            if (obj.getRestaurant() == null) return;
            RestaurantInner nestedObj = obj.getRestaurant();
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.mipmap.ic_launcher);
            requestOptions.error(R.mipmap.ic_launcher);
            Glide.with(itemView.getContext()).setDefaultRequestOptions(requestOptions).load(nestedObj.getFeatured_image()).into(ivFeaturedImage);
            tvName.setText(nestedObj.getName() != null ? nestedObj.getName() : "");
            ivBookmark.setImageResource(nestedObj.isBookMarked() ? R.drawable.ic_bookmarked : R.drawable.ic_bookmark);
            ivBookmark.setOnClickListener(v -> {
                if (nestedObj.isBookMarked()) return;
                nestedObj.setBookMarked(true);
                adapterRestaurantCallbacks.saveItem(items.get(getAdapterPosition()), nestedObj.isBookMarked());
                notifyItemChanged(getAdapterPosition());
            });
            itemView.setOnClickListener(v -> adapterRestaurantCallbacks.onClickRecycler(items.get(getAdapterPosition())));
        }
    }

    /**
     * Handling Data Deciding whether to paginate or not
     *
     * @param obj
     */
    public void addData(RestaurantsResponse obj) {
        if (obj.isHasToPaginate()) {
            int prevSize = items.size();
            items.addAll(obj.getRestaurants());
            notifyItemRangeChanged(prevSize, items.size());
        } else {
            this.items = obj.getRestaurants();
            notifyDataSetChanged();
        }
    }

    public void addData(List<Restaurant> obj) {
        this.items = obj;
        notifyDataSetChanged();
    }

    /**
     * Callbacks Of Adapter to Remove And Save items
     */
    public interface AdapterRestaurantCallbacks {
        public void saveItem(Restaurant restaurant, boolean save);

        public void onClickRecycler(Restaurant restaurant);

    }
}
