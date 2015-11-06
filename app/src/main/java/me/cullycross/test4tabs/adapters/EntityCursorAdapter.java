package me.cullycross.test4tabs.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import me.cullycross.test4tabs.R;
import me.cullycross.test4tabs.activities.FourTabsActivity;
import me.cullycross.test4tabs.fragments.EntityDialogFragment;
import me.cullycross.test4tabs.pojos.SomeEntity;

/**
 * Created by: Anton Shkurenko (cullycross)
 * Project: Test4tabs
 * Date: 11/5/15
 * Code style: SquareAndroid (https://github.com/square/java-code-styles)
 * Follow me: @tonyshkurenko
 */
public class EntityCursorAdapter extends CursorRecyclerViewAdapter<EntityCursorAdapter.EntityItem> {

  private static final SimpleDateFormat DATE_FORMAT =
      new SimpleDateFormat("EEEE, MMMM dd yyyy", Locale.getDefault());

  public EntityCursorAdapter(Context context, Cursor cursor) {
    super(context, cursor);
  }

  @Override public EntityItem onCreateViewHolder(ViewGroup parent, int viewType) {

    final View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.recycler_view_item_entity, parent, false);

    return new EntityItem(itemView);
  }

  @Override public void onBindViewHolder(EntityItem holder, Cursor cursor) {
    final SomeEntity entity = SomeEntity.fromCursor(cursor);

    holder.mLayout.setTag(entity.getId());
    holder.mName.setText(entity.getName());
    holder.mDescription.setText(entity.getDescription());
    holder.mLastUpdated.setText(DATE_FORMAT.format(new Date(entity.getUpdatedMillis())));
  }

  class EntityItem extends RecyclerView.ViewHolder {

    @Bind(R.id.entity_layout) LinearLayout mLayout;
    @Bind(R.id.name) TextView mName;
    @Bind(R.id.last_updated) TextView mLastUpdated;
    @Bind(R.id.description) TextView mDescription;

    public EntityItem(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    @OnClick(R.id.entity_layout) void edit(View v) {
      final FourTabsActivity ctx = ((FourTabsActivity) v.getContext());
      EntityDialogFragment.newInstance(((int) v.getTag()), mName.getText().toString(),
          mDescription.getText().toString(), getAdapterPosition())
          .show(ctx.getSupportFragmentManager(), null);
    }
  }
}