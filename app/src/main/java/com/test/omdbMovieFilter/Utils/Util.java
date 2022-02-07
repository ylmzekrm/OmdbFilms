package com.test.omdbMovieFilter.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;

import com.test.omdbMovieFilter.interfaces.OmdbInterface;
import com.test.omdbMovieFilter.models.DetailModel;
import com.test.omdbMovieFilter.models.ResultModel;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Util {
    private static  int LOADER_ID = 1;
    private static final String API_KEY = "26930e1d";
    public static  String API_URL = "http://www.omdbapi.com";
    public static OmdbInterface omdbInterface;
    public static List<DetailModel> movieDetailList;
    public static DetailModel DETAIL_MODEL = null ;
    public static String IMAGE_URL = null;

    public static  int getLoaderId(){
        LOADER_ID =LOADER_ID+1;
        return LOADER_ID;
    }

    public static void addToList(DetailModel detail) {
        movieDetailList.add(detail);
    }

    public static List<DetailModel> getMovieDetailList() {
        return movieDetailList;
    }

    private static void setsOmdb() {
        if (Util.omdbInterface == null) {
            OkHttpClient.Builder httpClient =
                    new OkHttpClient.Builder();
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("apikey", Util.API_KEY)
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            });
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Util.API_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Create an instance of our OMDB API interface.
            Util.omdbInterface = retrofit.create(OmdbInterface.class);
        }
    }

    public static ResultModel performSearch(String title) throws IOException {
        setsOmdb();
        Call<ResultModel> call = Util.omdbInterface.Result(title);
        return call.execute().body();
    }

    public static DetailModel getDetail(String imdbId) throws IOException {
        setsOmdb();
        Call<DetailModel> call = Util.omdbInterface.Detail(imdbId);
        return call.execute().body();
    }

    public static int dpToPx(int dp,Context context) {
        Resources r = context.getResources();
        return Math.round(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if(activity.getCurrentFocus().getWindowToken()!=null){
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
