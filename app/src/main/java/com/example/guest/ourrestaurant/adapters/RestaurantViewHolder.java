package com.example.guest.ourrestaurant.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.ourrestaurant.Constants;
import com.example.guest.ourrestaurant.R;
import com.example.guest.ourrestaurant.models.Restaurant;
import com.example.guest.ourrestaurant.ui.RestaurantDetailActivity;
import com.example.guest.ourrestaurant.ui.RestaurantDetailFragment;
import com.example.guest.ourrestaurant.util.ItemTouchHelperViewHolder;
import com.example.guest.ourrestaurant.util.OnRestaurantSelectedListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RestaurantViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
    public static final String TAG = RestaurantViewHolder.class.getSimpleName();

    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    @Bind(R.id.restaurantImageView) ImageView mRestaurantImageView;
    @Bind(R.id.restaurantNameTextView) TextView mNameTextView;
    @Bind(R.id.categoryTextView) TextView mCategoryTextView;
    @Bind(R.id.ratingTextView) TextView mRatingTextView;

    private Context mContext;
    private ArrayList<Restaurant> mRestaurants = new ArrayList<>();

    private int mOrientation;
    private Integer selectedPosition;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mPositionEditor;

    private OnRestaurantSelectedListener mRestaurantSelectedListener;


    public RestaurantViewHolder(View itemView, ArrayList<Restaurant> restaurants, OnRestaurantSelectedListener restaurantSelectedListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mRestaurants = restaurants;
        mOrientation = itemView.getResources().getConfiguration().orientation;
        mRestaurantSelectedListener = restaurantSelectedListener;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mPositionEditor = mSharedPreferences.edit();

        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            createDetailFragment(0);
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = getLayoutPosition();
                Log.d(TAG, "POSTION AND RESTAURANTS: " + selectedPosition + " " + mRestaurants.size());
                mRestaurantSelectedListener.onRestaurantSelected(selectedPosition, mRestaurants);

                if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    createDetailFragment(selectedPosition);
                } else {
                    Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_POSITION, selectedPosition);
//                    mPositionEditor.putInt(Constants.EXTRA_KEY_POSITION, selectedPosition);
                    intent.putExtra(Constants.EXTRA_KEY_RESTAURANTS, Parcels.wrap(mRestaurants));
                    mContext.startActivity(intent);
                }
            }
        });
    }


    private void createDetailFragment(int position) {
        Log.d(TAG, "" + mRestaurants.get(position).getName());
        RestaurantDetailFragment detailFragment = RestaurantDetailFragment.newInstance(mRestaurants, position);
        Log.d(TAG, "Hit create detail fragment: position = " + position);
        FragmentTransaction ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.restaurantDetailContainer, detailFragment);
        ft.commit();
    }

    public void bindRestaurant(Restaurant restaurant) {
        Picasso.with(mContext).load(restaurant.getImageUrl()).into(mRestaurantImageView);
        Picasso.with(mContext)
                .load(restaurant.getImageUrl())
                .resize(MAX_WIDTH, MAX_HEIGHT)
                .centerCrop()
                .into(mRestaurantImageView);

        mNameTextView.setText(restaurant.getName());
        mCategoryTextView.setText(restaurant.getCategories().get(0));
        mRatingTextView.setText("Rating: " + restaurant.getRating() + "/5");
    }

    @Override
    public void onItemSelected() {
        itemView.animate()
                .alpha(0.7f)
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(500);
    }

    @Override
    public void onItemClear() {
        itemView.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f);
    }


}

