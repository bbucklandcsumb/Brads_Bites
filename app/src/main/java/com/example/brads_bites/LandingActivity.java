package com.example.brads_bites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, LandingActivity.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }

    private static final String USER_ID_KEY = "com.example.brads_bites.userIdKey";
}