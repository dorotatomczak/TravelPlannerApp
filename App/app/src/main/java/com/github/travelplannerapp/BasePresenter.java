package com.github.travelplannerapp;

public class BasePresenter<V> {
    protected final V view;

    protected BasePresenter(V view) {
        this.view = view;
    }
}
