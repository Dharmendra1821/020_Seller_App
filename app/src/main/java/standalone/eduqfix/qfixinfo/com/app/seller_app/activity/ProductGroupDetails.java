package standalone.eduqfix.qfixinfo.com.app.seller_app.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.login.model.ProductCartResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.Bundle2;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.BundleProductAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.GroupProductAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.ImageZoomAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.ProductBundleAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.adapters.ProductGroupAdapter;
import standalone.eduqfix.qfixinfo.com.app.seller_app.container.BundleSizeContainer;
import standalone.eduqfix.qfixinfo.com.app.seller_app.interfaces.datachanged;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.BundleContainerModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.BundleProductOption;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CartItem;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CartRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerProductCartRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.MediaGalleryEntry;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewCartRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductBundle;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductCartNewRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductGroup;


import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductLink;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductLinkModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductSKU;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Request;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SKURequest;

import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SkuAttribute;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StockItem;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;


public class ProductGroupDetails extends AppCompatActivity implements datachanged, View.OnClickListener {

    RecyclerView recyclerView,recyclerView1,recyclerView2,recyclerView4;

    ImageZoomAdapter imageZoomAdapter;
    List<String> imageZooms;

    List<ProductBundle> BundleList;
    ProductBundleAdapter productBundleAdapter;
    List<String> id1;

    ProductGroupAdapter productGroupAdapter;

    List<ProductGroup> productGroupList;
    ProductGroup productGroup = new ProductGroup();


    List<ProductLink> productLinkList ;

    ArrayList<ProductLink> productLinkArrayList ;
    ArrayList<BundleProductOption> bundleProductOptions;
    ProductLink productLink;
    List<MediaGalleryEntry> mediaGalleryEntrieslist;
    StockItem stockItem;
    SkuAttribute skuAttribute;

    ProductSKU productSKU;
    SharedPreferences bundlesharedPreferences;

    ArrayList<BundleContainerModel> bundleContainerModels;

    List<BundleProductOption> bundleProductOptionList;
    ArrayList<ProductLink> productLinkBundleList;

    Bundle2 bundle2;
    Double Min;
    double price;
    ////////////////////
    GroupProductAdapter groupProductAdapter;
    BundleProductAdapter bundleProductAdapter;
    ArrayList<ProductLink> bundleproductLink ;
    public static ArrayList<String> skuId = new ArrayList<>();
    public static ArrayList<String> groupQunatity= new ArrayList<>();

    List<Integer> selectindex = new ArrayList<>();

    String Thumbnailimageurl;


    LinearLayout info,review,detail,Mainlinearlayout,containerLay;
    Button cartbutton,paybutton,Submit,Reset;
    Integer sum1 =0;
    EditText editText,yourname,youremail,yoursubject,yourquery;
    ScrollView scrollView;
    TextView detailtextView,customizetextview,goback,contactseller,fullprice,fromprice,toprice;
    PhotoView photoView;
    CardView dcard,icard,rcard;
    ImageView detailuparrow,detaildownarrow,infouparrow,infodownarrow,revuparrow,revdownarrow,imageView;
    TextView textCartItemCount,cartCountTextView;
    int mCartItemCount = 0;
    int CategoryId=0;
    int SubCategoryId=0;
    int CustomerId,CustomerAddressId,ProductCartId;
    Context context;
    LoginResponse customerDetails;
    SharedPreferences sharedPreferences;
    String cartCount;
    Menu menu;
    ProgressDialog progressDialog;
    Button addtocart,qpay,Customizecart;
    int sankalp,quantitychange;
    TextView productmainname,productskuid,fullname,reviewname , InSTock,textquantity,code,size;

