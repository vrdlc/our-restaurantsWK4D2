package com.example.guest.ourrestaurant.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.guest.ourrestaurant.Constants;
import com.example.guest.ourrestaurant.R;
import com.example.guest.ourrestaurant.adapters.FirebaseRestaurantListAdapter;
import com.example.guest.ourrestaurant.models.Restaurant;
import com.example.guest.ourrestaurant.util.OnRestaurantSelectedListener;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SavedRestaurantListActivity extends AppCompatActivity implements OnRestaurantSelectedListener {
    public static final String TAG = SavedRestaurantListActivity.class.getSimpleName();
    private Integer mPosition;
    private ArrayList<Restaurant> mRestaurants;


    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mPosition = savedInstanceState.getInt(Constants.EXTRA_KEY_POSITION);
                mRestaurants = Parcels.unwrap(savedInstanceState.getParcelable(Constants.EXTRA_KEY_RESTAURANTS));
                Intent intent = new Intent(this, RestaurantDetailActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_POSITION, mPosition);
                intent.putExtra(Constants.EXTRA_KEY_RESTAURANTS, Parcels.wrap(mRestaurants));
                startActivity(intent);
            }
        }
        setContentView(R.layout.activity_saved_restaurant_list);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(mPosition != null && mRestaurants != null) {
            outState.putInt(Constants.EXTRA_KEY_POSITION, Integer.valueOf(mPosition));
            outState.putParcelable(Constants.EXTRA_KEY_RESTAURANTS, Parcels.wrap(mRestaurants));
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onRestaurantSelected(Integer position, ArrayList<Restaurant> restaurants) {
        mPosition = position;
        mRestaurants = restaurants;
    }
}
