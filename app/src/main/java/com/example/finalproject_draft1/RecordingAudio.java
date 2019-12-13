package com.example.finalproject_draft1;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import com.example.finalproject_draft1.Database.DBHelper;

import java.io.File;
import java.io.IOException;

public class RecordingAudio extends Service {


  private MediaRecorder mediaRecorder;
  private long startingTimeMillis = 0;
  private long elapsedMillis = 0;

  private File file;

  private String fileName;

  private DBHelper dbHelper;

  @Override
  public void onCreate() {
    super.onCreate();

    dbHelper = new DBHelper(getApplicationContext());

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
    String action = intent.getAction() != null ? intent.getAction() : "";

    try {
      if (action.equals(RecordingWidget.ACTION_RECORD) || action.equals(""))
      {
        startRecording();
      }

      else if (action.equals(RecordingWidget.ACTION_STOP) || action.equals("")) {
        stopRecording();
      }

    }
    catch (Exception e)
    {
      e.printStackTrace();
//      System.out.println(action);
    }

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

      startingTimeMillis = System.currentTimeMillis();

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

    RecordingItem recordingItem = new RecordingItem(fileName, file.getAbsolutePath(), elapsedMillis, System.currentTimeMillis());

    dbHelper.addRecording(recordingItem);


  }

  @Override
  public void onDestroy() {

    if(mediaRecorder != null){
      stopRecording();
    }

    super.onDestroy();
  }


}