    Integer quantity, quantityBundle;
    String Productimage=null;
    String thumbnailimage=null;
    String smallimage=null;
    int sizebundle =0;
    LinearLayout tableLayout;
    LinearLayout bundlesizeview;
    String first;
    String second;
    String third;
    String fourth;
    String fifth;
    List<String> listDataHeader;
    HashMap<String, List<BundleContainerModel>> listDataChild;
    int flag;
    ////////////////
    private final static String SHARED_PREFERENCES_FILE_USER_INFO_LIST = "bundle_product";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productdetailgroup);
        sankalp = 0;
        quantitychange=0;


        int mCartItemCount = 0;
        int CategoryId=0;
        int SubCategoryId=0;
        int CustomerId,CustomerAddressId,ProductCartId;
        TextView textCartItemCount,cartCountTextView;

        editText = findViewById(R.id.eqty);
        cartbutton=findViewById(R.id.addcart);
        paybutton=findViewById(R.id.Qpay);
        imageZooms = new ArrayList<>();
        info = findViewById(R.id.infolayout);
        review=findViewById(R.id.reviewlayout);
        detail=findViewById(R.id.detaillayout);
        Mainlinearlayout=findViewById(R.id.mainlinearlayout);
        recyclerView1=findViewById(R.id.recyclerview1);
        detailtextView=findViewById(R.id.detailtextview);
        customizetextview =findViewById(R.id.customize);
        scrollView = findViewById(R.id.Scroll);
        recyclerView2 = findViewById(R.id.recyclerview3);
        imageView=findViewById(R.id.Mainimage);
        goback = findViewById(R.id.goback);
        addtocart = findViewById(R.id.addcart);
        qpay = findViewById(R.id.Qpay);

        bundlesizeview = findViewById(R.id.bundle_size_view);
      //  containerLay = findViewById(R.id.container_id);
        flag=0;

        productskuid = findViewById(R.id.skuid);
        productmainname = findViewById(R.id.PNAME);
        fullname = findViewById(R.id.fullproductname);
        contactseller = findViewById(R.id.contactseller);
        reviewname =findViewById(R.id.Reviewname);
        fullprice = findViewById(R.id.Fullprice);
        fromprice = findViewById(R.id.fromprice);
        toprice = findViewById(R.id.toprice);
        InSTock = findViewById(R.id.Instock);
        textquantity = findViewById(R.id.textquantity);
        code = findViewById(R.id.code);
        size = findViewById(R.id.size);
        Customizecart=findViewById(R.id.Cart);
        recyclerView4=findViewById(R.id.recyclerview4);
        tableLayout = findViewById(R.id.ppq);


        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<BundleContainerModel>>();


        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductGroupDetails.this,ProductListActivity.class);
                startActivity(intent);
            }
        });




        qpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Intent intent2 = new Intent(ProductDetails.this,poynt.class);
                //  startActivity(intent2);
            }
        });


        recyclerView = findViewById(R.id.recyclerview2);
        BundleList = new ArrayList<>();
        productGroupList = new ArrayList<>();
        id1 = new ArrayList<>();
        imageZooms = new ArrayList<>();

        data();
        uparrow();
        Downarrow();
        infoup();
        infodown();
        revup();
        revdown();

        dcard =findViewById(R.id.detailcardview);
        icard=findViewById(R.id.infocardview);
        rcard = findViewById(R.id.reviewcardview);

        cartbutton.setOnClickListener(ProductGroupDetails.this);

        dcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                detailuparrow.setVisibility(View.VISIBLE);
                detaildownarrow.setVisibility(View.GONE);
                detail.setVisibility(View.VISIBLE);
                info.setVisibility(View.GONE);
                review.setVisibility(View.GONE);
                infodownarrow.setVisibility(View.VISIBLE);
                infouparrow.setVisibility(View.GONE);
                revdownarrow.setVisibility(View.VISIBLE);
                revuparrow.setVisibility(View.GONE);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0,dcard.getTop());
                    }

                }) ;

            }
        });


        icard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                infouparrow.setVisibility(View.VISIBLE);
                infodownarrow.setVisibility(View.GONE);
                info.setVisibility(View.VISIBLE);
                review.setVisibility(View.GONE);
                detail.setVisibility(View.GONE);
                detailuparrow.setVisibility(View.GONE);
                revuparrow.setVisibility(View.GONE);
                revdownarrow.setVisibility(View.VISIBLE);
                detaildownarrow.setVisibility(View.VISIBLE);

                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0,icard.getTop());
                    }

                }) ;

            }
        });

        rcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                revuparrow.setVisibility(View.VISIBLE);
                revdownarrow.setVisibility(View.GONE);
                review.setVisibility(View.VISIBLE);
                detail.setVisibility(View.GONE);
                info.setVisibility(View.GONE);
                infouparrow.setVisibility(View.GONE);
                detailuparrow.setVisibility(View.GONE);
                detaildownarrow.setVisibility(View.VISIBLE);
                infodownarrow.setVisibility(View.VISIBLE);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0,rcard.getTop());
                    }

                }) ;
            }
        });

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        ProductCartId= sharedPreferences.getInt("ProductCartId",0);
        String details = sharedPreferences.getString("CustomerDetails",null);
        cartCount = sharedPreferences.getString("cartCount",null);
        customerDetails = new Gson().fromJson(details, new TypeToken<LoginResponse>() {}.getType());
        if (getIntent().getExtras()!=null)
        {
            CategoryId=getIntent().getExtras().getInt("CategoryId");
            SubCategoryId=getIntent().getExtras().getInt("SubCategoryId");
            sharedPreferences.edit().putInt("SubCategoryId",SubCategoryId).apply();
            CustomerAddressId=getIntent().getExtras().getInt("CustomerAddressId");
            CustomerId=getIntent().getExtras().getInt("CustomerId");

            if(CustomerId == 0){
                CustomerId= customerDetails.getCustomerDetails().getId();
            }
        }
        else
        {
            CategoryId= sharedPreferences.getInt("CategoryId",0);
            SubCategoryId= sharedPreferences.getInt("SubCategoryId",0);
            CustomerId= sharedPreferences.getInt("CustomerId",0);
            CustomerAddressId= sharedPreferences.getInt("CustomerAddressId",0);
        }

        getCartItemCount(flag);

    }


    //Start of Group and Bundle data....

    void groupdata(final String adminToken,String customertoken) {

        final SharedPreferences sh;
        sh = getApplicationContext().getSharedPreferences("GROUPVALUE", MODE_PRIVATE);
        quantity = sh.getInt("QUANTITY", 0);
        String sku = sh.getString("SKU", null);
        Productimage = sh.getString("PRODUCTIMAGE", null);
        smallimage = sh.getString("SMALLIMAGE", null);


        final String Productimageurl = Constant.PRODUCT_MEDIA_URL + Productimage;
        final String Smallimageurl = Constant.PRODUCT_MEDIA_URL + smallimage;

        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.SKU_URL);
        MPOSServices services = retrofit.create(MPOSServices.class);
        final String authToken = "Bearer "+customertoken;
        Request request = new Request();
        SKURequest skuRequest = new SKURequest();


        request.setAdminToken(adminToken);
        request.setSku(sku);
        skuRequest.setRequest(request);

        Call<ProductSKU> call = services.productsku(skuRequest);
        call.enqueue(new Callback<ProductSKU>() {
            @Override
            public void onResponse(Call<ProductSKU> call, Response<ProductSKU> response) {
                if (response.code() == 200 || response.isSuccessful()) {
                    productSKU = response.body();

                    Gson gson = new Gson();
                    String productGson = gson.toJson(response.body());
                    Log.d("Product JSON","Product Json Uniform== "+productGson);

                    Mainlinearlayout.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    productLinkList = new ArrayList<ProductLink>(productSKU.getProductLinks());

                    //MEDIA GALLERY
                    mediaGalleryEntrieslist = new ArrayList<MediaGalleryEntry>(productSKU.getMediaGalleryEntries());
                    stockItem = productSKU.getExtensionAttributes().getStockItem();

                    if (mediaGalleryEntrieslist.size()!=0)
                    {
                        for (int media =0 ; media<mediaGalleryEntrieslist.size();media++)
                        {
                            thumbnailimage = mediaGalleryEntrieslist.get(media).getFile();
                            Thumbnailimageurl = Constant.PRODUCT_MEDIA_URL + thumbnailimage;
                        }}
                    else
                    {
                        thumbnailimage = null;
                        Thumbnailimageurl = Constant.PRODUCT_MEDIA_URL + thumbnailimage;
                    }


                    contactseller.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog dialog = new Dialog(ProductGroupDetails.this);
                            dialog.setContentView(R.layout.customdialog);
                            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            Submit = dialog.findViewById(R.id.SSubmit);
                            Reset = dialog.findViewById(R.id.SReset);
                            yourname = dialog.findViewById(R.id.YourName);
                            youremail = dialog.findViewById(R.id.YourEmail);
                            yoursubject = dialog.findViewById(R.id.Subject);
                            yourquery = dialog.findViewById(R.id.YourQuery);

                            Submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    Toast.makeText(ProductGroupDetails.this, "Request Submitted ", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Reset.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    yourname.getText().clear();
                                    yourquery.getText().clear();
                                    yoursubject.getText().clear();
                                    youremail.setText("");


                                }
                            });

                            dialog.show();


                        }
                    });
                    if (productSKU.getTypeId().equals("bundle") || productSKU.getTypeId().equals("Bundle"))
                    {


                        bundleProductOptionList = new ArrayList<BundleProductOption>(productSKU.getExtensionAttributes().getBundleProductOptions());
                        if (quantity >0) {
                            editText.setText(quantity.toString());
                            textquantity.setText(quantity.toString());
                            quantitychange=quantity;
                        }
                        else
                        {
                            editText.setText("1");
                            textquantity.setText("1");
                            quantitychange=1;
                        }
                        if (bundleProductOptionList.size()>1)
                        {
                            for (int i=0;i<bundleProductOptionList.size();i++)
                            {
                                if (bundleProductOptionList.get(i).getType().equals("checkbox") || bundleProductOptionList.get(i).getType().equals("radio") || bundleProductOptionList.get(i).getType().equals("multi"))
                                {
                                    Integer index = i;
                                    productLinkList=new ArrayList<ProductLink>(productSKU.getExtensionAttributes().getBundleProductOptions().get(i).getProductLinks());
                                    customizetextview.setText("CUSTOMIZE BUNDLE");
                                    recyclerView2.setVisibility(View.GONE);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(ProductGroupDetails.this));
                                    productBundleAdapter = new ProductBundleAdapter(ProductGroupDetails.this, bundleProductOptionList,mediaGalleryEntrieslist,productLinkList,index, ProductGroupDetails.this);
                                    recyclerView.setAdapter(productBundleAdapter);
                                    productBundleAdapter.notifyDataSetChanged();

                                }
                                else
                                {

                                    selectindex.add(i);
                                    productLinkList=new ArrayList<ProductLink>(productSKU.getExtensionAttributes().getBundleProductOptions().get(i).getProductLinks());
                                    customizetextview.setText("CUSTOMIZE BUNDLE");
                                    recyclerView2.setVisibility(View.GONE);
                                    recyclerView4.setLayoutManager(new LinearLayoutManager(ProductGroupDetails.this));
                                    //   recyclerView.setLayoutManager(new LinearLayoutManager(ProductDetails.this));
                                    bundle2 = new Bundle2(ProductGroupDetails.this, bundleProductOptionList,mediaGalleryEntrieslist, productLinkList,selectindex,ProductGroupDetails.this);
                                    recyclerView4.setAdapter(bundle2);
                                    bundle2.notifyDataSetChanged();

                                }


                            }
                        }
                        else {

                            customizetextview.setText("CUSTOMIZE BUNDLE");
                            recyclerView2.setVisibility(View.GONE);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ProductGroupDetails.this));
                            productBundleAdapter = new ProductBundleAdapter(ProductGroupDetails.this, bundleProductOptionList,mediaGalleryEntrieslist, null,0,ProductGroupDetails.this);
                            recyclerView.setAdapter(productBundleAdapter);
                            productBundleAdapter.notifyDataSetChanged();
                        }

                        if (thumbnailimage != null) {
                            Glide.with(ProductGroupDetails.this).load(Thumbnailimageurl).into(imageView);
                        } else {
                            imageView.setImageResource(R.drawable.no_product);
                        }

                        if (smallimage != null) {
                            imageZooms.add(Smallimageurl);
                        } else {
                            imageZooms.add(null);
                        }
                        if (Productimage != null) {
                            imageZooms.add(Productimageurl);
                        } else {
                            imageZooms.add(null);
                        }




                        //IMAGE ZOOM ADAPTER

                        recyclerView1 = findViewById(R.id.recyclerview1);
                        recyclerView1.addItemDecoration(new DividerItemDecoration(ProductGroupDetails.this, LinearLayoutManager.HORIZONTAL));
                        imageZoomAdapter = new ImageZoomAdapter(ProductGroupDetails.this, imageZooms);
                        LinearLayoutManager horizontal = new LinearLayoutManager(ProductGroupDetails.this, LinearLayoutManager.HORIZONTAL, false);
                        recyclerView1.setLayoutManager(horizontal);
                        recyclerView1.setAdapter(imageZoomAdapter);
                        imageZoomAdapter.notifyDataSetChanged();


                    }





                    //START OF GROUP PRODUCT


                    else {


                        // productnames.add(productLinkList.g);
                        //prices.add(productSKU.getPrice().toString());



                        if (quantity >0) {
                            editText.setText(quantity.toString());
                            textquantity.setText(quantity.toString());
                            quantitychange=quantity;
                        }
                        else
                        {
                            editText.setText("1");
                            textquantity.setText("1");
                            quantitychange=1;

                        }




                        customizetextview.setText("CUSTOMIZE GROUP");
                        recyclerView2.setLayoutManager(new LinearLayoutManager(ProductGroupDetails.this));
                        productGroupAdapter = new ProductGroupAdapter(ProductGroupDetails.this, productLinkList,mediaGalleryEntrieslist, ProductGroupDetails.this);
                        recyclerView2.setAdapter(productGroupAdapter);
                        productGroupAdapter.notifyDataSetChanged();

                        if (thumbnailimage != null) {
                            Glide.with(ProductGroupDetails.this).load(Thumbnailimageurl).into(imageView);
                        } else {
                            imageView.setImageResource(R.drawable.no_product);
                        }

                        if (smallimage != null) {
                            imageZooms.add(Smallimageurl);
                        } else {
                            imageZooms.add(null);
                        }
                        if (Productimage != null) {
                            imageZooms.add(Productimageurl);
                        } else {
                            imageZooms.add(null);
                        }


                        recyclerView1 = findViewById(R.id.recyclerview1);
                        recyclerView1.addItemDecoration(new DividerItemDecoration(ProductGroupDetails.this, LinearLayoutManager.HORIZONTAL));
                        imageZoomAdapter = new ImageZoomAdapter(ProductGroupDetails.this, imageZooms);
                        LinearLayoutManager horizontal = new LinearLayoutManager(ProductGroupDetails.this, LinearLayoutManager.HORIZONTAL, false);
                        recyclerView1.setLayoutManager(horizontal);
                        recyclerView1.setAdapter(imageZoomAdapter);
                        imageZoomAdapter.notifyDataSetChanged();

                    }

                    //END OF ELSE


                    //MAIN image


                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProductGroupDetails.this);
                            View view = getLayoutInflater().inflate(R.layout.zoomdialog, null);
                            photoView = view.findViewById(R.id.photo123);
                            Glide.with(ProductGroupDetails.this).load(Thumbnailimageurl).into(photoView);
                            builder.setView(view);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });



                    //START OF ADD TO CART BUTTON

                  /*  addtocart.setOnClickListener(new View.OnClickListener() {


                        @Override
                        public void onClick(View view) {

                            if (productSKU.getTypeId().equals("bundle") || productSKU.getTypeId().equals("Bundle"))
                            {
                                if (bundleProductOptionList.size()>1)
                                {
                                    if (bundleProductOptionList.size()==selectindex.size()) {
                                        bundle2.addcartitem(authToken, quantitychange);

                                    }
                                    else {
                                        productBundleAdapter.addcartitem(authToken,quantitychange);
                                        bundle2.addcartitem(authToken, quantitychange);

                                    }
                                }

                                else {
                                    productBundleAdapter.addcartitem(authToken, quantitychange);

                                }
                            }
                            else
                            {
                                productGroupAdapter.addcartitem(authToken,quantitychange);
                            }

                        }
                    });
*/


                    //END OF ADD TO CART BUTTON


                    //CUSTOMIZE CART BUTTON START

                    Customizecart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            if (productSKU.getTypeId().equals("bundle") || productSKU.getTypeId().equals("Bundle"))
                            {
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView4.setVisibility(View.VISIBLE);

                            }
                            else {
                                tableLayout.setVisibility(View.VISIBLE);
                                recyclerView2.setVisibility(View.VISIBLE);
                            }
                        }
                    });






                    editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                            Integer p = Integer.parseInt(String.valueOf(editText.getText()));
                            textquantity.setText(p.toString());

                            if (p>0)
                            {
                                quantitychange = p;
                                Double SPrice =quantitychange*Min;
                                fullprice.setText(SPrice.toString());
                            }



                            return false;
                        }
                    });

                    //LAYOUT VALUE FILL FOR GROUP AND BUNDLE PRODUCTS

                    productmainname.setText(productSKU.getName());
                    productskuid.setText(productSKU.getSku());
                    fullname.setText(" x "+productSKU.getName());
                    reviewname.setText(productSKU.getName());


                    Double Max= Double.parseDouble(productSKU.getMaxPrice());
                    Min= Double.parseDouble(productSKU.getMinPrice());

                    Double SPrice =quantitychange*Min;


                    fullprice.setText(SPrice.toString());

                    fromprice.setText(Max.toString());
                    toprice.setText(Min.toString());
                    detailtextView.setText(productSKU.getName());


                    Boolean itemcheck = stockItem.getIsInStock();
                    if (itemcheck.booleanValue()== Boolean.TRUE)
                    {
                        InSTock.setText("IN STOCK");
                    }
                    else {
                        InSTock.setText("OUT OF STOCK");
                        InSTock.setTextColor(Color.RED);
                    }
                } else if (response.code() == 400) {
                    progressDialog.dismiss();
                    Toast.makeText(ProductGroupDetails.this, "Bad Request", Toast.LENGTH_LONG).show();
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Toast.makeText(ProductGroupDetails.this, "Unauthorised", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    progressDialog.dismiss();
                    Toast.makeText(ProductGroupDetails.this, "Internal server error", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ProductGroupDetails.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            }



            @Override
            public void onFailure(Call<ProductSKU> call, Throwable t) {
                Toast.makeText(ProductGroupDetails.this,"ERROR",Toast.LENGTH_SHORT).show();
            }
        });


    }

    //end of group and bundle data....

    void groupdata1(final String adminToken,String customertoken) {

        progressDialog = new ProgressDialog(ProductGroupDetails.this);
        progressDialog.setMessage("Loading data.........");
        progressDialog.show();
        final SharedPreferences sh;
        sh = getApplicationContext().getSharedPreferences("GROUPVALUE", MODE_PRIVATE);
        quantity = sh.getInt("QUANTITY", 0);
        String sku = sh.getString("SKU", null);
        Productimage = sh.getString("PRODUCTIMAGE", null);
        smallimage = sh.getString("SMALLIMAGE", null);

        sharedPreferences = getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        final String Productimageurl = "https://"+shopUrl+Constant.PRODUCT_MEDIA_URL1 + Productimage;
        final String Smallimageurl = "https://"+shopUrl+Constant.PRODUCT_MEDIA_URL1 + smallimage;

        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.SKU_URL);
       // Retrofit retrofit = mposRetrofitClient.getClient(Constant.SKU_URL);
        MPOSServices services = retrofit.create(MPOSServices.class);
        final String authToken = "Bearer "+customertoken;
        Request request = new Request();
        SKURequest skuRequest = new SKURequest();
        request.setAdminToken(adminToken);
        request.setSku(sku);
        skuRequest.setRequest(request);

        Gson gson = new Gson();
        String productGson = gson.toJson(skuRequest);
        Log.d("Product JSON", "Product Json Uniform== " + productGson);

        Call<ProductSKU> call = services.productsku(skuRequest);
        call.enqueue(new Callback<ProductSKU>() {
            @Override
            public void onResponse(Call<ProductSKU> call, Response<ProductSKU> response) {
                if (response.code() == 200 || response.isSuccessful()) {
                    productSKU = response.body();

                    Gson gson = new Gson();
                    String productGson = gson.toJson(response.body());
                    Log.d("Product JSON", "Product Json Uniform== " + productGson);

                    Mainlinearlayout.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    productLinkArrayList = new ArrayList<ProductLink>(productSKU.getProductLinks());

                    if (productSKU.getTypeId().equals("bundle") || productSKU.getTypeId().equals("Bundle")) {
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_USER_INFO_LIST, MODE_PRIVATE);

                        // Put the json format string to SharedPreferences object.
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("bund_product", productGson);
                        editor.commit();

                        bundlesharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_USER_INFO_LIST, MODE_PRIVATE);
                        String userInfoListJsonString = bundlesharedPreferences.getString("bund_product", "");
                        Log.d("Product JSON", "Product Json Uniform== " + userInfoListJsonString);

                        try {
                            bundleContainerModels = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(userInfoListJsonString);
                            int attId = jsonObject.getInt("attribute_set_id");
                            Log.d("attId....", String.valueOf(attId));
                            JSONObject extentionAtt = jsonObject.getJSONObject("extension_attributes");
                            JSONArray bundleproductoption = extentionAtt.getJSONArray("bundle_product_options");
                            for (int i = 0; i < bundleproductoption.length(); i++) {
                                JSONObject jsonObject1 = bundleproductoption.getJSONObject(i);
                                int optionId = jsonObject1.getInt("option_id");
                                boolean required = jsonObject1.getBoolean("required");
                                String sku = jsonObject1.getString("sku");
                                String title = jsonObject1.getString("title");
                                String type = jsonObject1.getString("type");

                                listDataHeader.add(sku);

                                BundleContainerModel bundleContainerModel = new BundleContainerModel();
                                bundleContainerModel.setMainoptionId(optionId);
                                bundleContainerModel.setSku(sku);
                                bundleContainerModel.setTitle(title);
                                bundleContainerModel.setType(type);


                                JSONArray productLink = jsonObject1.getJSONArray("product_links");
                                List<ProductLinkModel> productLinkModelList = new ArrayList<>();
                                for (int j = 0; j < productLink.length(); j++) {
                                    JSONObject jsonObject2 = productLink.getJSONObject(j);
                                    String id = jsonObject2.optString("id");
                                    int option_Id = jsonObject2.optInt("option_id");
                                    //      String productImage        = jsonObject2.getString("productImage");
                                    String productName = jsonObject2.optString("productName");
                                    String productPrice = jsonObject2.optString("productPrice");
                                    // String productSmallImage   = jsonObject2.getString("productSmallImage");
                                    String sku1 = jsonObject2.optString("sku");
                                    int qty = jsonObject2.optInt("qty");

                                    ProductLinkModel productLinkModel = new ProductLinkModel();
                                    productLinkModel.setId(id);
                                    productLinkModel.setOption_Id(option_Id);
                                    productLinkModel.setProductName(productName);
                                    productLinkModel.setProductPrice(productPrice);
                                    productLinkModel.setProductSmallImage(productPrice);
                                    productLinkModel.setSku1(sku1);
                                    productLinkModel.setQty(qty);
                                    productLinkModelList.add(productLinkModel);
                                }
                                bundleContainerModel.setProductLinkModelList(productLinkModelList);


                                final BundleSizeContainer productSizeContainer = new BundleSizeContainer(ProductGroupDetails.this);
                                productSizeContainer.setupView(listDataHeader.get(i), bundleContainerModel);
                                bundlesizeview.addView(productSizeContainer);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    //MEDIA GALLERY
                    mediaGalleryEntrieslist = new ArrayList<MediaGalleryEntry>(productSKU.getMediaGalleryEntries());
                    stockItem = productSKU.getExtensionAttributes().getStockItem();

                    if (mediaGalleryEntrieslist.size() != 0) {
                        for (int media = 0; media < mediaGalleryEntrieslist.size(); media++) {
                            thumbnailimage = mediaGalleryEntrieslist.get(media).getFile();
                            sharedPreferences = getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
                            String shopUrl  = sharedPreferences.getString("shop_url",null);
                            Thumbnailimageurl = "https://"+shopUrl+Constant.PRODUCT_MEDIA_URL1 + thumbnailimage;
                        }
                    } else {
                        thumbnailimage = null;
                        sharedPreferences = getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
                        String shopUrl  = sharedPreferences.getString("shop_url",null);
                        Thumbnailimageurl = "https://"+shopUrl+Constant.PRODUCT_MEDIA_URL1 + thumbnailimage;
                    }


                    recyclerView2.setLayoutManager(new LinearLayoutManager(ProductGroupDetails.this));
                    groupProductAdapter = new GroupProductAdapter(ProductGroupDetails.this, productLinkArrayList);
                    recyclerView2.setAdapter(groupProductAdapter);
                    groupProductAdapter.notifyDataSetChanged();

                    if (thumbnailimage != null) {
                        Glide.with(ProductGroupDetails.this).load(Thumbnailimageurl).into(imageView);
                    } else {
                        imageView.setImageResource(R.drawable.no_product);
                    }

                    if (smallimage != null) {
                        imageZooms.add(Smallimageurl);
                    } else {
                        imageZooms.add(null);
                    }
                    if (Productimage != null) {
                        imageZooms.add(Productimageurl);
                    } else {
                        imageZooms.add(null);
                    }

                    recyclerView1 = findViewById(R.id.recyclerview1);
                    recyclerView1.addItemDecoration(new DividerItemDecoration(ProductGroupDetails.this, LinearLayoutManager.HORIZONTAL));
                    imageZoomAdapter = new ImageZoomAdapter(ProductGroupDetails.this, imageZooms);
                    LinearLayoutManager horizontal = new LinearLayoutManager(ProductGroupDetails.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView1.setLayoutManager(horizontal);
                    recyclerView1.setAdapter(imageZoomAdapter);
                    imageZoomAdapter.notifyDataSetChanged();


                    Customizecart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (productSKU.getTypeId().equals("bundle") || productSKU.getTypeId().equals("Bundle")) {
                                bundlesizeview.setVisibility(View.VISIBLE);
                                customizetextview.setText("CUSTOMIZE BUNDLE");
                              /*  recyclerView4.setVisibility(View.VISIBLE);
                                tableLayout.setVisibility(View.GONE);
                                recyclerView2.setVisibility(View.GONE);
                                recyclerView4.setLayoutManager(new LinearLayoutManager(ProductGroupDetails.this));
                                bundleProductAdapter = new BundleProductAdapter(ProductGroupDetails.this, bundleProductOptions,productLinkBundleList);
                                recyclerView4.setAdapter(bundleProductAdapter);
                                bundleProductAdapter.notifyDataSetChanged();*/


                            } else {
                                customizetextview.setText("CUSTOMIZE GROUP");
                                tableLayout.setVisibility(View.VISIBLE);
                                recyclerView2.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    productmainname.setText(productSKU.getName());
                    productskuid.setText(productSKU.getSku());
                    fullname.setText(" x " + productSKU.getName());
                    reviewname.setText(productSKU.getName());


                    Double Max = Double.parseDouble(productSKU.getMaxPrice());
                    Min = Double.parseDouble(productSKU.getMinPrice());

                    Double SPrice = quantitychange * Min;


                    fullprice.setText(SPrice.toString());

                    fromprice.setText(Max.toString());
                    toprice.setText(Min.toString());
                    detailtextView.setText(productSKU.getName());


                    Boolean itemcheck = stockItem.getIsInStock();
                    if (itemcheck.booleanValue() == Boolean.TRUE) {
                        InSTock.setText("IN STOCK");
                    } else {
                        InSTock.setText("OUT OF STOCK");
                        InSTock.setTextColor(Color.RED);
                    }

                    progressDialog.dismiss();


                } else if (response.code() == 400) {
                    progressDialog.dismiss();
                    Toast.makeText(ProductGroupDetails.this, "Bad Request", Toast.LENGTH_LONG).show();
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Toast.makeText(ProductGroupDetails.this, "Unauthorised", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    progressDialog.dismiss();
                    Toast.makeText(ProductGroupDetails.this, "Internal server error", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ProductGroupDetails.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ProductSKU> call, Throwable t) {
                Toast.makeText(ProductGroupDetails.this,"ERROR",Toast.LENGTH_SHORT).show();
            }
        });


    }



    void data() {
        String [] id1 =getResources().getStringArray(R.array.product_names);

        for (int j=0;j<id1.length;j++)
        {
            BundleList.add(new ProductBundle(id1[j]));

        }


    }

    void uparrow()
    {
        detailuparrow = findViewById(R.id.detailuparrow);
        detailuparrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                detaildownarrow.setVisibility(View.VISIBLE);
                detailuparrow.setVisibility(View.GONE);
                detail.setVisibility(View.GONE);

            }
        });
    }

    void Downarrow()
    {
        detaildownarrow =findViewById(R.id.detaildownarrow);
        detaildownarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                detailuparrow.setVisibility(View.VISIBLE);
                detaildownarrow.setVisibility(View.GONE);
                detail.setVisibility(View.VISIBLE);
                info.setVisibility(View.GONE);
                review.setVisibility(View.GONE);
                infodownarrow.setVisibility(View.VISIBLE);
                infouparrow.setVisibility(View.GONE);
                revdownarrow.setVisibility(View.VISIBLE);
                revuparrow.setVisibility(View.GONE);

                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0,dcard.getTop());
                    }

                }) ;
            }
        });
    }

    void infoup()
    {
        infouparrow=findViewById(R.id.infouparrow);
        infouparrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infodownarrow.setVisibility(View.VISIBLE);
                infouparrow.setVisibility(View.GONE);
                info.setVisibility(View.GONE);
            }
        });
    }

    void infodown()
    {
        infodownarrow=findViewById(R.id.infodownarrow);
        infodownarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                infouparrow.setVisibility(View.VISIBLE);
                infodownarrow.setVisibility(View.GONE);
                info.setVisibility(View.VISIBLE);
                review.setVisibility(View.GONE);
                detail.setVisibility(View.GONE);
                detailuparrow.setVisibility(View.GONE);
                revuparrow.setVisibility(View.GONE);
                revdownarrow.setVisibility(View.VISIBLE);
                detaildownarrow.setVisibility(View.VISIBLE);


                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0,icard.getTop());
                    }

                }) ;

            }
        });
    }

    void revup()
    {
        revuparrow=findViewById(R.id.reviewuparrow);
        revuparrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revuparrow.setVisibility(View.GONE);
                revdownarrow.setVisibility(View.VISIBLE);
                review.setVisibility(View.GONE);
            }
        });
    }
    void revdown()
    {
        revdownarrow=findViewById(R.id.reviewdownarrow);
        revdownarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                revuparrow.setVisibility(View.VISIBLE);
                revdownarrow.setVisibility(View.GONE);
                review.setVisibility(View.VISIBLE);
                detail.setVisibility(View.GONE);
                info.setVisibility(View.GONE);
                infouparrow.setVisibility(View.GONE);
                detailuparrow.setVisibility(View.GONE);
                detaildownarrow.setVisibility(View.VISIBLE);
                infodownarrow.setVisibility(View.VISIBLE);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0,rcard.getTop());
                    }

                }) ;
            }
        });
    }



    public void getCartItemCount(int flag){

        SharedPreferences mPOSharedPreferences = getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        String adminToken = mPOSharedPreferences.getString("adminToken",null);
        String custToken = mPOSharedPreferences.getString("customerToken",null);
        NewCartRequest customerProductCart = new NewCartRequest();
        if(flag==0) {
            groupdata1(adminToken, custToken);

        }
        CartRequest customerProductCartRequest = new CartRequest();

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL);
        MPOSServices services = retrofit.create(MPOSServices.class);
        String cartId = sharedPreferences.getString("ProductCartId",null);
        customerProductCartRequest.setUser_id(cartId);
       // customerProductCartRequest.setUserToken(custToken);
        customerProductCart.setRequest(customerProductCartRequest);
        services.getCustomerProductInCart(customerProductCart).enqueue(new Callback<List<ProductCartResponse>>() {
            @Override
            public void onResponse(Call<List<ProductCartResponse>> call, Response<List<ProductCartResponse>> response) {
                if(response.code() == 200 ||response.isSuccessful()){

                    cartCount = String.valueOf(response.body().size());
                    sharedPreferences.edit().putString("cartCount", String.valueOf(response.body().size())).apply();
                    invalidateOptionsMenu();
                }else if(response.code() == 400){

                    Toast.makeText(ProductGroupDetails.this,"Bad Request",Toast.LENGTH_LONG).show();
                }else if(response.code() == 401){

                    Toast.makeText(ProductGroupDetails.this,"Unauthorised",Toast.LENGTH_LONG).show();
                }else if(response.code() == 500){

                    Toast.makeText(ProductGroupDetails.this,"Internal server error",Toast.LENGTH_LONG).show();
                }else {

                    Toast.makeText(ProductGroupDetails.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductCartResponse>> call, Throwable t) {

                Toast.makeText(ProductGroupDetails.this,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        Log.d("group......", String.valueOf(GroupProductAdapter.getGroupQuantityValue()));
        Log.d("bundle......", String.valueOf(BundleSizeContainer.getBundleQuantityValue()));
        System.out.println(" All the key value pairs " + BundleSizeContainer.HashMap);
        Iterator myVeryOwnIterator = BundleSizeContainer.HashMap.keySet().iterator();
        Log.d("size...", String.valueOf(BundleSizeContainer.HashMap.size()));
        int bundlesize = BundleSizeContainer.HashMap.size();
        SharedPreferences mPOSharedPreferences = getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        mPOSharedPreferences.edit().putInt("bundle_size", bundlesize).apply();
        int i = 0;
        while(myVeryOwnIterator.hasNext()) {

            int key=(int)myVeryOwnIterator.next();
            String value=(String)BundleSizeContainer.HashMap.get(key);
            StringTokenizer tokens = new StringTokenizer((String) value, ":");
            first  = tokens.nextToken();
            second = tokens.nextToken();
            third  = tokens.nextToken();
            fourth = tokens.nextToken();
            fifth  = tokens.nextToken();

            i++;

            Log.d("i.....", String.valueOf(i));
            addToCart(Integer.parseInt(second),first,fifth,fourth,third,i,"bundle");

        }
        retrieveValuesQuantity(GroupProductAdapter.getGroupQuantityValue());
    }

    public void retrieveValuesQuantity(List list) {
        //Retrieving values from list
        int size = list.size();
        SharedPreferences mPOSharedPreferences = getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        mPOSharedPreferences.edit().putInt("size", size).apply();
        for (int i = 0; i < size; i++) {
            System.out.println(list.get(i));
            StringTokenizer tokens = new StringTokenizer((String) list.get(i), ":");
              first  =  tokens.nextToken();
              second =  tokens.nextToken();
              third  =  tokens.nextToken();
              fourth =  tokens.nextToken();
              fifth  =  tokens.nextToken();
              Log.d("i val...",String.valueOf(i));
              addToCart(Integer.parseInt(second),first,fifth,fourth,third,i,"group");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        this.menu = menu;
        MenuItem menuItem = menu.findItem(R.id.menu_item_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        MenuItem item = menu.findItem(R.id.menu_item_logout);
        item.setVisible(false);

        if(TextUtils.isEmpty(cartCount) || cartCount == null || cartCount.equals("0")){
            textCartItemCount.setVisibility(View.GONE);
        }else {
            textCartItemCount.setVisibility(View.VISIBLE);
            textCartItemCount.setText(cartCount);
        }

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer count = Integer.valueOf(!TextUtils.isEmpty(textCartItemCount.getText().toString()) ? textCartItemCount.getText().toString() : "0");
                if(count > 0){
                    Intent intent = new Intent(ProductGroupDetails.this, ViewCartActivity.class);
                    intent.putExtra("CustomerId",CustomerId);
                    intent.putExtra("CustomerAddressId",CustomerAddressId);
                    startActivity(intent);
                }else {
                    Toast.makeText(ProductGroupDetails.this,"No Products added to cart",Toast.LENGTH_LONG).show();
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i){
            case R.id.menu_item_cart:
                Intent intent = new Intent(ProductGroupDetails.this, ViewCartActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    public void addToCart(final int quantity,final String sku,final String price,final String productName,final String productType,final int size,final String type){

        progressDialog.show();
        progressDialog.setMessage("Adding product to cart");

        sharedPreferences = getSharedPreferences("StandAlone",MODE_PRIVATE);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.BASE_URL);
        SharedPreferences sharedPreferences = getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
        Integer cartId = sharedPreferences.getInt("ProductCartId",0);
        MPOSServices services = retrofit.create(MPOSServices.class);
        boolean IsAllExecuted=true;
        ProductCartResponse productCartResponse=new ProductCartResponse();
        ProductCartNewRequest productCartNewRequest = new ProductCartNewRequest();
        ProductCart productCart = new ProductCart();
        CartItem cItems = new CartItem();
        cItems.setQty(quantity);
        cItems.setSku(sku);
        cItems.setUser_id(String.valueOf(cartId));
        cItems.setPrice(Double.valueOf(price));
        cItems.setName(productName);
        cItems.setProductType(productType);
        productCart.setCart(cItems);
        productCartNewRequest.setRequest(productCart);

        Gson gson = new Gson();
        String productGson = gson.toJson(productCart);
        Log.d("Product JSON","Product Json == "+productGson);
        final String customerToken ="Bearer "+ sharedPreferences.getString("customerToken",null);

        services.addProductToCart(productCartNewRequest).enqueue(new Callback<ProductCartResponse>() {
            @Override
            public void onResponse(Call<ProductCartResponse> call, Response<ProductCartResponse> response) {
                if(response.code() == 200 ||response.isSuccessful()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("StandAlone",Context.MODE_PRIVATE);
                    Integer sizeId = sharedPreferences.getInt("size",0);
                    Integer bundlesizeId = sharedPreferences.getInt("bundle_size",0);
                    if(type.equalsIgnoreCase("group")) {
                        if (size == (sizeId - 1)) {
                            flag = 1;
                            Toast.makeText(ProductGroupDetails.this, "Product Added To Cart Successfully", Toast.LENGTH_LONG).show();
                            invalidateOptionsMenu();
                            getCartItemCount(flag);
                            progressDialog.dismiss();
                            Intent intent = new Intent(ProductGroupDetails.this,ProductListActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    if(type.equalsIgnoreCase("bundle")) {
                        if (size == bundlesizeId) {
                            flag = 1;
                            Toast.makeText(ProductGroupDetails.this, "Product Added To Cart Successfully", Toast.LENGTH_LONG).show();
                            invalidateOptionsMenu();
                            getCartItemCount(flag);
                            progressDialog.dismiss();
                            Intent intent = new Intent(ProductGroupDetails.this,ProductListActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }


                }else if(response.code() == 400){
                    progressDialog.dismiss();
                    Toast.makeText(ProductGroupDetails.this,"Bad Request Or Product that you are trying to add is not available",Toast.LENGTH_LONG).show();
                }else if(response.code() == 401){
                    progressDialog.dismiss();
                    Toast.makeText(ProductGroupDetails.this,"Unauthorised",Toast.LENGTH_LONG).show();
                }else if(response.code() == 500){
                    progressDialog.dismiss();
                    Toast.makeText(ProductGroupDetails.this,"Internal server error",Toast.LENGTH_LONG).show();
                }else{
                    progressDialog.dismiss();
                    //Incase to change the quantity value in edittext get super parent control
                         }
            }

            @Override
            public void onFailure(Call<ProductCartResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProductGroupDetails.this,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
