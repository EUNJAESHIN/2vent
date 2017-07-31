package com.example.win.a2vent;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Created by win on 2017-07-10.
 */

public class owner_Event_Form extends AppCompatActivity {

    private final static String TAG = "테스트";

    private RelativeLayout rlEventForm;

    private RadioGroup rgType;
    private int type = 0;
    private int status;

    private String strComNo;
    private ArrayList<CharSequence> arrListStore, arrListComNo;
    private Spinner spinStore;
    private EditText etFixedPrice, etDiscount, etLimitPersons;

    private EditText etStartDateYear, etStartDateMonth, etStartDateDay, etStartHour, etStartMin, etEndDateYear, etEndDateMonth, etEndDateDay, etEndHour, etEndMin;

    private RadioGroup rgPayment;
    private RadioButton rbCashPayment, rbCardPayment;
    private int payment = 0;

    private EditText etEventName, etMinimumAge, etMaximumAge;

    private static final int REQ_CODE_SELECT_IMAGE = 100;
    private String absolutePath;
    private String uploadImgPath = "";
    private File filePath;
    private ImageView imgContents;

    private Switch swConditions;
    private int conditions = 0;

    private RadioGroup rgSex;
    private RadioButton rbMale, rbFemale, rbAllSex;
    private int sex = 2;

    private Spinner spinLocation;

    private Button btnAddSubmit, btnAddTemp, btnAddCancel;

