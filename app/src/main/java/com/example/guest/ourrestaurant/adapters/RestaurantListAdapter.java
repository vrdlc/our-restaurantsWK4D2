package com.example.guest.ourrestaurant.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guest.ourrestaurant.Constants;
import com.example.guest.ourrestaurant.R;
import com.example.guest.ourrestaurant.models.Restaurant;
import com.example.guest.ourrestaurant.ui.RestaurantDetailActivity;
import com.example.guest.ourrestaurant.util.FirebaseRecyclerAdapter;
import com.example.guest.ourrestaurant.util.ItemTouchHelperAdapter;
import com.example.guest.ourrestaurant.util.OnRestaurantSelectedListener;
import com.example.guest.ourrestaurant.util.OnStartDragListener;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantViewHolder> implements ItemTouchHelperAdapter {
    public static final String TAG = RestaurantListAdapter.class.getSimpleName();

//    private final OnStartDragListener mDragStartListener;


    private ArrayList<Restaurant> mRestaurants = new ArrayList<>();
    public RestaurantViewHolder viewHolder;
    private Context mContext;
    private OnRestaurantSelectedListener mOnRestaurantSelectedListener;
    private Restaurant mRestaurant;

    public RestaurantListAdapter(ArrayList<Restaurant> restaurants, OnRestaurantSelectedListener restaurantSelectedListener) {
        mRestaurants = restaurants;
//        mDragStartListener = dragStartListener;
        mOnRestaurantSelectedListener = restaurantSelectedListener;
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list_item, parent, false);
        viewHolder = new RestaurantViewHolder(view, mRestaurants, mOnRestaurantSelectedListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RestaurantViewHolder holder, int position) {
        holder.bindRestaurant(mRestaurants.get(position));
        holder.mRestaurantImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
//                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }


    @Override
    public void onItemDismiss(int position) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        String uid = sharedPreferences.getString(Constants.KEY_UID, null);
        Firebase ref = new Firebase(Constants.FIREBASE_URL_RESTAURANTS).child(uid);
        Firebase pushRef = ref.push();
        String restaurantPushId = pushRef.getKey();
        mRestaurant = mRestaurants.get(position);
        mRestaurant.setPushId(restaurantPushId);
        pushRef.setValue(mRestaurant);
        Toast.makeText(mContext, "Fuck Yeah", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return true;
    }


    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

//    @Override
//    protected void itemAdded(Restaurant item, String key, int position) {
//    }
//
//    @Override
//    protected void itemChanged(Restaurant oldItem, Restaurant newItem, String key, int position) {
//    }
//
//    @Override
//    protected void itemRemoved(Restaurant item, String key, int position) {
//    }
//
//    @Override
//    protected void itemMoved(Restaurant item, String key, int oldPosition, int newPosition) {
//    }

}