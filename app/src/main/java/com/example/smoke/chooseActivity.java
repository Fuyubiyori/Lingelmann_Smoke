package com.example.smoke;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.os.Environment;
import android.app.Activity;

import java.io.File;

public class chooseActivity extends AppCompatActivity {
    //选择图片
    private static final int IMAGE_SELECT = 1;
    //裁剪图片
    private static final int IMAGE_CUT = 2;
    private ImageView imageView;
    private Button btn1;
    private int btnx;
    private Matrix matrix;
    private RadioGroup rg;
    private RadioButton rb1, rb2, rb3, rb4, rb5, rb6;

    shareUtil shareUtil;
    Button btnqqimage;
    Button btnqqtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        //matrix = new Matrix();
        //DisplayMetrics dm = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(dm);

        btn1 = (Button) findViewById(R.id.bt1);
        imageView = (ImageView) findViewById(R.id.iv1);
        btn1.setOnClickListener(this::onClick);
        btnx = btn1.getWidth();
        rg = (RadioGroup) findViewById(R.id.rg);
        btnqqimage = (Button) findViewById(R.id.bt2);
        btnqqtext = (Button) findViewById(R.id.bt3);
        btnqqimage.setOnClickListener(this::onClick);
        btnqqtext.setOnClickListener(this::onClick);

        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        rb3 = (RadioButton) findViewById(R.id.rb3);
        rb4 = (RadioButton) findViewById(R.id.rb4);
        rb5 = (RadioButton) findViewById(R.id.rb5);
        rb6 = (RadioButton) findViewById(R.id.rb6);

        shareUtil = new shareUtil(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_CUT) {
                Bitmap bitmap = data.getParcelableExtra("data");
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    @SuppressLint("ResourceType")
    public void onClick(View v) {
        int qqtext = rg.getCheckedRadioButtonId();
        while (qqtext > 6) {
            qqtext = qqtext % 6;
        }
        qqtext += 1;



        switch (v.getId()) {
            case R.id.bt1: {
                matrix = new Matrix();
                //获取屏幕宽度
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = dm.widthPixels;
                int height = dm.widthPixels;
                Intent it1 = getImageClipIntent();
                startActivityForResult(it1, IMAGE_CUT);

                /*
                String path = null;
                Drawable drawable = imageView.getDrawable();
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "SHARE", null);
                File fileImage = new File(path);
                */

                //用于放大截取的图片
                imageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                //每当选择新的图片时候清空选择
                rg.clearCheck();
            }
                break;

            case R.id.bt2: {
                Drawable mDrawable = imageView.getDrawable();
                Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "Image I want to share", null);
                Uri uri = Uri.parse(path);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "分享到:"));
                //shareUtil.shareImg("com.tencent.mobileqq",
                        //"com.tencent.mobileqq.activity.JumpActivity", fileImage);
            }
                break;
            case R.id.bt3:
                if (!rb1.isChecked() && !rb2.isChecked() && !rb3.isChecked() && !rb4.isChecked() && !rb5.isChecked() && !rb6.isChecked()) {
                    shareUtil.shareText("com.tencent.mobileqq", null, "请提醒分享者并未选择图片黑度",
                            "分享标题", "分享主题");
                } else {
                    shareUtil.shareText("com.tencent.mobileqq", null, "图片黑度为" + qqtext,
                            "分享标题", "分享主题");
                }
                break;

        }
    }

    private Intent getImageClipIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("crop", "true");// 滑动选中图片区域
        intent.putExtra("aspectX", 1);// 表示裁剪切框的比例1:1的效果
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", btnx);// 指定输出图片的大小
        intent.putExtra("outputY", btnx);
        intent.putExtra("return-data", true);
        return intent;
    }

}
