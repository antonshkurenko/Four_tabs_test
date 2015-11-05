package me.cullycross.test4tabs.pojos;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

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

  private int mId;
  private String mName;
  private String mDescription;
  private boolean mIsActive;
  private long mUpdatedMillis;

  public static SomeEntity fromDatabase(Context ctx, int id) {
    final Uri uri = ContentUris.withAppendedId(ENTITY_CONTENT_URI, id);
    final Cursor c = ctx.getContentResolver().query(uri, null, null, null, null);
    return fromCursor(c);
  }

  public static SomeEntity fromCursor(Cursor c) {
    if (c.moveToFirst()) {
      final int columnId = c.getColumnIndex(ENTITY_ID);
      final int columnName = c.getColumnIndex(ENTITY_NAME);
      final int columnDescription = c.getColumnIndex(ENTITY_DESCRIPTION);
      final int columnActive = c.getColumnIndex(ENTITY_ACTIVE);
      final int columnLastUpdate = c.getColumnIndex(ENTITY_LAST_UPDATE);

      return new SomeEntity(c.getInt(columnId), c.getString(columnName),
          c.getString(columnDescription), c.getInt(columnActive) != 0, c.getLong(columnLastUpdate));
    } else {
      return null;
    }
  }

  public SomeEntity(int id, String name, String description, boolean isActive, long updatedMillis) {
    mId = id;
    mName = name;
    mDescription = description;
    mIsActive = isActive;
    mUpdatedMillis = updatedMillis;
  }

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
}
