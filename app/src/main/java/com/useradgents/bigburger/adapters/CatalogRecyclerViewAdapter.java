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
import com.useradgents.bigburger.models.Product;
import com.useradgents.bigburger.interfaces.ItemSelected;

import java.util.List;

/**
 * Created by AUBERT on 12/03/2017.
 */

public class CatalogRecyclerViewAdapter extends RecyclerView.Adapter<CatalogRecyclerViewAdapter.ViewHolder> {
    private int mBackground;
    private List<Product> mValues;
    private ItemSelected mListener;
    private final TypedValue mTypedValue = new TypedValue();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;

        public final View mView;
        public final TextView mPriceView;
        public final TextView mTitleView;
        public final TextView mDetailView;
        public final ImageView mThumbView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPriceView = (TextView) view.findViewById(R.id.txt_price);
            mThumbView = (ImageView) view.findViewById(R.id.img_thumb);
            mDetailView = (TextView) view.findViewById(R.id.txt_detail);
            mTitleView = (TextView) view.findViewById(R.id.txt_title);
        }
    }

    public Product getValueAt(int position) {
        return mValues.get(position);
    }

    public CatalogRecyclerViewAdapter(Context context, List<Product> items, ItemSelected callback) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mListener = callback;
        mValues = items;
    }

    @Override
    public CatalogRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_catalog_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new CatalogRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CatalogRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mDetailView.setText(mValues.get(position).getDetail());
        holder.mPriceView.setText(String.valueOf(mValues.get(position).priceToString()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = mValues.get(holder.getAdapterPosition());
                product.save();

                if (mListener != null) {
                    mListener.onItemSelected(product);
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
