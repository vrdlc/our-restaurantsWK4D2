package com.example.guest.ourrestaurant.ui;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.guest.ourrestaurant.Constants;
import com.example.guest.ourrestaurant.R;
import com.example.guest.ourrestaurant.adapters.RestaurantPagerAdapter;
import com.example.guest.ourrestaurant.models.Restaurant;
import com.example.guest.ourrestaurant.util.ScaleAndFadePageTransformer;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RestaurantDetailActivity extends AppCompatActivity {
    @Bind(R.id.viewPager)
    ViewPager mViewPager;
    private RestaurantPagerAdapter adapterViewPager;
    ArrayList<Restaurant> mRestaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mRestaurants = Parcels.unwrap(intent.getParcelableExtra(Constants.EXTRA_KEY_RESTAURANTS));
        Integer startingPosition = intent.getIntExtra(Constants.EXTRA_KEY_POSITION, 0);
//        int startingPosition = Integer.parseInt(getIntent().getStringExtra("position")); WHY DO WE CHANGE FROM "int" to "INTEGER"?

        adapterViewPager = new RestaurantPagerAdapter(getSupportFragmentManager(), mRestaurants);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);
        mViewPager.setPageTransformer(true, new ScaleAndFadePageTransformer());
    }
}
