package com.example.guest.ourrestaurant.util;

import android.util.Log;

/**
 * Created by Guest on 5/10/16.
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
