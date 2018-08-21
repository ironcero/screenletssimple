package com.example.ironcero.screenletssimple;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liferay.mobile.android.callback.typed.JSONArrayCallback;
import com.liferay.mobile.android.service.Session;
import com.liferay.mobile.android.v62.group.GroupService;
import com.liferay.mobile.screens.context.SessionContext;
import com.liferay.mobile.screens.context.storage.CredentialsStorageBuilder;

import org.json.JSONArray;
import org.json.JSONObject;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        try {
            SessionContext.loadStoredCredentials(CredentialsStorageBuilder.StorageType.SHARED_PREFERENCES);
            Session sessionFromCurrentSession = SessionContext.createSessionFromCurrentSession();
            sessionFromCurrentSession.setCallback(getCallback());

            Button logoutButton = (Button) findViewById(R.id.logout_button);
            logoutButton.setOnClickListener(getLogoutClickListener(this));

            JSONObject command = new JSONObject();
            command.put("/group/get-user-sites", new JSONObject());

            sessionFromCurrentSession.invoke(command);

            //GroupService service = new GroupService(sessionFromCurrentSession);

            //service.getUserSites();
        }
        catch (Exception e) {
            Log.e(_TAG, "Error during service call", e);
            TextView resultInput = (TextView) findViewById(R.id.site_result);
            resultInput.setText("Error during service call: " + e.toString());
        }
    }

    private JSONArrayCallback getCallback(){
        return new JSONArrayCallback(){


            @Override
            public void onFailure(Exception exception) {
                Log.e(_TAG, "Error during service call", exception);
            }

            @Override
            public void onSuccess(JSONArray result) {
                Log.i(_TAG, "Resultado: "+ result.toString());
                TextView resultInput = (TextView) findViewById(R.id.site_result);
                resultInput.setText(result.toString());
            }
        };
    }

    private View.OnClickListener getLogoutClickListener(final AppCompatActivity activity){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SessionContext.isLoggedIn()){
                    SessionContext.logout();
                    SessionContext.removeStoredCredentials(CredentialsStorageBuilder.StorageType.SHARED_PREFERENCES);
                    if(SessionContext.isLoggedIn()){
                        Log.e(_TAG, "No se pudo hacer logout");
                    }else{
                        Log.i(_TAG, "Usuario desconectado");
                        startActivity(new Intent(activity, AutoLoginActivity.class));
                    }
                }
            }
        };
    }

    private static final String _TAG = HomeActivity.class.getSimpleName();
}
