package com.example.ironcero.screenletssimple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.liferay.mobile.android.callback.typed.JSONArrayCallback;
import com.liferay.mobile.android.service.Session;
import com.liferay.mobile.android.v62.group.GroupService;
import com.liferay.mobile.screens.context.SessionContext;
import org.json.JSONArray;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Session sessionFromCurrentSession = SessionContext.createSessionFromCurrentSession();
        sessionFromCurrentSession.setCallback(getCallback());

        GroupService service = new GroupService(sessionFromCurrentSession);
        try {
            service.getUserSites();
        }
        catch (Exception e) {
            Log.e(_TAG, "Error during service call", e);
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

    private static final String _TAG = HomeActivity.class.getSimpleName();
}
