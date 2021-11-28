package com.example.smoke;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //btncamera与id关联
        //主菜单中打开摄像头按钮
        Button btncamera = (Button) findViewById(R.id.camera);

        //btnchoose与id关联
        Button btnchoose = (Button) findViewById(R.id.choose);

        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,cameraActivity.class);
                startActivity(intent);
            }
        });
        btnchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,chooseActivity.class);
                startActivity(intent);
            }
        });
        //btncamera.setOnClickListener(this::onClick);
        //btnchoose.setOnClickListener(this::onClick);
    }
    /*
    public void onClick(View v){
        switch (v.getId()){
            case R.id.camera:
                Intent intent1=new Intent(MainActivity.this,cameraActivity.class);
                startActivity(intent1);
                break;
            case R.id.choose:
                Intent intent2=new Intent(MainActivity.this,chooseActivity.class);
                startActivity(intent2);
                break;
        }
    }
     */

}