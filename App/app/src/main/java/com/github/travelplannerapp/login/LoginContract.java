package com.github.travelplannerapp.login;

public interface LoginContract {
    interface View{
        void showTravels();
    }

    interface Presenter{
        void signIn();
    }
}
