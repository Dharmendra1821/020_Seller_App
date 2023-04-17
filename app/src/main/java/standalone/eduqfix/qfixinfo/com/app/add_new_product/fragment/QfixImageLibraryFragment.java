package standalone.eduqfix.qfixinfo.com.app.add_new_product.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.SplashScreenActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.ComplaintsActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.EmailActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.MainDashboardActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.SellerDashboardActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.CategoryAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.ComplaintsAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.QfixImageLibraryAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.SubCategoryAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Categories;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ComplaintsModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.QfixLibraryModel;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static android.content.Context.MODE_PRIVATE;
import static standalone.eduqfix.qfixinfo.com.app.util.Constant.QFIX_LIBRARY_IMAGE;
import static standalone.eduqfix.qfixinfo.com.app.util.Constant.SKU_URL;

public class QfixImageLibraryFragment extends Fragment implements QfixImageLibraryAdapter.ClickListener {
    public static View fragment;

    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    int sellerId;
    QfixLibraryModel qfixLibraryModel;
    List<QfixLibraryModel> qfixLibraryModels;
    QfixImageLibraryAdapter qfixImageLibraryAdapter;
    EditText qfixEditSearch;
    public static ArrayList<QfixLibraryModel> array_sort;
    int textlength = 0;
    List<QfixLibraryModel> qfixSearchList = new ArrayList<>();
    int pos;
    Button searchBtn;
    String searchValue;
    int page;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.qfiximage_library, container, false);


        sharedPreferences = getActivity().getSharedPreferences("StandAlone",MODE_PRIVATE);
        progressDialog = new ProgressDialog(getActivity());
        recyclerView = fragment.findViewById(R.id.image_library_recyclerview);
       // qfixEditSearch = fragment.findViewById(R.id.qfixlibrary_search);
     //   searchBtn   = fragment.findViewById(R.id.qfix_library_search_btn);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        pos  = getArguments().getInt("position",0);
        page =1;
        getImageLibrary(page);
        array_sort = new ArrayList<>();

        qfixImageLibraryAdapter = new QfixImageLibraryAdapter();
        qfixImageLibraryAdapter.setOnItemClickListener(this);






        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            page++;
                            getImageLibrary1(page);
                            //   getProduct1(sellerId,page,searchProductName,searchProductSku);
                        }
                    }
                }
            }
        });

        return fragment;
    }

    public void getImageLibrary(final int page){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1 +"marketplace/imagemasterlist.php?searchCriteria=1&page="+page+"&row_per_page=30";

     //   String url = "https://surajshop.eduqfix.com/rest/V1/qfix-imagemasterlist/imagemasterlist/search?searchCriteria="+value;

        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading data.......");
        progressDialog.setCancelable(false);

        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response.....", response);
                try {
                    qfixLibraryModels = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray items = jsonObject.getJSONArray("items");
                    if(items.length() > 0){
                        for (int i = 0; i < items.length(); i++){
                            JSONObject jsonObject1 = items.getJSONObject(i);
                            String imageId = jsonObject1.getString("imagemasterlist_id");
                            String title = jsonObject1.getString("title");
                            String image = jsonObject1.getString("image");

                           qfixLibraryModels.add(new QfixLibraryModel(imageId,title,image));
                        }

                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
                        qfixImageLibraryAdapter = new QfixImageLibraryAdapter(getActivity(),qfixLibraryModels,"");
                        recyclerView.setAdapter(qfixImageLibraryAdapter);
                    }

                    progressDialog.dismiss();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Log.e("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();
            }
        }) {
            @Override
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept","application/json");
                params.put("Authorization","Bearer "+sharedPreferences.getString("adminToken",null));
                return params;
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;

            }
        };
        // Adding request to request queue

        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(getActivity()).addToRequestQueue(req, "tag");

    }

    public void getImageLibrary1(final int page){

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1 +"marketplace/imagemasterlist.php?searchCriteria=1&page="+page+"&row_per_page=30";

        //   String url = "https://surajshop.eduqfix.com/rest/V1/qfix-imagemasterlist/imagemasterlist/search?searchCriteria="+value;

        Log.e("Url...", "" + url);

        progressDialog.show();
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Loading data.......");
        progressDialog.setCancelable(false);

        StringRequest req = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response.....", response);
                try {
                    qfixLibraryModels = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray items = jsonObject.getJSONArray("items");
                    if(items.length() > 0){
                        for (int i = 0; i < items.length(); i++){
                            JSONObject jsonObject1 = items.getJSONObject(i);
                            String imageId = jsonObject1.getString("imagemasterlist_id");
                            String title = jsonObject1.getString("title");
                            String image = jsonObject1.getString("image");

                            qfixLibraryModels.add(new QfixLibraryModel(imageId,title,image));
                        }

//                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
//                        qfixImageLibraryAdapter = new QfixImageLibraryAdapter(getActivity(),qfixLibraryModels,"");
//                        recyclerView.setAdapter(qfixImageLibraryAdapter);
                        qfixImageLibraryAdapter.notifyDataSetChanged();
                        loading = true;
                    }

                    progressDialog.dismiss();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Log.e("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();
            }
        }) {
            @Override
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept","application/json");
                params.put("Authorization","Bearer "+sharedPreferences.getString("adminToken",null));
                return params;
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;

            }
        };
        // Adding request to request queue

        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(getActivity()).addToRequestQueue(req, "tag");

    }

    @Override
    public void onItemClick(View v, int position) {
        qfixLibraryModel = qfixImageLibraryAdapter.getWordAtPosition(position);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
      //  String url = "https://"+shopUrl + QFIX_LIBRARY_IMAGE+"qfix-imagemasterlist/imagemasterlist/search?searchCriteria="+value;
        String imageUrl = qfixLibraryModel.getImage();

        Intent intent = new Intent("qfix-message");
        intent.putExtra("position",pos);
        intent.putExtra("image",imageUrl);
        intent.putExtra("image_id",qfixLibraryModel.getImagemasterlist_id());
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        getActivity().finish();
    }
//
//    @Override
//    public void onClick(View v) {
//        getImageLibrary(searchValue);
//    }
}
