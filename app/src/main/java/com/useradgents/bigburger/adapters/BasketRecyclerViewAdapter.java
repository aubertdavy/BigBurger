package com.useradgents.bigburger.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.useradgents.bigburger.R;
import com.useradgents.bigburger.interfaces.Quantity;
import com.useradgents.bigburger.models.Product;
import com.useradgents.bigburger.interfaces.ItemSelected;

import java.util.List;

/**
 * Created by AUBERT on 12/03/2017.
 */

public class BasketRecyclerViewAdapter extends RecyclerView.Adapter<BasketRecyclerViewAdapter.ViewHolder> {
    private int mBackground;
    private List<Product> mValues;
    private ItemSelected mListener;
    private Quantity quantityListener;
    private final TypedValue mTypedValue = new TypedValue();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;

        public final View mView;
        public final TextView mNbView;
        public final TextView mPriceView;
        public final TextView mTitleView;
        public final ImageView mThumbView;
        public final ImageView mPlusView;
        public final ImageView mMinView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPriceView = (TextView) view.findViewById(R.id.txt_price);
            mThumbView = (ImageView) view.findViewById(R.id.img_thumb);
            mTitleView = (TextView) view.findViewById(R.id.txt_title);
            mPlusView = (ImageView) view.findViewById(R.id.img_plus);
            mMinView = (ImageView) view.findViewById(R.id.img_min);
            mNbView = (TextView) view.findViewById(R.id.txt_nb);
        }
    }

    public Product getValueAt(int position) {
        return mValues.get(position);
    }

    public BasketRecyclerViewAdapter(Context context, List<Product> items, ItemSelected callback, Quantity quantity) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        quantityListener = quantity;
        mListener = callback;
        mValues = items;
    }

    @Override
    public BasketRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_basket_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new BasketRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BasketRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mNbView.setText(String.valueOf(mValues.get(position).printNb()));
        holder.mPriceView.setText(String.valueOf(mValues.get(position).priceToString()));

        holder.mPlusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityListener != null) {
                    quantityListener.add(holder.getAdapterPosition());
                }
            }
        });

        holder.mMinView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityListener != null) {
                    quantityListener.min(holder.getAdapterPosition());
                }
            }
        });

        Picasso.with(holder.mThumbView.getContext())
                .load(mValues.get(position).getUrl())
                .into(holder.mThumbView);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}
