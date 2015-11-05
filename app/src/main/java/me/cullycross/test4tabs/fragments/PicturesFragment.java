package me.cullycross.test4tabs.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import me.cullycross.test4tabs.R;
import me.cullycross.test4tabs.activities.FourTabsActivity;
import me.cullycross.test4tabs.activities.FullScreenImageActivity;
import me.cullycross.test4tabs.adapters.PictureAdapter;

public class PicturesFragment extends Fragment implements FourTabsActivity.FabClickListener {

  public static final int REQUEST_OPEN_IMAGE = 22;

  @Bind(R.id.recycler_view_images) RecyclerView mRecyclerViewImages;

  private PictureAdapter mAdapter;

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

    mAdapter = new PictureAdapter(this);
    mRecyclerViewImages.setAdapter(mAdapter);
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void onFabClick() {
    mAdapter.reset();
    mAdapter.notifyDataSetChanged();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_OPEN_IMAGE) {
      if (resultCode == AppCompatActivity.RESULT_OK) {
        final String path = data.getStringExtra(FullScreenImageActivity.ARGS_PICTURE_PATH);
        final String[] splitted = path.split("/");
        final String random = splitted[splitted.length - 2];
        final String position = splitted[splitted.length - 1].split(" ")[1];
        mAdapter.remove(Integer.parseInt(random),
            Integer.parseInt(position));
      }
    }
  }
}
