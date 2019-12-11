package com.example.finalproject_draft1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.finalproject_draft1.Adapters.MyTabPagerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {



  AppBarLayout appBarLayout;
  ViewPager viewPager;
  TabLayout tabs;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    appBarLayout = findViewById(R.id.appBarLayout);

    MyTabPagerAdapter myTabPagerAdapter = new MyTabPagerAdapter(this, getSupportFragmentManager());

    viewPager = findViewById(R.id.view_pager);
    viewPager.setAdapter(myTabPagerAdapter);

    tabs = findViewById(R.id.tabs);
    tabs.setupWithViewPager(viewPager);


//
//    // if these permissions are not enabled this activity is essentially unusable
//    if (isPermissionsEnabled()) {
//      setupAudio();
//    }
//


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
//          startBtn.setEnabled(false);
        }
        return;
      }
    }
  }

}
