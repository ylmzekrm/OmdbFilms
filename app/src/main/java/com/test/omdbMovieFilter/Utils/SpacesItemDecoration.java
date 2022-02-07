package com.test.omdbMovieFilter.Utils;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(
            Rect outRect,
            @NonNull View view,
            @NonNull RecyclerView parent,
            @NonNull RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        outRect.top = space;
        // Add top margin only for the first or second item to avoid double space between items
        /* if ((parent.getChildCount() > 0 && parent.getChildPosition(view) == 0)
            || (parent.getChildCount() > 1 && parent.getChildPosition(view) == 1))
        outRect.top = space;*/
    }
}
