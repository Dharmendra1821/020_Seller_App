package standalone.eduqfix.qfixinfo.com.app.seller_app.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.ProductCartResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.RequestTrialDetailsActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.RequestTrialListActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.ViewCartActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.QfixImageLibraryAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.RequestTrialAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.SMSContactListAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerProductCartRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.QfixLibraryModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SMSContactListData;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SMSContactModel;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static standalone.eduqfix.qfixinfo.com.app.util.Constant.BASE_URL;
import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SKU_URL;

public class SMSContactListFragment extends BottomSheetDialogFragment {

    private static SMSContactListFragment.ClickListener clickListener;
    private static SMSContactListFragment.closeClickListener closeClickListener;
    Button submit;
    ImageView close;
    SharedPreferences sharedPreferences;
    TextView resetFilter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    String result;
    ArrayList<SMSContactModel> smsContactModels;
    ArrayList<SMSContactListData> smsContactListData;
    SMSContactModel smsContactModel;
    SMSContactListAdapter smsContactListAdapter;
    LinearLayoutManager linearLayoutManager;
    EditText sms_search;
    public static ArrayList<SMSContactModel> array_sort;
    int textlength = 0;
    public static SMSContactListFragment newInstance() {
        return new SMSContactListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sms_contact_fragment, container, false);

        sharedPreferences = getActivity().getSharedPreferences("StandAlone", Context.MODE_PRIVATE);

        progressDialog = new ProgressDialog(getActivity());
        close = view.findViewById(R.id.close_sheet);
        recyclerView = view.findViewById(R.id.sms_contact_recylerview);
        sms_search = view.findViewById(R.id.sms_contact_search);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeClickListener.onCloseClick(view);
            }
        });

     //   new RequestTask().execute();
        array_sort = new ArrayList<>();
        sms_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                textlength = sms_search.getText().length();
                array_sort.clear();
                for (int j = 0; j < smsContactModels.size(); j++) {
                    if (textlength <= smsContactModels.get(j).getListname().length()) {
                        if (smsContactModels.get(j).getListname().toLowerCase().trim().contains(
                                sms_search.getText().toString().toLowerCase().trim())) {
                            array_sort.add(smsContactModels.get(j));
                            Log.d("data", String.valueOf(array_sort));
                        }
                    }
                }
                smsContactListAdapter = new SMSContactListAdapter(getActivity(),smsContactModels);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(smsContactListAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
            }
        });

        return view;

    }

    public void setOnItemClickListener(SMSContactListFragment.ClickListener clickListener) {
        SMSContactListFragment.clickListener = clickListener;
    }

    public void setOnCloseClickListener(SMSContactListFragment.closeClickListener clickListener) {
        SMSContactListFragment.closeClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, String fromDate, String toDate);
    }

    public interface closeClickListener {
        void onCloseClick(View v);
    }

//    public class RequestTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... strings) {
//            try {
//
//                sharedPreferences = getActivity().getSharedPreferences("StandAlone", Context.MODE_PRIVATE);
//
//
//                HttpClient client = new DefaultHttpClient();
//
//
//                String adminToken = "Bearer 9l26s8w944f2agffimnenh33iwnm955j" ;
//                String shopUrl = sharedPreferences.getString("shop_url", null);
//
//                String url = "https://"+shopUrl+ BASE_URL +"qfix-sendflyer/list";
//                HttpPost post = new HttpPost(url);
//                 post.setHeader("Authorization", adminToken);
//
//                Log.d("url.....", url);
//                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
//                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//                entityBuilder.addTextBody("seller_id", "204");
//
//                String boundary = "---------------" + UUID.randomUUID().toString();
//                entityBuilder.setBoundary(boundary);
//
//                HttpEntity entity = entityBuilder.build();
//                post.setEntity(entity);
//                client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
//                HttpResponse response = client.execute(post);
//                HttpEntity httpEntity = response.getEntity();
//                smsContactModels = new ArrayList<>();
//                smsContactListData = new ArrayList<>();
//                result = EntityUtils.toString(httpEntity);
//
//                Log.v("result", result);
//                StatusLine statusLine = response.getStatusLine();
//                int statusCode = statusLine.getStatusCode();
//                if (statusCode == 200) {
//                    JSONArray jsonArray = new JSONArray(result);
//                    if(jsonArray.length() > 0){
//                        for(int i =0; i < jsonArray.length(); i++){
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            String id = jsonObject.getString("list_id");
//                            String listName = jsonObject.getString("listname");
//
//                            JSONArray jsonArray1 = jsonObject.getJSONArray("listdata");
//
//                            if(jsonArray1.length() > 0) {
//                                for (int ii = 0; ii < jsonArray1.length(); ii++) {
//                                    JSONObject jsonObject2 = jsonArray1.getJSONObject(ii);
//
//                                    String smsId = jsonObject2.getString("id");
//                                    String smslistId = jsonObject2.getString("list_id");
//                                    String smsName = jsonObject2.getString("name");
//                                    String smsMobile = jsonObject2.getString("mobile");
//                                    String smsEmail = jsonObject2.getString("emailid");
//
//                                    smsContactListData.add(new SMSContactListData(smsId,smslistId,smsName,smsMobile,smsEmail));
//
//                                }
//                            }
//
//
//                            smsContactModels.add(new SMSContactModel(id,listName,smsContactListData));
//                        }
//
//
//
//                    }
//                    Handler handler =  new Handler(getActivity().getMainLooper());
//                    handler.post( new Runnable(){
//                        public void run(){
//                            smsContactListAdapter = new SMSContactListAdapter(getActivity(),smsContactModels,"");
//                            recyclerView.setLayoutManager(linearLayoutManager);
//                            recyclerView.setAdapter(smsContactListAdapter);
//                            progressDialog.dismiss();                            }
//                    });
//                } else {
//                    Handler handler =  new Handler(getActivity().getMainLooper());
//                    handler.post( new Runnable(){
//                        public void run(){
//                            Toast.makeText(getActivity(), "Images not uploaded, please try again!", Toast.LENGTH_LONG).show();
//                            progressDialog.dismiss();                          }
//                    });
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            try {
//                Handler handler = new Handler(getActivity().getMainLooper());
//                handler.post(new Runnable() {
//                    public void run() {
//                        progressDialog.setMessage("Saving data...");
//                        progressDialog.show();
//                    }
//                });
//            }catch (Exception e){
//
//            }
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            //Do anything with response..
//            //  progressDialog.dismiss();
//        }
//    }

}
