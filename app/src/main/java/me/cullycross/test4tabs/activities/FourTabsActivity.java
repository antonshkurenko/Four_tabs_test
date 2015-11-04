package me.cullycross.test4tabs.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import me.cullycross.test4tabs.R;
import me.cullycross.test4tabs.fragments.ContactsFragment;
import me.cullycross.test4tabs.fragments.DatabaseFragment;
import me.cullycross.test4tabs.fragments.PicturesFragment;
import me.cullycross.test4tabs.fragments.SinglePictureFragment;

public class FourTabsActivity extends AppCompatActivity {

  private SectionsPagerAdapter mSectionsPagerAdapter;


  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.container) ViewPager mViewPager;
  @Bind(R.id.tabs) TabLayout mTabLayout;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_four_tabs);
    ButterKnife.bind(this);

    setSupportActionBar(mToolbar);

    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    mViewPager.setAdapter(mSectionsPagerAdapter);
    mTabLayout.setupWithViewPager(mViewPager);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show();
      }
    });
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
        case 0:
          return ContactsFragment.newInstance();
        case 1:
          return DatabaseFragment.newInstance();
        case 2:
          return PicturesFragment.newInstance();
        case 3:
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
