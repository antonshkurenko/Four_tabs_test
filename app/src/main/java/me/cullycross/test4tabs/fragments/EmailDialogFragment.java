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

public class EmailDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

  @Bind(R.id.email_subject) TextView mSubject;
  @Bind(R.id.email_body) TextView mBody;

  private static final String ARGS_EMAIL = "all_you_need_is_love";
  private static final String ARGS_NAME = "what_s_in_your_head";

  private OnFragmentInteractionListener mListener;

  public static EmailDialogFragment newInstance(String email, String name) {
    final EmailDialogFragment fragment = new EmailDialogFragment();
    final Bundle args = new Bundle();

    args.putString(ARGS_EMAIL, email);
    args.putString(ARGS_NAME, name);

    fragment.setArguments(args);
    return fragment;
  }

  public EmailDialogFragment() {
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

    final View dialogView = inflater.inflate(R.layout.fragment_email_dialog, null);

    ButterKnife.bind(this, dialogView);

    if(getArguments() != null) {
      mSubject.setText("Hello, " + getArguments().getString(ARGS_NAME));
      mBody.setText("Dear, " + getArguments().getString(ARGS_NAME));
    }

    builder.setView(dialogView).setPositiveButton("Send", this).setNegativeButton("Cancel", null);
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
          mListener.onSendEmail(getArguments().getString(ARGS_EMAIL), mSubject.getText().toString(),
              mBody.getText().toString());
        }
        break;
    }
  }

  public interface OnFragmentInteractionListener {
    void onSendEmail(String recipient, String subject, String body);
  }
}
