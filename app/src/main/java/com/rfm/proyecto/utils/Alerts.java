package com.rfm.proyecto.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.Circle;
import com.rfm.proyecto.R;
import com.rfm.proyecto.activities.MainActivity;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Alerts {

  public Alerts() {
    super();
  }

  public static void alertDialog(Context context, Drawable icon, String title, String message1,
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

  public static void displayMessage(View view, String message) {
    Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
    View snackbarView = snackbar.getView();
    int snackbarTextId = android.support.design.R.id.snackbar_text;
    TextView textView = snackbarView.findViewById(snackbarTextId);
    textView.setTextColor(Color.rgb(255, 255, 255));
    snackbar.show();
  }


}
