package com.rfm.proyecto.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rfm.proyecto.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

  private static final String TAG = "RecyclerViewAdapter";

  private List<String> imageNames = new ArrayList<>();
  private List<String> images = new ArrayList<>();
  private Context context;

  public RecyclerViewAdapter(Context context, List<String> imageNames, List<String> images) {
    this.imageNames = imageNames;
    this.images = images;
    this.context = context;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
    ViewHolder holder = new ViewHolder(view);
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
    Log.d(TAG, "onBindViewHolder: called.");

    Glide.with(context).asBitmap().load(images.get(position)).into(viewHolder.circleImageView);

    viewHolder.textViewImage.setText(imageNames.get(position));
    viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(TAG, "onClick: clicked on: " + imageNames.get(position));
        Toast.makeText(context, imageNames.get(position), Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  public int getItemCount() {
    return imageNames.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    CircleImageView circleImageView;
    TextView textViewImage;
    RelativeLayout parentLayout;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      circleImageView = itemView.findViewById(R.id.circleImageView);
      textViewImage = itemView.findViewById(R.id.textViewImage);
      parentLayout = itemView.findViewById(R.id.parentLayout);

    }
  }
}
