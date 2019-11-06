package rndmjck.com.si_tifuniks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import rndmjck.com.si_tifuniks.Utility.SessionManager;


public class SplashScreenActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        finish();


//        else {
//            Intent intent = new Intent(this, ActivityNavigation.class);
//            startActivity(intent);
//            finish();
//
//        }
    }

}

