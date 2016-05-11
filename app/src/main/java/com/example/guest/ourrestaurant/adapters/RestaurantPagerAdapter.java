package com.example.guest.ourrestaurant.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.guest.ourrestaurant.models.Restaurant;
import com.example.guest.ourrestaurant.ui.RestaurantDetailFragment;

import java.util.ArrayList;

public class RestaurantPagerAdapter extends FragmentPagerAdapter {
    public static final String TAG = RestaurantPagerAdapter.class.getSimpleName();
    private ArrayList<Restaurant> mRestaurants;

    public RestaurantPagerAdapter(FragmentManager fm, ArrayList<Restaurant> restaurants) {
        super(fm);
        mRestaurants = restaurants;
    }

    @Override
    public Fragment getItem(int position) {
        return RestaurantDetailFragment.newInstance(mRestaurants, position);
    }

    @Override
    public int getCount() {
        Log.d(TAG, "fartyfartfart");
        return mRestaurants.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mRestaurants.get(position).getName();
    }
}
