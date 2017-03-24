package com.useradgents.bigburger.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.useradgents.bigburger.ApplicationController;
import com.useradgents.bigburger.R;
import com.useradgents.bigburger.adapters.CatalogRecyclerViewAdapter;
import com.useradgents.bigburger.jsons.ProductJson;
import com.useradgents.bigburger.models.Product;
import com.useradgents.bigburger.interfaces.ItemSelected;
import com.useradgents.bigburger.interfaces.Request;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by AUBERT on 11/03/2017.
 */

public class BurgerListFragment extends Fragment implements ItemSelected {
    public final String TAG = BurgerListFragment.class.getSimpleName();

    private ArrayList<Product> mProducts = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshView;
    private ItemSelected mSelectedListener;
    private RecyclerView mRecyclerView;
    private Request mLoadListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_catalog);
        swipeRefreshView = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onLoadCatalog();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemSelected) {
            mSelectedListener = (ItemSelected)context;
        }
        if (context instanceof Request) {
            mLoadListener = (Request)context;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onLoadCatalog();
    }

    private void setUpRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new CatalogRecyclerViewAdapter(getActivity(), mProducts, this));
    }

    private void notifyErrorOnLoaded() {
        if (getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.error_loading_title));
            builder.setMessage(getString(R.string.error_loading_body));
            builder.setPositiveButton(getString(R.string.button_yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            onLoadCatalog();
                        }
                    });
            builder.setNegativeButton(getString(R.string.button_no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }


    private void onLoadCatalog() {
        if (mLoadListener != null) {
            mLoadListener.onLoad();
        }

        JsonArrayRequest request = new JsonArrayRequest(getString(R.string.catalog_api), new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    ProductJson[] products = new Gson().fromJson(response.toString(), ProductJson[].class);
                    for (ProductJson productJson : products) {
                        mProducts.add(new Product(
                                productJson.getRef(),
                                productJson.getTitle(),
                                productJson.getDescription(),
                                productJson.getThumbnail(),
                                productJson.getPrice()));
                    }

                    onLoadCatalogCompleted(true);
                }  catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, error.getMessage());

                onLoadCatalogCompleted(false);
            }
        });

        ApplicationController.getInstance().addToRequestQueue(request);
    }

    private void onLoadCatalogCompleted(boolean success) {
        swipeRefreshView.setRefreshing(false);

        if (mLoadListener != null) {
            mLoadListener.onLoadCompleted();
        }

        if (success) {
            setUpRecyclerView(mRecyclerView);
        } else {
            notifyErrorOnLoaded();
        }
    }

    @Override
    public void onItemSelected(Product product) {
        if (mSelectedListener != null) {
            mSelectedListener.onItemSelected(product);
        }
    }
}
