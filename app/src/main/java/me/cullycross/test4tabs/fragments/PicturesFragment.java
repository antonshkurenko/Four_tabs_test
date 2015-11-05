package me.cullycross.test4tabs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import me.cullycross.test4tabs.R;
import me.cullycross.test4tabs.adapters.DoggyAdapter;

public class PicturesFragment extends Fragment {

  @Bind(R.id.recycler_view_images) RecyclerView mRecyclerViewImages;

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
    View view = inflater.inflate(R.layout.fragment_pictures, container, false);
    ButterKnife.bind(this, view);

    mRecyclerViewImages.setAdapter(new DoggyAdapter(getContext()));
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
