package com.quiriletelese.troppadvisorproject.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.quiriletelese.troppadvisorproject.R;
import com.quiriletelese.troppadvisorproject.controllers.HomePageController;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomePageFragment extends Fragment {

    private static final int ACCESS_FINE_LOCATION = 100;
    private HomePageController homePageController;
    private RecyclerView recyclerViewHotel, recyclerViewRestaurant, recyclerViewAttraction;
    private TextView textViewHotelRecyclerView, textViewRestaurantRecyclerView, textViewAttractionRecyclerView;
    private List<Double> pointSearchArguments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViewComponents(view);
        initializeHomePageFragmentController();
        setListenerOnViewComponents();
        if (checkPermission()) {
            //pointSearchArguments = homePageController.getLocation();
            Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
            initializeRecyclerViewHotel();
            initializeRecyclerViewRestaurant();
            initializeRecyclerViewAttraction();
        } else
            Toast.makeText(getContext(), "NON GRANTED", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_button_menu_home_page_activity:
                startSearchActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeRecyclerViewHotel();
                initializeRecyclerViewRestaurant();
                initializeRecyclerViewAttraction();
            } else
                Toast.makeText(getContext(), "Camera Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeViewComponents(@NotNull View view) {
        recyclerViewHotel = view.findViewById(R.id.recycler_view_hotel);
        recyclerViewRestaurant = view.findViewById(R.id.recycler_view_restaurant);
        recyclerViewAttraction = view.findViewById(R.id.recycler_view_attraction);
        textViewHotelRecyclerView = view.findViewById(R.id.text_view_hotel_recycler_view);
        textViewRestaurantRecyclerView = view.findViewById(R.id.text_view_restaurant_recycler_view);
        textViewAttractionRecyclerView = view.findViewById(R.id.text_view_attraction_recycler_view);
    }

    private void initializeHomePageFragmentController(){
        homePageController = new HomePageController(HomePageFragment.this);
    }

    private void setListenerOnViewComponents(){
        homePageController.setListenerOnViewComponents();;
    }

    private boolean checkPermission() {
        boolean isGranted = true;
        if (homePageController.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) && homePageController.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            isGranted = false;
            homePageController.requestPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_FINE_LOCATION);
        }
        return isGranted;
    }

    private void startSearchActivity() {
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }

    private void initializeRecyclerViewHotel() {
        homePageController.initializeRecyclerViewHotel(homePageController.getLocation());
    }

    private void initializeRecyclerViewRestaurant() {
        homePageController.initializeRecyclerViewRestaurant(homePageController.getLocation());
    }

    private void initializeRecyclerViewAttraction() {
        homePageController.initializeRecyclerViewAttraction(homePageController.getLocation());
    }

    public RecyclerView getRecyclerViewHotel() {
        return recyclerViewHotel;
    }

    public RecyclerView getRecyclerViewRestaurant() {
        return recyclerViewRestaurant;
    }

    public RecyclerView getRecyclerViewAttraction() {
        return recyclerViewAttraction;
    }

    public TextView getTextViewHotelRecyclerView() {
        return textViewHotelRecyclerView;
    }

    public TextView getTextViewRestaurantRecyclerView() {
        return textViewRestaurantRecyclerView;
    }

    public TextView getTextViewAttractionRecyclerView() {
        return textViewAttractionRecyclerView;
    }

}