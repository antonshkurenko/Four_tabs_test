package me.cullycross.test4tabs.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import me.cullycross.test4tabs.R;
import me.cullycross.test4tabs.adapters.ContactsCursorAdapter;
import me.cullycross.test4tabs.views.FastScrollerView;

public class ContactsFragment extends Fragment {

  @Bind(R.id.recycler_view_contacts) RecyclerView mRecyclerViewContacts;
  @Bind(R.id.fastscroller) FastScrollerView mFastscroller;

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
    final View view = inflater.inflate(R.layout.fragment_contacts, container, false);
    ButterKnife.bind(this, view);

    setHasOptionsMenu(true);

    final ContentResolver cr = getContext().getContentResolver();
    final Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
        "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");

    final ContactsCursorAdapter adapter = new ContactsCursorAdapter(getContext(), cursor);

    mRecyclerViewContacts.setLayoutManager(new LinearLayoutManager(getContext()) {
      @Override
      public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        final int firstVisibleItemPosition = findFirstVisibleItemPosition();
        if (firstVisibleItemPosition != 0) {
          // this avoids trying to handle un-needed calls
          if (firstVisibleItemPosition == -1) {
            //not initialized, or no items shown, so hide fast-scroller
            mFastscroller.setVisibility(View.GONE);
          }
          return;
        }
        final int lastVisibleItemPosition = findLastVisibleItemPosition();
        int itemsShown = lastVisibleItemPosition - firstVisibleItemPosition + 1;
        //if all items are shown, hide the fast-scroller
        mFastscroller.setVisibility(adapter.getItemCount() > itemsShown ? View.VISIBLE : View.GONE);
      }
    });

    mFastscroller.setRecyclerView(mRecyclerViewContacts);
    mFastscroller.setViewsToUse(R.layout.fast_scroller_layout, R.id.fastscroller_bubble,
        R.id.fastscroller_handle);

    mRecyclerViewContacts.setAdapter(adapter);

    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_search, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_search) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
