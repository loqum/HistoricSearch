package com.rfm.proyecto.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rfm.proyecto.R;
import com.rfm.proyecto.controller.FirebaseDatabaseHelper;
import com.rfm.proyecto.pojo.User;
import com.rfm.proyecto.utils.Alerts;
import com.rfm.proyecto.utils.InputValidation;
import com.rfm.proyecto.utils.LangUtils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

  private FirebaseAuth mAuth;

  private static final String TAG = "EmailPassword";

  private final AppCompatActivity activity = RegisterActivity.this;

  private NestedScrollView nestedScrollView;

  private TextInputLayout textInputLayoutUsername;
  private TextInputLayout textInputLayoutEmail;
  private TextInputLayout textInputLayoutPassword;
  private TextInputLayout textInputLayoutConfirmPassword;

  private TextInputEditText textInputEditTextUsername;
  private TextInputEditText textInputEditTextEmail;
  private TextInputEditText textInputEditTextPassword;
  private TextInputEditText textInputEditTextConfirmPassword;
  private TextInputEditText textInputEditTextTelephone;

  private ProgressBar progressBarRegister;

  private AppCompatButton appCompatButtonRegister;
  private AppCompatTextView appCompatTextViewLoginLink;

  private InputValidation inputValidation;
  private User user;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);
    LangUtils.getLocale(this);
    mAuth = FirebaseAuth.getInstance();

    initViews();
    initListeners();
    initObjects();
  }

  private void initViews() {
    nestedScrollView = findViewById(R.id.nestedScrollView);

    textInputLayoutUsername = findViewById(R.id.textInputLayoutUsername);
    textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
    textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
    textInputLayoutConfirmPassword = findViewById(R.id.textInputLayoutConfirmPassword);

    textInputEditTextUsername = findViewById(R.id.textInputEditTextUsername);
    textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
    textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
    textInputEditTextConfirmPassword = findViewById(R.id.textInputEditTextConfirmPassword);
    textInputEditTextTelephone = findViewById(R.id.textInputEditTextTelephone);

    progressBarRegister = findViewById(R.id.progressBarRegister);
    progressBarRegister.setVisibility(View.GONE);

    appCompatButtonRegister = findViewById(R.id.appCompatButtonRegister);

    appCompatTextViewLoginLink = findViewById(R.id.appCompatTextViewLoginLink);

  }

  private void initListeners() {
    appCompatButtonRegister.setOnClickListener(this);
    appCompatTextViewLoginLink.setOnClickListener(this);

  }

  private void initObjects() {
    inputValidation = new InputValidation(activity);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {

      case R.id.appCompatButtonRegister:
        createAccount();
        break;

      case R.id.appCompatTextViewLoginLink:
        finish();
        break;
    }
  }

  private void createAccount() {
    if (!inputValidation.isInputEditTextFilled(textInputEditTextUsername, textInputLayoutUsername, getString(R.string.error_message_name))) {
      return;
    }
    if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
      return;
    }
    if (!inputValidation.isInputEditTextMail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
      return;
    }
    if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
      return;
    }
    if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
            textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
      return;
    }

    if (!inputValidation.isInputEditTextPasswordCorrect(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_lenght_password))) {
      return;
    }


    if (textInputEditTextEmail.getText() != null
            && textInputEditTextPassword.getText() != null
            && textInputEditTextTelephone.getText() != null
            && textInputEditTextUsername.getText() != null) {


      progressBarRegister.setVisibility(View.VISIBLE);
      mAuth.createUserWithEmailAndPassword(textInputEditTextEmail.getText().toString(), textInputEditTextPassword.getText().toString())
              .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                  if (task.isSuccessful()) {

                    Log.d(TAG, "createUserWithEmail:success");

                    user = new User(
                            textInputEditTextUsername.getText().toString(),
                            textInputEditTextTelephone.getText().toString(),
                            textInputEditTextEmail.getText().toString()
                    );

                    progressBarRegister.setVisibility(View.GONE);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                        Alerts.displayMessage(nestedScrollView, getString(R.string.success_message));
                        emptyInputEditText();
                      }
                    });

                    toLoginActivity();

                  } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                      Alerts.displayMessage(nestedScrollView, getString(R.string.error_email_exists));

                    }

                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegisterActivity.this, getString(R.string.error_registration),
                            Toast.LENGTH_SHORT).show();
                  }

                }
              });
    }

  }

  private void emptyInputEditText() {
    textInputEditTextUsername.setText(null);
    textInputEditTextEmail.setText(null);
    textInputEditTextPassword.setText(null);
    textInputEditTextConfirmPassword.setText(null);
    textInputEditTextTelephone.setText(null);
  }

  private void toLoginActivity() {
    Handler handler = new Handler();
    handler.postDelayed(toLoginTask, 2500);
  }

  private Runnable toLoginTask = new Runnable() {
    public void run() {
      Intent intent = new Intent(getApplication(), LoginActivity.class);
      startActivity(intent);
    }
  };

}
