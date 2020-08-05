package com.remixtrainer;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;

public class PreLoginActivity extends AppCompatActivity {

  private Intent nextIntent;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pre_login);
  }

  public void onLogin(View v)
  {
    nextIntent = new Intent(PreLoginActivity.this, LoginActivity.class);
    startActivity(nextIntent);
    finish();
  }

  public void onRegister(View v)
  {
    nextIntent = new Intent(PreLoginActivity.this, RegisterActivity.class);
    startActivity(nextIntent);
    finish();
  }
}
