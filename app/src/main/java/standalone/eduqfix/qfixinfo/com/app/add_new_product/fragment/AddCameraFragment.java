package standalone.eduqfix.qfixinfo.com.app.add_new_product.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.DetectSwipeDirectionActivity;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;

import static android.content.Context.MODE_PRIVATE;

public class AddCameraFragment extends Fragment {
    public static View fragment;

    private int REQUEST_CAMERA = 0;
    ContentValues values;
    Uri imageUri;
    Button save_image,advanced;
    LinearLayout image_layout;
    ImageView imageView;
    SharedPreferences sharedPreferences;
    String imageurl;
    String imageString;
    int position;
    String temp;
    Uri fileUri;
    Bitmap saveBitMap;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.qfix_library, container, false);


        imageView = fragment.findViewById(R.id.surface_imageview);
        save_image = fragment.findViewById(R.id.save_camera_image);
        advanced  = fragment.findViewById(R.id.advanced_filter);
        image_layout = fragment.findViewById(R.id.new_imageview_layout);
        sharedPreferences = getActivity().getSharedPreferences("StandAlone", MODE_PRIVATE);

        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAMERA);

        position = getArguments().getInt("position");


        save_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent("camera-message");
                intent.putExtra("position",position);
                intent.putExtra("bitmap",saveBitMap);
                intent.putExtra("filePath",imageUri.getPath());
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                getActivity().finish();
            }
        });


        return fragment;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA)
                try {
                    //   Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                    image_layout.setVisibility(View.VISIBLE);
                    imageurl = getRealPathFromURI(imageUri);


                  saveBitMap =  changeBitmapContrastBrightness(thumbnail, 4, 50, 1.5f);

                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



  /*  public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        imageView.setImageBitmap(newBitmap);
        return newBitmap;

    }*/

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    protected int sizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else {
            return data.getByteCount();
        }
    }

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageState()),
                Constant.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TAG", "Oops! Failed create "
                        + Constant.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.ENGLISH.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    public Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness, float saturation) {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });
        cm.setSaturation(saturation);
        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        Canvas canvas = new Canvas(ret);
        Paint paint = new Paint();


        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2.0f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        // paint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.INNER));
        // paint.setShadowLayer(10.0f, 8.0f, 8.0f, Color.BLACK);
        canvas.drawBitmap(bmp, 0, 0, paint);


        getImageUri(getActivity(), ret);

        Bitmap newBitmap = getResizedBitmap(ret,700);
        int size = sizeOf(newBitmap);
        Log.d("size...",String.valueOf(size));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        imageView.setImageBitmap(newBitmap);
        BitMapToString(newBitmap);

        byte[] imageBytes = bytes.toByteArray();
        imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);



        return ret;//476//8830721465
    }


    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        sharedPreferences.edit().putString("uri", String.valueOf(path)).apply();
        return Uri.parse(path);
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        Log.d("bytess..", String.valueOf(b));
         temp = Base64.encodeToString(b, Base64.DEFAULT);




        /*if(position==1){
            sharedPreferences.edit().putString("bitmap1", String.valueOf(temp)).apply();
        }
        else if(position==2){
            sharedPreferences.edit().putString("bitmap2", String.valueOf(temp)).apply();
        }
        else {
            sharedPreferences.edit().putString("bitmap3", String.valueOf(temp)).apply();
        }*/

        return temp;
    }


}
