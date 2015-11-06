package me.cullycross.test4tabs.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;

/**
 * Created by: Anton Shkurenko (cullycross)
 * Project: Test4tabs
 * Date: 11/6/15
 * Code style: SquareAndroid (https://github.com/square/java-code-styles)
 * Follow me: @tonyshkurenko
 */
public class ColorUtils {

  private ColorUtils() {

  }

  public static int getColor(Context ctx, @ColorRes int color) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      return ctx.getColor(color);
    } else {
      //noinspection deprecation
      return ctx.getResources().getColor(color);
    }
  }
}
