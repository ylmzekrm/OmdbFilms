package com.test.omdbMovieFilter.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.test.omdbMovieFilter.R;
import com.test.omdbMovieFilter.Utils.Util;
import com.test.omdbMovieFilter.activities.DetailActivity;
import com.test.omdbMovieFilter.models.DetailModel;

import java.util.List;
import java.util.Objects;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private final Context context;
    private final List<DetailModel> list;
    private final Activity activity;

    public MoviesAdapter(Context context, List<DetailModel> list, Activity activity) {
        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesAdapter.ViewHolder holder, final int position) {

        final DetailModel detail = list.get(position);
        holder.title.setText(detail.getTitle());
        holder.year.setText(detail.getYear());
        holder.action.setText(detail.getGenre());
        if (!Objects.equals(detail.getPoster(),"N/A")) {
            Glide.with(activity).load(detail.getPoster()).into(holder.thumbImage);
        } else {
            Glide.with(activity).load(R.drawable.ic_movie_poster).into(holder.thumbImage);
        }


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            Util.DETAIL_MODEL = detail;
            Util.IMAGE_URL = detail.getPoster();

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(activity,
                            holder.thumbImage, "poster");
            context.startActivity(intent, options.toBundle());
        });
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView year;
        public final TextView action;
        public final ImageView thumbImage;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.movie_title);
            year = view.findViewById(R.id.movie_year);
            thumbImage = view.findViewById(R.id.thumbnail);
            action = view.findViewById(R.id.movie_action);
        }
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
    }


}
