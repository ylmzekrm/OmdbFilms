package com.test.omdbMovieFilter.models;

import com.test.omdbMovieFilter.Utils.Util;

import java.util.ArrayList;

public class ResultWithDetailModel {

    private final String totalResults;
    private final String Response;

    public ResultWithDetailModel(ResultModel result) {
        this.totalResults = result.getTotalResults();
        this.Response = result.getResponse();
        Util.movieDetailList = new ArrayList<>();
    }



    public String getTotalResults() {
        return totalResults;
    }

    public String getResponse() {
        return Response;
    }
}
