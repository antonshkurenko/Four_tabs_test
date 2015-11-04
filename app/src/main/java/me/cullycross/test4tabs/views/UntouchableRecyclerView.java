package me.cullycross.test4tabs.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by: Anton Shkurenko (cullycross)
 * Project: Test4tabs
 * Date: 11/4/15
 * Code style: SquareAndroid (https://github.com/square/java-code-styles)
 * Follow me: @tonyshkurenko
 */
public class UntouchableRecyclerView extends RecyclerView {
  public UntouchableRecyclerView(Context context) {
    super(context);
  }

  public UntouchableRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public UntouchableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override public boolean onTouchEvent(MotionEvent e) {
    return false;
  }
}