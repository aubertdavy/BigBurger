package com.useradgents.bigburger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.useradgents.bigburger.R;
import com.useradgents.bigburger.fragments.BurgerListFragment;
import com.useradgents.bigburger.models.Product;
import com.useradgents.bigburger.interfaces.ItemSelected;
import com.useradgents.bigburger.interfaces.Request;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by AUBERT on 11/03/2017.
 */

public class MainActivity extends AppCompatActivity implements ItemSelected, Request {
    public final String TAG = MainActivity.class.getSimpleName();

    private Unbinder unbinder;
    private ImageView basketView;
    private ProgressBar progressBarView;

    @BindView(R.id.toolbar)Toolbar mToolView;
    @BindView(R.id.main_content)CoordinatorLayout main_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        mToolView = (Toolbar) findViewById(R.id.toolbar);
        progressBarView = (ProgressBar) mToolView.findViewById(R.id.progressBar);
        basketView = (ImageView) mToolView.findViewById(R.id.toolbar_basket);
        setSupportActionBar(mToolView);

        ImageView basketView = (ImageView)mToolView.findViewById(R.id.toolbar_basket);
        basketView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BasketActivity.class));
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.place_holder, new BurgerListFragment())
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onItemSelected(Product product) {
        setAnimation();

        Snackbar.make(main_content, product.getTitle() + " " + getString(R.string.burger_snack), Snackbar.LENGTH_SHORT)
                .setDuration(3000).show();
    }

    private void setAnimation() {
        Animation firstSlideLeft = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        firstSlideLeft.setDuration(400);
        basketView.startAnimation(firstSlideLeft);

        Animation secondSlideLeft = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        secondSlideLeft.setDuration(500);
        basketView.startAnimation(secondSlideLeft);

        Animation thirdSlideLeft = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        thirdSlideLeft.setDuration(600);
        basketView.startAnimation(thirdSlideLeft);
    }

    @Override
    public void onLoad() {
        progressBarView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadCompleted() {
        progressBarView.setVisibility(View.INVISIBLE);
    }
}
