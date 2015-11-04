package me.cullycross.test4tabs.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import me.cullycross.test4tabs.R;
import me.cullycross.test4tabs.views.ExpandedRecyclerViewLinearManager;
import me.cullycross.test4tabs.views.UntouchableRecyclerView;

/**
 * Created by: Anton Shkurenko (cullycross)
 * Project: Test4tabs
 * Date: 11/4/15
 * Code style: SquareAndroid (https://github.com/square/java-code-styles)
 * Follow me: @tonyshkurenko
 */
public class ContactsCursorAdapter
    extends CursorRecyclerViewAdapter<ContactsCursorAdapter.ContactItem> {

  private Context mContext;

  private int mExpandedPosition = -1;

  public ContactsCursorAdapter(Context context, Cursor cursor) {
    super(context, cursor);

    mContext = context;
  }

  @Override public ContactItem onCreateViewHolder(ViewGroup parent, int viewType) {

    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.recycler_view_item_contact, parent, false);

    return new ContactItem(itemView);
  }

  @Override public void onBindViewHolder(ContactItem holder, Cursor cursor) {

    final String image =
        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

    if (image != null) {
      Picasso.with(mContext).load(Uri.parse(image)).fit().centerCrop().into(holder.mPhoto);
    } else {
      holder.mPhoto.setImageResource(R.mipmap.ic_launcher);
    }

    final String name =
        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

    holder.mName.setText(name);

    final String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
    final ContentResolver contentResolver = mContext.getContentResolver();

    initPhones(holder, id, contentResolver);
    initEmails(holder, id, contentResolver);

    if (holder.getAdapterPosition() == mExpandedPosition) {
      holder.mExpandArea.setVisibility(View.VISIBLE);
    } else {
      holder.mExpandArea.setVisibility(View.GONE);
    }

    holder.mContactLayout.setOnClickListener(v -> {
      if (mExpandedPosition >= 0) {
        int prev = mExpandedPosition;
        notifyItemChanged(prev);
      }
      if (mExpandedPosition != holder.getAdapterPosition()) {
        mExpandedPosition = holder.getAdapterPosition();
      } else {
        mExpandedPosition = -1;
      }
      notifyItemChanged(mExpandedPosition);
    });
  }

  private void initEmails(ContactItem holder, String id, ContentResolver contentResolver) {
    final Cursor emailCursor =
        contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[] { id }, null);

    final ExpandedRecyclerViewLinearManager emailManager =
        new ExpandedRecyclerViewLinearManager(mContext, LinearLayoutManager.VERTICAL, false);

    holder.mEmailsRecyclerView.setLayoutManager(emailManager);
    holder.mEmailsRecyclerView.setAdapter(new EmailCursorAdapter(mContext, emailCursor));
  }

  private void initPhones(ContactItem holder, String id, ContentResolver contentResolver) {
    final Cursor phoneCursor =
        contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);

    final ExpandedRecyclerViewLinearManager phoneManager =
        new ExpandedRecyclerViewLinearManager(mContext, LinearLayoutManager.VERTICAL, false);

    holder.mPhonesRecyclerView.setLayoutManager(phoneManager);
    holder.mPhonesRecyclerView.setAdapter(new PhoneCursorAdapter(mContext, phoneCursor));
  }

  private void initEmpty(ContactItem holder) {
    holder.mPhonesRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
  }

  static class ContactItem extends RecyclerView.ViewHolder {

    @Bind(R.id.contact_layout) View mContactLayout;
    @Bind(R.id.photo) ImageView mPhoto;
    @Bind(R.id.name) TextView mName;
    @Bind(R.id.phones_recycler_view) UntouchableRecyclerView mPhonesRecyclerView;
    @Bind(R.id.emails_recycler_view) UntouchableRecyclerView mEmailsRecyclerView;
    @Bind(R.id.expand_area) LinearLayout mExpandArea;

    public ContactItem(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}
