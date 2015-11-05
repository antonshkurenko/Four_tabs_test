package me.cullycross.test4tabs.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import me.cullycross.test4tabs.pojos.SomeEntity;

/**
 * Created by: Anton Shkurenko (cullycross)
 * Project: Test4tabs
 * Date: 11/5/15
 * Code style: SquareAndroid (https://github.com/square/java-code-styles)
 * Follow me: @tonyshkurenko
 */
public class EntityCursorAdapter extends CursorRecyclerViewAdapter<EntityCursorAdapter.EntityItem> {

  public EntityCursorAdapter(Context context, Cursor cursor) {
    super(context, cursor);
  }

  @Override public EntityItem onCreateViewHolder(ViewGroup parent, int viewType) {

    final View itemView = LayoutInflater.from(parent.getContext())
        .inflate(android.R.layout.simple_list_item_1, parent, false);

    return new EntityItem(itemView);
  }

  @Override public void onBindViewHolder(EntityItem holder, Cursor cursor) {
    final SomeEntity entity = SomeEntity.fromCursor(cursor);
  }

  class EntityItem extends RecyclerView.ViewHolder {


    public EntityItem(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

  }
}