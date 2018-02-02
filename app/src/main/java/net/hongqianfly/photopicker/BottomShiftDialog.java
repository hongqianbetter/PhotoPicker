package net.hongqianfly.photopicker;

/**
 * Created by HongQian.Wang on 2017/12/13.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.yalantis.ucrop.UCrop;

import java.io.File;

public class BottomShiftDialog implements View.OnClickListener {
    private Activity context;
    private Dialog dialog;
    private Display display;
    private Window window;

    public BottomShiftDialog(Activity context) {
        this.context = context;
        this.display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    public BottomShiftDialog build() {
        this.dialog = new Dialog(context, R.style.BottomSheetDialog);
        window = dialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        this.dialog.onWindowAttributesChanged(attributes);
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        View inflate = View.inflate(this.context, R.layout.dialog_view, null);
        inflate.setMinimumWidth(this.display.getWidth());
        inflate.setMinimumHeight((int) (DensityUtil.dp2px(context, 205)));
        Button btn_camera = inflate.findViewById(R.id.camera);
        Button btn_gallery = inflate.findViewById(R.id.gallery);
        Button btn_cancel = inflate.findViewById(R.id.cancel);
        btn_camera.setOnClickListener(this);
        btn_gallery.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        this.dialog.setContentView(inflate);
        return this;
    }

    public void dismiss() {
        this.dialog.dismiss();
    }


    public BottomShiftDialog setOutsideTouchEnabled(boolean paramBoolean) {
        this.dialog.setCanceledOnTouchOutside(paramBoolean);
        return this;
    }

    public void show() {
        this.dialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.gallery) {
            Intent intent = new Intent(context, GalleryActivity.class);
            context.startActivityForResult(intent, UCrop.REQUEST_CROP);
            dialog.dismiss();
        } else if (view.getId() == R.id.camera) {
            takePhoto(context);
            dialog.dismiss();
        } else if (view.getId() == R.id.cancel) {
            dialog.dismiss();
        }
    }
//public static final int GALLERY_START_CODE=0x133;
    public static final int TAKE_PICTURE=0x111;
    private void takePhoto(Activity context) {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile =getPhotoImagePhoto();
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        context.startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }


    public File getPhotoImagePhoto() {
        File cameraFile = new File(Environment.getExternalStorageDirectory(), "camera");
        if (!cameraFile.exists()) {
            cameraFile.mkdirs();
        }

        File[] files = cameraFile.listFiles();

        for (File file : files) {
            file.delete();
        }

        File file = new File(cameraFile, "cameraPhoto.jpg");

        return file;
    }
}
