package com.rfm.proyecto.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class LangUtils {

  public LangUtils() {
    super();
  }

  public static void setLocale(@NotNull Context context, String lang) {
    Locale locale = new Locale(lang);
    Locale.setDefault(locale);
    Configuration configuration = new Configuration();
    configuration.locale = locale;
    context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    SharedPreferences.Editor editor = context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
    editor.putString("my_lang", lang);
    editor.apply();
  }

  public static void getLocale(@NotNull Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
    String language = sharedPreferences.getString("my_lang", "");
    setLocale(context, language);
  }

}
