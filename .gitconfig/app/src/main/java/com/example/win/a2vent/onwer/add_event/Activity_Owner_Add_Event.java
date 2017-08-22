package com.example.win.a2vent.onwer.add_event;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.example.win.a2vent.util.GetImageURI;
import com.example.win.a2vent.util.GlobalData;
import com.example.win.a2vent.util.ImageURI;
import com.example.win.a2vent.R;
import com.example.win.a2vent.util.ServerConnector;
import com.example.win.a2vent.databinding.ActivityOwnerAddEventBinding;

/**
 * Created by win on 2017-07-10.
 */

public class Activity_Owner_Add_Event extends AppCompatActivity {

    private final static String TAG = "테스트";

    private Context mContext;

    private int type = 0;
    private int status;

    private String strComNo;
    private ArrayList<CharSequence> arrListStore, arrListComNo;
    private Spinner spinStore;

    private int payment = 0;

    private RecyclerView.Adapter<Owner_Add_Event_Holder> rvAdtContents;
    private ArrayList<Owner_Add_Event_Item> arrayListContents;
    private int arrayPosition = 0;
    private final int MAX_IMAGE_LIST = 3;
    private ImageURI imageURI;
    byte[] bytes;
    private int conditions = 0;
    private int sex = 2;

    private ActivityOwnerAddEventBinding binding;

