package com.derek.ddoodleboard;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.derek.ddoodleboard.view.PaintView;

public class MainFragment extends Fragment {
	private PaintView mPaintView;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);

		mPaintView = (PaintView) rootView.findViewById(R.id.view_paint_view);

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_item_clear) {
			mPaintView.clear();
			return true;
		} else if (id == R.id.menu_item_save) {
			showprogress(true);
			Bitmap bitmap = mPaintView.getBitmap();

			ContentResolver cr = getActivity().getContentResolver();
			String filename = MediaStore.Images.Media.insertImage(cr, bitmap,
					"myPhoto", "this is a Photo");

			MediaScannerConnection.scanFile(getActivity(),
					new String[] { filename }, null,
					new MediaScannerConnection.OnScanCompletedListener() {
						public void onScanCompleted(String path, Uri uri) {
							showprogress(false);
							Toast.makeText(getActivity(), "保存成功！",
									Toast.LENGTH_SHORT).show();
						}
					});

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showprogress(boolean isshow) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("正在保存。。。");
		}
		if (isshow)
			progressDialog.show();
		else
			progressDialog.dismiss();

	}

}
