package com.useradgents.bigburger.interfaces;

/**
 * Created by AUBERT on 12/03/2017.
 */

public interface Quantity {
    /**
     * Notify an increase in the quantity.
     * @param position
     */
    void add(int position);

    /**
     * Notify a decrease in the quantity.
     * @param position
     */
    void min(int position);
}
