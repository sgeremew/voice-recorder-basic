package com.example.finalproject_draft1.Adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject_draft1.R;
import com.example.finalproject_draft1.RecordingItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FileViewerAdapter extends RecyclerView.Adapter<FileViewerAdapter.FileViewerHolder> {

  Context context;
  List<RecordingItem> recordingItems;
  LinearLayoutManager layoutManager;


  public FileViewerAdapter(Context context, ArrayList<RecordingItem> recordingItems, LinearLayoutManager layoutManager) {
    this.context = context;
    this.recordingItems = recordingItems;
    this.layoutManager = layoutManager;
  }


  @NonNull
  @Override
  public FileViewerAdapter.FileViewerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {


    View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recording_item_card_view, viewGroup, false);


    return new FileViewerHolder(itemView);

  }

  @Override
  public void onBindViewHolder(@NonNull FileViewerAdapter.FileViewerHolder holder, int position) {

    RecordingItem recordingItem = recordingItems.get(position);

    long minutes = TimeUnit.MILLISECONDS.toMinutes(recordingItem.getLength());
    long seconds = TimeUnit.MILLISECONDS.toSeconds(recordingItem.getLength()) - TimeUnit.MINUTES.toSeconds(minutes);


    holder.tvName.setText(recordingItem.getName());
    holder.tvLength.setText(String.format("%02d:%02d", minutes, seconds));

    holder.tvDateTimeAdded.setText(DateUtils.formatDateTime(context, recordingItem.getTime_added(),
      DateUtils.FORMAT_SHOW_DATE |
        DateUtils.FORMAT_NUMERIC_DATE |
        DateUtils.FORMAT_SHOW_TIME |
        DateUtils.FORMAT_SHOW_YEAR));

  }

  @Override
  public int getItemCount() {
    return recordingItems.size();
  }


  public class FileViewerHolder extends RecyclerView.ViewHolder {


    TextView tvName;
    TextView tvLength;
    TextView tvDateTimeAdded;
    CardView cardView;

    public FileViewerHolder(@NonNull View itemView) {
      super(itemView);


      tvName = itemView.findViewById(R.id.file_name);
      tvLength = itemView.findViewById(R.id.file_length);
      tvDateTimeAdded = itemView.findViewById(R.id.file_datetime_recorded);
      cardView = itemView.findViewById(R.id.cardView);






    }
  }

}
