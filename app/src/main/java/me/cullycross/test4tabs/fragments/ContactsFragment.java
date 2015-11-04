package me.cullycross.test4tabs.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import me.cullycross.test4tabs.R;
import me.cullycross.test4tabs.adapters.ContactsCursorAdapter;

public class ContactsFragment extends Fragment {

  @Bind(R.id.recycler_view_contacts) RecyclerView mRecyclerViewContacts;

  public static ContactsFragment newInstance() {
    final ContactsFragment fragment = new ContactsFragment();
    final Bundle args = new Bundle();

    // set args here

    fragment.setArguments(args);
    return fragment;
  }

  public ContactsFragment() {
    // Required empty public constructor
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_contacts, container, false);
    ButterKnife.bind(this, view);

    final ContentResolver cr = getContext().getContentResolver();
    final Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

    final LinearLayoutManager manager = new LinearLayoutManager(getContext());
    mRecyclerViewContacts.setLayoutManager(manager);
    mRecyclerViewContacts.setAdapter(new ContactsCursorAdapter(getContext(), cursor));

    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
