package com.example.ironcero.screenletssimple;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.liferay.mobile.android.auth.Authentication;
import com.liferay.mobile.android.auth.SignIn;
import com.liferay.mobile.android.callback.typed.JSONArrayCallback;
import com.liferay.mobile.android.callback.typed.JSONObjectCallback;
import com.liferay.mobile.android.oauth.OAuth;
import com.liferay.mobile.android.oauth.OAuthConfig;
import com.liferay.mobile.android.oauth.activity.OAuthActivity;
import com.liferay.mobile.android.service.Session;
import com.liferay.mobile.android.service.SessionImpl;
import com.liferay.mobile.android.v62.user.UserService;
import com.liferay.mobile.screens.auth.login.LoginListener;
import com.liferay.mobile.screens.auth.login.LoginScreenlet;
import com.liferay.mobile.screens.context.SessionContext;
import com.liferay.mobile.screens.context.User;
import com.liferay.mobile.screens.context.storage.CredentialsStorageBuilder;
import com.liferay.mobile.screens.util.ServiceProvider;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoginListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _loginScreenlet = (LoginScreenlet)findViewById(R.id.login_screenlet);
        _loginScreenlet.setListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                Log.i(_TAG, "Usuario logado con exito");

                OAuthConfig config = (OAuthConfig) data.getSerializableExtra(
                        OAuthActivity.EXTRA_OAUTH_CONFIG);

                String consumerKey = config.getConsumerKey();
                String consumerSecret = config.getConsumerSecret();
                String token = config.getToken();
                String tokenSecret = config.getTokenSecret();

                Authentication auth = new OAuth(consumerKey, consumerSecret, token, tokenSecret);
                Session session = new SessionImpl(getString(R.string.liferay_server), auth);

                session.setCallback(getCallback(this, config));
                ServiceProvider.getInstance().getCurrentUserConnector(session).getCurrentUser();
            }
        }catch (Exception exception){
            Log.e(_TAG, "err: ", exception);
            exception.printStackTrace();
        }
    }

    @Override
    public void onLoginSuccess(User user) {

    }

    @Override
    public void onLoginFailure(Exception e) {

    }

    private JSONObjectCallback getCallback(final AppCompatActivity mainActivity, final OAuthConfig config){
        return new JSONObjectCallback(){


            @Override
            public void onFailure(Exception exception) {
                Log.e(_TAG, "Error during service call", exception);
                Toast.makeText(mainActivity,"Error during service call", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(JSONObject result) {
                Log.i(_TAG, "Resultado: "+ result.toString());

                User currentUser = new User(result);
                SessionContext.setCurrentUser(currentUser);
                SessionContext.createOAuthSession(config);

                SessionContext.storeCredentials(CredentialsStorageBuilder.StorageType.SHARED_PREFERENCES);

                Intent it = new Intent(mainActivity, HomeActivity.class);
                startActivity(it);
            }
        };
    }



    private LoginScreenlet _loginScreenlet;

    private static final String _TAG = MainActivity.class.getSimpleName();
}
