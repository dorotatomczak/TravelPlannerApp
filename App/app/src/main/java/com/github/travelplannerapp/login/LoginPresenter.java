package com.github.travelplannerapp.login;

import com.github.travelplannerapp.BasePresenter;

public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    protected LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void signIn() {
        //example of the view and presenter flow
        view.showTravels();
    }
}
