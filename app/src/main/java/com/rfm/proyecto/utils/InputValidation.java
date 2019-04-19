package com.rfm.proyecto.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class InputValidation {

    private Context context;

    public InputValidation(Context context) {
        this.context = context;
    }

    public boolean isInputEditTextFilled(TextInputEditText editText, TextInputLayout inputLayout, String message) {
        String value = editText.getText().toString().trim();

        if (value.isEmpty()) {
            inputLayout.setError(message);
            hideKeyboardFrom(editText);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public boolean isInputEditTextMail(TextInputEditText editText, TextInputLayout inputLayout, String message) {
        String value = editText.getText().toString().trim();

        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            inputLayout.setError(message);
            hideKeyboardFrom(editText);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }

        return true;

    }

    public boolean isInputEditTextMatches(TextInputEditText editTextOne, TextInputEditText editTextTwo, TextInputLayout inputLayout, String message) {
        String value1 = editTextOne.getText().toString().trim();
        String value2 = editTextTwo.getText().toString().trim();
        if (!value1.contentEquals(value2)) {
            inputLayout.setError(message);
            hideKeyboardFrom(editTextTwo);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean isInputEditTextPasswordCorrect(TextInputEditText editText, TextInputLayout inputLayout, String message) {
        String value = editText.getText().toString().trim();

        if (value.length() < 6) {
            inputLayout.setError(message);
            hideKeyboardFrom(editText);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
