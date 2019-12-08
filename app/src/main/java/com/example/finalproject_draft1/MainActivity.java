package com.example.finalproject_draft1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  private MediaRecorder myRecorder;
  private MediaPlayer myPlayer;
  private String outputFile = null;
  private Button startBtn;
  private Button stopBtn;
  private Button playBtn;
  private Button stopPlayBtn;
  private TextView text;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    text = (TextView) findViewById(R.id.text1);
    // store it to sd card
    outputFile = Environment.getExternalStorageDirectory().
      getAbsolutePath() + "/myRecording.3gpp";

    // if these permissions are not enabled this activity is essentially unusable
    if (isPermissionsEnabled()) {
      setupAudio();
    }

    startBtn = (Button) findViewById(R.id.start);
    startBtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        start(v);
      }
    });

    stopBtn = (Button) findViewById(R.id.stop);
    stopBtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        stop(v);
      }
    });

    playBtn = (Button) findViewById(R.id.play);
    playBtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        play(v);
      }
    });

    stopPlayBtn = (Button) findViewById(R.id.stopPlay);
    stopPlayBtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        stopPlay(v);
      }
    });
  }

  // sets up the audio source and output
  public void setupAudio() {
    myRecorder = new MediaRecorder();
    myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
    myRecorder.setOutputFile(outputFile);
  }

  public void start(View view) {
    try {
      myRecorder.prepare();
      myRecorder.start();
    } catch (IllegalStateException e) {
      // start:it is called before prepare()
      // prepare: it is called after start() or before setOutputFormat()
      e.printStackTrace();
    } catch (IOException e) {
      // prepare() fails
      e.printStackTrace();
    }

    text.setText("Recording Point: Recording");
    startBtn.setEnabled(false);
    stopBtn.setEnabled(true);

    Toast.makeText(getApplicationContext(), "Start recording...",
      Toast.LENGTH_SHORT).show();
  }

  public void stop(View view) {
    try {
      myRecorder.stop();
      myRecorder.release();
      myRecorder = null;

      stopBtn.setEnabled(false);
      playBtn.setEnabled(true);
      text.setText("Recording Point: Stop recording");

      Toast.makeText(getApplicationContext(), "Stop recording...",
        Toast.LENGTH_SHORT).show();
    } catch (IllegalStateException e) {
      //  it is called before start()
      e.printStackTrace();
    } catch (RuntimeException e) {
      // no valid audio/video data has been received
      e.printStackTrace();
    }
  }

  public void play(View view) {
    try {
      myPlayer = new MediaPlayer();
      myPlayer.setDataSource(outputFile);
      myPlayer.prepare();
      myPlayer.start();

      playBtn.setEnabled(false);
      stopPlayBtn.setEnabled(true);
      text.setText("Recording Point: Playing");

      Toast.makeText(getApplicationContext(), "Start play the recording...",
        Toast.LENGTH_SHORT).show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void stopPlay(View view) {
    try {
      if (myPlayer != null) {
        myPlayer.stop();
        myPlayer.release();
        myPlayer = null;
        playBtn.setEnabled(true);
        stopPlayBtn.setEnabled(false);
        text.setText("Recording Point: Stop playing");

        Toast.makeText(getApplicationContext(), "Stop playing the recording...",
          Toast.LENGTH_SHORT).show();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  private final int MY_PERMISSIONS = 1; // constant used as request code in callback

  // Determines if this Activity can record using the hardware audio recorder
  // If not the user is given the opportunity to allow or deny
  // Upon denying the user will not be able to record audio
  private boolean isPermissionsEnabled() {
    // "this" is the current Activity
    if ((ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO))
        + (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
        != PackageManager.PERMISSION_GRANTED) {

      // Request permission for recording audio
      ActivityCompat.requestPermissions(this,
        new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
        MY_PERMISSIONS);

      // Permission is NOT granted
      // Should we show an explanation?
      if (ActivityCompat.shouldShowRequestPermissionRationale(this,
        Manifest.permission.RECORD_AUDIO)) {
        Toast.makeText(this, "Audio permissions allow the recording of your audio.", Toast.LENGTH_LONG).show();
      }

      // Permission is NOT granted
      // Should we show an explanation?
      if (ActivityCompat.shouldShowRequestPermissionRationale(this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        Toast.makeText(this, "Storage permissions allows you to save your recordings.", Toast.LENGTH_LONG).show();
      }

      return false;
    }
    //If permission is granted, then go ahead recording audio
    else {
      return true;
    }
  }


  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         String[] permissions, int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
          && grantResults[0] + grantResults[1]
          == PackageManager.PERMISSION_GRANTED) {
          // permission was granted
        } else {
          // permission denied
          // Disable the recording functionality that depends on this permission.
          startBtn.setEnabled(false);
        }
        return;
      }
    }
  }

}
