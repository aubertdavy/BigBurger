package com.useradgents.bigburger.interfaces;

import com.useradgents.bigburger.models.Product;

/**
 * Created by AUBERT on 12/03/2017.
 */

public interface ItemSelected {
    /**
     * Return a product object selected from a list.
     * @param product
     */
    void onItemSelected(Product product);
}
