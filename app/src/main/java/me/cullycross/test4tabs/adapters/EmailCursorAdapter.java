package me.cullycross.test4tabs.adapters;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.cullycross.test4tabs.activities.FourTabsActivity;
import me.cullycross.test4tabs.fragments.EmailDialogFragment;

/**
 * Created by: Anton Shkurenko (cullycross)
 * Project: Test4tabs
 * Date: 11/4/15
 * Code style: SquareAndroid (https://github.com/square/java-code-styles)
 * Follow me: @tonyshkurenko
 */
public class EmailCursorAdapter extends CursorRecyclerViewAdapter<EmailCursorAdapter.EmailItem> {

  private final String mName;

  public EmailCursorAdapter(Context context, Cursor cursor, String name) {
    super(context, cursor);
    mName = name;
  }

  @Override public EmailItem onCreateViewHolder(ViewGroup parent, int viewType) {

    final View itemView = LayoutInflater.from(parent.getContext())
        .inflate(android.R.layout.simple_list_item_1, parent, false);

    return new EmailItem(itemView);
  }

  @Override public void onBindViewHolder(EmailItem holder, Cursor cursor) {

    final String email = cursor.getString(
        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

    holder.mEmail.setText("Send: " + email);
    holder.mEmail.setTag(email);
  }

  class EmailItem extends RecyclerView.ViewHolder {

    @Bind(android.R.id.text1) TextView mEmail;

    public EmailItem(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    @OnClick(android.R.id.text1) void sendEmail(TextView t) {
      final FourTabsActivity ctx = ((FourTabsActivity) t.getContext());
      EmailDialogFragment.newInstance(t.getTag().toString(), mName)
          .show(ctx.getSupportFragmentManager(), null);
    }
  }
}