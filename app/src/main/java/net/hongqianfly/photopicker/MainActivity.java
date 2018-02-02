package net.hongqianfly.photopicker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Random;

public class MainActivity extends Activity implements View.OnClickListener {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.btn);
        iv = findViewById(R.id.iv);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      new BottomShiftDialog(this).build().setOutsideTouchEnabled(false).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            Glide.with(this).load(resultUri).into(iv);
            Toast.makeText(MainActivity.this, resultUri.getPath(), Toast.LENGTH_SHORT).show();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Toast.makeText(MainActivity.this, cropError.getMessage(), Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_OK&&requestCode == BottomShiftDialog.TAKE_PICTURE) { // 如果返回数据
               File cameraFile = new File(Environment.getExternalStorageDirectory(), "camera/cameraPhoto.jpg");
               if(cameraFile.exists()) {
                   handlePhoto(cameraFile);
               }else {
                   Toast.makeText(MainActivity.this,"保存图片失败！", Toast.LENGTH_SHORT).show();
               }
        }
    }

    private void handlePhoto(File cameraFile) {
        UCrop.Options options = new UCrop.Options();
        //设置图片处理的格式JPEG
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        //设置压缩后的图片精度
        options.setCompressionQuality(96);
//得到头像的缓存地址

        UCrop uCrop = UCrop.of(Uri.fromFile(cameraFile),
                Uri.fromFile(getPortraitFile()))
                .withAspectRatio(1, 1) //1:1比例
                .withMaxResultSize(520, 520)
                .withOptions(options);
        Intent intent = uCrop.getIntent(MainActivity.this);
        intent.putExtra(UCrop.Options.EXTRA_HIDE_BOTTOM_CONTROLS, true);
        intent.putExtra(UCrop.Options.EXTRA_STATUS_BAR_COLOR, Color.parseColor("#dd807a7a"));
        intent.putExtra(UCrop.Options.EXTRA_TOOL_BAR_COLOR, Color.parseColor("#dd807a7a"));

        uCrop.start(MainActivity.this);

    }


    public File getPortraitFile(){
        Random random = new Random(5000);
        int randowInt = random.nextInt();
        File portraitFile = new File(getCacheDir(), "portrait");
        if(!portraitFile.exists()) {
            portraitFile.mkdirs();
        }
        File[] files = portraitFile.listFiles();
        for (File file:files){
            file.delete();
        }
        File file = new File(portraitFile, SystemClock.currentThreadTimeMillis()+"A"+randowInt+"portrait.jpg");
        return file;
    }
}
