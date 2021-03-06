package com.afirez.app.dagger2;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.afirez.app.dagger2.a_inject_component.InjectComponentActivity;
import com.afirez.app.dagger2.b_module.ModuleActivity;
import com.afirez.app.dagger2.c_deep_insight_component.DeepInsightComponentActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void helloWorldOnClick(View view) {
        Toast.makeText(this, "Hello World!", Toast.LENGTH_SHORT).show();
    }

    public void injectComponentOnClick(View view) {
        Intent intent = new Intent(this, InjectComponentActivity.class);
        startActivity(intent);
    }

    public void moduleOnClick(View view) {
        Intent intent = new Intent(this, ModuleActivity.class);
        startActivity(intent);
    }

    public void deepInsightOnClick(View view) {
        Intent intent = new Intent(this, DeepInsightComponentActivity.class);
        startActivity(intent);
    }
}
