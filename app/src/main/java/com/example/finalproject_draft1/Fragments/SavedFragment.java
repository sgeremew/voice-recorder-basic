package com.example.finalproject_draft1.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalproject_draft1.Database.DBHelper;
import com.example.finalproject_draft1.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link SavedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedFragment extends Fragment {



  public SavedFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment SavedFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static SavedFragment newInstance() {
    SavedFragment fragment = new SavedFragment();

    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


  }

  RecyclerView recyclerView;
  DBHelper dbHelper;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View savedFragmentView = inflater.inflate(R.layout.fragment_saved, container, false);

    recyclerView = savedFragmentView.findViewById(R.id.recyclerView);

    return savedFragmentView;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    dbHelper = new DBHelper(getContext());
    recyclerView.setHasFixedSize(true);

    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    layoutManager.setReverseLayout(true);
    layoutManager.setStackFromEnd(true);
    recyclerView.setLayoutManager(layoutManager);

  }


}
