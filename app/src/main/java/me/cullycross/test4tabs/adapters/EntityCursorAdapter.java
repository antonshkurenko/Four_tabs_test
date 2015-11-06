package me.cullycross.test4tabs.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import me.cullycross.test4tabs.fragments.DatabaseFragment;
import me.cullycross.test4tabs.fragments.dialogs.EntityDialogFragment;
import me.cullycross.test4tabs.pojos.SomeEntity;
import me.cullycross.test4tabs.utils.ColorUtils;

/**
 * Created by: Anton Shkurenko (cullycross)
 * Project: Test4tabs
 * Date: 11/5/15
 * Code style: SquareAndroid (https://github.com/square/java-code-styles)
 * Follow me: @tonyshkurenko
 */
public class EntityCursorAdapter extends CursorRecyclerViewAdapter<EntityCursorAdapter.EntityItem> {

  private static final SimpleDateFormat DATE_FORMAT =
      new SimpleDateFormat("EE, MM/dd/yy HH:mm:ss.SSS", Locale.getDefault());

  private Context mContext;
  private DatabaseFragment mFragment;

  public EntityCursorAdapter(DatabaseFragment fragment, Cursor cursor) {
    super(fragment.getContext(), cursor);
    mFragment = fragment;
    mContext = fragment.getContext();
  }

  @Override public EntityItem onCreateViewHolder(ViewGroup parent, int viewType) {

    final View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.recycler_view_item_entity, parent, false);

    return new EntityItem(itemView);
  }

  @Override public void onBindViewHolder(EntityItem holder, Cursor cursor) {
    final SomeEntity entity = SomeEntity.fromCursor(cursor);

    holder.mLayout.setTag(entity.getId());

    if (entity.isActive()) {
      holder.mLayout.setBackgroundColor(
          ColorUtils.getColor(mContext, android.R.color.holo_blue_light));
    } else {
      holder.mLayout.setBackgroundColor(ColorUtils.getColor(mContext, android.R.color.white));
    }

    holder.mActive.setTag(R.id.tag_key_id, entity.getId());
    holder.mActive.setTag(R.id.tag_key_row, holder.getAdapterPosition());
    holder.mName.setText(entity.getName());
    holder.mDescription.setText(entity.getDescription());
    holder.mLastUpdated.setText(DATE_FORMAT.format(new Date(entity.getUpdatedMillis())));

    /**
     * I use setOnCheckedChangeListener after setChecked() call
     * and not Butterknife's @OnCheckedChange
     * because of conflict setChecked() and SomeEntity#save()
     * together they make infinite cycle calls
     * because of content observer in base cursor adapter
     */
    holder.mActive.setOnCheckedChangeListener(null);
    holder.mActive.setChecked(entity.isActive());
    holder.mActive.setOnCheckedChangeListener(mCheckedChangeListener);
  }

  final private CompoundButton.OnCheckedChangeListener mCheckedChangeListener = (c, flag) -> {

    if (mContext == null) {
      return; // should never happen, but still
    }

    SomeEntity.fromDatabase(mContext, ((int) c.getTag(R.id.tag_key_id)), e -> {
      if (e != null) {
        e.setIsActive(flag);
        e.save(mContext);
        mFragment.setPositionToScroll(((int) c.getTag(R.id.tag_key_row)));
      }
    });
  };

  class EntityItem extends RecyclerView.ViewHolder {

    @Bind(R.id.entity_layout) LinearLayout mLayout;
    @Bind(R.id.name) TextView mName;
    @Bind(R.id.last_updated) TextView mLastUpdated;
    @Bind(R.id.description) TextView mDescription;
    @Bind(R.id.active) CheckBox mActive;

    public EntityItem(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    @OnClick(R.id.entity_layout) void edit(View v) {
      final FourTabsActivity ctx = ((FourTabsActivity) mContext);
      EntityDialogFragment.newInstance(((int) v.getTag()), mName.getText().toString(),
          mDescription.getText().toString(), getAdapterPosition())
          .show(ctx.getSupportFragmentManager(), null);
    }
  }
}