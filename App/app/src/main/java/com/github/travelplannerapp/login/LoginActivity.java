package com.github.travelplannerapp.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.github.travelplannerapp.travels.TravelsActivity;
import com.github.travelplannerapp.R;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    LoginContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //set up presenter
        presenter =  new LoginPresenter(this);

        //set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set up button
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.signIn();
            }
        });
    }

    @Override
    public void showTravels() {
        Intent intent = new Intent(this, TravelsActivity.class);
        startActivity(intent);
    }
}
