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
import me.cullycross.test4tabs.activities.FourTabsActivity;
import me.cullycross.test4tabs.adapters.EntityCursorAdapter;
import me.cullycross.test4tabs.content.EntityContentProvider;
import me.cullycross.test4tabs.pojos.SomeEntity;

public class DatabaseFragment extends Fragment
    implements LoaderManager.LoaderCallbacks<Cursor>, FourTabsActivity.FabClickListener,
    FourTabsActivity.RowUpdateListener {

  public static final int FLAG_START = -2;

  @Bind(R.id.recycler_view_entities) RecyclerView mRecyclerViewEntities;

  private ProgressDialog mDialog;
  private EntityCursorAdapter mAdapter;

  private int mPositionToScroll = FLAG_START;
      // save here position to scroll after loader is restarted

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

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    getLoaderManager().initLoader(0, null, this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_database, container, false);
    ButterKnife.bind(this, view);

    mAdapter = new EntityCursorAdapter(this, null);
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

    if (mPositionToScroll != FLAG_START) {
      if (mPositionToScroll == SomeEntity.FLAG_NEW_ENTITY) {
        mRecyclerViewEntities.smoothScrollToPosition(mAdapter.getItemCount() - 1);
      } else {
        mRecyclerViewEntities.smoothScrollToPosition(mPositionToScroll);
      }
    }
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
    mAdapter.changeCursor(null);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void onFabClick() {

    EntityDialogFragment.newInstance(SomeEntity.FLAG_NEW_ENTITY, null, null,
        SomeEntity.FLAG_NEW_ENTITY).show(getActivity().getSupportFragmentManager(), null);
  }

  @Override public void onRowUpdate(int position) {
    // bad decision, have to use notifyItemInserted/Changed (not implemented in this cursor adapter)
    // only swapCursor with stableIds
    restartLoader();
    mPositionToScroll = position;
  }

  public void setPositionToScroll(int positionToScroll) {
    mPositionToScroll = positionToScroll;
  }

  private void restartLoader() {
    final Loader<Object> loader = getLoaderManager().getLoader(0);
    if (loader != null && !loader.isReset()) {
      getLoaderManager().restartLoader(0, null, DatabaseFragment.this);
    } else {
      getLoaderManager().initLoader(0, null, DatabaseFragment.this);
    }
  }
}