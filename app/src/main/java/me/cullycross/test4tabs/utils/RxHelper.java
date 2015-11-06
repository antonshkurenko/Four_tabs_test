package me.cullycross.test4tabs.utils;

import android.util.Log;
import java.util.concurrent.Callable;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by: Anton Shkurenko (cullycross)
 * Project: Test4tabs
 * Date: 11/6/15
 * Code style: SquareAndroid (https://github.com/square/java-code-styles)
 * Follow me: @tonyshkurenko
 */
public class RxHelper {

  private static final String TAG = RxHelper.class.getSimpleName();

  public static <T> Observable<T> makeColdObservable(final Callable<T> func) {
    return Observable.create(
        new Observable.OnSubscribe<T>() {
          @Override
          public void call(Subscriber<? super T> subscriber) {
            try {
              subscriber.onNext(func.call());
            } catch(Exception e) {
              Log.e(TAG, "Something wrong happened", e);
            }
          }
        });
  }
}