    private String mEvent_number = "-1";
    private boolean flagUpdate = false;
    private boolean flagRegisterReceiver = false;
    private String mResultTempData;
    private boolean flagRegisterURIReceiver = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_owner_add_event);

        mContext = getApplicationContext();

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog().build());

        binding.rlEventForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        binding.rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

        binding.etFixedPrice.addTextChangedListener(new TextWatcher_MoneyToComma(binding.etFixedPrice));
        binding.etDiscount.addTextChangedListener(new TextWatcher_MoneyToComma(binding.etDiscount));
        binding.etLimitPersons.addTextChangedListener(new TextWatcher_MoneyToComma(binding.etLimitPersons));

        binding.rgPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rbCashPayment:
                        //Toast.makeText(activity_owner_add_event.this, rbCashPayment.getText().toString(), Toast.LENGTH_SHORT).show();
                        payment = 0;
                        break;
                    case R.id.rbCardPayment:
                        //Toast.makeText(activity_owner_add_event.this, rbCardPayment.getText().toString(), Toast.LENGTH_SHORT).show();
                        payment = 1;
                        break;
                }
            }
        });

        binding.etContents.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        imageURI = new ImageURI(Activity_Owner_Add_Event.this);
        arrayListContents = new ArrayList<>();
        addImageList(null, null, null);

        binding.swConditions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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


        binding.rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

        ArrayAdapter<CharSequence> arrAdtLocations = ArrayAdapter.createFromResource(Activity_Owner_Add_Event.this, R.array.locations, R.layout.support_simple_spinner_dropdown_item);

        binding.spinLocation.setAdapter(arrAdtLocations);
        binding.spinLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spinLocation.setEnabled(false);

        binding.btnAddSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_AddSubmit();
            }
        });
        binding.btnAddTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_AddTemp();
            }
        });
        binding.btnAddCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_Owner_Add_Event.this.onBackPressed();
            }
        });
        //TODO 이벤트 등록시 미리보기
        binding.btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date = binding.etStartDateYear.getText().toString() + "-" +
                        binding.etStartDateMonth.getText().toString() + "-" +
                        binding.etStartDateDay.getText().toString() + " " +
                        binding.etStartHour.getText().toString() + ":" +
                        binding.etStartMin.getText().toString() + ":00 ~ \n                  " +
                        binding.etEndDateYear.getText().toString() + "-" +
                        binding.etEndDateMonth.getText().toString() + "-" +
                        binding.etEndDateDay.getText().toString() + " " +
                        binding.etEndHour.getText().toString() + ":" +
                        binding.etEndMin.getText().toString() + ":00";

                Intent intent_goReview = new Intent(mContext, Activity_Owner_Add_Event_Review.class);
                intent_goReview.putExtra("event_img_dir", imageURI.getFileDir().concat(imageURI.getFileName()));
                intent_goReview.putExtra("event_name", binding.etEventName.getText().toString());
                intent_goReview.putExtra("event_price", binding.etFixedPrice.getText().toString());
                intent_goReview.putExtra("event_dis_price", binding.etDiscount.getText().toString());
                intent_goReview.putExtra("event_people", binding.etLimitPersons.getText().toString());
                intent_goReview.putExtra("event_date", date);
                intent_goReview.putExtra("event_content", binding.etContents.getText().toString());
                startActivity(intent_goReview);
            }
        });

        invisiblePayment();

        GetStoreData getStoreData = new GetStoreData();
        getStoreData.execute(GlobalData.getUserID());

        if ((getIntent()) != null) {
            Log.d(TAG, "exist intent data");
            try {
                Intent intent = getIntent();
                mEvent_number = intent.getExtras().getString("event_number");

                GetTempEvent getTempEvent = new GetTempEvent();
                getTempEvent.execute(mEvent_number);

                flagUpdate = true;
            } catch (NullPointerException e) {

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!flagRegisterReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(GlobalData.ADD_EVENT_RECEIVER);
            registerReceiver(broadcastReceiver, filter);
            flagRegisterReceiver = true;
        }

        if (!flagRegisterURIReceiver) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(GlobalData.GET_URI_RECEIVER);
            registerReceiver(broadcastURIReceiver, filter);
            flagRegisterURIReceiver = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (flagRegisterURIReceiver) {
            unregisterReceiver(broadcastURIReceiver);
            flagRegisterURIReceiver = false;
        }

        if (flagRegisterReceiver) {
            unregisterReceiver(broadcastReceiver);
            flagRegisterReceiver = false;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(Activity_Owner_Add_Event.this)
                .setTitle("취소하시겠습니까?").setCancelable(false)
                .setMessage("작성한 내용이 저장되지 않습니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Activity_Owner_Add_Event.this.finish();
                    }
                }).setNegativeButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "ActivityResult");

        Log.d(TAG, "ActivityResult - resultCode = " + requestCode);
        if (resultCode != RESULT_OK) {
            Toast.makeText(Activity_Owner_Add_Event.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
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
            MediaScannerConnection.scanFile(Activity_Owner_Add_Event.this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
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
                Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI.getPhotoUri());
                bitmap2.compress(Bitmap.CompressFormat.PNG, 40, bs);
                bytes = bs.toByteArray();

                setImageList(arrayPosition, imageURI.getFileName(), imageURI.getFileDir().concat(imageURI.getFileName()), bitmap);
            } catch (Exception e) {
                Log.d(TAG, e.getMessage().toString());
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
        binding.etMinimumAge.setEnabled(true);
        binding.etMinimumAge.setTextColor(darkGray);
        binding.etMaximumAge.setEnabled(true);
        binding.etMaximumAge.setTextColor(darkGray);
        binding.rbMale.setEnabled(true);
        binding.rbMale.setTextColor(darkGray);
        binding.rbFemale.setEnabled(true);
        binding.rbFemale.setTextColor(darkGray);
        binding.rbAllSex.setEnabled(true);
        binding.rbAllSex.setTextColor(darkGray);
        binding.spinLocation.setEnabled(true);
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
        binding.etMinimumAge.setEnabled(false);
        binding.etMinimumAge.setTextColor(Color.LTGRAY);
        binding.etMaximumAge.setEnabled(false);
        binding.etMaximumAge.setTextColor(Color.LTGRAY);
        binding.rbMale.setEnabled(false);
        binding.rbMale.setTextColor(Color.LTGRAY);
        binding.rbFemale.setEnabled(false);
        binding.rbFemale.setTextColor(Color.LTGRAY);
        binding.rbAllSex.setEnabled(false);
        binding.rbAllSex.setTextColor(Color.LTGRAY);
        binding.spinLocation.setEnabled(false);
    }

    private void visiblePayment() {
        TextView tvPayment = (TextView) findViewById(R.id.tvPayment);
        tvPayment.setVisibility(View.VISIBLE);
        binding.rgPayment.setVisibility(View.VISIBLE);
        binding.rbCashPayment.setEnabled(true);
        binding.rbCardPayment.setEnabled(true);
    }

    private void invisiblePayment() {
        TextView tvPayment = (TextView) findViewById(R.id.tvPayment);
        tvPayment.setVisibility(View.GONE);
        binding.rgPayment.setVisibility(View.GONE);
        binding.rbCashPayment.setEnabled(false);
        binding.rbCardPayment.setEnabled(false);
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
            Toast.makeText(Activity_Owner_Add_Event.this, msg, Toast.LENGTH_SHORT).show();
        } else {
            String _name = binding.etEventName.getText().toString();
            String _type = String.valueOf(type);
            String _stats = String.valueOf(status);
            String _content = binding.etContents.getText().toString();
            String _price = binding.etFixedPrice.getText().toString().trim().replaceAll(",", "");
            String _dis_price = binding.etDiscount.getText().toString().trim().replaceAll(",", "");
            String _people = binding.etLimitPersons.getText().toString().trim().replaceAll(",", "");
            String _startday = binding.etStartDateYear.getText().toString().trim() +
                    binding.etStartDateMonth.getText().toString().trim() +
                    binding.etStartDateDay.getText().toString().trim();
            String _endday = binding.etEndDateYear.getText().toString().trim() +
                    binding.etEndDateMonth.getText().toString().trim() +
                    binding.etEndDateDay.getText().toString().trim();
            String _starttime = binding.etStartHour.getText().toString().trim() +
                    binding.etStartMin.getText().toString().trim() + "00";
            String _endtime = binding.etEndHour.getText().toString().trim() +
                    binding.etEndMin.getText().toString().trim() + "00";
            String _payment = String.valueOf(payment);
            String _target = String.valueOf(conditions);
            String _minage;
            String _maxage;
            String _sex;
            String _area;
            String _com_number = strComNo;
            String _id = GlobalData.getUserID();

            if (conditions == 1) {
                _minage = binding.etMinimumAge.getText().toString().trim();
                _maxage = binding.etMaximumAge.getText().toString().trim();
                _sex = String.valueOf(sex);
                _area = binding.spinLocation.getSelectedItem().toString();
            } else {
                _minage = "";
                _maxage = "";
                _sex = "";
                _area = "";
            }

            InsertData inputTask = new InsertData();
            if (flagUpdate) {
                Log.d(TAG, "flagUpdate: " + flagUpdate);
                inputTask.execute(_name, _type, _stats, _content, _price, _dis_price, _people, _startday,
                        _endday, _starttime, _endtime, _payment, _target, _minage, _maxage, _sex, _area,
                        _com_number, _id, mEvent_number);
            } else {
                inputTask.execute(_name, _type, _stats, _content, _price, _dis_price, _people, _startday,
                        _endday, _starttime, _endtime, _payment, _target, _minage, _maxage, _sex, _area,
                        _com_number, _id, "");
            }
        }
    }

    private String checkInputData() {
        if (isEmptyEditText(binding.etFixedPrice)) {
            return "정상가를 입력하세요";
        } else if (isEmptyEditText(binding.etDiscount)) {
            return "할인가를 입력하세요";
        } else if (isEmptyEditText(binding.etLimitPersons)) {
            return "인원제한을 입력하세요";
        } else if (isEmptyEditText(binding.etStartDateYear) || isEmptyEditText(binding.etStartDateMonth)
                || isEmptyEditText(binding.etStartDateDay) || isEmptyEditText(binding.etStartHour)
                || isEmptyEditText(binding.etStartMin)) {
            return "이벤트 시작일시를 확인하세요";
        } else if (!checkStartDate(binding.etStartDateYear, binding.etStartDateMonth,
                binding.etStartDateDay, binding.etStartHour, binding.etStartMin)) {
            return "현재 날짜보다 이전입니다";
        } else if (isEmptyEditText(binding.etEndDateYear) || isEmptyEditText(binding.etEndDateMonth)
                || isEmptyEditText(binding.etEndDateDay) || isEmptyEditText(binding.etEndHour)
                || isEmptyEditText(binding.etEndMin)) {
            return "이벤트 종료일시를 확인하세요";
        } else if (!checkEndDate(binding.etStartDateYear, binding.etStartDateMonth, binding.etStartDateDay,
                binding.etStartHour, binding.etStartMin, binding.etEndDateYear, binding.etEndDateMonth,
                binding.etEndDateDay, binding.etEndHour, binding.etEndMin)) {
            return "종료 날짜가 시작 날짜보다 이전입니다";
        } else if (isEmptyEditText(binding.etEventName)) {
            return "이벤트 명을 입력하세요";
        } else if (binding.swConditions.isChecked()) {
            if (isEmptyEditText(binding.etMinimumAge) && isEmptyEditText(binding.etMaximumAge)) {
                return "참가 나이를 입력하세요";
            } else {
                int maxAge = Integer.parseInt(binding.etMaximumAge.getText().toString().trim());
                int minAge = Integer.parseInt(binding.etMinimumAge.getText().toString().trim());

                if (maxAge < minAge) {
                    return "참가 나이를 확인하세요";
                }
            }
        }

        return null;
    }

    private String checkInputTempData() {
        if (isEmptyEditText(binding.etFixedPrice)) {
            return "정상가를 입력하세요";
        } else if (isEmptyEditText(binding.etDiscount)) {
            return "할인가를 입력하세요";
        } else if (isEmptyEditText(binding.etEventName)) {
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

        imm.hideSoftInputFromWindow(binding.etFixedPrice.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etDiscount.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etLimitPersons.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etStartDateYear.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etStartDateMonth.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etStartDateDay.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etStartHour.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etStartMin.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etEndDateYear.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etEndDateMonth.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etEndDateDay.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etEndHour.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etEndMin.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etEventName.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etMinimumAge.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(binding.etMaximumAge.getWindowToken(), 0);
    }

    private class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Activity_Owner_Add_Event.this, "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            String event_name = params[0];
            String event_type = params[1];
            String event_stats = params[2];
            String event_content = params[3];
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
                serverConnector.addPostData("event_content", event_content);
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

                if (arrayListContents.get(0).getFileName() != null) {
                    for (int i = 0; i < arrayListContents.size(); i++) {
                        if (arrayListContents.get(i).getFileName() != null) {
                            serverConnector.addPostData("event_URI" + i, arrayListContents.get(i).getFileName());
                        }
                    }
                }

                serverConnector.addDelimiter();
                serverConnector.writePostData();

                if (arrayListContents.get(0).getURI() != null) {
                    for (int i = 0; i < arrayListContents.size(); i++) {
                        if (arrayListContents.get(i).getURI() != null) {
                            Log.d(TAG, "uploadedPath: " + arrayListContents.get(i).getURI());

                            serverConnector.addFileData("uploaded_file" + i, arrayListContents.get(i).getURI());

                            serverConnector.writeFileData(arrayListContents.get(i).getURI());
                        }
                    }
                    serverConnector.finish();
                } else {
                    serverConnector.finish();
                }

                return serverConnector.response();

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
                        Toast.makeText(Activity_Owner_Add_Event.this, "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(Activity_Owner_Add_Event.this, "임시저장 되었습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
                Activity_Owner_Add_Event.this.finish();
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

    private class GetStoreData extends AsyncTask<String, Void, String> {
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
                    Toast.makeText(Activity_Owner_Add_Event.this,
                            "등록된 매장이 없습니다. 매장을 등록해주세요.", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            Activity_Owner_Add_Event.this.onBackPressed();
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

        // TODO
        public void setAdapter(final ArrayList<CharSequence> arrList) {
            ArrayAdapter<CharSequence> arrAdtSpinner = new ArrayAdapter<>(Activity_Owner_Add_Event.this, R.layout.support_simple_spinner_dropdown_item, arrList);
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

        String event_number = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String phpPage = "2ventGetTempEvent.php";
            event_number = params[0];

            try {
                ServerConnector serverConnector = new ServerConnector(phpPage);

                serverConnector.addPostData("event_number", event_number);
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

            Log.d(TAG, "response - " + result);

            if (result == null) {

            } else {
                mResultTempData = result;
                new GetImageURI(mContext).execute(event_number, "1", "1", "");
            }
        }
    }

    private void setTempData(String result1, String result2) {
        try {
            JSONObject jsonObject1 = new JSONObject(result1);
            JSONArray jsonArray1 = jsonObject1.getJSONArray("TempEvent");
            JSONObject jsonObject2 = new JSONObject(result2);
            JSONArray jsonArray2 = jsonObject2.getJSONArray("EventImage");

            JSONObject item1, item2;
            String event_name = null, event_type = null, event_stats = null, event_content = null, event_price = null, event_dis_price = null,
                    event_people = null, event_startday = null, event_endday = null, event_starttime = null, event_endtime = null,
                    event_payment = null, event_target = null, event_minage = null, event_maxage = null, event_sex = null,
                    event_area = null, com_name = null, event_URI = null;

            String tmp_year, tmp_month, tmp_day, tmp_hour, tmp_min;
            DecimalFormat format = new DecimalFormat("###,###");

            for (int i = 0; i < jsonArray1.length(); i++) {
                item1 = jsonArray1.getJSONObject(i);

                event_name = item1.getString("event_name");
                event_type = item1.getString("event_type");
                event_stats = item1.getString("event_stats");
                event_content = item1.getString("event_content");
                event_price = item1.getString("event_price");
                event_dis_price = item1.getString("event_dis_price");
                event_people = item1.getString("event_people");
                event_startday = item1.getString("event_startday");
                event_endday = item1.getString("event_endday");
                event_starttime = item1.getString("event_starttime");
                event_endtime = item1.getString("event_endtime");
                event_payment = item1.getString("event_payment");
                event_target = item1.getString("event_target");
                event_minage = item1.getString("event_minage");
                event_maxage = item1.getString("event_maxage");
                event_sex = item1.getString("event_sex");
                event_area = item1.getString("event_area");
                com_name = item1.getString("com_name");
            }

            if (event_type.equals("0")) {
                binding.rgType.check(R.id.rbTypeEnter);
                invisiblePayment();
                type = 0;
            } else if (event_type.equals("1")) {
                binding.rgType.check(R.id.rbTypePay);
                visiblePayment();
                type = 1;
            }

            for (int j = 0; j < arrListStore.size(); j++) {
                if (arrListStore.get(j) == com_name) {
                    spinStore.setSelection(j);
                }
            }

            binding.etFixedPrice.setText(format.format(Long.parseLong(event_price)));
            binding.etDiscount.setText(format.format(Long.parseLong(event_dis_price)));
            binding.etLimitPersons.setText(format.format(Long.parseLong(event_people)));

            StringTokenizer tokenDay;
            StringTokenizer tokenTime;

            if (!event_startday.equals("0000-00-00")) {
                tokenDay = new StringTokenizer(event_startday, "-");

                try {
                    tmp_year = tokenDay.nextToken();
                    tmp_month = tokenDay.nextToken();
                    tmp_day = tokenDay.nextToken();

                    binding.etStartDateYear.setText(tmp_year);
                    binding.etStartDateMonth.setText(tmp_month);
                    binding.etStartDateDay.setText(tmp_day);
                } catch (NoSuchElementException e) {

                }
            }

            if (!event_starttime.equals("00:00:00")) {
                tokenTime = new StringTokenizer(event_starttime, ":");

                try {
                    tmp_hour = tokenTime.nextToken();
                    tmp_min = tokenTime.nextToken();

                    binding.etStartHour.setText(tmp_hour);
                    binding.etStartMin.setText(tmp_min);
                } catch (NoSuchElementException e) {

                }
            }

            if (!event_endday.equals("0000-00-00")) {
                tokenDay = new StringTokenizer(event_endday, "-");

                try {
                    tmp_year = tokenDay.nextToken();
                    tmp_month = tokenDay.nextToken();
                    tmp_day = tokenDay.nextToken();

                    binding.etEndDateYear.setText(tmp_year);
                    binding.etEndDateMonth.setText(tmp_month);
                    binding.etEndDateDay.setText(tmp_day);
                } catch (NoSuchElementException e) {

                }
            }

            if (!event_endtime.equals("00:00:00")) {
                tokenTime = new StringTokenizer(event_endtime, ":");

                try {
                    tmp_hour = tokenTime.nextToken();
                    tmp_min = tokenTime.nextToken();

                    binding.etEndHour.setText(tmp_hour);
                    binding.etEndMin.setText(tmp_min);
                } catch (NoSuchElementException e) {

                }
            }

            binding.etEventName.setText(event_name);

            binding.etContents.setText(event_content);

            if (event_target.equals("0")) {
                binding.swConditions.setChecked(false);
                inActiveContents();
            } else if (event_target.equals("1")) {
                binding.swConditions.setChecked(true);
                activeContents();
                if (!event_minage.equals("0")) binding.etMinimumAge.setText(event_minage);
                if (!event_maxage.equals("0")) binding.etMaximumAge.setText(event_maxage);
                if (event_sex.equals("0")) {
                    binding.rgSex.check(R.id.rbFemale);
                } else if (event_sex.equals("1")) {
                    binding.rgSex.check(R.id.rbMale);
                } else if (event_sex.equals("2")) {
                    binding.rgSex.check(R.id.rbAllSex);
                }
                if (!event_area.equals("")) {
                    for (int i = 0; i < binding.spinLocation.getCount(); i++) {
                        if (binding.spinLocation.getItemAtPosition(i).equals(event_area)) {
                            binding.spinLocation.setSelection(i);
                        }
                    }
                }
            }

            InputStream in;
            Bitmap bitmap;
            OutputStream out;
            File file;

            for (int i = 0; i < jsonArray2.length(); i++) {
                item2 = jsonArray2.getJSONObject(i);

                event_URI = item2.getString("event_URI");

                StringTokenizer token = new StringTokenizer(event_URI, "/");

                String fileName = null;

                try {
                    token.nextToken();
                    token.nextToken();
                    fileName = token.nextToken();
                } catch (NoSuchElementException e) {

                }

                try {
                    in = new URL(GlobalData.getURL() + event_URI).openStream();
                    bitmap = BitmapFactory.decodeStream(in);
                    in.close();

                    file = new File(Environment.getExternalStorageDirectory().toString(), "/2vent/Images/" + fileName);
                    out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                    out.flush();
                    out.close();

                    setImageList(i, fileName, Environment.getExternalStorageDirectory().toString() + "/2vent/Images/" + fileName, bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String value = intent.getExtras().getString("add_image");
            final int position = intent.getExtras().getInt("position");

            Log.d(TAG, "receiver position : " + position);

            arrayPosition = position;

            if (value.equals("add")) {
                showImageDialog();
            } else if (value.equals("modify")) {
                Toast.makeText(Activity_Owner_Add_Event.this, "이미지 수정", Toast.LENGTH_SHORT).show();
            } else if (value.equals("remove")) {
                removeImageList(arrayPosition);
            }
        }
    };

    private void showImageDialog() { // TODO
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Owner_Add_Event.this);
        builder.setTitle("이미지 삽입").setCancelable(true)
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

    private void addImageList(String fileName, String uri, Bitmap bitmap) {
        if (arrayListContents.size() < MAX_IMAGE_LIST) {
            arrayListContents.add(new Owner_Add_Event_Item(fileName, uri, bitmap));

        } else {
            //Toast.makeText(Activity_Owner_Add_Event.this, "이미지는 3개까지만 등록 가능합니다.", Toast.LENGTH_SHORT).show();
        }
        rvAdtContents = new Owner_Add_Event_Adapter(arrayListContents, Activity_Owner_Add_Event.this);
        binding.rvContents.setAdapter(rvAdtContents);
        rvAdtContents.notifyDataSetChanged();
    }

    private void setImageList(int index, String fileName, String uri, Bitmap bitmap) {
        arrayListContents.set(index, new Owner_Add_Event_Item(fileName, uri, bitmap));

        if (arrayListContents.get(arrayListContents.size() - 1).getBitmap() != null) {

            addImageList(null, null, null);

        } else {
            rvAdtContents = new Owner_Add_Event_Adapter(arrayListContents, Activity_Owner_Add_Event.this);
            binding.rvContents.setAdapter(rvAdtContents);
            rvAdtContents.notifyDataSetChanged();
        }

        Log.d(TAG, "URI:" + arrayListContents.get(index).getURI());
    }

    private void removeImageList(int position) {
        if (arrayListContents.size() > 1) {

            if ((arrayListContents.size() == MAX_IMAGE_LIST)
                    && (arrayListContents.get(arrayListContents.size() - 1).getBitmap() != null)) {

                arrayListContents.remove(position);
                addImageList(null, null, null);

            } else if (arrayListContents.get(position).getBitmap() != null) {
                arrayListContents.remove(position);

                rvAdtContents = new Owner_Add_Event_Adapter(arrayListContents, Activity_Owner_Add_Event.this);
                binding.rvContents.setAdapter(rvAdtContents);
                rvAdtContents.notifyDataSetChanged();
            }
        }
    }

    private BroadcastReceiver broadcastURIReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String value = intent.getExtras().getString("finish");

            if (value == null) {

            } else {
                setTempData(mResultTempData, value);
            }
        }
    };
}
