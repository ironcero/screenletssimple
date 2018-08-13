package com.example.ironcero.screenletssimple;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liferay.mobile.screens.context.LiferayScreensContext;
import com.liferay.mobile.screens.context.SessionContext;
import com.liferay.mobile.screens.context.storage.CredentialsStorageBuilder;

public class AutoLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_login);

        LiferayScreensContext.init(this);
        SessionContext.loadStoredCredentials(CredentialsStorageBuilder.StorageType.SHARED_PREFERENCES);

        if(SessionContext.isLoggedIn()){
            startActivity(new Intent(this, HomeActivity.class));
        }else{
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
