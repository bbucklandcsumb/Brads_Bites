package com.example.brads_bites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.brads_bites.DB.AppDataBase;
import com.example.brads_bites.DB.ItemsDAO;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameField;
    private EditText mPasswordField;

    private Button mButtonLogin;
    private Button mButtonCreate;

    private ItemsDAO mItemsDAO;

    private String mUsername;
    private String mPassword;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        wireupDisplay();

        getDatabase();


    }

    private void wireupDisplay(){
        mUsernameField = findViewById(R.id.editTextTextLoginUserName);
        mPasswordField = findViewById(R.id.editTextLoginPassword);

        mButtonLogin = findViewById(R.id.buttonLogin);
        mButtonCreate = findViewById(R.id.buttonCreateUser);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                if(checkForUserInDatabase()) {
                    if (!validatePassword()){
                        Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }else{
                        //Intent intent = MainActivity.intentFactory(getApplicationContext(),mUser.getUserId());
                        //startActivity(intent);
                        //TODO: make this go to landing page
                        Intent intent = LandingActivity.intentFactory(getApplicationContext(), mUser.getUserId());
                        startActivity(intent);
                    }
                }
            }
        });

        mButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                User user = new User(mUsername, mPassword);
                if(!checkForUserInDatabase()) {
                    mItemsDAO.insert(user);
                    Toast.makeText(LoginActivity.this, "User Created", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(LoginActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validatePassword(){
        return mUser.getPassword().equals(mPassword);
    }

    private void getValuesFromDisplay(){
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();
    }

    private boolean checkForUserInDatabase(){
        mUser = mItemsDAO.getUserByUsername(mUsername);
        if (mUser == null){
            Toast.makeText(this, "no user" + mUsername + " found", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void getDatabase(){
        mItemsDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.Items_Name)
                .allowMainThreadQueries()
                .build()
                .ItemsDAO();
    }

    public static Intent intentFactory(Context context){

        return new Intent(context, LoginActivity.class);
    }
}