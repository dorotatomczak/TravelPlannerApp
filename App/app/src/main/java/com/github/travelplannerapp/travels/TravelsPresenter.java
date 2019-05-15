package com.github.travelplannerapp.travels;

import com.github.travelplannerapp.BasePresenter;

public class TravelsPresenter extends BasePresenter<TravelsContract.View>
        implements TravelsContract.Presenter {

    protected TravelsPresenter(TravelsContract.View view) {
        super(view);
    }
}
