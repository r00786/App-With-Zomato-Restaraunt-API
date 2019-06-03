package com.example.restraunt_search.activities.bookmarks;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restraunt_search.R;
import com.example.restraunt_search.activities.restaurant.AdapterRestaurant;
import com.example.restraunt_search.models.Restaurant;

import javax.inject.Inject;

/**
 * Extension of the Restaurant Adapter As most of the required functionality are common
 */
public class AdapterBookMarks extends AdapterRestaurant {
    @Inject
    public AdapterBookMarks(AdapterRestaurantCallbacks adapterRestaurantCallbacks) {
        super(adapterRestaurantCallbacks);
    }


    @NonNull
    @Override
    public AdapterRestaurant.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_rv_restaurants, viewGroup, false));
    }

    public class ViewHolder extends AdapterRestaurant.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void bindItems(Restaurant obj) {
            super.bindItems(obj);
            ivBookmark.setImageResource(R.drawable.ic_close_black_24dp);
            ivBookmark.setOnClickListener(v -> {
                obj.getRestaurant().setBookMarked(!obj.getRestaurant().isBookMarked());
                getAdapterRestaurantCallbacks().saveItem(getItems().get(getAdapterPosition()), false);
                getItems().remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });
        }
    }
}
