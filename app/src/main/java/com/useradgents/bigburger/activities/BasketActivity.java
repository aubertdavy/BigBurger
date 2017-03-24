package com.useradgents.bigburger.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.useradgents.bigburger.R;
import com.useradgents.bigburger.fragments.BurgerBasketFragment;
import com.useradgents.bigburger.interfaces.Price;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by AUBERT on 11/03/2017.
 */

public class BasketActivity extends AppCompatActivity implements Price {
    public final String TAG = BasketActivity.class.getSimpleName();

    @BindView(R.id.main_content)CoordinatorLayout main_content;
    @BindView(R.id.collapsing_toolbar)CollapsingToolbarLayout collapsingToolbar;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        unbinder = ButterKnife.bind(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar.setTitle(getString(R.string.basket_view));

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.place_holder, new BurgerBasketFragment())
                .commit();
    }

    @Override
    public void total(double total) {
        collapsingToolbar.setTitle("Panier " + String.format("%.2f", total) + "€");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.fab_cash)
    public void pay() {
        Snackbar.make(main_content, R.string.snackbar_text, Snackbar.LENGTH_SHORT)
                .setDuration(3000).show();
    }
}
