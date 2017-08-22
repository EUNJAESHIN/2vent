package com.example.win.a2vent.onwer.add_store;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.win.a2vent.util.GlobalData;
import com.example.win.a2vent.util.ImageURI;
import com.example.win.a2vent.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class Activity_Owner_Add_Store extends AppCompatActivity implements View.OnClickListener {

    TextView v_com_name;
    TextView v_com_addr;
    RadioButton v_com_radio_culture;
    RadioButton v_com_radio_food;
    RadioButton v_com_radio_beauty;
    RadioButton v_com_radio_fashion;
    RadioButton v_com_radio_travel;
    Spinner v_com_manager;
    TextView v_com_number;
    URI v_com_URI;
    ImageView v_com_iv;
    Button v_com_button;
    RadioGroup v_com_radio;
    Button btn_search_addr;
    TextView v_com_addr2;


    String com_name = null;
    String com_addr = null;
    String com_category;
    String com_manager = null;
    String com_URI = null;
    //유저목록 불러와야됨
    //이미지 삽입 해야됨
    String ID;
    String com_number;
    ArrayList<String> managerList = null;

    //주소 검색
    WebView webview;
    private Handler handler;
    private static final int SEARCH_ADDRESS = 4;


    //사진 관련
    Button btn_gall;
    Button btn_picture;
    String[] city = null;
    ImageURI imageURI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_add_store);

        v_com_name = (TextView) findViewById(R.id.com_form_name);
        v_com_addr = (TextView) findViewById(R.id.com_form_addr);
        v_com_addr2 = (TextView) findViewById(R.id.com_form_addr2);
        v_com_radio_culture = (RadioButton) findViewById(R.id.com_form_radio_culture);
        v_com_radio_food = (RadioButton) findViewById(R.id.com_form_radio_meal);
        v_com_radio_beauty = (RadioButton) findViewById(R.id.com_form_radio_beauty);
        v_com_radio_fashion = (RadioButton) findViewById(R.id.com_form_radio_fashion);
        v_com_radio_travel = (RadioButton) findViewById(R.id.com_form_radio_meal);
        v_com_manager = (Spinner) findViewById(R.id.com_form_manager);
        v_com_number = (TextView) findViewById(R.id.com_form_number);
        v_com_radio = (RadioGroup) findViewById(R.id.com_form_radio);
        btn_gall = (Button) findViewById(R.id.com_btn_gall);
        btn_picture = (Button) findViewById(R.id.com_btn_picture);
        btn_search_addr = (Button) findViewById(R.id.com_btn_search_addr);


//        v_com_URI
        v_com_iv = (ImageView) findViewById(R.id.com_form_iv);
        v_com_button = (Button) findViewById(R.id.com_button);

        clicked clicked = new clicked();
        v_com_button.setOnClickListener(clicked);

        v_com_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.com_form_radio_culture:
                        com_category = "0";
                        break;
                    case R.id.com_form_radio_meal:
                        com_category = "1";
                        break;
                    case R.id.com_form_radio_beauty:
                        com_category = "2";
                        break;
                    case R.id.com_form_radio_fashion:
                        com_category = "3";
                        break;
                }
            }
        });

        imageURI = new ImageURI(Activity_Owner_Add_Store.this);

        btn_gall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageURI.goToAlbum();
            }
        });

        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageURI.takePhoto();
            }
        });

        btn_search_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_Owner_Add_Store.this, Activity_Owner_Add_Store_WebView.class);
                startActivityForResult(i, SEARCH_ADDRESS);
//                // WebView 초기화
//                init_webView();
//
//                // 핸들러를 통한 JavaScript 이벤트 반응
//                handler = new Handler();
            }
        });


        managerList = new ArrayList<String>();


//        Log.e("시티",city.toString());
        getManager getManager = new getManager();
        getManager.execute();


