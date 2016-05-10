package com.example.guest.ourrestaurant.util;

/**
 * Created by Guest on 5/10/16.
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
