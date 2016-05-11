package com.example.guest.ourrestaurant.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.guest.ourrestaurant.Constants;
import com.example.guest.ourrestaurant.R;
import com.example.guest.ourrestaurant.adapters.RestaurantListAdapter;
import com.example.guest.ourrestaurant.services.YelpService;
import com.example.guest.ourrestaurant.models.Restaurant;
import com.example.guest.ourrestaurant.util.OnRestaurantSelectedListener;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.Callback;

public class RestaurantListActivity extends AppCompatActivity implements OnRestaurantSelectedListener{
    public static final String TAG = RestaurantListActivity.class.getSimpleName();
    private Integer mPosition;
    private ArrayList<Restaurant> mRestaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "I'm here!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        if (savedInstanceState != null) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mPosition = savedInstanceState.getInt(Constants.EXTRA_KEY_POSITION);
                mRestaurants = Parcels.unwrap(savedInstanceState.getParcelable(Constants.EXTRA_KEY_RESTAURANTS));
                if (mPosition != null && mRestaurants != null) {
                    Intent intent = new Intent(this, RestaurantDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_POSITION, mPosition);
                    intent.putExtra(Constants.EXTRA_KEY_RESTAURANTS, Parcels.wrap(mRestaurants));
                    startActivity(intent);
                }
            }
        }
        setContentView(R.layout.activity_restaurants);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mPosition != null && mRestaurants !=null) {
            outState.putInt(Constants.EXTRA_KEY_POSITION, mPosition);
            outState.putParcelable(Constants.EXTRA_KEY_RESTAURANTS, Parcels.wrap(mRestaurants));
        }
        super.onSaveInstanceState(outState);//IF WE MAKE THIS THE SAME AS SAVED RESTAURANT LIST, WHAT HAPPENS?
    }

    @Override
    public void onRestaurantSelected(Integer position, ArrayList<Restaurant> restaurants) {
        mPosition = position;
        mRestaurants = restaurants;
    }

}