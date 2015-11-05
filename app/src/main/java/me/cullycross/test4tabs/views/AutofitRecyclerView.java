package me.cullycross.test4tabs.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by: Anton Shkurenko (cullycross)
 * Project: Test4tabs
 * Date: 11/5/15
 * Code style: SquareAndroid (https://github.com/square/java-code-styles)
 * Follow me: @tonyshkurenko
 *
 * see <a href="https://github.com/chiuki/android-recyclerview/blob/master/app/src/main/java/com/sqisland/android/recyclerview/AutofitRecyclerView.java">
 *   this</>
 */
public class AutofitRecyclerView extends RecyclerView {
  private GridLayoutManager mManager;
  private int mColumnWidth = -1;

  public AutofitRecyclerView(Context context) {
    super(context);
    init(context, null);
  }

  public AutofitRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public AutofitRecyclerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    if (attrs != null) {
      int[] attrsArray = {
          android.R.attr.columnWidth
      };
      TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
      mColumnWidth = array.getDimensionPixelSize(0, -1);
      array.recycle();
    }

    mManager = new GridLayoutManager(getContext(), 1);
    setLayoutManager(mManager);
  }

  @Override protected void onMeasure(int widthSpec, int heightSpec) {
    super.onMeasure(widthSpec, heightSpec);
    if (mColumnWidth > 0) {
      int spanCount = Math.max(1, getMeasuredWidth() / mColumnWidth);
      mManager.setSpanCount(spanCount);
    }
  }
}
