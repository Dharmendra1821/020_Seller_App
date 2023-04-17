package standalone.eduqfix.qfixinfo.com.app.add_product.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import standalone.eduqfix.qfixinfo.com.app.R;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class QfixlibraryFragment extends Fragment {
    public static View fragment;

    private static Camera mCamera;
    private CameraPreview mPreview;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.surfaceview_layout, container, false);

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


        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCamera != null)
        {
            mCamera.stopPreview();
            mCamera.release();
        }
    }
}
