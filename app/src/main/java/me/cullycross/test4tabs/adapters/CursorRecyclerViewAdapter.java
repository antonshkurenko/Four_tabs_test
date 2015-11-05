package me.cullycross.test4tabs.adapters;

/*
 * Copyright (C) 2014 skyfish.jy@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;

/**
 * Created by skyfishjy on 10/31/14.
 *
 * See <a href="https://gist.github.com/skyfishjy/443b7448f59be978bc59">Original gist</a>
 */

public abstract class CursorRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

  private Context mContext;

  private Cursor c;

  private boolean mDataValid;

  private int mRowIdColumn;

  private DataSetObserver mDataSetObserver;

  public CursorRecyclerViewAdapter(Context context, Cursor cursor) {
    mContext = context;
    c = cursor;
    mDataValid = cursor != null;
    mRowIdColumn = mDataValid ? c.getColumnIndex("_id") : -1;
    mDataSetObserver = new NotifyingDataSetObserver();
    if (c != null) {
      c.registerDataSetObserver(mDataSetObserver);
    }
  }

  public Cursor getCursor() {
    return c;
  }

  @Override
  public int getItemCount() {
    if (mDataValid && c != null) {
      return c.getCount();
    }
    return 0;
  }

  @Override
  public long getItemId(int position) {
    if (mDataValid && c != null && c.moveToPosition(position)) {
      return c.getLong(mRowIdColumn);
    }
    return 0;
  }

  @Override
  public void setHasStableIds(boolean hasStableIds) {
    super.setHasStableIds(true);
  }

  public abstract void onBindViewHolder(VH viewHolder, Cursor cursor);

  @Override
  public void onBindViewHolder(VH viewHolder, int position) {
    if (!mDataValid) {
      throw new IllegalStateException("this should only be called when the cursor is valid");
    }
    if (!c.moveToPosition(position)) {
      throw new IllegalStateException("couldn't move cursor to position " + position);
    }
    onBindViewHolder(viewHolder, c);
  }

  /**
   * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
   * closed.
   */
  public void changeCursor(Cursor cursor) {
    Cursor old = swapCursor(cursor);
    if (old != null) {
      old.close();
    }
  }

  /**
   * Swap in a new Cursor, returning the old Cursor.  Unlike
   * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
   * closed.
   */
  public Cursor swapCursor(Cursor newCursor) {
    if (newCursor == c) {
      return null;
    }
    final Cursor oldCursor = c;
    if (oldCursor != null && mDataSetObserver != null) {
      oldCursor.unregisterDataSetObserver(mDataSetObserver);
    }
    c = newCursor;
    if (c != null) {
      if (mDataSetObserver != null) {
        c.registerDataSetObserver(mDataSetObserver);
      }
      mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
      mDataValid = true;
      notifyDataSetChanged();
    } else {
      mRowIdColumn = -1;
      mDataValid = false;
      notifyDataSetChanged();
      //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
    }
    return oldCursor;
  }

  private class NotifyingDataSetObserver extends DataSetObserver {
    @Override
    public void onChanged() {
      super.onChanged();
      mDataValid = true;
      notifyDataSetChanged();
    }

    @Override
    public void onInvalidated() {
      super.onInvalidated();
      mDataValid = false;
      notifyDataSetChanged();
      //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
    }
  }
}