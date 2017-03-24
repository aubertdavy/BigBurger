package com.useradgents.bigburger.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.useradgents.bigburger.R;
import com.useradgents.bigburger.adapters.BasketRecyclerViewAdapter;
import com.useradgents.bigburger.interfaces.Price;
import com.useradgents.bigburger.interfaces.Quantity;
import com.useradgents.bigburger.models.Product;

import java.util.List;

/**
 * Created by AUBERT on 11/03/2017.
 */

public class BurgerBasketFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Product>>, Quantity {
    public final String TAG = BurgerBasketFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshView;
    private BasketRecyclerViewAdapter recyclerViewAdapter;

    private static List<Product> mProducts;
    private static Price mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_catalog);
        swipeRefreshView = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefreshView.setEnabled(false);
        swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLoaderManager().restartLoader(0, null, BurgerBasketFragment.this);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Price) {
            mListener = (Price)context;
        }
    }

    @Override
    public Loader<List<Product>> onCreateLoader(int id, Bundle args) {
        return new ContentLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Product>> loader, List<Product> data) {
        recyclerViewAdapter = new BasketRecyclerViewAdapter(getActivity(), data, null, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(recyclerViewAdapter);

        ItemTouchHelper.Callback callback = new SwipeHelper(recyclerViewAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        mProducts = data;

        if (mListener != null) {
            mListener.total(Product.totalPrice(data));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Product>> loader) {

    }

    @Override
    public void add(int position) {
        Product currentProduct = recyclerViewAdapter.getValueAt(position);
        if (currentProduct != null) {
            currentProduct.add();
        }

        if (mProducts != null && mProducts.size() >= position) {
            recyclerViewAdapter.notifyItemChanged(position);

            if (mListener != null) {
                mListener.total(Product.totalPrice(mProducts));
            }
        }
    }

    @Override
    public void min(int position) {
        Product currentProduct = recyclerViewAdapter.getValueAt(position);
        if (currentProduct != null) {
            currentProduct.min();
        }

        if (currentProduct.getNb() == 0) {
            remove(recyclerViewAdapter, position);
        } else {
            recyclerViewAdapter.notifyItemChanged(position);

            if (mListener != null) {
                mListener.total(Product.totalPrice(mProducts));
            }
        }
    }

    public static class ContentLoader extends AsyncTaskLoader<List<Product>> {
        List<Product> mProducts;

        public ContentLoader(Context context) {
            super(context);
        }

        @Override
        public List<Product> loadInBackground() {
            return Product.getAll();
        }

        @Override
        protected void onStartLoading() {
            if (mProducts != null) {
                deliverResult(mProducts);
            }

            if (takeContentChanged() || mProducts == null) {
                forceLoad();
            }
        }

        @Override
        public void deliverResult(List<Product> data) {
            mProducts = data;
            if (isStarted()) {
                super.deliverResult(mProducts);
            }
        }
    }

    public static void remove(BasketRecyclerViewAdapter adapter, int position) {
        if (mProducts != null && mProducts.size() >= position) {
            mProducts.remove(position);
            adapter.notifyItemRemoved(position);

            if (mListener != null) {
                mListener.total(Product.totalPrice(mProducts));
            }
        }
    }

    public static class SwipeHelper extends ItemTouchHelper.SimpleCallback {
        private BasketRecyclerViewAdapter adapter;

        public SwipeHelper(BasketRecyclerViewAdapter adapter) {
            super(0, ItemTouchHelper.RIGHT);
            this.adapter = adapter;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == ItemTouchHelper.RIGHT) {
                int position = viewHolder.getAdapterPosition();

                Product currentProduct = adapter.getValueAt(position);
                if (currentProduct != null) {
                    currentProduct.delete();

                    remove(adapter, position);
                }
            }
        }
    }
}
