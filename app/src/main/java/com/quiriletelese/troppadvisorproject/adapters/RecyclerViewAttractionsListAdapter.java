package com.quiriletelese.troppadvisorproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quiriletelese.troppadvisorproject.R;
import com.quiriletelese.troppadvisorproject.model_helpers.Constants;
import com.quiriletelese.troppadvisorproject.model_helpers.PointSearch;
import com.quiriletelese.troppadvisorproject.models.Attraction;
import com.quiriletelese.troppadvisorproject.views.AccomodationDetailMapsActivity;
import com.quiriletelese.troppadvisorproject.views.AttractionDetailActivity;
import com.quiriletelese.troppadvisorproject.views.WriteReviewActivity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * @author Alessandro Quirile, Mauro Telese
 */

public class RecyclerViewAttractionsListAdapter extends RecyclerView.Adapter<RecyclerViewAttractionsListAdapter.ViewHolder> {

    private final Context context;
    private final List<Attraction> attractions;
    private final PointSearch pointSearch;

    public RecyclerViewAttractionsListAdapter(Context context, List<Attraction> attractions, PointSearch pointSearch) {
        this.context = context;
        this.attractions = attractions;
        this.pointSearch = pointSearch;
    }

    @NonNull
    @Override
    public RecyclerViewAttractionsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_all_accomodations_list_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAttractionsListAdapter.ViewHolder holder, int position) {
        setFieldsOnBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return attractions.size();
    }

    public void addListItems(List<Attraction> attractions) {
        this.attractions.addAll(attractions);
    }

    private void setFieldsOnBindViewHolder(ViewHolder viewHolder, int position) {
        setImage(viewHolder, position);
        viewHolder.ratingBarAttractionsList.setRating(attractions.get(position).getAvarageRating().floatValue());
        viewHolder.textViewAccomodationName.setText(attractions.get(position).getName());
        viewHolder.textViewAccomodationReview.setText(createAvarageRatingString(attractions.get(position)));
        viewHolder.textViewAccomodationAddress.setText(createAddressString(attractions.get(position)));
        viewHolder.textViewDistance.setText(String.valueOf(distance(attractions.get(position).getLatitude(),
                attractions.get(position).getLongitude(), pointSearch.getLatitude(), pointSearch.getLongitude()))
                .concat(" ").concat(getString(R.string.approximately_distance)));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setImage(ViewHolder viewHolder, int position) {
        if (hasImage(position)) {
            Picasso.with(context).load(getFirtsImage(position))
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.app_icon_no_background)
                    .error(R.drawable.picasso_error)
                    .into(viewHolder.imageViewAccomodation);
        } else
            viewHolder.imageViewAccomodation.setImageDrawable(context.getResources().getDrawable(R.drawable.picasso_error));
    }

    private String createAddressString(Attraction attraction) {
        String attractionAddress = "";
        attractionAddress = attractionAddress.concat(attraction.getTypeOfAddress() + " ");
        attractionAddress = attractionAddress.concat(attraction.getStreet() + ", ");
        if (!attraction.getHouseNumber().isEmpty())
            attractionAddress = attractionAddress.concat(attraction.getHouseNumber() + ", ");
        attractionAddress = attractionAddress.concat(attraction.getCity() + ", ");
        if (!attraction.getProvince().equals(attraction.getCity()))
            attractionAddress = attractionAddress.concat(attraction.getProvince() + ", ");
        attractionAddress = attractionAddress.concat(attraction.getPostalCode());
        return attractionAddress;
    }

    private boolean hasImage(int position) {
        return attractions.get(position).hasImage();
    }

    private String getFirtsImage(int position) {
        return attractions.get(position).getFirstImage();
    }

    private String createAvarageRatingString(Attraction attraction) {
        return !hasReviews(attraction) ? getString(R.string.no_reviews) : createAvarageRatingStringHelper(attraction);
    }

    @NotNull
    private String createAvarageRatingStringHelper(@NotNull Attraction attraction) {
        if (attraction.getTotalReviews().intValue() == 1)
            return attraction.getTotalReviews() + " " + getString(R.string.review);
        else
            return attraction.getTotalReviews() + " " + getString(R.string.reviews);
    }

    private Resources getResources() {
        return context.getResources();
    }

    @NotNull
    private String getString(int stringId) {
        return getResources().getString(stringId);
    }

    private boolean hasReviews(@NotNull Attraction attraction) {
        return attraction.hasReviews();
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = (dist * 60 * 1.1515) * 1.609;
        return (double) Math.round(dist * 100) / 100;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RelativeLayout relativeLayoutAccomodation;
        private ImageView imageViewAccomodation;
        private RatingBar ratingBarAttractionsList;
        private TextView textViewAccomodationName, textViewAccomodationReview, textViewAccomodationAddress,
                textViewDistance;
        private Button buttonWriteReview, buttonSeeAccomodationOnMap;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initializeComponents();
            setListenerOnComponents();
        }

        @Override
        public void onClick(View view) {
            onClickHelper(view);
        }

        private void initializeComponents() {
            relativeLayoutAccomodation = itemView.findViewById(R.id.relative_layout_main);
            imageViewAccomodation = itemView.findViewById(R.id.image_view_accomodation_list);
            ratingBarAttractionsList = itemView.findViewById(R.id.rating_bar_attractions_list);
            textViewAccomodationName = itemView.findViewById(R.id.text_view_accomodation_name);
            textViewAccomodationReview = itemView.findViewById(R.id.text_view_accomodation_review);
            textViewAccomodationAddress = itemView.findViewById(R.id.text_view_hotel_address);
            textViewDistance = itemView.findViewById(R.id.text_view_distance);
            buttonWriteReview = itemView.findViewById(R.id.button_write_review);
            buttonSeeAccomodationOnMap = itemView.findViewById(R.id.button_see_accomodation_on_map);
        }

        private void setListenerOnComponents() {
            buttonWriteReview.setOnClickListener(this);
            buttonSeeAccomodationOnMap.setOnClickListener(this);
            relativeLayoutAccomodation.setOnClickListener(this);
        }

        private void onClickHelper(View view) {
            switch (view.getId()) {
                case R.id.button_write_review:
                    startWriteReviewActivity();
                    break;
                case R.id.button_see_accomodation_on_map:
                    startAccomodationDetailMapsActivity();
                    break;
                case R.id.relative_layout_main:
                    startDetailActivity();
                    break;
            }
        }

        private void startWriteReviewActivity() {
            context.startActivity(createWriteReviewActivityIntent());
        }

        private Intent createWriteReviewActivityIntent() {
            Intent writeReviewActivityIntent = new Intent(context, WriteReviewActivity.class);
            writeReviewActivityIntent.putExtra(Constants.getId(), getId());
            writeReviewActivityIntent.putExtra(Constants.getAccomodationType(), Constants.getAttraction());
            writeReviewActivityIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            return writeReviewActivityIntent;
        }

        private void startAccomodationDetailMapsActivity() {
            context.startActivity(createAccomodationDetailMapsIntent());
        }

        private Intent createAccomodationDetailMapsIntent() {
            Intent accomodationDetailMapsIntent = new Intent(context, AccomodationDetailMapsActivity.class);
            accomodationDetailMapsIntent.putExtra(Constants.getAccomodation(), getAttraction());
            accomodationDetailMapsIntent.putExtra(Constants.getAccomodationType(), Constants.getAttraction());
            accomodationDetailMapsIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            return accomodationDetailMapsIntent;
        }

        private void startDetailActivity() {
            context.startActivity(createStartDetailActivityIntent());
        }

        private Intent createStartDetailActivityIntent() {
            Intent startDetailActivityIntent = new Intent(context, AttractionDetailActivity.class);
            startDetailActivityIntent.putExtra(Constants.getId(), getId());
            startDetailActivityIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            return startDetailActivityIntent;
        }

        private String getId() {
            return attractions.get(this.getAdapterPosition()).getId();
        }

        private Attraction getAttraction() {
            return attractions.get(this.getAdapterPosition());
        }

    }

}
