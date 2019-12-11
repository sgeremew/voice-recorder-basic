package com.example.finalproject_draft1.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.finalproject_draft1.R;
import com.example.finalproject_draft1.RecordingItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PlaybackFragment extends DialogFragment {


  private RecordingItem recordingItem;
  private Handler handler = new Handler();
  private MediaPlayer mediaPlayer;

  private boolean isPlaying = false;

  long minutes = 0;
  long seconds = 0;


  TextView tvFileName;
  TextView tvFileLength;
  TextView tvFileCurrentProgress;
  SeekBar seekBar;
  FloatingActionButton floatingActionButton;


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);






  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

    Dialog dialog = super.onCreateDialog(savedInstanceState);
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_playback, null);
    tvFileName = view.findViewById(R.id.file_name_text);
    tvFileLength = view.findViewById(R.id.file_length_text);
    tvFileCurrentProgress = view.findViewById(R.id.current_progress);
    seekBar = view.findViewById(R.id.seekbar);
    floatingActionButton = view.findViewById(R.id.fab_play);

    builder.setView(view);

    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    return builder.create();

  }



}
