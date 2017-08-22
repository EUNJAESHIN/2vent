package com.example.win.a2vent.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by win on 2017-08-09.
 */

public class ImageURI { // TODO

    private final String TAG = "테스트";

    public static final int PICK_FROM_CAMERA = 1; //카메라 촬영으로 사진 가져오기
    public static final int PICK_FROM_ALBUM = 2; //앨범에서 사진 가져오기
    public static final int CROP_FROM_CAMERA = 3; //가져온 사진을 자르기 위한 변수

    private Activity mActivity;

    private Uri mPhotoUri;
    private String mFileName;
    private String mFileDir;

    public ImageURI(Activity activity) {
        mActivity = activity;
    }

    public void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK); //ACTION_PICK 즉 사진을 고르겠다!
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        mActivity.startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //사진을 찍기 위하여 설정합니다.
        File photoFile = null;

        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            mActivity.finish();
        }

        if (photoFile != null) {
            mPhotoUri = FileProvider.getUriForFile(mActivity,
                    "com.example.win.a2vent.provider", photoFile); //FileProvider의 경우 이전 포스트를 참고하세요.

            Toast.makeText(mActivity, mPhotoUri.toString(), Toast.LENGTH_SHORT).show();

            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri); //사진을 찍어 해당 Content uri를 photoUri에 적용시키기 위함

            mActivity.startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    // Android M에서는 Uri.fromFile 함수를 사용하였으나 7.0부터는 이 함수를 사용할 시 FileUriExposedException이
    // 발생하므로 아래와 같이 함수를 작성합니다. 이전 포스트에 참고한 영문 사이트를 들어가시면 자세한 설명을 볼 수 있습니다.
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "2vent" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/2vent/Images/"); //test라는 경로에 이미지를 저장하기 위함

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mFileDir = storageDir.toString() + "/";
        mFileName = image.getName();

        return image;
    }

    //모든 작업에 있어 사전에 FALG_GRANT_WRITE_URI_PERMISSION과 READ 퍼미션을 줘야 uri를 활용한 작업에 지장을 받지 않는다는 것이 핵심입니다.
    public void cropImage() {
        mActivity.grantUriPermission("com.android.camera", mPhotoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(mPhotoUri, "image/*");

        List<ResolveInfo> list = mActivity.getPackageManager().queryIntentActivities(intent, 0);

        mActivity.grantUriPermission(list.get(0).activityInfo.packageName, mPhotoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (list.size() == 0) {
            return;
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 4);
            intent.putExtra("aspectY", 3);
            intent.putExtra("scale", true);

            File croppedFileName = null;

            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File folder = new File(Environment.getExternalStorageDirectory() + "/2vent/Images/");
            File tempFile = new File(folder.toString(), croppedFileName.getName());
            mPhotoUri = FileProvider.getUriForFile(mActivity, "com.example.win.a2vent.provider", tempFile);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //Bitmap 형태로 받기 위해 해당 작업 진행

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);

            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            mActivity.grantUriPermission(res.activityInfo.packageName, mPhotoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

            Log.d(TAG, "ImageURI - startActivityForResult CROP_FROM_CAMERA");
            mActivity.startActivityForResult(i, CROP_FROM_CAMERA);
        }
    }

    public String getFileName() {
        return mFileName;
    }

    public String getFileDir() {
        return mFileDir;
    }

    public Uri getPhotoUri() {
        return mPhotoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        mPhotoUri = photoUri;
    }
}
