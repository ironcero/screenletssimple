package com.example.ironcero.screenletssimple;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liferay.mobile.screens.auth.login.LoginListener;
import com.liferay.mobile.screens.auth.login.LoginScreenlet;
import com.liferay.mobile.screens.context.User;

public class MainActivity extends AppCompatActivity implements LoginListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginScreenlet loginScreenlet = (LoginScreenlet)findViewById(R.id.login_screenlet);
        loginScreenlet.setListener(this);
    }


    @Override
    public void onLoginSuccess(User user) {
        Intent it = new Intent(this, HomeActivity.class);
        startActivity(it);
    }

    @Override
    public void onLoginFailure(Exception e) {
        int i = 0;
    }

    @Override
    public void onAuthenticationBrowserShown() {
        int i = 0;
    }
}
