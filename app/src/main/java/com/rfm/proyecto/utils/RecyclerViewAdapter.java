package com.rfm.proyecto.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rfm.proyecto.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

  private static final String TAG = "RecyclerViewAdapter";

  private List<String> imageNames;
  private List<String> images;
  private List<String> descriptionLocation;
  private Context context;

  public RecyclerViewAdapter(Context context, List<String> imageNames, List<String> images, List<String> descriptionLocation) {
    this.imageNames = imageNames;
    this.images = images;
    this.descriptionLocation = descriptionLocation;
    this.context = context;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
    return new RecyclerViewAdapter.ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
    Log.d(TAG, "onBindViewHolder: called.");

    Glide.with(context).asBitmap().load(images.get(position)).into(viewHolder.circleImageView);

    viewHolder.textViewImage.setText(imageNames.get(position));
    viewHolder.textViewDescriptionLocation.setText(descriptionLocation.get(position));

  }

  @Override
  public int getItemCount() {
    return imageNames.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    CircleImageView circleImageView;
    TextView textViewImage;
    TextView textViewDescriptionLocation;
    CardView cardView;

    private ViewHolder(@NonNull View itemView) {
      super(itemView);
      circleImageView = itemView.findViewById(R.id.circleImageView);
      textViewImage = itemView.findViewById(R.id.textViewImage);
      textViewDescriptionLocation = itemView.findViewById(R.id.textViewDescriptionLocation);
      cardView = itemView.findViewById(R.id.cardView);
      itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
      menu.add(0, 121, getAdapterPosition(), context.getString(R.string.more_info));
      menu.add(0, 122, getAdapterPosition(), context.getString(R.string.view_on_map));
    }

  }

}
