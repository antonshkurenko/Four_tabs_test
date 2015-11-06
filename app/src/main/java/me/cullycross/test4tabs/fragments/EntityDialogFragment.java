package me.cullycross.test4tabs.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import me.cullycross.test4tabs.R;

public class EntityDialogFragment extends DialogFragment
    implements DialogInterface.OnClickListener {

  @Bind(R.id.title) TextView mTitle;
  @Bind(R.id.body) TextView mBody;

  private static final String ARGS_ID = "when_life_brings_trouble";
  private static final String ARGS_TITLE = "you_can_fight_or_run_away";
  private static final String ARGS_BODY =
      "stay_strong_through_the_struggle_that_s_what_life_is_about";

  private static final String ARGS_ROW = "oh_baby_it_s_a_wild_world";

  private OnFragmentInteractionListener mListener;

  public static EntityDialogFragment newInstance(int id, String title, String body, int row) {
    final EntityDialogFragment fragment = new EntityDialogFragment();
    final Bundle args = new Bundle();

    args.putInt(ARGS_ID, id);
    args.putString(ARGS_TITLE, title);
    args.putString(ARGS_BODY, body);

    args.putInt(ARGS_ROW, row);

    fragment.setArguments(args);
    return fragment;
  }

  public EntityDialogFragment() {
    // Required empty public constructor
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (OnFragmentInteractionListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(
          activity.toString() + " must implement OnFragmentInteractionListener");
    }
  }

  @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {

    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    final LayoutInflater inflater = getActivity().getLayoutInflater();

    final View dialogView = inflater.inflate(R.layout.fragment_title_body_dialog, null);

    ButterKnife.bind(this, dialogView);

    if (getArguments() != null) {
      mTitle.setText(getArguments().getString(ARGS_TITLE));
      mBody.setText(getArguments().getString(ARGS_BODY));
    }

    builder.setView(dialogView).setPositiveButton("Save", this).setNegativeButton("Cancel", null);
    return builder.create();
  }

  @Override public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override public void onClick(DialogInterface dialog, int which) {
    switch (which) {
      case DialogInterface.BUTTON_POSITIVE:
        if (getArguments() != null) {
          mListener.onSubmit(getArguments().getInt(ARGS_ID), mTitle.getText().toString(),
              mBody.getText().toString(), getArguments().getInt(ARGS_ROW));
        }
        break;
    }
  }

  public interface OnFragmentInteractionListener {
    void onSubmit(int id, String title, String body, int row);
  }
}
