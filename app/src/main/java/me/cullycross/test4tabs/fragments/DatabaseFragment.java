package me.cullycross.test4tabs.fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import me.cullycross.test4tabs.R;
import me.cullycross.test4tabs.adapters.EntityCursorAdapter;
import me.cullycross.test4tabs.content.EntityContentProvider;

public class DatabaseFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

  @Bind(R.id.recycler_view_entities) RecyclerView mRecyclerViewEntities;

  private ProgressDialog mDialog;
  private EntityCursorAdapter mAdapter;

  public static DatabaseFragment newInstance() {
    final DatabaseFragment fragment = new DatabaseFragment();
    final Bundle args = new Bundle();

    // set args here

    fragment.setArguments(args);
    return fragment;
  }

  public DatabaseFragment() {
    // Required empty public constructor
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_database, container, false);
    ButterKnife.bind(this, view);

    mAdapter = new EntityCursorAdapter(getContext(), null);
    mRecyclerViewEntities.setLayoutManager(new LinearLayoutManager(getContext()));
    mRecyclerViewEntities.setAdapter(mAdapter);

    return view;
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    mDialog = ProgressDialog.show(getContext(), "Wait...", "Results are loading", true);

    return new CursorLoader(getActivity(), EntityContentProvider.ENTITY_CONTENT_URI, null, null,
        null, null);
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

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
