package com.useradgents.bigburger.interfaces;

/**
 * Created by AUBERT on 12/03/2017.
 */

public interface Request {
    /**
     * Notify request catalog API loading.
     */
    void onLoad();

    /**
     * Notify request catalog API loaded.
     */
    void onLoadCompleted();
}
