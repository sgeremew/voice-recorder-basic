package com.example.finalproject_draft1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Environment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment implements View.OnClickListener {




  public RecordFragment() {
    // Required empty public constructor
  }




  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment RecordFragment.
   */
  public static RecordFragment newInstance() {
    RecordFragment fragment = new RecordFragment();

    return fragment;
  }


  private Chronometer chronometer;
  private TextView tvRecordingStatus;
  private FloatingActionButton btnFloatingAction;
  private Button btnPause;

  private boolean startRecording = true;
  private boolean pauseRecording = true;
  private long timeAtPause = 0;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View recordFragmentView = inflater.inflate(R.layout.fragment_record, container, false);

    btnFloatingAction = recordFragmentView.findViewById(R.id.fab);
    btnPause = recordFragmentView.findViewById(R.id.pause);
    chronometer = recordFragmentView.findViewById(R.id.chronometer);
    tvRecordingStatus = recordFragmentView.findViewById(R.id.recordingStatusText);


    btnFloatingAction.setOnClickListener(this);

    return recordFragmentView;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    btnPause.setVisibility(View.GONE);

  }


  @Override
  public void onClick(View view) {

    switch (view.getId()){
      case R.id.fab:
        recordAudio();
        break;
    }

  }

  // On the click of the floatingActionButton in the RecordFragment
  public void recordAudio(){

    onRecord(startRecording);
    startRecording = !startRecording;

  }

  private void onRecord(boolean start){

    Intent intent = new Intent(getActivity(), RecordingAudio.class);

    if(start){

      btnFloatingAction.setImageResource(R.drawable.ic_media_stop);
      Toast.makeText(getContext(), "Recording started", Toast.LENGTH_LONG).show();

      File folder = new File(Environment.getExternalStorageDirectory() + "/MySoundRecording");

      if(!folder.exists()){
        folder.mkdir();
      }


      chronometer.setBase((SystemClock.elapsedRealtime()));
      chronometer.start();


      getActivity().startService(intent);
      getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


      tvRecordingStatus.setText("Recording...");

    }

    else{
      btnFloatingAction.setImageResource(R.drawable.ic_mic_white);
      chronometer.stop();
      chronometer.setBase(SystemClock.elapsedRealtime());
      timeAtPause = 0;
      tvRecordingStatus.setText("Tap the button to start recording");


      getActivity().stopService(intent);
    }

  }




}
