package com.test.omdbMovieFilter.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.test.omdbMovieFilter.R;
import com.test.omdbMovieFilter.Utils.Util;
import com.test.omdbMovieFilter.models.DetailModel;

import java.lang.reflect.Field;
import java.util.Objects;


public class DetailActivity extends AppCompatActivity {

 private CoordinatorLayout coordinatorLayout;
 private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (Util.IMAGE_URL != null && Util.DETAIL_MODEL!= null){
            DetailModel model =Util.DETAIL_MODEL;
            ImageView img = findViewById(R.id.img);
            if (!Objects.equals(Util.IMAGE_URL,"N/A")) {
                Glide.with(this).load(Util.IMAGE_URL).into(img);
            } else {
                Glide.with(this).load(R.drawable.ic_movie_poster).into(img);
            }
            CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.main_collapsing);
            collapsingToolbarLayout.setTitle(model.getTitle());
            makeCollapsingToolbarLayoutLooksGood(collapsingToolbarLayout);
            TextView txtTitle = findViewById(R.id.title);
            TextView txtWriters = findViewById(R.id.writers);
            TextView txtActors = findViewById(R.id.actors);
            TextView txtDirector = findViewById(R.id.director);
            TextView txtGenre = findViewById(R.id.genre);
            TextView txtReleased = findViewById(R.id.released);
            TextView txtPlot = findViewById(R.id.plot);
            TextView txtRuntime = findViewById(R.id.runtime);
            txtTitle.setText(model.getTitle());
            txtWriters.setText(model.getWriter());
            txtActors.setText(model.getActors());
            txtDirector.setText(model.getDirector());
            txtGenre.setText(model.getGenre());
            txtReleased.setText(model.getReleased());
            txtPlot.setText(model.getPlot());
            txtRuntime.setText(model.getRuntime());
        }else {
             coordinatorLayout =findViewById(R.id.root_layout);
            snackBar(getResources().getString(R.string.error_occurred),getColor(R.color.red),Snackbar.LENGTH_LONG);

        }

    }

    private void makeCollapsingToolbarLayoutLooksGood(CollapsingToolbarLayout collapsingToolbarLayout) {
        try {
            final Field field = collapsingToolbarLayout.getClass().getDeclaredField("mCollapsingTextHelper");
            field.setAccessible(true);

            final Object object = field.get(collapsingToolbarLayout);
            final Field tpf = Objects.requireNonNull(object).getClass().getDeclaredField("mTextPaint");
            tpf.setAccessible(true);

            ((TextPaint) Objects.requireNonNull(tpf.get(object))).setTypeface(Typeface.createFromAsset(getAssets(), "montserrat_bold.ttf"));
            ((TextPaint) Objects.requireNonNull(tpf.get(object))).setColor(getColor(R.color.white));
        } catch (Exception ignored) {
        }
    }

    public void snackBar(final String message, final int colorId, int duration) {
        //duration -1 short, 0 long, -2 indefinite
        dismissSnackBar();
        snackbar = Snackbar.make(coordinatorLayout, message, duration);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(colorId);
        TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getColor(R.color.white));
        textView.setTextSize(14);
        textView.setMaxLines(5);
        snackbar.show();
    }

    private void dismissSnackBar() {
        if (snackbar != null) {
            snackbar.dismiss();
            snackbar = null;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissSnackBar();
    }
}
