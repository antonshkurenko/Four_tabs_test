package me.cullycross.test4tabs.content;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.util.Random;

public class EntityContentProvider extends ContentProvider {

  /**
   * Database section
   */

  private static final String DB_NAME = "i_love_you"; // Royal Bliss
  private static final int DB_VERSION = 1;

  // tables
  private static final String ENTITY_TABLE = "entities";

  // columns
  public static final String ENTITY_ID = "_id";
  public static final String ENTITY_NAME = "name";
  public static final String ENTITY_DESCRIPTION = "description";
  public static final String ENTITY_ACTIVE = "active";
  public static final String ENTITY_LAST_UPDATE = "last_update";

  // creation sql
  private static final String DB_CREATE =
      "create table " + ENTITY_TABLE + "(" + ENTITY_ID + " integer primary key autoincrement, "
          + ENTITY_NAME + " varchar(60), " + ENTITY_DESCRIPTION + " text, " + ENTITY_ACTIVE
          + " integer, " + ENTITY_LAST_UPDATE + " integer " + ");";

  private static final String DUMMY_STRING =
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ";

  /****************************/

  /**
   * Content provider section
   */

  private static final String AUTHORITY = "me.cullycross.content.Entities";
  private static final String ENTITY_PATH = "entities";

  public static final Uri ENTITY_CONTENT_URI =
      Uri.parse("content://" + AUTHORITY + "/" + ENTITY_PATH);

  // multiple items
  private static final String ENTITY_CONTENT_TYPE =
      "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + ENTITY_PATH;

  // one item
  private static final String ENTITY_CONTENT_ITEM_TYPE =
      "vnd.android.cursor.item/vnd." + AUTHORITY + "." + ENTITY_PATH;

  // General Uri
  private static final int URI_ENTITIES = 1;

  // Item Uri
  private static final int URI_ENTITIES_ID = 2;

  private static final UriMatcher URI_MATCHER;

  static {
    URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    URI_MATCHER.addURI(AUTHORITY, ENTITY_PATH, URI_ENTITIES);
    URI_MATCHER.addURI(AUTHORITY, ENTITY_PATH + "/#", URI_ENTITIES_ID);
  }

  /****************************/

  private DBHelper mDatabaseHelper;
  private SQLiteDatabase mDatabase;

  public EntityContentProvider() {
  }

  @Override public boolean onCreate() {

    mDatabaseHelper = new DBHelper(getContext());
    return true;
  }

  @Override public Cursor query(@NonNull Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {
    switch (URI_MATCHER.match(uri)) {
      case URI_ENTITIES:
        // currently ignored
        break;
      case URI_ENTITIES_ID:
        final String id = uri.getLastPathSegment();

        if (TextUtils.isEmpty(selection)) {
          selection = ENTITY_ID + " = " + id;
        } else {
          selection = selection + " AND " + ENTITY_ID + " = " + id;
        }
        break;
      default:
        throw new IllegalArgumentException("Wrong URI: " + uri);
    }
    mDatabase = mDatabaseHelper.getWritableDatabase();
    Cursor cursor =
        mDatabase.query(ENTITY_TABLE, projection, selection, selectionArgs, null, null, sortOrder);

    if (getContext() != null) {
      cursor.setNotificationUri(getContext().getContentResolver(), ENTITY_CONTENT_URI);
    }
    return cursor;
  }

  @Override public String getType(@NonNull Uri uri) {
    switch (URI_MATCHER.match(uri)) {
      case URI_ENTITIES:
        return ENTITY_CONTENT_TYPE;
      case URI_ENTITIES_ID:
        return ENTITY_CONTENT_ITEM_TYPE;
      default:
        return null;
    }
  }

  @Override public Uri insert(@NonNull Uri uri, ContentValues values) {

    if (URI_MATCHER.match(uri) != URI_ENTITIES) {
      throw new IllegalArgumentException("Wrong URI: " + uri);
    }

    mDatabase = mDatabaseHelper.getWritableDatabase();
    final long rowID = mDatabase.insert(ENTITY_TABLE, null, values);
    final Uri resultUri = ContentUris.withAppendedId(ENTITY_CONTENT_URI, rowID);
    if (getContext() != null) {
      getContext().getContentResolver().notifyChange(resultUri, null);
    }
    return resultUri;
  }

  @Override public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
    switch (URI_MATCHER.match(uri)) {
      case URI_ENTITIES:
        // currently ignored
        break;
      case URI_ENTITIES_ID:
        final String id = uri.getLastPathSegment();

        if (TextUtils.isEmpty(selection)) {
          selection = ENTITY_ID + " = " + id;
        } else {
          selection = selection + " AND " + ENTITY_ID + " = " + id;
        }
        break;
      default:
        throw new IllegalArgumentException("Wrong URI: " + uri);
    }
    mDatabase = mDatabaseHelper.getWritableDatabase();
    final int cnt = mDatabase.delete(ENTITY_TABLE, selection, selectionArgs);
    if (getContext() != null) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return cnt;
  }

  @Override public int update(@NonNull Uri uri, ContentValues values, String selection,
      String[] selectionArgs) {
    switch (URI_MATCHER.match(uri)) {
      case URI_ENTITIES:
        // currently ignored
        break;
      case URI_ENTITIES_ID:
        final String id = uri.getLastPathSegment();
        if (TextUtils.isEmpty(selection)) {
          selection = ENTITY_ID + " = " + id;
        } else {
          selection = selection + " AND " + ENTITY_ID + " = " + id;
        }
        break;
      default:
        throw new IllegalArgumentException("Wrong URI: " + uri);
    }
    mDatabase = mDatabaseHelper.getWritableDatabase();
    int cnt = mDatabase.update(ENTITY_TABLE, values, selection, selectionArgs);
    if (getContext() != null) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return cnt;
  }

  private class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
      super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

      db.execSQL(DB_CREATE);

      final Random rnd = new Random();

      ContentValues cv = new ContentValues();
      for (int i = 1; i <= 3; i++) {
        cv.put(ENTITY_NAME, "name " + i);
        cv.put(ENTITY_DESCRIPTION, new String(new char[i]).replace("\0", DUMMY_STRING));
        cv.put(ENTITY_ACTIVE, rnd.nextBoolean());
        cv.put(ENTITY_LAST_UPDATE, System.currentTimeMillis());
        db.insert(ENTITY_TABLE, null, cv);
      }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
  }
}
