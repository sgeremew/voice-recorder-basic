package com.example.finalproject_draft1;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class RecordingAudio extends Service {


  MediaRecorder mediaRecorder;
  long startingTimeMillis = 0;
  long elapsedMillis = 0;

  File file;

  String fileName;

  @Override
  public void onCreate() {
    super.onCreate();
  }


  public RecordingAudio() {
  }

  @Override
  public IBinder onBind(Intent intent) {
    // TODO: Return the communication channel to the service.
    throw new UnsupportedOperationException("Not yet implemented");
  }


  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    startRecording();
    return START_STICKY;
  }

  private void startRecording(){


    Long timeStampLong = System.currentTimeMillis()/1000;
    String ts = timeStampLong.toString();

    fileName = "audio_"+ts;

    file = new File(Environment.getExternalStorageDirectory() + "/MySoundRecording/" + fileName + ".mp3");


    mediaRecorder = new MediaRecorder();
    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
    mediaRecorder.setOutputFile(file.getAbsolutePath());
    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
    mediaRecorder.setAudioChannels(1);


    try{

      mediaRecorder.prepare();
      mediaRecorder.start();

    } catch (IOException e){

      e.printStackTrace();
    }

  }


  private void stopRecording(){


    mediaRecorder.stop();
    elapsedMillis = (System.currentTimeMillis() - startingTimeMillis);
    mediaRecorder.release();
    Toast.makeText(getApplicationContext(), "Recording saved " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();


    // TODO: ADD TO DATABASE


  }

  @Override
  public void onDestroy() {

    if(mediaRecorder != null){
      stopRecording();
    }

    super.onDestroy();
  }


}
