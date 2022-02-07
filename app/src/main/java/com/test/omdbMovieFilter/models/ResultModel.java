package com.test.omdbMovieFilter.models;


import java.util.List;

public class ResultModel {
    private List<MovieModel> Search;
    private String totalResults;
    private String Response;

    public List<MovieModel> getSearch() {
        return Search;
    }

    public void setSearch(List<MovieModel> search) {
        Search = search;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }


}
