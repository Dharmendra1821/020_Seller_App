package standalone.eduqfix.qfixinfo.com.app.add_product.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
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

import java.io.ByteArrayOutputStream;


import standalone.eduqfix.qfixinfo.com.app.R;

import static android.content.Context.MODE_PRIVATE;

public class NewCameraFragment extends Fragment {

    public static View fragment;
    static ImageView imageView;
    Button save_image,advanced;
    LinearLayout image_layout;
    private int REQUEST_CAMERA = 0;
    ContentValues values;
    Uri imageUri;
    String imageurl;
    String productId;
    int position;
    String valueId;
    String temp;
    String imageString;
    String productSku;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.qfix_library, container, false);

        progressDialog = new ProgressDialog(getActivity());
        imageView = fragment.findViewById(R.id.surface_imageview);
        save_image = fragment.findViewById(R.id.save_camera_image);
        advanced  = fragment.findViewById(R.id.advanced_filter);
        image_layout = fragment.findViewById(R.id.new_imageview_layout);
        sharedPreferences = getActivity().getSharedPreferences("StandAlone", MODE_PRIVATE);

        productId = getArguments().getString("product_id");
        position  = getArguments().getInt("position");
        valueId   = getArguments().getString("value_id");
        productSku = getArguments().getString("product_sku");


        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAMERA);

        advanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), EditorDemoActivity.class);
//                intent.putExtra("product_id", productId);
//                intent.putExtra("position", position);
//                intent.putExtra("value_id", valueId);
//                intent.putExtra("product_sku", productSku);
//                intent.putExtra("product_page", "1");
//                startActivity(intent);
            }
        });

        save_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //sendImages(productSku);
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

                    changeBitmapContrastBrightness(thumbnail, 4, 50, 1.5f);

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

    private void onCaptureImageResult(Intent data) {
    //    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        image_layout.setVisibility(View.VISIBLE);
       //  scaleDown(thumbnail,200,true);
    }


    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        imageView.setImageBitmap(newBitmap);
        return newBitmap;

    }

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

        Bitmap newBitmap = getResizedBitmap(ret,600);
        int size = sizeOf(newBitmap);
        Log.d("size...",String.valueOf(size));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        imageView.setImageBitmap(newBitmap);

        byte[] imageBytes = bytes.toByteArray();
        imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);


        return ret;
    }


    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        sharedPreferences.edit().putString("uri", String.valueOf(path)).apply();
        return Uri.parse(path);
    }

  /*  public void sendImages(final String skuId) {

        sharedPreferences = getActivity().getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+ Constant.BASE_URL);
        MPOSServices mposServices = retrofit.create(MPOSServices.class);

        EntryModel entryModel = new EntryModel();
        Entry entry = new Entry();
        entry.setMedia_type("image");
        entry.setPosition(position);
        entry.setDisabled(false);
        entry.setLabel("Nmae");
        Content content = new Content();
        content.setBase64_encoded_data(imageString);
        //   content.setBase64_encoded_data("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");
        content.setType("image/jpeg");
        content.setName("newww");
        entry.setContent(content);
        entryModel.setEntry(entry);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Updating product image....");
        progressDialog.show();

        Gson gson = new Gson();
        String gsonData = gson.toJson(entryModel);
        Log.d("GSONData=====","GSONData====="+gsonData);

        Log.d("SKU....","Sku...."+skuId);
        final String authToken = "Bearer " + sharedPreferences.getString("adminToken", null);
        Log.d("Auth Token.....", authToken);
        mposServices.sendImages(skuId, entryModel, authToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    Log.d("Response.....", String.valueOf(response.code()));
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Product image updated successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), AddProductListActivity.class);
                        startActivity(intent);
                    } else if (response.code() == 400) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Bad Request", Toast.LENGTH_LONG).show();
                    } else if (response.code() == 401) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Unauthorised", Toast.LENGTH_LONG).show();
                    } else if (response.code() == 500) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Internal Server error", Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                        Log.d("failure...", String.valueOf(response.body()));
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("failure...", t.getMessage());
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            }//57433
        });
    }*/



}
