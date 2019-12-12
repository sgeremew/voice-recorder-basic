package com.example.finalproject_draft1.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject_draft1.Database.DBHelper;
import com.example.finalproject_draft1.Fragments.PlaybackFragment;
import com.example.finalproject_draft1.Interfaces.OnDBChangeListener;
import com.example.finalproject_draft1.R;
import com.example.finalproject_draft1.RecordingItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FileViewerAdapter extends RecyclerView.Adapter<FileViewerAdapter.FileViewerHolder> implements OnDBChangeListener {

  Context context;
  List<RecordingItem> recordingItems;
  LinearLayoutManager layoutManager;

  DBHelper dbHelper;


  public FileViewerAdapter(Context context, ArrayList<RecordingItem> recordingItems, LinearLayoutManager layoutManager) {
    this.context = context;
    this.recordingItems = recordingItems;
    this.layoutManager = layoutManager;
    dbHelper = new DBHelper(context);

    dbHelper.setOnDBChangeListener(this);
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

  @Override
  public void onNewDBEntryAdded(RecordingItem recordingItem) {

    recordingItems.add(recordingItem);
    notifyItemInserted(recordingItems.size() - 1);

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


      cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          PlaybackFragment playbackFragment = new PlaybackFragment();
          Bundle b = new Bundle();

          b.putSerializable("item", recordingItems.get(getAdapterPosition()));
          playbackFragment.setArguments(b);

          FragmentTransaction fragmentTransaction = ( (FragmentActivity) context ).getSupportFragmentManager().beginTransaction();

          playbackFragment.show(fragmentTransaction, "dialog_playback");

        }
      });

    }
  }

}
