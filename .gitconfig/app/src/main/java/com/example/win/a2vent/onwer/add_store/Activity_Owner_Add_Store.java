package com.example.win.a2vent.onwer.add_store;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.win.a2vent.onwer.add_event.Activity_Owner_Add_Event;
import com.example.win.a2vent.util.GlobalData;
import com.example.win.a2vent.util.ImageURI;
import com.example.win.a2vent.R;
import com.example.win.a2vent.databinding.ActivityOwnerAddStoreBinding;
import com.example.win.a2vent.util.ServerConnector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class Activity_Owner_Add_Store extends AppCompatActivity {

    private final String TAG = "테스트";

    ActivityOwnerAddStoreBinding binding;

    String com_category;
    ArrayList<String> mManagerList = null;

    //주소 검색
    private static final int SEARCH_ADDRESS = 4;

    String[] arrayManager = null;
    ImageURI imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_owner_add_store);

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String com_number = binding.etComNumber.getText().toString();
                String com_name = binding.etComName.getText().toString();
                String com_addr = binding.etComAddress.getText().toString();
                String com_detail_addr = binding.etComDetailAddress.getText().toString();
                String ID = GlobalData.getUserID();
                String com_manager = binding.spinComManager.getSelectedItem().toString();
                String com_URI = imageURI.getFileName();

                new InsertData().execute(com_number, com_name, com_addr, com_detail_addr, com_category, com_manager, com_URI, ID,
                        imageURI.getFileDir().concat(imageURI.getFileName()));
            }
        });

        binding.rgComCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rbComCulture:
                        com_category = "0";
                        break;
                    case R.id.rbComFood:
                        com_category = "1";
                        break;
                    case R.id.rbComBeauty:
                        com_category = "2";
                        break;
                    case R.id.rbComFashion:
                        com_category = "3";
                        break;
                }
            }
        });

        imageURI = new ImageURI(Activity_Owner_Add_Store.this);

        binding.ivComImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Owner_Add_Store.this);
                builder.setMessage("이미지 삽입").setCancelable(true)
                        .setPositiveButton("앨범", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                imageURI.goToAlbum();
                            }
                        }).setNegativeButton("촬영", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imageURI.takePhoto();
                    }
                }).setNeutralButton("닫기", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        binding.etComAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_Owner_Add_Store.this, Activity_Owner_Add_Store_WebView.class);
                startActivityForResult(i, SEARCH_ADDRESS);
            }
        });

        mManagerList = new ArrayList<>();

        GetManager GetManager = new GetManager();
        GetManager.execute();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(Activity_Owner_Add_Store.this)
                .setTitle("취소하시겠습니까?").setCancelable(false)
                .setMessage("작성한 내용이 저장되지 않습니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imageURI.deleteCroppedImage();
                        Activity_Owner_Add_Store.this.finish();
                    }
                }).setNegativeButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        }).show();
    }

    public void setSpinnerManagerList() {
        arrayManager = new String[mManagerList.size()];

        for (int i = 0; i < arrayManager.length; i++) {
            arrayManager[i] = mManagerList.get(i);
        }

        Log.e("새로 만든거", String.valueOf(arrayManager.length));

        ArrayAdapter spin_adapter = new ArrayAdapter(getApplicationContext(), R.layout.owner_add_store_spin, arrayManager);
        spin_adapter.setDropDownViewResource(R.layout.owner_add_store_spin_drop);

        binding.spinComManager.setAdapter(spin_adapter);
        binding.spinComManager.setSelection(0);
        binding.spinComManager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    class InsertData extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Activity_Owner_Add_Store.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {

            String com_number = params[0];
            String com_name = params[1];
            String com_addr = params[2];
            String com_detail_addr = params[3];
            String com_category = params[4];
            String com_manager = params[5];
            String com_URI = params[6];
            String id = params[7];
            String sourceFileUri = params[8];

            String phpPage = "2ventAddstore.php";

            try {
                ServerConnector serverConnector = new ServerConnector(phpPage);

                serverConnector.addPostData("com_number", com_number);
                serverConnector.addPostData("com_name", com_name);
                serverConnector.addPostData("com_addr", com_addr);
                serverConnector.addPostData("com_detail_addr", com_detail_addr);
                serverConnector.addPostData("com_category", com_category);
                serverConnector.addPostData("com_manager", com_manager);
                serverConnector.addPostData("com_URI", com_URI);
                serverConnector.addPostData("id", id);

                serverConnector.addDelimiter();
                serverConnector.writePostData();

                serverConnector.addFileData("uploaded_file", sourceFileUri);
                serverConnector.writeFileData(sourceFileUri);

                serverConnector.finish();

                return serverConnector.response();

            } catch (Exception e) {
                Log.d("DB", "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d(TAG, "POST response  - " + result);

            if (result.equals("SQL문 처리 성공")) {
                imageURI.deleteCroppedImage();
                Toast.makeText(Activity_Owner_Add_Store.this, "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                Activity_Owner_Add_Store.this.finish();
            } else {
                Toast.makeText(Activity_Owner_Add_Store.this, "처리 실패", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "ActivityResult");

        Log.d(TAG, "ActivityResult - resultCode = " + requestCode);
        if (resultCode != RESULT_OK) {
            Toast.makeText(Activity_Owner_Add_Store.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == ImageURI.PICK_FROM_ALBUM) {
            Log.d(TAG, "ActivityResult - PICK_FROM_ALBUM");

            if (data == null) {
                return;
            }

            imageURI.setPhotoUri(data.getData());
            imageURI.cropImage();
        } else if (requestCode == ImageURI.PICK_FROM_CAMERA) {
            Log.d(TAG, "ActivityResult - PICK_FROM_CAMERA");
            imageURI.cropImage();
            MediaScannerConnection.scanFile(Activity_Owner_Add_Store.this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
                    new String[]{imageURI.getPhotoUri().getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } else if (requestCode == ImageURI.CROP_FROM_CAMERA) {
            Log.d(TAG, "ActivityResult - CROP_FROM_CAMERA");
            try { //저는 bitmap 형태의 이미지로 가져오기 위해 아래와 같이 작업하였으며 Thumbnail을 추출하였습니다.
//                this.grantUriPermission("com", photoUri,
//                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI.getPhotoUri());
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 128, 128);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs); //이미지가 클 경우 OutOfMemoryException 발생이 예상되어 압축

                //여기서는 ImageView에 setImageBitmap을 활용하여 해당 이미지에 그림을 띄우시면 됩니다.
                binding.ivComImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage().toString());
            }
        } else if (requestCode == SEARCH_ADDRESS) {
            try {
                binding.etComAddress.setText(data.getStringExtra("addr"));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }

    }

    private class GetManager extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Activity_Owner_Add_Store.this,
                    "Please Wait", null, true, true);
        }

        protected String doInBackground(String... args) {

            try {
                ServerConnector serverConnector = new ServerConnector("2ventAddstoreGetManager.php");

                serverConnector.addDelimiter();
                serverConnector.writePostData();

                serverConnector.finish();

                return serverConnector.response();

            } catch (MalformedURLException e) {
                return new String("NullPoint: " + e.getMessage());
            } catch (Exception e) {
                return new String("Error: " + e.getMessage());
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "response - " + result);

            if (result == null) {

            } else {
                jsonParser(result);
                setSpinnerManagerList();
            }

            progressDialog.dismiss();
        }

        private void jsonParser(String str) {
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray("manager");

                JSONObject item;

                for (int i = 0; i < jsonArray.length(); i++) {
                    item = jsonArray.getJSONObject(i);

                    mManagerList.add(item.getString("id"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
