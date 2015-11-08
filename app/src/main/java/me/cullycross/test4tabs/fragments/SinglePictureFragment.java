package me.cullycross.test4tabs.fragments;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.cullycross.test4tabs.R;

public class SinglePictureFragment extends Fragment {

  @Bind(R.id.image) ImageView mImage;

  public static SinglePictureFragment newInstance() {
    final SinglePictureFragment fragment = new SinglePictureFragment();
    final Bundle args = new Bundle();

    // set args here

    fragment.setArguments(args);
    return fragment;
  }

  public SinglePictureFragment() {
    // Required empty public constructor
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_single_picture, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @OnClick(R.id.rotate) void rotate() {
    final Matrix m = new Matrix();
    final Bitmap bitmap = ((BitmapDrawable) mImage.getDrawable()).getBitmap();

    m.postRotate(90);

    final Bitmap rotatedBitmap =
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

    mImage.setImageBitmap(rotatedBitmap);
  }

  @OnClick(R.id.flip_vertical) void flipVertical() {
    final Matrix m = new Matrix();
    final Bitmap bitmap = ((BitmapDrawable) mImage.getDrawable()).getBitmap();

    m.preScale(1, -1);
    final Bitmap flippedBitmap =
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
    mImage.setImageBitmap(flippedBitmap);
  }

  @OnClick(R.id.flip_horizontal) void flipHorizontal() {
    final Matrix m = new Matrix();
    final Bitmap bitmap = ((BitmapDrawable) mImage.getDrawable()).getBitmap();

    m.preScale(-1, 1);
    final Bitmap flippedBitmap =
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
    mImage.setImageBitmap(flippedBitmap);
  }
}
