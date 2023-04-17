package standalone.eduqfix.qfixinfo.com.app.util;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ConfigDetails;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;

/**
 * Created by darshan on 27/2/19.
 */

public class MPOSApplication extends Application {
    ProgressDialog progressDialog = null;

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static MPOSApplication mInstance;
    SharedPreferences sharedPreferences;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
         sharedPreferences = getSharedPreferences("StandAlone", Context.MODE_PRIVATE);
       //  getAdminToken();
        //writeToFile("=================Logs==========");
    }

    public static synchronized MPOSApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }



    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void writeToFile(String content) {
        FileWriter writer = null;
        Boolean isFileCreated = false;
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/test.txt");
            if (!file.exists()) {
                try{
                    isFileCreated = file.createNewFile();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            writer = new FileWriter(file);
            if(isFileCreated){
                writer.append(content);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            try {
                if(writer != null){
                    if(isFileCreated){
                        writer.append(content);
                    }
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

//    public void getAdminToken(){
//        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
//        String shopUrl  = sharedPreferences.getString("shop_url",null);
//        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
//        Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.BASE_URL);
//        MPOSServices mposServices = retrofit.create(MPOSServices.class);
////qwe123@@
//        ConfigDetails configDetails = new ConfigDetails();
//        configDetails.setUsername("admin");
//        configDetails.setPassword("Suraj@123");
//        Gson gson = new Gson();
//        final String data = gson.toJson(configDetails);
//        Log.d("Request Data ===","Request Data ==="+data);
//
//        mposServices.getAdminToken(configDetails).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if(response.code() == 200 || response.isSuccessful()){
//                    //progressDialog.hide();
//                    SharedPreferences sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
//                    sharedPreferences.edit().putString("adminToken",response.body()).apply();
//                }else {
//                    //progressDialog.hide();
//                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                //progressDialog.hide();
//                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }
}
