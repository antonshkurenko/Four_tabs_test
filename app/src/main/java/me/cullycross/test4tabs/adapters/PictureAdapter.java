package me.cullycross.test4tabs.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.cullycross.test4tabs.R;
import me.cullycross.test4tabs.activities.FullScreenImageActivity;
import me.cullycross.test4tabs.fragments.PicturesFragment;

/**
 * Created by: Anton Shkurenko (cullycross)
 * Project: Test4tabs
 * Date: 11/5/15
 * Code style: SquareAndroid (https://github.com/square/java-code-styles)
 * Follow me: @tonyshkurenko
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureItem> {

  private static final String URL = "http://lorempixel.com/200/200/food/%d/%s";

  private final Context mContext;
  private final PicturesFragment mFragment;
  private final Random mRandom;

  private List<Pair<Integer, Integer>> mIntegerPairs;

  public PictureAdapter(PicturesFragment fragment) {
    super();
    mContext = fragment.getContext();
    mFragment = fragment;
    mRandom = new Random();

    initList();
  }

  @Override public PictureItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {

    View v = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.recycler_view_item_picture, viewGroup, false);

    return new PictureItem(v);
  }

  @Override public void onBindViewHolder(PictureItem holder, final int position) {

    final Pair<Integer, Integer> pair = mIntegerPairs.get(position);

    final String path = String.format(URL, pair.first, "Position " + pair.second);
    Picasso.with(mContext)
        .load(path)
        .fit()
        .centerCrop()
        .placeholder(R.mipmap.ic_launcher)
        .into(holder.mImage);
    holder.mImage.setTag(path);
  }

  @Override public int getItemCount() {
    return mIntegerPairs.size();
  }

  public void reset() {
    initList();
  }

  public void remove(int random, int startPosition) {
    final Pair<Integer, Integer> pair = new Pair<>(random, startPosition);
    final int index = mIntegerPairs.indexOf(pair);
    mIntegerPairs.remove(index);
    notifyItemRemoved(index);
  }

  private void initList() {
    mIntegerPairs = new ArrayList<>(50);
    for (int i = 0; i < 50; i++) {
      mIntegerPairs.add(new Pair<>(mRandom.nextInt(10) + 1, i));
    }
  }

  class PictureItem extends RecyclerView.ViewHolder {

    @Bind(R.id.image) ImageView mImage;

    public PictureItem(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.image) void openImage(View v) {
      mFragment.startActivityForResult(new Intent(mContext, FullScreenImageActivity.class).putExtra(
              FullScreenImageActivity.ARGS_PICTURE_PATH, ((String) v.getTag())),
          PicturesFragment.REQUEST_OPEN_IMAGE);
    }
  }
}
