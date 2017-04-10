package com.lotus.mapslocale;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }

    public void bt_newuser(View view){
        startActivity(
                new Intent(this,MainActivity.class)
        );
        finish();
    }
    public void bt_map(View view){
        startActivity(
                new Intent(this,MapsActivity.class)
        );
        finish();
    }
}
