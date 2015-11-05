package me.cullycross.test4tabs.fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import java.util.concurrent.TimeUnit;
import me.cullycross.test4tabs.R;
import me.cullycross.test4tabs.adapters.ContactsCursorAdapter;
import me.cullycross.test4tabs.views.FastScrollerView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.BehaviorSubject;

public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

  @Bind(R.id.recycler_view_contacts) RecyclerView mRecyclerViewContacts;
  @Bind(R.id.fastscroller) FastScrollerView mFastscroller;

  private static final String SELECTION = ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
  private static final int MINIMAL_SEARCH_LENGTH = 0;

  private String mSearchString = "";
  private String[] mSelectionArgs = { mSearchString };

  private ContactsCursorAdapter mAdapter;
  private ProgressDialog mDialog;

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

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    getLoaderManager().initLoader(0, null, this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    final View view = inflater.inflate(R.layout.fragment_contacts, container, false);
    ButterKnife.bind(this, view);

    setHasOptionsMenu(true);

    mAdapter = new ContactsCursorAdapter(getContext(), null);

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
        mFastscroller.setVisibility(
            mAdapter.getItemCount() > itemsShown ? View.VISIBLE : View.GONE);
      }
    });

    mFastscroller.setRecyclerView(mRecyclerViewContacts);
    mFastscroller.setViewsToUse(R.layout.fast_scroller_layout, R.id.fastscroller_bubble,
        R.id.fastscroller_handle);

    mRecyclerViewContacts.setAdapter(mAdapter);

    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_search, menu);

    final MenuItem searchItem = menu.findItem(R.id.action_search);
    final SearchView searchView = (SearchView) searchItem.getActionView();
    getTextChangedObservable(searchView).debounce(750, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(string -> mSearchString = string)
        .subscribe(string -> restartLoader());
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {

    mDialog = ProgressDialog.show(getContext(), "Wait...", "Results are loading", true);

    mSelectionArgs[0] = "%" + mSearchString + "%";
    return new CursorLoader(getActivity(), ContactsContract.Contacts.CONTENT_URI, null, SELECTION,
        mSelectionArgs, "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    mAdapter.changeCursor(data);

    if (mDialog != null) {
      mDialog.dismiss();
    }
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
    mAdapter.changeCursor(null);
  }

  private Observable<String> getTextChangedObservable(SearchView searchView) {

    final BehaviorSubject<String> subject = BehaviorSubject.create();
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override public boolean onQueryTextSubmit(String query) {
        if (query.length() >= MINIMAL_SEARCH_LENGTH) {
          mSearchString = query;
          restartLoader();
          return true;
        }
        return false;
      }

      @Override public boolean onQueryTextChange(String newText) {
        if (newText.length() >= MINIMAL_SEARCH_LENGTH) {
          subject.onNext(newText);
          return true;
        }
        return false;
      }
    });
    return subject;
  }

  private void restartLoader() {
    final Loader<Object> loader = getLoaderManager().getLoader(0);
    if (loader != null && !loader.isReset()) {
      getLoaderManager().restartLoader(0, null, ContactsFragment.this);
    } else {
      getLoaderManager().initLoader(0, null, ContactsFragment.this);
    }
  }
}
