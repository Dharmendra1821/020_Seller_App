package standalone.eduqfix.qfixinfo.com.app.add_new_product.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;



import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;

import static android.app.Activity.RESULT_OK;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;

public class AddGalleryFragment extends Fragment {
    public static View fragment;
    Button select_gallery;
    String productId,valueId;
    String productSku;
    int position;
    Button save_image,advanced;
    LinearLayout image_layout;
    ImageView imageView;
    BroadcastReceiver mMessageReceiver;
    String bitmap1;Bitmap bitmap;
    Uri fileUri;
    String pathFile;

    public static final int PICK_IMAGE_CODE = 200;
    public static final int DS_PHOTO_EDITOR_REQUEST_CODE = 300;

    private static final int REQUEST_EXTERNAL_STORAGE_CODE = 1000;

    public static final String OUTPUT_PHOTO_DIRECTORY = "ds_photo_editor_sample";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.gallery_fragment, container, false);


    //    select_gallery = fragment.findViewById(R.id.select_image_gallery);
        imageView = fragment.findViewById(R.id.surface_imageview);
        save_image = fragment.findViewById(R.id.save_camera_image);
     //   advanced  = fragment.findViewById(R.id.advanced_filter);
        image_layout = fragment.findViewById(R.id.new_imageview_layout);

        position  = getArguments().getInt("position",0);

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_CODE);




        save_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    fileUri = getOutputMediaFileUri();
                Intent intent = new Intent("camera-message");
                intent.putExtra("position",position);
                intent.putExtra("bitmap",bitmap);
                intent.putExtra("filePath",fileUri.getPath());
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                getActivity().finish();
            }
        });
        return fragment;
      }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mMessageReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                try {
//                     position = intent.getIntExtra("position",0);
//                     bitmap1 = intent.getStringExtra("bitmap");
//                     pathFile = intent.getStringExtra("filePath");
//
//                     bitmap = StringToBitMap(bitmap1);
//                    imageView.setImageBitmap(bitmap);
//                    image_layout.setVisibility(View.VISIBLE);
//                    Log.d("path....",pathFile);
//
//                }catch (Exception e){
//
//                }
//            }
//        };
//
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver , new IntentFilter("gallery-message"));
//    }


   /* @Override
    protected void onStop() {
        super.onStop();
        try {
            LocalBroadcastManager.getInstance(DetectSwipeDirectionActivity.this).unregisterReceiver(mMessageReceiver);
        }catch (Exception e){

        }
    }*/

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        try {
//            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
//        }catch (Exception e){
//
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE_CODE:
                    Uri inputImageUri = data.getData();
                    if (inputImageUri != null) {
                        Log.d("image...", String.valueOf(inputImageUri));
                        Intent dsPhotoEditorIntent = new Intent(getActivity(), DsPhotoEditorActivity.class);
                        dsPhotoEditorIntent.setData(inputImageUri);

                        // This is optional. By providing an output directory, the edited photo
                        // will be saved in the specified folder on your device's external storage;
                        // If this is omitted, the edited photo will be saved to a folder
                        // named "DS_Photo_Editor" by default.
                        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, OUTPUT_PHOTO_DIRECTORY);

                        // You can also hide some tools you don't need as below
                        int[] toolsToHide = {DsPhotoEditorActivity.TOOL_PIXELATE, DsPhotoEditorActivity.TOOL_ORIENTATION};
                        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);

                        startActivityForResult(dsPhotoEditorIntent, DS_PHOTO_EDITOR_REQUEST_CODE);
                    } else {
                        Toast.makeText(getActivity(), "Please select an image from the Gallery", Toast.LENGTH_LONG).show();
                    }
                    break;



                case DS_PHOTO_EDITOR_REQUEST_CODE:
                    fileUri = data.getData();
                    Log.d("file....", String.valueOf(fileUri));
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageURI(fileUri);
                    image_layout.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Photo saved in " + OUTPUT_PHOTO_DIRECTORY + " folder.", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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

    }
