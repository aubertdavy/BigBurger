package com.useradgents.bigburger.models;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by AUBERT on 11/03/2017.
 */

@Table(name = "Products")
public class Product extends Model {
    public final String TAG = Product.class.getSimpleName();

    @Column(name = "ref_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public int mRef;

    @Column(name = "price")
    public double mPrice;

    @Column(name = "title")
    public String mTitle;

    @Column(name = "detail")
    public String mDetail;

    @Column(name = "url")
    public String mUrl;

    @Column(name = "nb")
    public int mNb;

    public Product() {
        super();
    }

    public Product(int ref, String title, String detail, String url, int price) throws Exception {
        super();
        mNb = 1;
        mRef = ref;
        mUrl = url;
        mTitle = title;
        mDetail = decode(detail);
        mPrice = ((double)price) /100;
    }

    public int getRef() {
        return mRef;
    }

    public double getPrice() {
        return mPrice;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDetail(){
        return mDetail;
    }

    public String getUrl() {
        return mUrl;
    }

    public int getNb() {
        return mNb;
    }

    public String printNb() {
        return "x" + getNb();
    }

    public String priceToString() {
        return "Prix : " + String.format("%.2f", mPrice) + "€";
    }

    private String decode(String value) throws Exception {
        try  {
            return new String(value.getBytes(), "UTF-8");
        }
        catch (UnsupportedEncodingException exception) {
            Log.e(TAG, exception.getMessage());
        }
        return value;
    }

    public static List<Product> getAll() {
        return new Select()
                .from(Product.class)
                .orderBy("ref_id ASC")
                .execute();
    }

    public static double totalPrice(List<Product> data) {
        double total = 0;
        for(Product d : data) {
            total += (d.getPrice()*d.getNb());
        }
        return total;
    }

    public void add() {
        mNb += 1;
        save();
    }

    public void min() {
        mNb -= 1;
        if (mNb > 0) {
            save();
        } else {
            delete();
        }
    }
}