    private int mEvent_number;
    private boolean flagUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_event_form);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog().build());

        verifyStoragePermissions(owner_Event_Form.this);

        rlEventForm = (RelativeLayout) findViewById(R.id.rlEventForm);
        rlEventForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        rgType = (RadioGroup) findViewById(R.id.rgType);
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rbTypeEnter:
                        invisiblePayment();
                        type = 0;
                        break;
                    case R.id.rbTypePay:
                        visiblePayment();
                        type = 1;
                        break;
                }
            }
        });

        etFixedPrice = (EditText) findViewById(R.id.etFixedPrice);
        etDiscount = (EditText) findViewById(R.id.etDiscount);
        etFixedPrice.addTextChangedListener(new TextWatcher_MoneyToComma(etFixedPrice));
        etDiscount.addTextChangedListener(new TextWatcher_MoneyToComma(etDiscount));

        etLimitPersons = (EditText) findViewById(R.id.etLimitPersons);
        etLimitPersons.addTextChangedListener(new TextWatcher_MoneyToComma(etLimitPersons));

        etStartDateYear = (EditText) findViewById(R.id.etStartDateYear);
        etStartDateMonth = (EditText) findViewById(R.id.etStartDateMonth);
        etStartDateDay = (EditText) findViewById(R.id.etStartDateDay);
        etStartHour = (EditText) findViewById(R.id.etStartHour);
        etStartMin = (EditText) findViewById(R.id.etStartMin);

        etEndDateYear = (EditText) findViewById(R.id.etEndDateYear);
        etEndDateMonth = (EditText) findViewById(R.id.etEndDateMonth);
        etEndDateDay = (EditText) findViewById(R.id.etEndDateDay);
        etEndHour = (EditText) findViewById(R.id.etEndHour);
        etEndMin = (EditText) findViewById(R.id.etEndMin);

        rbCashPayment = (RadioButton) findViewById(R.id.rbCashPayment);
        rbCardPayment = (RadioButton) findViewById(R.id.rbCardPayment);
        rgPayment = (RadioGroup) findViewById(R.id.rgPayment);
        rgPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rbCashPayment:
                        //Toast.makeText(owner_event_form.this, rbCashPayment.getText().toString(), Toast.LENGTH_SHORT).show();
                        payment = 0;
                        break;
                    case R.id.rbCardPayment:
                        //Toast.makeText(owner_event_form.this, rbCardPayment.getText().toString(), Toast.LENGTH_SHORT).show();
                        payment = 1;
                        break;
                }
            }
        });

        etEventName = (EditText) findViewById(R.id.etEventName);

        imgContents = (ImageView) findViewById(R.id.imgContents);
        imgContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent _intent = new Intent(Intent.ACTION_PICK);
                _intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                _intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(_intent, REQ_CODE_SELECT_IMAGE);
                Log.d(TAG, "터치");
            }
        });

        swConditions = (Switch) findViewById(R.id.swConditions);
        swConditions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedConditions(isChecked);
                if (isChecked) {
                    conditions = 1;
                } else {
                    conditions = 0;
                }
            }
        });

        etMinimumAge = (EditText) findViewById(R.id.etMinimumAge);
        etMaximumAge = (EditText) findViewById(R.id.etMaximumAge);

        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        rbAllSex = (RadioButton) findViewById(R.id.rbAllSex);
        rgSex = (RadioGroup) findViewById(R.id.rgSex);
        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rbMale:
                        sex = 1;
                        break;
                    case R.id.rbFemale:
                        sex = 0;
                        break;
                    case R.id.rbAllSex:
                        sex = 2;
                        break;
                }
            }
        });

        ArrayAdapter<CharSequence> arrAdtLocations = ArrayAdapter.createFromResource(owner_Event_Form.this, R.array.locations, R.layout.support_simple_spinner_dropdown_item);
        spinLocation = (Spinner) findViewById(R.id.spinLocation);
        spinLocation.setAdapter(arrAdtLocations);
        spinLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinLocation.setEnabled(false);

        btnAddSubmit = (Button) findViewById(R.id.btnAddSubmit);
        btnAddSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_AddSubmit();
            }
        });
        btnAddTemp = (Button) findViewById(R.id.btnAddTemp);
        btnAddTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_AddTemp();
            }
        });
        btnAddCancel = (Button) findViewById(R.id.btnAddCancel);
        btnAddCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                owner_Event_Form.super.onBackPressed();
            }
        });

        invisiblePayment();

        GetData getData = new GetData();
        getData.execute(GlobalData.getUserID());

        if ((getIntent()) != null) {
            try {
                Intent intent = getIntent();
                mEvent_number = Integer.parseInt(intent.getStringExtra("event_number"));

                GetTempEvent getTempEvent = new GetTempEvent();
                getTempEvent.execute(String.valueOf(mEvent_number));

                flagUpdate = true;

                Log.d(TAG, "flagUpdate: " + flagUpdate);
            } catch (NumberFormatException e) {

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        this.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "ActivityResult");
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            Log.d(TAG, "REQ_CODE_SELECT_IMAGE");
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Log.d(TAG, "RESULT_OK");
                    // Uri에서 이미지 이름을 얻어온다.
                    String strName = getImageNameToUri(data.getData());
                    Uri selPhotoUri = data.getData();
                    Log.d(TAG, "strName: " + strName);
                    // 절대경로 획득
                    Cursor cursor = getContentResolver().query(Uri.parse(selPhotoUri.toString()), null, null, null, null);
                    cursor.moveToNext();
                    absolutePath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));

                    // 이미지 데이터를 비트맵으로 받아옴
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    // 이미지 리사이징
                    int height = image_bitmap.getHeight();
                    int width = image_bitmap.getWidth();

                    Bitmap src = BitmapFactory.decodeFile(absolutePath);
                    Bitmap resized = Bitmap.createScaledBitmap(src, width / 2, height / 2, true);

                    saveBitmapToJPEG(resized, "2ventApp", strName);

                    Log.d(TAG, "불러오기");

                    imgContents.setImageBitmap(resized);
                    imgContents.setTag("exist");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkedConditions(boolean isChecked) {
        if (isChecked) {
            activeContents();
        } else {
            inActiveContents();
        }
    }

    private void activeContents() {
        int darkGray = Color.argb(255, 102, 102, 102);
        TextView tvAge = (TextView) findViewById(R.id.tvAge);
        TextView tvAge1 = (TextView) findViewById(R.id.tvAge1);
        TextView tvSex = (TextView) findViewById(R.id.tvSex);
        TextView tvLocations = (TextView) findViewById(R.id.tvLocation);
        tvAge.setTextColor(darkGray);
        tvAge1.setTextColor(darkGray);
        tvSex.setTextColor(darkGray);
        tvLocations.setTextColor(darkGray);
        etMinimumAge.setEnabled(true);
        etMinimumAge.setTextColor(darkGray);
        etMaximumAge.setEnabled(true);
        etMaximumAge.setTextColor(darkGray);
        rbMale.setEnabled(true);
        rbMale.setTextColor(darkGray);
        rbFemale.setEnabled(true);
        rbFemale.setTextColor(darkGray);
        rbAllSex.setEnabled(true);
        rbAllSex.setTextColor(darkGray);
        spinLocation.setEnabled(true);
    }

    private void inActiveContents() {
        TextView tvAge = (TextView) findViewById(R.id.tvAge);
        TextView tvAge1 = (TextView) findViewById(R.id.tvAge1);
        TextView tvSex = (TextView) findViewById(R.id.tvSex);
        TextView tvLocations = (TextView) findViewById(R.id.tvLocation);
        tvAge.setTextColor(Color.LTGRAY);
        tvAge1.setTextColor(Color.LTGRAY);
        tvSex.setTextColor(Color.LTGRAY);
        tvLocations.setTextColor(Color.LTGRAY);
        etMinimumAge.setEnabled(false);
        etMinimumAge.setTextColor(Color.LTGRAY);
        etMaximumAge.setEnabled(false);
        etMaximumAge.setTextColor(Color.LTGRAY);
        rbMale.setEnabled(false);
        rbMale.setTextColor(Color.LTGRAY);
        rbFemale.setEnabled(false);
        rbFemale.setTextColor(Color.LTGRAY);
        rbAllSex.setEnabled(false);
        rbAllSex.setTextColor(Color.LTGRAY);
        spinLocation.setEnabled(false);
    }

    private void visiblePayment() {
        TextView tvPayment = (TextView) findViewById(R.id.tvPayment);
        tvPayment.setVisibility(View.VISIBLE);
        rgPayment.setVisibility(View.VISIBLE);
        rbCashPayment.setEnabled(true);
        rbCardPayment.setEnabled(true);
    }

    private void invisiblePayment() {
        TextView tvPayment = (TextView) findViewById(R.id.tvPayment);
        tvPayment.setVisibility(View.GONE);
        rgPayment.setVisibility(View.GONE);
        rbCashPayment.setEnabled(false);
        rbCardPayment.setEnabled(false);
    }

    private void onClick_AddSubmit() {
        status = 0;
        String msg = checkInputData();
        input(msg);
    }

    private void onClick_AddTemp() {
        status = 1;
        String msg = checkInputTempData();
        input(msg);
        Log.d(TAG, "event_number: " + mEvent_number);
    }

    private void input(String msg) {
        if (msg != null) {
            Toast.makeText(owner_Event_Form.this, msg, Toast.LENGTH_SHORT).show();
        } else {
            String _name = etEventName.getText().toString();
            String _type = String.valueOf(type);
            String _stats = String.valueOf(status);
            String _URI;
            String _price = etFixedPrice.getText().toString().trim().replaceAll(",", "");
            String _dis_price = etDiscount.getText().toString().trim().replaceAll(",", "");
            String _people = etLimitPersons.getText().toString().trim().replaceAll(",", "");
            String _startday = etStartDateYear.getText().toString().trim() + etStartDateMonth.getText().toString().trim() + etStartDateDay.getText().toString().trim();
            String _endday = etEndDateYear.getText().toString().trim() + etEndDateMonth.getText().toString().trim() + etEndDateDay.getText().toString().trim();
            String _starttime = etStartHour.getText().toString().trim() + etStartMin.getText().toString().trim() + "00";
            String _endtime = etEndHour.getText().toString().trim() + etEndMin.getText().toString().trim() + "00";
            String _payment = String.valueOf(payment);
            String _target = String.valueOf(conditions);
            String _minage;
            String _maxage;
            String _sex;
            String _area;
            String _com_number = strComNo;
            String _id = GlobalData.getUserID();

            if (!uploadImgPath.equals("")) {
                _URI = "event_img/" + _id + "/" + _name + ".jpg";
            } else {
                _URI = "";
            }

            if (conditions == 1) {
                _minage = etMinimumAge.getText().toString().trim();
                _maxage = etMaximumAge.getText().toString().trim();
                _sex = String.valueOf(sex);
                _area = spinLocation.getSelectedItem().toString();
            } else {
                _minage = "";
                _maxage = "";
                _sex = "";
                _area = "";
            }

            InsertData inputTask = new InsertData();
            if (flagUpdate) {
                inputTask.execute(_name, _type, _stats, _URI, _price, _dis_price, _people, _startday,
                        _endday, _starttime, _endtime, _payment, _target, _minage, _maxage, _sex, _area,
                        _com_number, _id, String.valueOf(mEvent_number));
            } else {
                inputTask.execute(_name, _type, _stats, _URI, _price, _dis_price, _people, _startday,
                        _endday, _starttime, _endtime, _payment, _target, _minage, _maxage, _sex, _area,
                        _com_number, _id, "");
            }



        }
    }

    private String checkInputData() {
        if (isEmptyEditText(etFixedPrice)) {
            return "정상가를 입력하세요";
        } else if (isEmptyEditText(etDiscount)) {
            return "할인가를 입력하세요";
        } else if (isEmptyEditText(etLimitPersons)) {
            return "인원제한을 입력하세요";
        } else if (isEmptyEditText(etStartDateYear) || isEmptyEditText(etStartDateMonth) || isEmptyEditText(etStartDateDay) || isEmptyEditText(etStartHour) || isEmptyEditText(etStartMin)) {
            return "이벤트 시작일시를 확인하세요";
        } else if (!checkStartDate(etStartDateYear, etStartDateMonth, etStartDateDay, etStartHour, etStartMin)) {
            return "현재 날짜보다 이전입니다";
        } else if (isEmptyEditText(etEndDateYear) || isEmptyEditText(etEndDateMonth) || isEmptyEditText(etEndDateDay) || isEmptyEditText(etEndHour) || isEmptyEditText(etEndMin)) {
            return "이벤트 종료일시를 확인하세요";
        } else if (!checkEndDate(etStartDateYear, etStartDateMonth, etStartDateDay, etStartHour, etStartMin, etEndDateYear, etEndDateMonth, etEndDateDay, etEndHour, etEndMin)) {
            return "종료 날짜가 시작 날짜보다 이전입니다";
        } else if (isEmptyEditText(etEventName)) {
            return "이벤트 명을 입력하세요";
        } else if (swConditions.isChecked()) {
            if (isEmptyEditText(etMinimumAge) && isEmptyEditText(etMaximumAge)) {
                return "참가 나이를 입력하세요";
            } else {
                int maxAge = Integer.parseInt(etMaximumAge.getText().toString().trim());
                int minAge = Integer.parseInt(etMinimumAge.getText().toString().trim());

                if (maxAge < minAge) {
                    return "참가 나이를 확인하세요";
                }
            }
        } else if (uploadImgPath.equals("")) {
            return "이미지를 등록하세요";
        }

        return null;
    }

    private String checkInputTempData() {
        if (isEmptyEditText(etFixedPrice)) {
            return "정상가를 입력하세요";
        } else if (isEmptyEditText(etDiscount)) {
            return "할인가를 입력하세요";
        } else if (isEmptyEditText(etEventName)) {
            return "이벤트 명을 입력하세요";
        }

        return null;
    }

    private boolean isEmptyEditText(EditText et) {
        if (TextUtils.isEmpty(et.getText().toString().trim())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkStartDate(EditText etYear, EditText etMonth, EditText etDay, EditText etHour, EditText etMin) {
        int year = Integer.parseInt(etYear.getText().toString().trim());
        int month = Integer.parseInt(etMonth.getText().toString().trim());
        int day = Integer.parseInt(etDay.getText().toString().trim());
        int hour = Integer.parseInt(etHour.getText().toString().trim());
        int min = Integer.parseInt(etMin.getText().toString().trim());
        int curYear, curMonth, curDay, curHour, curMin;
        Calendar cal = Calendar.getInstance();

        curYear = cal.get(Calendar.YEAR);

        if (year < curYear) {
            return false;
        } else {
            curMonth = cal.get(Calendar.MONTH) + 1;
            if (month < curMonth) {
                return false;
            } else {
                if (month == curMonth) {
                    curDay = cal.get(Calendar.DATE);
                    if (day < curDay) {
                        return false;
                    } else {
                        if (day == curDay) {
                            curHour = cal.get(Calendar.HOUR);
                            if (hour < curHour) {
                                return false;
                            } else {
                                if (hour == curHour) {
                                    curMin = cal.get(Calendar.MINUTE);
                                    if (min < curMin) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean checkEndDate(EditText etYear1, EditText etMonth1, EditText etDay1, EditText etHour1, EditText etMin1, EditText etYear2, EditText etMonth2, EditText etDay2, EditText etHour2, EditText etMin2) {
        int year1 = Integer.parseInt(etYear1.getText().toString().trim());
        int month1 = Integer.parseInt(etMonth1.getText().toString().trim());
        int day1 = Integer.parseInt(etDay1.getText().toString().trim());
        int hour1 = Integer.parseInt(etHour1.getText().toString().trim());
        int min1 = Integer.parseInt(etMin1.getText().toString().trim());
        int year2 = Integer.parseInt(etYear2.getText().toString().trim());
        int month2 = Integer.parseInt(etMonth2.getText().toString().trim());
        int day2 = Integer.parseInt(etDay2.getText().toString().trim());
        int hour2 = Integer.parseInt(etHour2.getText().toString().trim());
        int min2 = Integer.parseInt(etMin2.getText().toString().trim());

        if (year1 > year2) {
            return false;
        } else {
            if (month1 > month2) {
                return false;
            } else {
                if (month1 == month2) {
                    if (day1 > day2) {
                        return false;
                    } else {
                        if (day1 == day2) {
                            if (hour1 > hour2) {
                                return false;
                            } else {
                                if (hour1 == hour2) {
                                    if (min1 > min2) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(etFixedPrice.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etDiscount.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etLimitPersons.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etStartDateYear.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etStartDateMonth.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etStartDateDay.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etStartHour.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etStartMin.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etEndDateYear.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etEndDateMonth.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etEndDateDay.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etEndHour.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etEndMin.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etEventName.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etMinimumAge.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etMaximumAge.getWindowToken(), 0);
    }

    private static void verifyStoragePermissions(Activity activity) {
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    private void saveBitmapToJPEG(Bitmap bitmap, String folder, String name) {
        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folder_name = "/" + folder + "/";
        String file_name = name;
        String str_path = ex_storage + folder_name;
        uploadImgPath = str_path + file_name;

        Log.d(TAG, "saveBitmapToJPEG");

        try {
            filePath = new File(str_path);
            Log.d(TAG, "isDirectory");
            if (!filePath.isDirectory()) {
                filePath.mkdirs();
                Log.d(TAG, "mkdirs");
            }
            FileOutputStream out = new FileOutputStream(uploadImgPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("FileNotFountException", e.getMessage());
            Log.d(TAG, "FileNotFoundException");
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
            Log.d(TAG, "IOException");
        }
    }

    private String getImageNameToUri(Uri data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        return imgName;
    }

    private class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(owner_Event_Form.this, "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            String event_name = params[0];
            String event_type = params[1];
            String event_stats = params[2];
            String event_URI = params[3];
            String event_price = params[4];
            String event_dis_price = params[5];
            String event_people = params[6];
            String event_startday = params[7];
            String event_endday = params[8];
            String event_starttime = params[9];
            String event_endtime = params[10];
            String event_payment = params[11];
            String event_target = params[12];
            String event_minage = params[13];
            String event_maxage = params[14];
            String event_sex = params[15];
            String event_area = params[16];
            String com_number = params[17];
            String id = params[18];
            String event_number = params[19];

            String phpPage = "2ventAddevent.php";

            try {

                ServerConnector serverConnector = new ServerConnector(phpPage);

                serverConnector.addPostData("event_name", event_name);
                serverConnector.addPostData("event_type", event_type);
                serverConnector.addPostData("event_stats", event_stats);
                serverConnector.addPostData("event_URI", event_URI);
                serverConnector.addPostData("event_price", event_price);
                serverConnector.addPostData("event_dis_price", event_dis_price);
                serverConnector.addPostData("event_people", event_people);
                serverConnector.addPostData("event_startday", event_startday);
                serverConnector.addPostData("event_endday", event_endday);
                serverConnector.addPostData("event_starttime", event_starttime);
                serverConnector.addPostData("event_endtime", event_endtime);
                serverConnector.addPostData("event_payment", event_payment);
                serverConnector.addPostData("event_target", event_target);
                serverConnector.addPostData("event_minage", event_minage);
                serverConnector.addPostData("event_maxage", event_maxage);
                serverConnector.addPostData("event_sex", event_sex);
                serverConnector.addPostData("event_area", event_area);
                serverConnector.addPostData("com_number", com_number);
                serverConnector.addPostData("id", id);
                serverConnector.addPostData("event_number", event_number);

                if (!uploadImgPath.equals("")) {
                    // 파일 첨부
                    Log.d(TAG, "uploadedPath: " + uploadImgPath);
                    serverConnector.addFileData("uploaded_file", uploadImgPath);

                    // 전송 작업 시작
                    serverConnector.writeFileData(uploadImgPath);
                    serverConnector.finish();
                } else {
                    serverConnector.addDelimiter();
                    serverConnector.writePostData();
                    serverConnector.finish();
                }

                return serverConnector.response();

            } catch (NullPointerException e) {
                return new String("NullPoint: " + e.getMessage());
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error", e);

                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d(TAG, "POST response - " + result);

            if (result.equals("SQL문 처리 성공")) {
                switch (status) {
                    case 0:
                        Toast.makeText(owner_Event_Form.this, "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(owner_Event_Form.this, "임시저장 되었습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
                owner_Event_Form.this.onBackPressed();
            }
        }
    }

    private class TextWatcher_MoneyToComma implements TextWatcher {
        private EditText mEditText;
        String strAmount = "";

        public TextWatcher_MoneyToComma(EditText e) {
            mEditText = e;
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(strAmount)) {
                strAmount = setStrDataToComma(s.toString().replace(",", ""));
                mEditText.setText(strAmount);
                Editable e = mEditText.getText();
                Selection.setSelection(e, strAmount.length());
            }
        }

        protected String setStrDataToComma(String str) {
            if (str.length() == 0) {
                return "";
            }
            long value = Long.parseLong(str);
            DecimalFormat format = new DecimalFormat("###,###");
            return format.format(value);
        }
    }

    private class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ServerConnector serverConnector = new ServerConnector("2ventGetStoreName.php");

                serverConnector.addPostData("id", params[0]);
                serverConnector.addDelimiter();

                serverConnector.writePostData();
                serverConnector.finish();

                return serverConnector.response();

            } catch (NullPointerException e) {
                return new String("NullPoint: " + e.getMessage());
            } catch (Exception e) {
                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "json : " + result);
            jsonParser(result);
        }

        public void jsonParser(String str) {
            JSONObject jsonObject;
            ArrayList<CharSequence> arrList1 = new ArrayList<>();
            ArrayList<CharSequence> arrList2 = new ArrayList<>();

            try {
                jsonObject = new JSONObject(str);
                JSONArray jsonArray = jsonObject.getJSONArray("ComNameList");

                JSONObject item;
                String com_number;
                String com_name;

                if (jsonArray.length() == 0) {
                    Toast.makeText(owner_Event_Form.this,
                            "등록된 매장이 없습니다. 매장을 등록해주세요.", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            owner_Event_Form.this.onBackPressed();
                        }
                    }, 2000);
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    item = jsonArray.getJSONObject(i);

                    com_number = item.getString("com_number");
                    com_name = item.getString("com_name");

                    arrList1.add(com_number);
                    arrList2.add(com_name);
                }

                arrListComNo = arrList1;
                arrListStore = arrList2;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            setAdapter(arrListStore);
        }

        public void setAdapter(final ArrayList<CharSequence> arrList) {
            ArrayAdapter<CharSequence> arrAdtSpinner = new ArrayAdapter<>(owner_Event_Form.this, R.layout.support_simple_spinner_dropdown_item, arrList);
            spinStore = (Spinner) findViewById(R.id.spinStore);
            spinStore.setAdapter(arrAdtSpinner);
            spinStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    for (int i = 0; i < spinStore.getCount(); i++) {
                        if (spinStore.getSelectedItemPosition() == i) {
                            strComNo = String.valueOf(arrListComNo.get(i));
                            Log.d(TAG, "strComNo : " + strComNo);
                        }
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private class GetTempEvent extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String phpPage = "2ventGetTempEvent.php";

            try {
                ServerConnector serverConnector = new ServerConnector(phpPage);

                serverConnector.addPostData("event_number", params[0]);
                serverConnector.addDelimiter();

                serverConnector.writePostData();
                serverConnector.finish();

                return serverConnector.response();

            } catch (NullPointerException e) {
                return new String("NullPoint: " + e.getMessage());
            } catch (Exception e) {
                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {

            } else {
                setTempData(result);
            }
        }

        private void setTempData(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("TempEvent");

                JSONObject item;
                String event_name = null, event_type = null, event_stats = null, event_URI = null, event_price = null, event_dis_price = null,
                        event_people = null, event_startday = null, event_endday = null, event_starttime = null, event_endtime = null,
                        event_payment = null, event_target = null, event_minage = null, event_maxage = null, event_sex = null,
                        event_area = null, com_name = null;

                String tmp_year, tmp_month, tmp_day, tmp_hour, tmp_min;
                DecimalFormat format = new DecimalFormat("###,###");

                for (int i = 0; i < jsonArray.length(); i++) {
                    item = jsonArray.getJSONObject(i);

                    event_name = item.getString("event_name");
                    event_type = item.getString("event_type");
                    event_stats = item.getString("event_stats");
                    event_URI = item.getString("event_URI");
                    event_price = item.getString("event_price");
                    event_dis_price = item.getString("event_dis_price");
                    event_people = item.getString("event_people");
                    event_startday = item.getString("event_startday");
                    event_endday = item.getString("event_endday");
                    event_starttime = item.getString("event_starttime");
                    event_endtime = item.getString("event_endtime");
                    event_payment = item.getString("event_payment");
                    event_target = item.getString("event_target");
                    event_minage = item.getString("event_minage");
                    event_maxage = item.getString("event_maxage");
                    event_sex = item.getString("event_sex");
                    event_area = item.getString("event_area");
                    com_name = item.getString("com_name");
                }

                if (event_type.equals("0")) {
                    rgType.check(R.id.rbTypeEnter);
                    invisiblePayment();
                    type = 0;
                } else if (event_type.equals("1")) {
                    rgType.check(R.id.rbTypePay);
                    visiblePayment();
                    type = 1;
                }

                for (int j = 0; j < arrListStore.size(); j++) {
                    if (arrListStore.get(j) == com_name) {
                        spinStore.setSelection(j);
                    }
                }

                etFixedPrice.setText(format.format(Long.parseLong(event_price)));
                etDiscount.setText(format.format(Long.parseLong(event_dis_price)));
                etLimitPersons.setText(format.format(Long.parseLong(event_people)));

                StringTokenizer tokenDay;
                StringTokenizer tokenTime;

                if (!event_startday.equals("0000-00-00")) {
                    tokenDay = new StringTokenizer(event_startday, "-");

                    try {
                        tmp_year = tokenDay.nextToken();
                        tmp_month = tokenDay.nextToken();
                        tmp_day = tokenDay.nextToken();

                        etStartDateYear.setText(tmp_year);
                        etStartDateMonth.setText(tmp_month);
                        etStartDateDay.setText(tmp_day);
                    } catch (NoSuchElementException e) {

                    }
                }

                if (!event_starttime.equals("00:00:00")) {
                    tokenTime = new StringTokenizer(event_starttime, ":");

                    try {
                        tmp_hour = tokenTime.nextToken();
                        tmp_min = tokenTime.nextToken();

                        etStartHour.setText(tmp_hour);
                        etStartMin.setText(tmp_min);
                    } catch (NoSuchElementException e) {

                    }
                }

                if (!event_endday.equals("0000-00-00")) {
                    tokenDay = new StringTokenizer(event_endday, "-");

                    try {
                        tmp_year = tokenDay.nextToken();
                        tmp_month = tokenDay.nextToken();
                        tmp_day = tokenDay.nextToken();

                        etEndDateYear.setText(tmp_year);
                        etEndDateMonth.setText(tmp_month);
                        etEndDateDay.setText(tmp_day);
                    } catch (NoSuchElementException e) {

                    }
                }

                if (!event_endtime.equals("00:00:00")) {
                    tokenTime = new StringTokenizer(event_endtime, ":");

                    try {
                        tmp_hour = tokenTime.nextToken();
                        tmp_min = tokenTime.nextToken();

                        etEndHour.setText(tmp_hour);
                        etEndMin.setText(tmp_min);
                    } catch (NoSuchElementException e) {

                    }
                }

                etEventName.setText(event_name);

                Log.d(TAG, "event_URI: " + event_URI);
                if (!event_URI.equals("")) {
                    Picasso.with(getApplicationContext()).load(GlobalData.getURL() + event_URI)
                            .placeholder(R.drawable.event_default).into(imgContents);
                    uploadImgPath = event_URI;
                } else {
                    uploadImgPath = "";
                }

                if (event_target.equals("0")) {
                    swConditions.setChecked(false);
                    inActiveContents();
                } else if (event_target.equals("1")) {
                    swConditions.setChecked(true);
                    activeContents();
                    if (!event_minage.equals("0")) etMinimumAge.setText(event_minage);
                    if (!event_maxage.equals("0")) etMaximumAge.setText(event_maxage);
                    if (event_sex.equals("0")) {
                        rgSex.check(R.id.rbFemale);
                    } else if (event_sex.equals("1")) {
                        rgSex.check(R.id.rbMale);
                    } else if (event_sex.equals("2")) {
                        rgSex.check(R.id.rbAllSex);
                    }
                    if (!event_area.equals("")) {
                        for (int i = 0; i < spinLocation.getCount(); i++) {
                            if (spinLocation.getItemAtPosition(i).equals(event_area)) {
                                spinLocation.setSelection(i);
                            }
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
