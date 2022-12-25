package com.example.imagepro;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.IOException;

public class StorageActivity extends AppCompatActivity {

    private Button select_image;
    private Button select_from_camera;
    private ImageView predicted_image_view;
    private TextView emotion_result_txt;
    private facialExpressionRecognition facialExpressionRecognition;
    int SELECT_PICTURE =200;
    int SELECT_FROM_CAMERA = 3;

    public String emotion_txt(String input){
        String emo_txt = input;
        return emo_txt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        select_image = findViewById(R.id.select_image);
        select_from_camera = findViewById(R.id.select_from_camera);
        predicted_image_view = findViewById(R.id.predicted_image_view);
        emotion_result_txt = findViewById(R.id.emotion_result_txt);

        try{
            // input size of model is 48
            int inputSize=48;
            facialExpressionRecognition=new facialExpressionRecognition(getAssets(),StorageActivity.this,
                    "model300.tflite",inputSize);

        }
        catch (IOException e){
            e.printStackTrace();
        }

        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_chooser();
            }
        });

        select_from_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, SELECT_FROM_CAMERA);
                    //startActivityForResult(Intent.createChooser(cameraIntent,"Select Image"),SELECT_FROM_CAMERA);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });

    }

    private void image_chooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),SELECT_PICTURE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK){
            if(requestCode==SELECT_PICTURE){
                Uri selectedImageUri = data.getData();
                if(selectedImageUri!=null){
                    Log.d("StoragePActivity","Output Uri: "+selectedImageUri);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImageUri);
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }

                    Mat selected_image = new Mat(bitmap.getHeight(),bitmap.getWidth(), CvType.CV_8UC4);
                    Utils.bitmapToMat(bitmap,selected_image);
                    selected_image=facialExpressionRecognition.recognizePhoto(selected_image);
                    Bitmap bitmap1 = null;
                    bitmap1 = Bitmap.createBitmap(selected_image.cols(),selected_image.rows(),Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(selected_image,bitmap1);
                    predicted_image_view.setImageBitmap(bitmap1);
                }
            }
            else if(requestCode==SELECT_FROM_CAMERA){
                Bitmap imageCam = (Bitmap) data.getExtras().get("data");
                //int dimension = Math.min(image.getWidth(), image.getHeight());
                //image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                Mat selected_image = new Mat(imageCam.getHeight(),imageCam.getWidth(), CvType.CV_8UC4);
                Utils.bitmapToMat(imageCam,selected_image);
                selected_image=facialExpressionRecognition.recognizePhoto(selected_image);
                Bitmap bitmap2 = null;
                bitmap2 = Bitmap.createBitmap(selected_image.cols(),selected_image.rows(),Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(selected_image,bitmap2);
                predicted_image_view.setImageBitmap(bitmap2);

                //emotion_result_txt.setText(emotion_txt("input"));
                String emotion_result = facialExpressionRecognition.emotion_s;
                emotion_result_txt.setText(emotion_result);
            }
        }
    }




}