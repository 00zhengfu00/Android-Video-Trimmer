package com.iknow.android.features.select;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import com.iknow.android.models.VideoInfo;
import iknow.android.utils.callback.SimpleCallback;
import java.util.ArrayList;
import java.util.List;

/**
 * author : J.Chou
 * e-mail : who_know_me@163.com
 * time   : 2018/10/04 1:51 PM
 * version: 1.0
 * description:
 */
public class VideoCursorLoader implements LoaderManager.LoaderCallbacks<Cursor>, ILoader {

  private Context mContext;
  private SimpleCallback mSimpleCallback;

  @Override public void load(final Context context, final SimpleCallback listener) {
    mContext = context;
    mSimpleCallback = listener;
    ((FragmentActivity)context).getSupportLoaderManager().initLoader(1, null, this);
  }

  @NonNull @Override public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
    String columnBucketId = MediaStore.Video.Media.BUCKET_ID;
    String selection = columnBucketId + " is not null and " + MediaStore.Video.Media.MIME_TYPE + " in (?, ?, ?, ?) ) group by ( " + columnBucketId;
    return new CursorLoader(
        mContext,
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        null,
        null,
        null,
        MediaStore.Video.Media.DATE_MODIFIED + " desc"
    );
  }

  @Override public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
    List<VideoInfo> videos = new ArrayList<>();
    if (cursor != null) {
      while (cursor.moveToNext()) {
        VideoInfo videoInfo = new VideoInfo();
        if (cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)) != 0) {
          videoInfo.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)));
          videoInfo.setVideoPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
          videoInfo.setCreateTime(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)));
          videoInfo.setVideoName(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));
          videos.add(videoInfo);
        }
      }
      cursor.close();
    }
    if (mSimpleCallback != null) {
      mSimpleCallback.success(videos);
    }
  }

  @Override public void onLoaderReset(@NonNull Loader<Cursor> loader) {

  }
}
