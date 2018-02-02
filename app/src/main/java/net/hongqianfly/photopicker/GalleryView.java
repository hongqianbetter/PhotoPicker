package net.hongqianfly.photopicker;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.ArrayList;

/**
 * Created by HongQian.Wang on 2018/2/1.
 */

public class GalleryView extends RecyclerView {
    private static final int LOADER_ID = 0X001;
    public static final int MIN_IMAGE_FILE_SIZE=10 * 1024;
    private Activity context;

    public GalleryAdapter getmAdapter() {
        return mAdapter;
    }

    private GalleryAdapter mAdapter = new GalleryAdapter();
    private LoaderManager.LoaderCallbacks mLoaderCallBack = new GalleryLoaderCallbacks();

    public GalleryView(Context context) {
        this(context, null);
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = (Activity) context;
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(context, 4));
        RecyclerViewDivider divider = new RecyclerViewDivider(DensityUtil.dp2px(context,3), Color.parseColor("#FFFFFF"));
        addItemDecoration(divider);
        setAdapter(mAdapter);

    }

    public void setUp(LoaderManager loaderManager,GalleryAdapter.OnItemClickListener onItemClickListener) {
        loaderManager.initLoader(LOADER_ID, null, mLoaderCallBack);
        mAdapter.setOnItemClickListener(onItemClickListener);
    }
    private void updateSource(ArrayList<Image> imagesList) {
           mAdapter.replaceAll(imagesList);
    }



    private class GalleryLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
        private String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA, //图片路径
                MediaStore.Images.Media.DATE_ADDED, //添加时间
                MediaStore.Images.Media.SIZE  //图片大小
        };

        @Override
        public Loader onCreateLoader(int id, Bundle bundle) {
            if (id == LOADER_ID) {
                return new CursorLoader(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        , IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[2] + " DESC");//倒序查询
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            ArrayList<Image> imagesList = new ArrayList<>();
            if (cursor != null) {
                int count = cursor.getCount();
                if (count > 0) {
                    cursor.moveToFirst();
                    int indexId = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int indexPath = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int indexDate = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                    int indexSize = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[3]);

                    do {
                        if(cursor.getLong(indexSize)<=MIN_IMAGE_FILE_SIZE) {
                            continue;
                        }
                        Image image = new Image(cursor.getInt(indexId), cursor.getString(indexPath), cursor.getLong(indexDate), cursor.getLong(indexSize));
                        imagesList.add(image);

                    } while (cursor.moveToNext());


                }
            }
            updateSource(imagesList);
        }


        @Override
        public void onLoaderReset(Loader loader) {
                updateSource(null); //当loader销毁或者重置了,进行界面清空
        }
    }


}
