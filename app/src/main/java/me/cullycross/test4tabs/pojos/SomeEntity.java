package me.cullycross.test4tabs.pojos;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import me.cullycross.test4tabs.content.EntityContentProvider;
import me.cullycross.test4tabs.utils.RxHelper;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static me.cullycross.test4tabs.content.EntityContentProvider.ENTITY_ACTIVE;
import static me.cullycross.test4tabs.content.EntityContentProvider.ENTITY_CONTENT_URI;
import static me.cullycross.test4tabs.content.EntityContentProvider.ENTITY_DESCRIPTION;
import static me.cullycross.test4tabs.content.EntityContentProvider.ENTITY_ID;
import static me.cullycross.test4tabs.content.EntityContentProvider.ENTITY_LAST_UPDATE;
import static me.cullycross.test4tabs.content.EntityContentProvider.ENTITY_NAME;

/**
 * Created by: Anton Shkurenko (cullycross)
 * Project: Test4tabs
 * Date: 11/5/15
 * Code style: SquareAndroid (https://github.com/square/java-code-styles)
 * Follow me: @tonyshkurenko
 */
public class SomeEntity {

  public static final int FLAG_NEW_ENTITY = -1;

  private int mId = FLAG_NEW_ENTITY;
  private String mName;
  private String mDescription;
  private boolean mIsActive;
  private long mUpdatedMillis;

  public static void fromDatabase(Context ctx, int id, FromDatabaseCallback callback) {

    if (id == FLAG_NEW_ENTITY) {
      callback.fromDatabase(null);
      return;
    }

    final Uri uri = ContentUris.withAppendedId(ENTITY_CONTENT_URI, id);

    RxHelper.makeColdObservable(() -> ctx.getContentResolver().query(uri, null, null, null, null))
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(c -> {
          if (c != null && c.moveToFirst()) {
            final SomeEntity entity = fromCursor(c);
            c.close();
            callback.fromDatabase(entity);
          } else {
            callback.fromDatabase(null);
          }
        });
  }

  public static SomeEntity fromCursor(Cursor c) {

    final int columnId = c.getColumnIndex(ENTITY_ID);
    final int columnName = c.getColumnIndex(ENTITY_NAME);
    final int columnDescription = c.getColumnIndex(ENTITY_DESCRIPTION);
    final int columnActive = c.getColumnIndex(ENTITY_ACTIVE);
    final int columnLastUpdate = c.getColumnIndex(ENTITY_LAST_UPDATE);

    return new SomeEntity(c.getInt(columnId), c.getString(columnName),
        c.getString(columnDescription), c.getInt(columnActive) != 0, c.getLong(columnLastUpdate));
  }

  public SomeEntity(String name, String description) {
    mName = name;
    mDescription = description;
    mIsActive = false;
    update();
  }

  /**
   * for fabric methods
   *
   * @param id database id
   * @param name entity name
   * @param description entity description
   * @param isActive is entity active
   * @param updatedMillis last update time
   */
  private SomeEntity(int id, String name, String description, boolean isActive,
      long updatedMillis) {
    mId = id;
    mName = name;
    mDescription = description;
    mIsActive = isActive;
    mUpdatedMillis = updatedMillis;
  }

  public void save(Context ctx) {

    Observable<Void> obs;
    if (mId == FLAG_NEW_ENTITY) {
      obs = RxHelper.makeColdObservable(() -> {
        ctx.getContentResolver()
            .insert(EntityContentProvider.ENTITY_CONTENT_URI, toContentValues());
        return null;
      });

    } else {
      final Uri uri = ContentUris.withAppendedId(EntityContentProvider.ENTITY_CONTENT_URI, mId);
      obs = RxHelper.makeColdObservable(() -> {
        ctx.getContentResolver().update(uri, toContentValues(), null, null);
        return null;
      });
    }
    obs.subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
  }

  public ContentValues toContentValues() {
    return toContentValues(mUpdatedMillis);
  }

  public ContentValues toContentValues(long updateTime) {
    final ContentValues cv = new ContentValues();
    cv.put(ENTITY_NAME, mName);
    cv.put(ENTITY_DESCRIPTION, mDescription);
    cv.put(ENTITY_ACTIVE, mIsActive);

    mUpdatedMillis = updateTime;
    cv.put(ENTITY_LAST_UPDATE, updateTime);
    return cv;
  }

  public void update() {
    mUpdatedMillis = System.currentTimeMillis();
  }

  /**
   * Setters
   */

  public void setName(String name) {
    mName = name;
    update();
  }

  public void setDescription(String description) {
    mDescription = description;
    update();
  }

  public void setIsActive(boolean isActive) {
    mIsActive = isActive;
    update();
  }

  /***********************/

  /**
   * Getters
   */

  public int getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }

  public String getDescription() {
    return mDescription;
  }

  public boolean isActive() {
    return mIsActive;
  }

  public long getUpdatedMillis() {
    return mUpdatedMillis;
  }

  /***********************/

  public interface FromDatabaseCallback {
    void fromDatabase(@Nullable SomeEntity entity);
  }
}
