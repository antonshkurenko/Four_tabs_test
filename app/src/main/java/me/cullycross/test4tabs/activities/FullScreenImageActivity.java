package me.cullycross.test4tabs.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileOutputStream;
import me.cullycross.test4tabs.R;
import uk.co.senab.photoview.PhotoViewAttacher;

public class FullScreenImageActivity extends AppCompatActivity {

  public static final String ARGS_PICTURE_PATH = "you_got_me_hypnotized";

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.image) ImageView mImage;

  private PhotoViewAttacher mAttacher;

  private ActionMode mActionMode;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_full_screen_image);
    ButterKnife.bind(this);

    mAttacher = new PhotoViewAttacher(mImage);

    Picasso.with(this)
        .load(getIntent().getStringExtra(ARGS_PICTURE_PATH))
        .placeholder(R.mipmap.ic_launcher)
        .into(mImage, new Callback() {
          @Override public void onSuccess() {
            mAttacher.update();
          }

          @Override public void onError() {
            //ignored
          }
        });

    setSupportActionBar(mToolbar);
    //noinspection ConstantConditions
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_edit, menu);

    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        break;
      case R.id.action_edit:
        mActionMode = startSupportActionMode(mCallback);
        break;
      default:
        return super.onOptionsItemSelected(item);
    }
    return true;
  }

  private final ActionMode.Callback mCallback = new ActionMode.Callback() {
    @Override public boolean onCreateActionMode(ActionMode mode, Menu menu) {
      mode.getMenuInflater().inflate(R.menu.menu_action_edit, menu);
      return true;
    }

    @Override public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
      return false;
    }

    @Override public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

      switch (item.getItemId()) {
        case R.id.action_save: {

          final Bitmap bitmap = ((BitmapDrawable) mImage.getDrawable()).getBitmap();
          final File file = new File(
              Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
              Integer.toString(bitmap.hashCode()) + ".png");
          try {
            FileOutputStream fos = null;
            try {
              fos = new FileOutputStream(file);
              bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
              MediaScannerConnection.scanFile(FullScreenImageActivity.this,
                  new String[] { file.getAbsolutePath() }, null, null);
              Toast.makeText(FullScreenImageActivity.this,
                  "Image was saved to the: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } finally {
              if (fos != null) fos.close();
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
          return true;
        }
        case R.id.action_delete: {

        }
      }

      return false;
    }

    @Override public void onDestroyActionMode(ActionMode mode) {
      mActionMode = null;
    }
  };
}
