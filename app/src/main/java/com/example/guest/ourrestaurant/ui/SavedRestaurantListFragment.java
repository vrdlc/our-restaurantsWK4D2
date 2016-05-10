package com.example.guest.ourrestaurant.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guest.ourrestaurant.Constants;
import com.example.guest.ourrestaurant.R;
import com.example.guest.ourrestaurant.adapters.FirebaseRestaurantListAdapter;
import com.example.guest.ourrestaurant.fragments.BaseFragment;
import com.example.guest.ourrestaurant.models.Restaurant;
import com.example.guest.ourrestaurant.util.OnStartDragListener;
import com.example.guest.ourrestaurant.util.SimpleItemTouchHelperCallback;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedRestaurantListFragment extends BaseFragment implements OnStartDragListener {
    private Query mQuery;
    private Firebase mFirebaseRestaurantsRef;
    private FirebaseRestaurantListAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseRestaurantsRef = new Firebase(Constants.FIREBASE_URL_RESTAURANTS);
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

    private void setUpFirebaseQuery() {
        String userUid = mSharedPreferences.getString(Constants.KEY_UID, null);
        String location = mFirebaseRestaurantsRef.child(userUid).toString();
        mQuery = new Firebase(location).orderByChild("index");
    }

    private void setUpRecyclerView() {
        mAdapter = new FirebaseRestaurantListAdapter(mQuery, Restaurant.class, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
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
        for (Restaurant restaurant : mAdapter.getItems()) {
            String pushId = restaurant.getPushId();
            restaurant.setIndex(Integer.toString(mAdapter.getItems().indexOf(restaurant)));
            mFirebaseRestaurantsRef.child(uid)
                    .child(pushId)
                    .setValue(restaurant);
        }
    }

}
