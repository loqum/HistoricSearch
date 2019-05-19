package com.rfm.proyecto.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rfm.proyecto.R;
import com.rfm.proyecto.utils.Alerts;
import com.rfm.proyecto.utils.InputValidation;
import com.rfm.proyecto.utils.LangUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

  private static final String TAG = "LoginActivity";

  private final AppCompatActivity activity = LoginActivity.this;

  private FirebaseAuth mAuth;
  private NestedScrollView nestedScrollView;
  private TextInputLayout textInputLayoutEmail;
  private TextInputLayout textInputLayoutPassword;
  private TextInputEditText editTextMail;
  private TextInputEditText editTextPassword;
  private AppCompatButton buttonLogin;
  private AppCompatTextView textViewLinkRegister;
  private InputValidation inputValidation;
  private ProgressBar progressBarLogin;
  private ImageButton buttonLangEs;
  private ImageButton buttonLangCa;
  private ImageButton buttonLangEn;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    LangUtils.getLocale(this);
    FirebaseApp.initializeApp(this);

    mAuth = FirebaseAuth.getInstance();

    initViews();
    initListeners();
    initObjects();

    progressBarLogin.setVisibility(View.GONE);
  }

  private void initViews() {
    nestedScrollView = findViewById(R.id.nestedScrollView);
    textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
    textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
    editTextMail = findViewById(R.id.editTextEmail);
    editTextPassword = findViewById(R.id.editTextPassword);
    buttonLogin = findViewById(R.id.buttonLogin);
    textViewLinkRegister = findViewById(R.id.textViewLinkRegister);
    progressBarLogin = findViewById(R.id.progressBarLogin);
    buttonLangEs = findViewById(R.id.button_lang_es);
    buttonLangCa = findViewById(R.id.button_lang_ca);
    buttonLangEn = findViewById(R.id.button_lang_en);

  }

  private void initListeners() {
    buttonLogin.setOnClickListener(this);
    textViewLinkRegister.setOnClickListener(this);
    buttonLangEs.setOnClickListener(this);
    buttonLangCa.setOnClickListener(this);
    buttonLangEn.setOnClickListener(this);
  }

  private void initObjects() {
    inputValidation = new InputValidation(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.buttonLogin:
        login();
        break;

      case R.id.textViewLinkRegister:
        Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intentRegister);
        break;

      case R.id.button_lang_es:
        LangUtils.setLocale(this, "es");
        recreate();
        break;

      case R.id.button_lang_ca:
        LangUtils.setLocale(this, "ca");
        recreate();
        break;

      case R.id.button_lang_en:
        LangUtils.setLocale(this, "en");
        recreate();
        break;
    }
  }

  private void login() {
    if (!inputValidation.isInputEditTextFilled(editTextMail, textInputLayoutEmail, getString(R.string.error_message_email))) {
      return;
    }
    if (!inputValidation.isInputEditTextMail(editTextMail, textInputLayoutEmail, getString(R.string.error_message_email))) {
      return;
    }
    if (!inputValidation.isInputEditTextFilled(editTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
      return;
    }

    progressBarLogin.setVisibility(View.VISIBLE);
    mAuth.signInWithEmailAndPassword(editTextMail.getText().toString(), editTextPassword.getText().toString())
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                  progressBarLogin.setVisibility(View.GONE);

                  Log.d(TAG, "signInWithEmail:success");
                  FirebaseUser user = mAuth.getCurrentUser();
                  Alerts.displayMessage(nestedScrollView, getString(R.string.welcome_message));
                  updateUI(user);

                } else {

                  progressBarLogin.setVisibility(View.GONE);

                  Log.w(TAG, "signInWithEmail:failure", task.getException());
                }

                if (!task.isSuccessful()) {
                  Alerts.displayMessage(nestedScrollView, getString(R.string.error_valid_email_password));
                }
              }
            });

  }

  private void emptyInputEditText() {
    editTextMail.setText(null);
    editTextPassword.setText(null);
  }

  private void updateUI(FirebaseUser user) {
    Intent intent = new Intent(getApplication(), MainActivity.class);
    intent.putExtra("EMAIL", editTextMail.getText().toString().trim());
    emptyInputEditText();
    startActivity(intent);
    finish();
  }

}
