package com.useradgents.bigburger.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.useradgents.bigburger.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by AUBERT on 11/03/2017.
 */

public class LauncherActivity extends AppCompatActivity {
    public static String TAG = LauncherActivity.class.getSimpleName();

    private Unbinder unbinder;
    @BindView(R.id.img_logo)ImageView mLogoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        unbinder = ButterKnife.bind(this);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mLogoView.setAnimation(animation);
    }

    @OnClick(R.id.img_logo)
    public void submit() {
        start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void start() {
        startActivity(new Intent(LauncherActivity.this, MainActivity.class));
        finish();
    }
}
