package com.example.finalproject_draft1.Fragments;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PlaybackFragment extends DialogFragment {


  private RecordingItem recordingItem;
  private Handler handler = new Handler();
  private MediaPlayer mediaPlayer;

  private boolean isPlaying = false;

  private long minutes = 0;
  private long seconds = 0;


  private TextView tvFileName;
  private TextView tvFileLength;
  private TextView tvFileCurrentProgress;
  private SeekBar seekBar;
  private FloatingActionButton floatingActionButton;


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    recordingItem = (RecordingItem) getArguments().getSerializable("item");

    minutes = TimeUnit.MILLISECONDS.toMinutes(recordingItem.getLength());
    seconds = TimeUnit.MILLISECONDS.toSeconds(recordingItem.getLength()) - TimeUnit.MINUTES.toSeconds(minutes);

  }

  // Initialize everything
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

    setSeekbarValues();

    floatingActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        try {
          onPlay(isPlaying);
        } catch (IOException e) {
          e.printStackTrace();
        }
        isPlaying = !isPlaying;


      }
    });


    tvFileName.setText(recordingItem.getName());
    tvFileLength.setText(String.format("%02d:%02d", minutes, seconds));


    builder.setView(view);

    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    return builder.create();

  }

  // onClick of the floatingActionButton
  private void onPlay(boolean isPlaying) throws IOException {

    // If mediaPlayer is not playing
    if (!isPlaying) {

      // If mediaPlayer does NOT exist
      if (mediaPlayer == null) {
        startPlaying();
      } else {
        resumePlaying();  // It was paused, now resume
      }

    } else {
      // Pause mediaPlayer
      pausePlaying();

    }

  }

  // Resume mediaPlayer playback
  private void resumePlaying() {

    floatingActionButton.setImageResource(R.drawable.ic_media_pause);
    mediaPlayer.start();

    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        stopPlaying();
      }
    });

    updateSeekbar();

    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

  }

  // Pause mediaPlayer playback
  private void pausePlaying() {

    floatingActionButton.setImageResource(R.drawable.ic_media_play);
    handler.removeCallbacks(runnable);
    mediaPlayer.pause();

  }

  // Start mediaPlayer playback
  private void startPlaying() throws IOException {

    floatingActionButton.setImageResource(R.drawable.ic_media_pause);
    mediaPlayer = new MediaPlayer();

    mediaPlayer.setDataSource(recordingItem.getPath());
    mediaPlayer.prepare();
    seekBar.setMax(mediaPlayer.getDuration());

    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
      }
    });

    // Upon completion stop playback
    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        stopPlaying();
      }
    });

    updateSeekbar();

    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

  }

  @TargetApi(16)
  private void setSeekbarValues() {

    ColorFilter colorFilter = new LightingColorFilter(getResources().getColor(R.color.colorPrimary),
      getResources().getColor(R.color.colorPrimary));

    seekBar.getProgressDrawable().setColorFilter(colorFilter);
    // TODO this only works in Api >=16
    seekBar.getThumb().setColorFilter(colorFilter);

    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (mediaPlayer != null && fromUser) {

          mediaPlayer.seekTo(progress);
          handler.removeCallbacks(runnable);

          long minutes = TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getCurrentPosition());
          long seconds = TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition()) - TimeUnit.MINUTES.toSeconds(minutes);

          tvFileCurrentProgress.setText(String.format("%02d:%02d", minutes, seconds));

          updateSeekbar();

        } else if (mediaPlayer == null && fromUser) {

          try {
            prepareMediaPlayerFromPoint(progress);
          } catch (IOException e) {
            e.printStackTrace();
          }
          updateSeekbar();

        }

      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });


  }


  // Used when user moves the thumb of the seekbar around (e.g. fast-forward and rewind)
  private void prepareMediaPlayerFromPoint(int progress) throws IOException {

    mediaPlayer = new MediaPlayer();
    mediaPlayer.setDataSource(recordingItem.getPath());
    mediaPlayer.prepare();

    seekBar.setMax(mediaPlayer.getDuration());

    mediaPlayer.seekTo(progress);
    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {

        stopPlaying();

      }
    });

  }

  // Ends and kills the mediaPlayer and completes the seekbar
  private void stopPlaying() {

    floatingActionButton.setImageResource(R.drawable.ic_media_play);

    handler.removeCallbacks(runnable);

    mediaPlayer.stop();
    mediaPlayer.reset();
    mediaPlayer.release();
    mediaPlayer = null;

    seekBar.setProgress(seekBar.getMax());
    isPlaying = !isPlaying;

    tvFileCurrentProgress.setText(tvFileLength.getText());
    seekBar.setProgress(seekBar.getMax());

  }


  // Used to simultaneously update the progress of the seekbar
  private Runnable runnable = new Runnable() {

    @Override
    public void run() {

      if (mediaPlayer != null) {

        int currentPosition = mediaPlayer.getCurrentPosition();
        seekBar.setProgress(currentPosition);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(currentPosition);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(currentPosition) - TimeUnit.MINUTES.toSeconds(minutes);

        tvFileCurrentProgress.setText(String.format("%02d:%02d", minutes, seconds));
        updateSeekbar();

      }

    }
  };

  private void updateSeekbar() {

    handler.postDelayed(runnable, 1000);

  }


}
