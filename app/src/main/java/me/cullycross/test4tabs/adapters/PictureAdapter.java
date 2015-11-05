package me.cullycross.test4tabs.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import java.util.Random;
import me.cullycross.test4tabs.R;

/**
 * Created by: Anton Shkurenko (cullycross)
 * Project: Test4tabs
 * Date: 11/5/15
 * Code style: SquareAndroid (https://github.com/square/java-code-styles)
 * Follow me: @tonyshkurenko
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.DogItem> {

  private static final String URL = "http://lorempixel.com/200/200/food/%d/%s";

  private final Context mContext;
  private final Random mRandom;

  private int mCount = 50;

  public PictureAdapter(Context ctx) {
    super();
    mContext = ctx;
    mRandom = new Random();
  }

  @Override public DogItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {

    View v = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.recycler_view_item_picture, viewGroup, false);

    return new DogItem(v);
  }

  @Override public void onBindViewHolder(DogItem holder, final int position) {

    Picasso.with(mContext)
        .load(String.format(URL, mRandom.nextInt(10) + 1, "Position " + position))
        .fit()
        .centerCrop().placeholder(R.mipmap.ic_launcher).into(holder.mImage);
  }

  @Override public int getItemCount() {
    return mCount;
  }

  public static class DogItem extends RecyclerView.ViewHolder {

    @Bind(R.id.image) ImageView mImage;

    public DogItem(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
