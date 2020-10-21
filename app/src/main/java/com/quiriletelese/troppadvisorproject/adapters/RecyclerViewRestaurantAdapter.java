package com.quiriletelese.troppadvisorproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quiriletelese.troppadvisorproject.R;
import com.quiriletelese.troppadvisorproject.model_helpers.Constants;
import com.quiriletelese.troppadvisorproject.models.Restaurant;
import com.quiriletelese.troppadvisorproject.views.RestaurantDetailActivity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Alessandro Quirile, Mauro Telese
 */

public class RecyclerViewRestaurantAdapter extends RecyclerView.Adapter<RecyclerViewRestaurantAdapter.ViewHolder> {
    private final Context context;
    private final List<Restaurant> restaurants;

    public RecyclerViewRestaurantAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public RecyclerViewRestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_page_recycler_view_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewRestaurantAdapter.ViewHolder holder, int position) {
        setFieldsOnBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return restaurants == null ? 0 : restaurants.size();
    }

    private void setFieldsOnBindViewHolder(ViewHolder viewHolder, int position) {
        setImage(viewHolder, position);
        viewHolder.textViewRestaurantName.setText(restaurants.get(position).getName());
        viewHolder.textViewRestaurantRating.setText(createAvarageRatingString(restaurants.get(position)));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setImage(ViewHolder viewHolder, int position) {
        if (hasImage(position)) {
            Picasso.with(context).load(getFirtsImage(position))
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.troppadvisor_logo)
                    .error(R.drawable.picasso_error)
                    .into(viewHolder.imageViewAccomodation);
        } else
            viewHolder.imageViewAccomodation.setImageDrawable(context.getResources().getDrawable(R.drawable.picasso_error));
    }

    private void startDetailActivity(String id) {
        Intent intentDetailActivity = new Intent(context, RestaurantDetailActivity.class);
        intentDetailActivity.putExtra(Constants.getId(), id);
        intentDetailActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentDetailActivity);
    }

    private String createAvarageRatingString(Restaurant restaurant) {
        return !hasReviews(restaurant) ? getString(R.string.no_reviews) : createAvarageRatingStringHelper(restaurant);
    }

    @NotNull
    private String createAvarageRatingStringHelper(@NotNull Restaurant restaurant) {
        if (restaurant.getTotalReviews().intValue() == 1)
            return restaurant.getAvarageRating().intValue() + "/5 (" + restaurant.getTotalReviews() + " " + getString(R.string.review) + ")";
        else
            return restaurant.getAvarageRating().intValue() + "/5 (" + restaurant.getTotalReviews() + " " + getString(R.string.reviews) + ")";
    }

    private boolean hasImage(int position) {
        return restaurants.get(position).hasImage();
    }

    private String getFirtsImage(int position) {
        return restaurants.get(position).getFirstImage();
    }

    private boolean hasReviews(Restaurant restaurant) {
        return restaurant.hasReviews();
    }

    private Resources getResources() {
        return context.getResources();
    }

    private String getString(int string) {
        return getResources().getString(string);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageViewAccomodation;
        private TextView textViewRestaurantName, textViewRestaurantRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initializeComponents();
        }

        @Override
        public void onClick(View view) {
            startDetailActivity(restaurants.get(this.getAdapterPosition()).getId());
        }

        private void initializeComponents() {
            LinearLayout linearLayoutHomePageRecyclerView = itemView.findViewById(R.id.linear_layout_home_page_recycler_view);
            imageViewAccomodation = itemView.findViewById(R.id.image_view_accomodation_home);
            textViewRestaurantName = itemView.findViewById(R.id.text_view_accomodation_name);
            textViewRestaurantRating = itemView.findViewById(R.id.text_view_accomodation_rating);
            linearLayoutHomePageRecyclerView.setOnClickListener(this);
        }

    }

}
