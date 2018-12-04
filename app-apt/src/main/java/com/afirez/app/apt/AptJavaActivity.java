package com.afirez.app.apt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.afirez.apt.api.HelloWorld;

public class AptJavaActivity extends AppCompatActivity {

    @HelloWorld
    private String name = "afirez";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apt_java);
        String helloWorld = new AptJavaActivityApi().helloWorld(this);
        Log.i("AptJavaActivity", helloWorld);
    }
}
