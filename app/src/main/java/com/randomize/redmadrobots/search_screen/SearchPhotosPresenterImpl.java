package com.randomize.redmadrobots.search_screen;

import com.randomize.redmadrobots.models.SearchResults;

public class SearchPhotosPresenterImpl implements
        SearchContract.Presenter, SearchContract.GetNoticeIntractor.OnFinishedListener {

    private SearchContract.SearchView searchView;
    private SearchContract.GetNoticeIntractor getNoticeIntractor;

    public SearchPhotosPresenterImpl(SearchContract.SearchView searchView, SearchContract.GetNoticeIntractor getNoticeIntractor) {
        this.searchView = searchView;
        this.getNoticeIntractor = getNoticeIntractor;
    }

    @Override
    public void onDestroy() {
        searchView = null;
    }

    @Override
    public void requestFirstData(String query) {
        if (searchView != null) {
            searchView.showRecyclerView();
            searchView.showProgress();
            searchView.hideNetworkError();
            searchView.hideNoResultView();
            getNoticeIntractor.getFirstNoticeList(this, query);
        }
    }

    @Override
    public void requestAddData(String query, int countPage) {
        if (searchView != null) {
            searchView.showProgress();
            searchView.hideNoResultView();
            getNoticeIntractor.getNoticeList(this, query, countPage);
        }
    }

    @Override
    public void onFirstFinished(SearchResults results) {
        if (searchView != null) {
            searchView.setTotalPhoto(results.getTotal());
            searchView.setDataToRecyclerView(results.getResults());
            searchView.hideProgress();
        }
    }

    @Override
    public void onFinished(SearchResults results) {
        if (searchView != null) {
            searchView.addDataToRecyclerView(results.getResults());
            searchView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        if (searchView != null) {
            searchView.onResponseFailure(throwable);
        }
    }
}
