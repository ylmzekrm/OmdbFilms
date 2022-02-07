package com.test.omdbMovieFilter.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.test.omdbMovieFilter.R;
import com.test.omdbMovieFilter.Utils.SpacesItemDecoration;
import com.test.omdbMovieFilter.Utils.Util;
import com.test.omdbMovieFilter.adapters.MoviesAdapter;
import com.test.omdbMovieFilter.models.ResultWithDetailModel;
import com.test.omdbMovieFilter.request.Retrofit;


import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ResultWithDetailModel> {

    private TextInputEditText searchEditText;
    private RecyclerView moviesRecyclerView;
    private MoviesAdapter moviesAdapter;
    private Dialog dialog;
    private Snackbar snackbar;
    private ConstraintLayout rootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = findViewById(R.id.root_layout);
        searchEditText = findViewById(R.id.search_edittext);
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getMovies();
                return true;

            }
            return false;
        });
        moviesRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        layoutManager.setAlignItems(AlignItems.CENTER);
        moviesRecyclerView.setLayoutManager(layoutManager);
        moviesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        moviesRecyclerView.addItemDecoration(new SpacesItemDecoration(Util.dpToPx(5,this)));
        moviesAdapter = new MoviesAdapter(this, Util.getMovieDetailList(), MainActivity.this);
        moviesRecyclerView.setAdapter(moviesAdapter);
        ImageView imgSearch= findViewById(R.id.search_img);
        imgSearch.setOnClickListener(view -> getMovies());

    }

    @Override
    protected void onStart() {
        super.onStart();
        dismissSnackBar();
        handler.removeMessages(100);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog !=null){
            dialog.dismiss();
            dialog=null;
        }
        handler.removeMessages(100);
        dismissSnackBar();
    }

    public void snackBar(final String message, final int colorId, int duration) {
        //duration -1 short, 0 long, -2 indefinite
        dismissSnackBar();
        snackbar = Snackbar.make(rootLayout, message, duration);
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



    @NonNull
    @Override
    public Loader<ResultWithDetailModel> onCreateLoader(int id, Bundle args) {
        return new Retrofit(MainActivity.this, Objects.requireNonNull(args).getString("movieTitle"));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ResultWithDetailModel> loader, ResultWithDetailModel resultWithDetail) {
        dismissSnackBar();
        handler.removeMessages(100);
        if (resultWithDetail.getResponse().equals("True")) {
            moviesAdapter = new MoviesAdapter(this, Util.getMovieDetailList(), MainActivity.this);
            moviesRecyclerView.setAdapter(moviesAdapter);
        } else {
            moviesAdapter = new MoviesAdapter(this, new ArrayList<>(), MainActivity.this);
            moviesRecyclerView.setAdapter(moviesAdapter);
            snackBar(getResources().getString(R.string.title_not_found),getColor(R.color.red),Snackbar.LENGTH_LONG);

        }


        MainActivity.this.runOnUiThread(() -> {
            if(dialog !=null){
                dialog.dismiss();
                dialog=null;
            }
        });
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ResultWithDetailModel> loader) {
        runOnUiThread(() -> {
            if(dialog !=null){
                dialog.dismiss();
            }
        });
        dismissSnackBar();
        handler.removeMessages(100);
        moviesAdapter = new MoviesAdapter(this, new ArrayList<>(), MainActivity.this);
        moviesRecyclerView.setAdapter(moviesAdapter);
    }


    private void getMovies() {
        if (Util.isNetworkAvailable(getApplicationContext())) {
            Util.hideSoftKeyboard(MainActivity.this);
            searchEditText.clearFocus();
            String movieTitle = Objects.requireNonNull(searchEditText.getText()).toString().trim();
            if (!movieTitle.isEmpty()) {
                displayDialog();
                Bundle args = new Bundle();
                args.putString("movieTitle", movieTitle);
                LoaderManager.getInstance(MainActivity.this).initLoader(Util.getLoaderId(), args, this);
            } else
                snackBar(getResources().getString(R.string.title_empty),getColor(R.color.peru),Snackbar.LENGTH_LONG);

        } else {
            snackBar(getResources().getString(R.string.network_not_available),getColor(R.color.red),Snackbar.LENGTH_LONG);

        }
    }

    private void displayDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Drawable d = new ColorDrawable(ContextCompat.getColor(this, R.color.black));
        d.setAlpha(200);
        dialog.setContentView(R.layout.dialog_progress);
        if (dialog.getWindow() != null) {
            View decorView = dialog.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            );
            dialog.getWindow()
                    .setLayout(
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(d);
        }
        dialog.show();
        handler.removeMessages(100);
        handler.sendEmptyMessageDelayed(100,20000);
    }

    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            System.out.println("handler test1");
            if(msg.what == 100){
                if(dialog!=null){
                    System.out.println("handler test2");
                    dialog.dismiss();
                    dialog=null;
                }
                snackBar(getResources().getString(R.string.title_not_found),getColor(R.color.red),Snackbar.LENGTH_LONG);
            }
        }
    };

}