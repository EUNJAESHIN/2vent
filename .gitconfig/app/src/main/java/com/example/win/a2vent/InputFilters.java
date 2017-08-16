package com.example.win.a2vent;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017-08-16.
 */

public class InputFilters {

    // 영문만 허용 (숫자 포함)
    protected static InputFilter filter = new InputFilter() {

        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

    // 숫자만 허용
    protected static InputFilter filterNum = new InputFilter() {

        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[0-9]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

    // 한글만 허용
    protected static InputFilter filterKor = new InputFilter() {

        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[가-힣ㄱ-ㅎㅏ-ㅣ]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };
}
