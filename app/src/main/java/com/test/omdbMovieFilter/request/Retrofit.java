package com.test.omdbMovieFilter.request;

import android.content.Context;
import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

import com.google.gson.Gson;
import com.test.omdbMovieFilter.Utils.Util;
import com.test.omdbMovieFilter.models.MovieModel;
import com.test.omdbMovieFilter.models.ResultModel;
import com.test.omdbMovieFilter.models.ResultWithDetailModel;


public class Retrofit extends AsyncTaskLoader<ResultWithDetailModel> {
    private final String title;
    private ResultWithDetailModel data;

    public Retrofit(Context context, String title) {
        super(context);
        this.title = title;
        System.out.println("testx1");
    }

    @Override
    public ResultWithDetailModel loadInBackground() {
        try {
            System.out.println("testx2");
            ResultModel result = Util.performSearch(title);
            ResultWithDetailModel resultWithDetail = new ResultWithDetailModel(result);

            if (result.getSearch() != null) {
                System.out.println("testx3");
                for (MovieModel movie : result.getSearch()) {
                    Util.addToList(Util.getDetail(movie.getImdbID()));
                    //Log.d("Retrofit "," detail = "+new Gson().toJson(Util.getDetail(movie.getImdbID())));
                }
            }
            return resultWithDetail;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onStartLoading() {
        System.out.println("testx4");
        if (data != null) {
            System.out.println("testx5");
            deliverResult(data);
        } else {
            System.out.println("testx6");
            forceLoad();
        }
    }


    @Override
    protected void onReset() {
        super.onReset();
        data = null;
        System.out.println("testx7");
    }

    @Override
    public void deliverResult(ResultWithDetailModel data) {
        System.out.println("test8");
        if (isReset()) {
            return;
        }
        this.data = data;
        if (isStarted()) {
            super.deliverResult(data);
        }

    }
}
