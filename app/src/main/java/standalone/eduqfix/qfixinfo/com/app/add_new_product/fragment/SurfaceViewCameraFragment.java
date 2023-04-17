package standalone.eduqfix.qfixinfo.com.app.add_new_product.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_product.fragments.CameraPreview;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class SurfaceViewCameraFragment extends Fragment implements View.OnClickListener {

    public static View fragment;
    private static Camera mCamera;
    private CameraPreview mPreview;
    Button save,advanced;
    String temp;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    LinearLayout surface_layout1,surface_layout2;
    Bitmap rotatedBitmap,saveBitmap;
    int position;
    ProgressDialog progressDialog;
    Uri fileUri;
    public static final int DS_PHOTO_EDITOR_REQUEST_CODE = 200;

    private static final int REQUEST_EXTERNAL_STORAGE_CODE = 1000;

    public static final String OUTPUT_PHOTO_DIRECTORY = "ds_photo_editor_sample";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.surfaceview_layout, container, false);

        sharedPreferences = getActivity().getSharedPreferences("StandAlone", MODE_PRIVATE);
        progressDialog = new ProgressDialog(getActivity());


        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(getActivity(), mCamera);
        FrameLayout preview =  fragment.findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        mCamera.setDisplayOrientation(90);
        Button captureButton = fragment.findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera

                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );

        position = getArguments().getInt("position",0);


        save = fragment.findViewById(R.id.save_preview_image);
        advanced = fragment.findViewById(R.id.preview_iamge_advanced);
        imageView = fragment.findViewById(R.id.save_imageview_preview);
        surface_layout1 = fragment.findViewById(R.id.surface_layout);
        surface_layout2 = fragment.findViewById(R.id.surface_layout1);

        save.setOnClickListener(this);
        advanced.setOnClickListener(this);


        return fragment;

    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance

        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap bitmapPicture = BitmapFactory.decodeByteArray(data, 0, data.length);
            Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            if (display.getRotation() == Surface.ROTATION_0) {

                //rotate bitmap, because camera sensor usually in landscape mode
                 Matrix matrix = new Matrix();
                 matrix.postRotate(90);
                 rotatedBitmap = Bitmap.createBitmap(bitmapPicture, 0, 0, bitmapPicture.getWidth(), bitmapPicture.getHeight(), matrix, true);

            }

            File pictureFile = getOutputMediaFile();

            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fileUri = getOutputMediaFileUri();

                Log.d("uri.....",fileUri.getPath());
                fos.close();

            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }

            surface_layout1.setVisibility(View.GONE);
            surface_layout2.setVisibility(View.VISIBLE);
            saveBitmap = changeBitmapContrastBrightness(rotatedBitmap, 4, 50, 1.5f);

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();

            }
        }catch (Exception e){

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.save_preview_image:

             //   handlerMethod(saveBitmap);
                Intent intent = new Intent("camera-message");
                intent.putExtra("position",position);
                intent.putExtra("bitmap",saveBitmap);
                intent.putExtra("filePath",fileUri.getPath());
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                getActivity().finish();
                break;

            case R.id.preview_iamge_advanced:
                Uri inputImageUri1 = getImageUri(getActivity(),saveBitmap);
                if (inputImageUri1 != null) {
                    Intent dsPhotoEditorIntent = new Intent(getActivity(), DsPhotoEditorActivity.class);
                    dsPhotoEditorIntent.setData(inputImageUri1);

                    // This is optional. By providing an output directory, the edited photo
                    // will be saved in the specified folder on your device's external storage;
                    // If this is omitted, the edited photo will be saved to a folder
                    // named "DS_Photo_Editor" by default.
                    dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, OUTPUT_PHOTO_DIRECTORY);

                    // You can also hide some tools you don't need as below
                    int[] toolsToHide = {DsPhotoEditorActivity.TOOL_PIXELATE, DsPhotoEditorActivity.TOOL_ORIENTATION};
                    dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);

                    startActivityForResult(dsPhotoEditorIntent, DS_PHOTO_EDITOR_REQUEST_CODE);
                }
                break;
        }
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

        imageView.setImageBitmap(ret);

     //   BitMapToString(bmp);


        return ret;//476//8830721465
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String BitMapToString(Bitmap bitmap){
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            temp = Base64.encodeToString(b, Base64.DEFAULT);

            Intent intent = new Intent("camera-message");
            intent.putExtra("position",position);
            intent.putExtra("bitmap",temp);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            getActivity().finish();

        }catch (Exception e){

        }
        return temp;
    }

    public void handlerMethod(final Bitmap bitmap){
        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
               BitMapToString(bitmap);
            }
        };

        handler.postDelayed(r, 5000);
    }

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    /**
     * returning image / video
     */
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case DS_PHOTO_EDITOR_REQUEST_CODE:
                    fileUri = data.getData();
                    try {
                        saveBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageURI(fileUri);
                  //  Toast.makeText(getActivity(), "Photo saved in " + OUTPUT_PHOTO_DIRECTORY + " folder.", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