//        Thread workingTread = new Thread(){
//            public void run(){
//
//
//
//                }
//
//        };
//
//        workingTread.start();
//        try {
//            workingTread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


    }

    public void asdf() {
        city = new String[managerList.size()];

        for (int i = 0; i < city.length; i++) {
            city[i] = managerList.get(i);

        }


//        Log.e("왜", String.valueOf(managerList.get(4)));

        Log.e("새로 만든거", String.valueOf(city.length));


        ArrayAdapter spin_adapter = new ArrayAdapter(getApplicationContext(), R.layout.owner_add_store_spin, city);
        spin_adapter.setDropDownViewResource(R.layout.owner_add_store_spin_drop);

        v_com_manager.setAdapter(spin_adapter);
        v_com_manager.setSelection(0);
//        Log.e("매니저 값",v_com_manager.getSelectedItem());
        v_com_manager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                v_com_manager.setSelection(Integer.parseInt(managerList.get(position).toString()));
//                Log.e("매니저",v_com_manager.getSelectedItem().toString());
//                Toast.makeText(getApplicationContext(),Integer.toString(position),Toast.LENGTH_LONG).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    class clicked implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.com_button:
                    com_number = v_com_number.getText().toString();
                    com_name = v_com_name.getText().toString();
                    if (v_com_addr2 == null) {
                        com_addr = null;
                        com_addr = v_com_addr.getText().toString();
                    } else {
                        com_addr = null;
                        com_addr = v_com_addr.getText().toString() + v_com_addr2.getText().toString();
                    }

                    Log.e("맞나?", com_addr);


                    ID = GlobalData.getUserID();
                    com_manager = v_com_manager.getSelectedItem().toString();
                    com_URI = imageURI.getFileName();

                    Log.i("asyntask", "됨");
                    InsertData_com com_Task = new InsertData_com();
                    Log.i("asyntask", "됨");
                    com_Task.execute(com_number, com_name, com_addr, com_category, com_manager, com_URI, ID,
                            imageURI.getFileDir() + "" + imageURI.getFileName(), imageURI.getFileDir(), imageURI.getFileName());

                    break;

            }
        }
    }

    @Override
    public void onClick(View v) {


    }

    class InsertData_com extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.i("asyntask", "됨");
            progressDialog = ProgressDialog.show(Activity_Owner_Add_Store.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {

            String com_number = (String) params[0];
            String com_name = (String) params[1];
            String com_addr = (String) params[2];
            String com_category = (String) params[3];
            String com_manager = (String) params[4];
            String com_URI = (String) params[5];
            String id = (String) params[6];
            String sourceFileUri = (String) params[7];


            String fileName = sourceFileUri;
            File sourceFile = new File(sourceFileUri);

            String boundary = "^******^";
            DataOutputStream dos = null;
            String delimiter = "\r\n--" + boundary + "\r\n";
            int bytesRead, bytesAvailable, bufferSize;
            int maxBufferSize = 1 * 1024 * 1024;
            byte[] buffer;

            String serverURL = GlobalData.getURL() + "YTest.php";
            String postParameters = "&com_number=" + com_number + "&com_name=" + com_name
                    + "&com_addr=" + com_addr + "&com_category=" + com_category
                    + "&com_manager=" + com_manager + "&com_URI=" + com_URI + "&id=" + id;


            try {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true); // Allow Inputs
                httpURLConnection.setDoOutput(true); // Allow Outputs
                httpURLConnection.setUseCaches(false); // Don't use a Cached Copy
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                StringBuffer postDataBuilder = new StringBuffer();

                postDataBuilder.append(delimiter);
                postDataBuilder.append(setValue("com_number", com_number));
                postDataBuilder.append(delimiter);
                postDataBuilder.append(setValue("com_name", com_name));
                postDataBuilder.append(delimiter);
                postDataBuilder.append(setValue("com_addr", com_addr));
                postDataBuilder.append(delimiter);
                postDataBuilder.append(setValue("com_category", com_category));
                postDataBuilder.append(delimiter);
                postDataBuilder.append(setValue("com_manager", com_manager));
                postDataBuilder.append(delimiter);
                postDataBuilder.append(setValue("com_URI", com_URI));
                postDataBuilder.append(delimiter);
                postDataBuilder.append(setValue("id", id));
                postDataBuilder.append(delimiter);

                dos = new DataOutputStream(httpURLConnection.getOutputStream());

                postDataBuilder.append(setFile("uploaded_file", fileName));
                postDataBuilder.append("\r\n");

                dos.writeUTF(postDataBuilder.toString());

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(delimiter);
                dos.flush();
                dos.close();
                fileInputStream.close();


//                outputStream.flush();
//                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("DB", "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();

                return sb.toString();

            } catch (Exception e) {
                Log.d("DB", "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d("DB", "POST response  - " + result);
        }

        public String setValue(String key, String value) {
            return "Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n" + value;
        }

        public String setFile(String key, String fileName) {
            return "Content-Disposition: form-data; name=\"" + key + "\";filename=\"" + fileName + "\"\r\n";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Toast.makeText(Activity_Owner_Add_Store.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == ImageURI.PICK_FROM_ALBUM) {
            Log.d("테스트", "ActivityResult - PICK_FROM_ALBUM");

            if (data == null) {
                return;
            }

            imageURI.setPhotoUri(data.getData());
            imageURI.cropImage();


        } else if (requestCode == ImageURI.PICK_FROM_CAMERA) {
            Log.d("테스트", "ActivityResult - PICK_FROM_CAMERA");
            imageURI.cropImage();
            MediaScannerConnection.scanFile(Activity_Owner_Add_Store.this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
                    new String[]{imageURI.getPhotoUri().getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } else if (requestCode == ImageURI.CROP_FROM_CAMERA) {
            Log.d("테스트", "ActivityResult - CROP_FROM_CAMERA");
            try { //저는 bitmap 형태의 이미지로 가져오기 위해 아래와 같이 작업하였으며 Thumbnail을 추출하였습니다.
//                this.grantUriPermission("com", photoUri,
//                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI.getPhotoUri());
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 128, 128);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs); //이미지가 클 경우 OutOfMemoryException 발생이 예상되어 압축


                //여기서는 ImageView에 setImageBitmap을 활용하여 해당 이미지에 그림을 띄우시면 됩니다.
                v_com_iv.setImageBitmap(bitmap);

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage().toString());

            }
        } else if (requestCode == SEARCH_ADDRESS) {
            try {
                v_com_addr.setText(data.getStringExtra("addr"));
                String test = data.getStringExtra("addr");
                Log.e("맞나?", test);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }

    }

    private class getManager extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        protected String doInBackground(String... args) {
            StringBuilder sb = null;
            try {


                URL url = new URL(GlobalData.getURL() + "2ventAddstoreGetManager.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream os = httpURLConnection.getOutputStream();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF8"));
//                writer.write("?event_number="+args[0]);
                writer.flush();
                writer.close();
                os.close();

                httpURLConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF8")); //캐릭터셋 설정

                sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (sb.length() > 0) {
                        sb.append("\n");
                    }
                    sb.append(line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("받은 json ", s);
            jsonParser(s);
            asdf();


        }

    }

    public void jsonParser(String s) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(s);

            JSONArray jsonArray = jsonObject.getJSONArray("manager");


            for (int i = 0; i < jsonArray.length(); i++) {


                JSONObject item = jsonArray.getJSONObject(i);

//                managerList.
                managerList.add(item.getString("id"));
//                managerList.add(item.getgetString("id"));
//            managerList.add(item.getString());
                Log.e("item", item.toString());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
