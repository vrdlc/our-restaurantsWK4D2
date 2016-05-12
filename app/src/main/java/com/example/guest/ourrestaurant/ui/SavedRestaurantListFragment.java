package com.example.guest.ourrestaurant.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.guest.ourrestaurant.Constants;
import com.example.guest.ourrestaurant.R;
import com.example.guest.ourrestaurant.adapters.FirebaseRestaurantListAdapter;
import com.example.guest.ourrestaurant.adapters.RestaurantListAdapter;
import com.example.guest.ourrestaurant.fragments.BaseFragment;
import com.example.guest.ourrestaurant.models.Restaurant;
import com.example.guest.ourrestaurant.services.YelpService;
import com.example.guest.ourrestaurant.util.OnRestaurantSelectedListener;
import com.example.guest.ourrestaurant.util.OnStartDragListener;
import com.example.guest.ourrestaurant.util.SimpleItemTouchHelperCallback;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedRestaurantListFragment extends BaseFragment implements OnStartDragListener {
    private Query mQuery;
    private Firebase mFirebaseRestaurantsRef;
    private FirebaseRestaurantListAdapter mFirebaseAdapter;
    private RestaurantListAdapter mRestaurantAdapter;
    private ItemTouchHelper mItemTouchHelper;
    OnRestaurantSelectedListener mRestaurantSelectedListener;
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    public ArrayList<Restaurant> mRestaurants = new ArrayList<>();
    OnRestaurantSelectedListener mOnRestaurantSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mRestaurantSelectedListener = (OnRestaurantSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + e.getMessage());
        }

        try {
            mOnRestaurantSelectedListener = (OnRestaurantSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + e.getMessage());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseRestaurantsRef = new Firebase(Constants.FIREBASE_URL_RESTAURANTS);
        setHasOptionsMenu(true);
    }

    public SavedRestaurantListFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_restaurant_list, container, false);
        ButterKnife.bind(this, view);
        setUpFirebaseQuery();
        setUpRecyclerView();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d("THIS IS A ", "MESSAGE FROM ON CREATE MENU");
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                addToSharedPreferences(query);
                getRestaurants(query);
                Intent intent = new Intent(getActivity(), RestaurantListActivity.class);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
            case R.id.action_search:
                return true;
            default:
                break;
        }
        return false;
    }

    private void getRestaurants(String location) {
        final YelpService yelpService = new YelpService();

        yelpService.findRestaurants(location, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                mRestaurants = yelpService.processResults(response);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRestaurantAdapter = new RestaurantListAdapter(mRestaurants, mOnRestaurantSelectedListener);
                        mRecyclerView.setAdapter(mRestaurantAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                    }
                });
            }
        });
    }

    private void setUpFirebaseQuery() {
        String userUid = mSharedPreferences.getString(Constants.KEY_UID, null);
        String location = mFirebaseRestaurantsRef.child(userUid).toString();
        mQuery = new Firebase(location).orderByChild("index");
    }

    private void setUpRecyclerView() {
        mFirebaseAdapter = new FirebaseRestaurantListAdapter(mQuery, Restaurant.class, this, mRestaurantSelectedListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFirebaseAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mFirebaseAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onPause() {
        String uid = mSharedPreferences.getString(Constants.KEY_UID, null);
        super.onPause();
        for (Restaurant restaurant : mFirebaseAdapter.getItems()) {
            String pushId = restaurant.getPushId();
            restaurant.setIndex(Integer.toString(mFirebaseAdapter.getItems().indexOf(restaurant)));
            mFirebaseRestaurantsRef.child(uid)
                    .child(pushId)
                    .setValue(restaurant);
        }
    }

}
