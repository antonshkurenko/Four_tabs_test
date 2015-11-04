package me.cullycross.test4tabs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import me.cullycross.test4tabs.R;

public class PicturesFragment extends Fragment {

  public static PicturesFragment newInstance() {
    final PicturesFragment fragment = new PicturesFragment();
    final Bundle args = new Bundle();

    // set args here

    fragment.setArguments(args);
    return fragment;
  }

  public PicturesFragment() {
    // Required empty public constructor
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_pictures, container, false);
  }
}
