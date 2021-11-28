package com.example.smoke;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class cameraActivity extends AppCompatActivity {

    //调用Camera类
    private Camera camera;
    //ispreview检测摄像头是否打开
    private boolean ispreview = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //设置摄像头全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //获取surfaceview和surfaceholder
        final SurfaceView surface = findViewById(R.id.surfaceview);//获取surfaceview
        final SurfaceHolder surfaceHolder = surface.getHolder();//获取surfaceholder
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //获取监听预览按钮
        Button preview = findViewById(R.id.preview);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ispreview) {//如果没打开
                    camera = Camera.open();//打开摄像头
                    ispreview = true;
                }
                try {
                    camera.setPreviewDisplay(surfaceHolder);//设置预览
                    Camera.Parameters parameters = camera.getParameters();//获取摄像头参数

                    parameters.setPictureFormat(PixelFormat.JPEG);//设置图片为jpg
                    parameters.set("jpeg-quality", 80);//设置图片质量

                    camera.setParameters(parameters);//重新设置摄像头参数
                    camera.startPreview();//开始预览
                    camera.setDisplayOrientation(90);//不加的话，预览的图像就是横的
                    camera.autoFocus(null);//自动对焦

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        //拍照
        Button take_photo = (Button) findViewById(R.id.take_photo);//获取按钮
        take_photo.setOnClickListener(new View.OnClickListener() {//监听
            @Override
            public void onClick(View v) {
                if (camera != null) {
                    camera.takePicture(null, null, jpeg);
                }
            }
        });
    }


    final Camera.PictureCallback jpeg = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            //根据拍照得到的数据集创建位图
            camera.stopPreview();
            ispreview = false;
            File appDir = new File(Environment.getExternalStorageDirectory(), "/DCIM/Camera/");
            if (!appDir.exists()) {//如果目录不存在
                appDir.mkdir();//创建目录
            }
            String filename = System.currentTimeMillis() + ".jpg";
            File file = new File(appDir, filename);

            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                //创建文件输出流
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                //将图片压缩成JPEG格式输出到输出流
                outputStream.flush();//将缓冲区的数据都输入到输出流
                outputStream.close();//关闭输出流
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //将图片插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(cameraActivity.this.getContentResolver(), file.getAbsolutePath(), filename, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //通知图库更新
            cameraActivity.this.sendBroadcast(new Intent(Intent.
                    ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + "")));
            Toast.makeText(cameraActivity.this, "照片已存" + file, Toast.LENGTH_LONG).show();
            resetCamera();//图片保存后，判断，是否需要重新打开预览，重新创建一个方法，第8步
        }
    };


    //重新检测摄像头是否在预览
    private void resetCamera() {
        if (!ispreview) {
            camera.startPreview();
            ispreview = true;
        }
    }

    //释放摄像头资源
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.stopPreview();
            camera.release();
        }
    }


}