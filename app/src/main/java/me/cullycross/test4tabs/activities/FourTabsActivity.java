package me.cullycross.test4tabs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnPageChange;
import me.cullycross.test4tabs.R;
import me.cullycross.test4tabs.fragments.ContactsFragment;
import me.cullycross.test4tabs.fragments.DatabaseFragment;
import me.cullycross.test4tabs.fragments.EmailDialogFragment;
import me.cullycross.test4tabs.fragments.PicturesFragment;
import me.cullycross.test4tabs.fragments.SinglePictureFragment;

public class FourTabsActivity extends AppCompatActivity
    implements EmailDialogFragment.OnFragmentInteractionListener {

  public static final int CONTACTS_POS = 0;
  public static final int DATABASE_POS = 1;
  public static final int PICTURES_POS = 2;
  public static final int SINGLE_PICTURE_POS = 3;

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.container) ViewPager mViewPager;
  @Bind(R.id.tabs) TabLayout mTabLayout;
  @Bind(R.id.fab) FloatingActionButton mFab;

  private SectionsPagerAdapter mSectionsPagerAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_four_tabs);
    ButterKnife.bind(this);

    setSupportActionBar(mToolbar);

    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    mViewPager.setAdapter(mSectionsPagerAdapter);
    mTabLayout.setupWithViewPager(mViewPager);

    mFab.setTag(false);
    mFab.hide();
  }

  @Override public void onSendEmail(String recipient, String subject, String body) {
    final Intent i = new Intent(Intent.ACTION_SEND);
    i.setType("message/rfc822");
    i.putExtra(Intent.EXTRA_EMAIL, new String[] { recipient });
    i.putExtra(Intent.EXTRA_SUBJECT, subject);
    i.putExtra(Intent.EXTRA_TEXT, body);
    try {
      startActivity(Intent.createChooser(i, "Send mail..."));
    } catch (android.content.ActivityNotFoundException ex) {
      Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
    }
  }

  @OnPageChange(R.id.container) public void onPageSelected(int position) {
    switch (position) {
      case DATABASE_POS:
        mFab.setTag(true);
        mFab.setVisibility(View.INVISIBLE);
        mFab.setImageResource(R.drawable.ic_add);
        mFab.show();
        break;
      case PICTURES_POS:
        mFab.setTag(true);
        mFab.setVisibility(View.INVISIBLE);
        mFab.setImageResource(R.drawable.ic_update);
        mFab.show();
        break;
      default:
        mFab.setTag(false);
        mFab.hide();
        break;
    }
  }

  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public static final String CONTACTS = "Contacts";
    public static final String DATABASE = "Database";
    public static final String PICTURES = "Pictures";
    public static final String SINGLE_PICTURE = "Single picture";

    public SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {

      switch (position) {
        case CONTACTS_POS:
          return ContactsFragment.newInstance();
        case DATABASE_POS:
          return DatabaseFragment.newInstance();
        case PICTURES_POS:
          return PicturesFragment.newInstance();
        case SINGLE_PICTURE_POS:
          return SinglePictureFragment.newInstance();
        default:
          return null;
      }
    }

    @Override public int getCount() {
      return 4;
    }

    @Override public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return CONTACTS;
        case 1:
          return DATABASE;
        case 2:
          return PICTURES;
        case 3:
          return SINGLE_PICTURE;
        default:
          return null;
      }
    }
  }
}
