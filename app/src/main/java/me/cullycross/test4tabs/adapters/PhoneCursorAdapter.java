package me.cullycross.test4tabs.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.tbruyelle.rxpermissions.RxPermissions;
import rx.subjects.PublishSubject;

/**
 * Created by: Anton Shkurenko (cullycross)
 * Project: Test4tabs
 * Date: 11/4/15
 * Code style: SquareAndroid (https://github.com/square/java-code-styles)
 * Follow me: @tonyshkurenko
 */
public class PhoneCursorAdapter extends CursorRecyclerViewAdapter<PhoneCursorAdapter.PhoneItem> {

  private final PublishSubject<Void> mSubject = PublishSubject.create();

  public PhoneCursorAdapter(Context context, Cursor cursor) {
    super(context, cursor);
  }

  @Override public PhoneItem onCreateViewHolder(ViewGroup parent, int viewType) {

    final View itemView = LayoutInflater.from(parent.getContext())
        .inflate(android.R.layout.simple_list_item_1, parent, false);

    return new PhoneItem(itemView);
  }

  @Override public void onBindViewHolder(PhoneItem holder, Cursor cursor) {

    final String phone =
        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

    holder.mPhone.setText(phone);
  }

  class PhoneItem extends RecyclerView.ViewHolder {

    @Bind(android.R.id.text1) TextView mPhone;

    public PhoneItem(View view) {
      super(view);
      ButterKnife.bind(this, view);

      RxPermissions.getInstance(view.getContext())
          .request(mSubject, Manifest.permission.CAMERA)
          .subscribe(granted -> {
            if (granted) {
              final Intent intent = new Intent(Intent.ACTION_CALL);
              intent.setData(Uri.parse("tel:" + mPhone.getText().toString()));
              //noinspection ResourceType (it's for permissions)
              mPhone.getContext().startActivity(intent);
            } else {
              Toast.makeText(mPhone.getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
          });
    }

    @OnClick(android.R.id.text1) void call() {
      mSubject.onNext(null);
    }
  }
}
