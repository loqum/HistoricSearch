package com.rfm.proyecto.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

public class Alerts {

  public Alerts() {
    super();
  }

  public static void alertDialogAbout(Context context, Drawable icon, String title, String message1,
                                      String message2, String buttonMessage) {

    final SpannableString GITHUB = new SpannableString(message2);
    Linkify.addLinks(GITHUB, Linkify.ALL);

    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
    alertDialog.setTitle(title);
    alertDialog.setIcon(icon);
    alertDialog.setMessage((message1).concat("\n") + GITHUB);
    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, buttonMessage,
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
              }
            });

    alertDialog.show();

  }




}
