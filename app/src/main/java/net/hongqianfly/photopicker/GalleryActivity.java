package net.hongqianfly.photopicker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Random;

public class GalleryActivity extends Activity implements View.OnClickListener {

    private GalleryView galleryView;
    private ImageButton ib_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        galleryView = findViewById(R.id.galleryView);
        ib_back = findViewById(R.id.ib_back);
        galleryView.setUp(getLoaderManager(), new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Image image, int position) {
                UCrop.Options options = new UCrop.Options();
                //设置图片处理的格式JPEG
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                //设置压缩后的图片精度
                options.setCompressionQuality(96);
//得到头像的缓存地址

                UCrop uCrop = UCrop.of(Uri.fromFile(new File(image.getPath())),
                        Uri.fromFile(getPortraitFile()))
                        .withAspectRatio(1, 1) //1:1比例
                        .withMaxResultSize(520, 520)
                        .withOptions(options);
                Intent intent = uCrop.getIntent(GalleryActivity.this);
                intent.putExtra(UCrop.Options.EXTRA_HIDE_BOTTOM_CONTROLS, true);
                intent.putExtra(UCrop.Options.EXTRA_STATUS_BAR_COLOR,Color.parseColor("#dd807a7a"));
                intent.putExtra(UCrop.Options.EXTRA_TOOL_BAR_COLOR, Color.parseColor("#dd807a7a"));

                uCrop.start(GalleryActivity.this);


            }
        });
        ib_back.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            setResult(RESULT_OK ,data); //将打开相册的得到的剪切回调回去
            finish();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Toast.makeText(GalleryActivity.this, cropError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
