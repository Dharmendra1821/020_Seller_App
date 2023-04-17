package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.SocketImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.AddDeliveryBoyActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.activities.DeliveryManagementActivity;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.DeliveryBoyList;
import standalone.eduqfix.qfixinfo.com.app.database.DBManager;
import standalone.eduqfix.qfixinfo.com.app.login.model.ProductCartResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.ProductDetails;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.ProductGroupDetails;
import standalone.eduqfix.qfixinfo.com.app.seller_app.activity.ProductListActivity;
import standalone.eduqfix.qfixinfo.com.app.seller_app.container.ConfigProductModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.container.ProductSizeContainer;
import standalone.eduqfix.qfixinfo.com.app.seller_app.container.ProductSizeModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.interfaces.IProductList;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CartItem;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerProductCartRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.DeleteCartResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Product;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductCartNewRequest;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;
import standalone.eduqfix.qfixinfo.com.app.util.MaintainRequestQueue;

import static android.content.Context.MODE_PRIVATE;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyProductList> implements Filterable {

    Context context;
    ArrayList<Product> productList;
    public static TextView cartTextView;
    List<Product> productsList = new ArrayList<>();
    TextView textView;
    ProgressDialog progressDialog;
    String ProductCartId,AutorizedToken;
    IProductList iProduct;
    SharedPreferences sharedPreferences;
    String cartCount;
    private ArrayList<Product> catList;
    private static ProductAdapter.ClickListener clickListener;
    private SparseBooleanArray itemStateArray= new SparseBooleanArray();
    int quantity ;
    ArrayList<String> variantValue;
    private ArrayList<ProductSizeModel> productSizeDBModels;
    private DBManager dbManager;
    public static java.util.HashMap<Integer,Integer> selectionSpinner=new HashMap<Integer, Integer>();
    Map<Integer, Integer> mSpinnerSelectedItem = new HashMap<Integer, Integer>();
    double price;
    public ProductAdapter(Context context){
        this.iProduct = (IProductList) context;

    }

    public ProductAdapter(){

    }
    public ProductAdapter(Context context, ArrayList<Product> productList,String productCartId,String autorizedToken,Activity activity){
        this.context = context;
        this.productList = productList;
        this.ProductCartId=productCartId;
        this.catList = productList;
        this.AutorizedToken=autorizedToken;
        this.iProduct = ((IProductList) activity);
        quantity =0;

        dbManager = new DBManager(context);
    }

    @NonNull
    @Override
    public MyProductList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_product_list_view,parent,false);

        return new MyProductList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyProductList holder, final int position) {
        final Product product = productList.get(position);
        final String productPrice = product.getProductPrice();
        String selectedQuantity = product.getSelectedQuantity();

        final String sku = product.getSku();

         sharedPreferences = context.getSharedPreferences("GROUPVALUE", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        variantValue = new ArrayList<>();
        productSizeDBModels = new ArrayList<>();

        dbManager.open();

        String type = product.getProductType();

        int productStock = product.getIsProductInStock();

        if (product.getProductType().equalsIgnoreCase("1") || product.getProductType().equalsIgnoreCase("2")
                || product.getProductType().equalsIgnoreCase("4")) {

                if(productStock==0){
                    holder.outofstockLayout.setVisibility(View.VISIBLE);
                    holder.incrementTextView.setEnabled(false);
                    holder.decrementTextView.setEnabled(false);
                    holder.productImageView.setVisibility(View.GONE);
                }
                else {
                    holder.outofstockLayout.setVisibility(View.GONE);
                    holder.incrementTextView.setEnabled(true);
                    holder.decrementTextView.setEnabled(true);
                    holder.productImageView.setVisibility(View.VISIBLE);
                }


                holder.variant_layout.setVisibility(View.GONE);
                holder.relativeLayout.setVisibility(View.VISIBLE);
                final double price = productPrice != null ? Math.round(Double.parseDouble(productPrice) * 100) / 100.00 : 0.0;
                if (price > 0) {
                    sharedPreferences = context.getSharedPreferences("StandAlone", Context.MODE_PRIVATE);
                    String shopUrl = sharedPreferences.getString("shop_url", null);
                    String imageUrl = product.getThumbnail();
                    if (product.getThumbnail() != null) {

                        Glide.with(context).load(imageUrl).into(holder.productImageView);
                    } else {
                        //need to add image
                        holder.productImageView.setImageResource(R.drawable.no_product);
                    }

                    holder.productNameTextView.setText(product.getProductName());
                    holder.productSubNameTextView.setText(TextUtils.isEmpty(product.getSku()) || product.getSku() == null ? "NA" : product.getSku());
                    holder.rupeeTextView.setText(String.valueOf(price));
                    //  holder.quantityEditText.setText(selectedQuantity);
                    //  holder.selectProductCheckBox.setChecked(product.getChecked());

                } else {
                    sharedPreferences = context.getSharedPreferences("StandAlone", Context.MODE_PRIVATE);
                    String shopUrl = sharedPreferences.getString("shop_url", null);
                    String imageUrl = "https://" + shopUrl + Constant.PRODUCT_MEDIA_URL1 + product.getThumbnail();
                    if (product.getThumbnail() != null) {
                        Glide.with(context).load(imageUrl).into(holder.productImageView);
                    } else {
                        //need to add image
                        holder.productImageView.setImageResource(R.drawable.no_product);
                    }

                    holder.productNameTextView.setText(product.getProductName());
                    holder.productSubNameTextView.setText(TextUtils.isEmpty(product.getSku()) || product.getSku() == null ? "NA" : product.getSku());
                    //  holder.rupeeTextView.setText(String.valueOf(price));
                    //    holder.quantityEditText.setText(selectedQuantity);
                    //    holder.selectProductCheckBox.setVisibility(View.GONE);
                    holder.rupeeTextView.setVisibility(View.GONE);
                    holder.rupeeImageview.setVisibility(View.GONE);
                    //     holder.relativeLayout.setVisibility(View.GONE);
                    //  holder.selectProductCheckBox.setChecked(product.getChecked());


                }

                holder.incrementTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         quantity = Integer.valueOf(holder.quantityEditText.getText().toString());
                         product.setProductPrice(String.valueOf(price * 1));
                         product.setSelectedQuantity(String.valueOf(1));
                         product.setProductName(product.getProductName());
                         product.setProductType(product.getProductType());
                        addCart(product,holder);

                    }
                });

                holder.decrementTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         quantity = Integer.valueOf(holder.quantityEditText.getText().toString());
                        if (quantity > 0) {

                            deleteItemCount(product,holder);
                        }

                    }
                });




        }

        if (mSpinnerSelectedItem.containsKey(position)) {
            holder.config_variant.setSelection(mSpinnerSelectedItem.get(position));
        }




        final ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(context, R.layout.spinner_text_layout1, variantValue);
        holder.config_variant.setAdapter(statusAdapter);


        holder.config_variant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                productSizeDBModels = dbManager.getProductOption(Integer.parseInt(product.getProductId()));
                for(ProductSizeModel productSizeModel : productSizeDBModels){
                    if(productSizeModel.getLabel().equalsIgnoreCase(adapterView.getSelectedItem().toString())){
                        price = productSizeModel.getPrice() != null ? Math.round(Double.parseDouble(productSizeModel.getPrice()) * 100) / 100.00 : 0.0;
                        holder.rupeeTextView.setText(String.valueOf(price));
                        selectionSpinner.put(position,i);
                        product.setSku(productSizeModel.getSku());
                        product.setProductPrice(String.valueOf(price));

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        holder.share_product.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clickListener.onItemClick(view, position,"share");
//            }
//        });

        dbManager.close();
    }

    public void addToCart(final Product product,final ProductAdapter.MyProductList holder){

        SharedPreferences sharedPreferences = context.getSharedPreferences("StandAlone", MODE_PRIVATE);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Adding product to cart...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
        MPOSServices services = retrofit.create(MPOSServices.class);

        Integer cartId = sharedPreferences.getInt("ProductCartId",0);
        boolean IsAllExecuted=true;

           ProductCartNewRequest productCartNewRequest = new ProductCartNewRequest();

            ProductCart productCart = new ProductCart();
            CartItem cItems = new CartItem();
            cItems.setQty(Integer.valueOf(product.getSelectedQuantity()));
            cItems.setSku(product.getSku());
            cItems.setUser_id(String.valueOf(cartId));
            cItems.setPrice(Double.valueOf(product.getProductPrice()));
            productCart.setCart(cItems);
            productCartNewRequest.setRequest(productCart);

            Gson gson = new Gson();
            String productGson = gson.toJson(productCartNewRequest);
            Log.d("Product JSON","Product Json == "+productGson);

          //  Log.d("Token value,...",AutorizedToken);

            services.addProductToCart(productCartNewRequest).enqueue(new Callback<ProductCartResponse>() {
                @Override
                public void onResponse(Call<ProductCartResponse> call, Response<ProductCartResponse> response) {
                    if(response.code() == 200) {
                        Toast.makeText(context,"Product Added To Cart Successfully",Toast.LENGTH_LONG).show();
                      //  productsList.add(product);
                        iProduct.productAddedOrRemoved();
                   //     product.setChecked(true);
                        quantity = quantity + 1;
                        holder.quantityEditText.setText(String.valueOf(quantity));
                        progressDialog.dismiss();
                    }else if(response.code() == 400){
                        progressDialog.dismiss();
                        Toast.makeText(context,"Product that you are trying to add is gone out of stock",Toast.LENGTH_LONG).show();
                    }else if(response.code() == 401){
                        progressDialog.dismiss();
                        Toast.makeText(context,"Unauthorised",Toast.LENGTH_LONG).show();
                    }
                    else if(response.code() == 404){
                        progressDialog.dismiss();
                        Toast.makeText(context,"Please try again....",Toast.LENGTH_LONG).show();
                    }
                    else if(response.code() == 500){
                        progressDialog.dismiss();
                        Toast.makeText(context,"Internal server error",Toast.LENGTH_LONG).show();
                    }else{
                        //Incase to change the quantity value in edittext get super parent control
                                        }
                }

                @Override
                public void onFailure(Call<ProductCartResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            });
    }

//    public void configaddToCart(final Product product){
//        SharedPreferences sharedPreferences = context.getSharedPreferences("StandAlone", MODE_PRIVATE);
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("Adding product to cart...");
//        progressDialog.show();
//        String shopUrl  = sharedPreferences.getString("shop_url",null);
//        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
//        Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.BASE_URL);
//        MPOSServices services = retrofit.create(MPOSServices.class);
//
//        Integer cartId = sharedPreferences.getInt("ProductCartId",0);
//        boolean IsAllExecuted=true;
//
//        ProductCart productCart = new ProductCart();
//        CartItem cItems = new CartItem();
//        cItems.setQty(Integer.valueOf(product.getSelectedQuantity()));
//        cItems.setSku(product.getSku());
//        cItems.setQuote_id(String.valueOf(cartId));
//        cItems.setPrice(Double.valueOf(product.getProductPrice()));
//        productCart.setCart(cItems);
//
//        Gson gson = new Gson();
//        String productGson = gson.toJson(productCart);
//        Log.d("Product JSON","Product Json == "+productGson);
//
//        Log.d("Token value,...",AutorizedToken);
//
//        services.addProductToCart(productCart,AutorizedToken).enqueue(new Callback<ProductCartResponse>() {
//            @Override
//            public void onResponse(Call<ProductCartResponse> call, Response<ProductCartResponse> response) {
//                if(response.code() == 200 ||response.isSuccessful()) {
//                    Toast.makeText(context,"Product Added To Cart Successfully",Toast.LENGTH_LONG).show();
//                    productsList.add(product);
//                    iProduct.productAddedOrRemoved();
//                    product.setChecked(true);
//                    progressDialog.dismiss();
//                }else if(response.code() == 400){
//                    progressDialog.dismiss();
//                    Toast.makeText(context,"Bad Request Or Product that you are trying to add is not available",Toast.LENGTH_LONG).show();
//                }else if(response.code() == 401){
//                    progressDialog.dismiss();
//                    Toast.makeText(context,"Unauthorised",Toast.LENGTH_LONG).show();
//                }else if(response.code() == 500){
//                    progressDialog.dismiss();
//                    Toast.makeText(context,"Internal server error",Toast.LENGTH_LONG).show();
//                }else{
//                    //Incase to change the quantity value in edittext get super parent control
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ProductCartResponse> call, Throwable t) {
//                progressDialog.hide();
//                Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
//            }
//        });
//    }
    public void UpdateShared()
    {
        Gson gson = new Gson();
        String selectedProducts = gson.toJson(productsList);
        SharedPreferences sharedPreferences = context.getSharedPreferences("StandAlone", MODE_PRIVATE);
        sharedPreferences.edit().putString("selectedProductList", selectedProducts).apply();
    }
    public void removeProductFromCart(Product product){
        product.setChecked(false);
        for(Product prod : productsList){
            if(prod.getProductId().equals(product.getProductId())){
                productsList.remove(product);
                break;
            }
        }
        if (cartTextView!=null)
        {
            cartTextView.setText(String.valueOf(productsList.size()));
        }
        UpdateShared();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void getConfigProduct() {


    }

    public class MyProductList extends RecyclerView.ViewHolder {
        ImageView productImageView,rupeeImageview;
        TextView productNameTextView,productSubNameTextView,rupeeTextView,decrementTextView,incrementTextView,linktextview;
        EditText quantityEditText;
        CheckBox selectProductCheckBox;
        RelativeLayout relativeLayout;
        Button addcart_btn;
        TextView variant_title;
        Spinner  config_variant;
        LinearLayout variant_layout;
        ImageView outofstockLayout;
        Button share_product;

        public MyProductList(View itemView) {
            super(itemView);

            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productSubNameTextView = itemView.findViewById(R.id.productsSubNameTextView);
            rupeeTextView = itemView.findViewById(R.id.rupeeTextView);
            decrementTextView = itemView.findViewById(R.id.decrementTextView);
            incrementTextView = itemView.findViewById(R.id.incrementTextView);
            quantityEditText = itemView.findViewById(R.id.quantityEditText);
          //  selectProductCheckBox = itemView.findViewById(R.id.selectProductCheckBox);
            linktextview = itemView.findViewById(R.id.textlink);
            rupeeImageview = itemView.findViewById(R.id.rupeeImageView);
            relativeLayout = itemView.findViewById(R.id.addtocart_layout);
            addcart_btn = itemView.findViewById(R.id.addcart_button);
            variant_title = itemView.findViewById(R.id.config_variant_title);
            config_variant = itemView.findViewById(R.id.config_variant);
            variant_layout = itemView.findViewById(R.id.variant_layout);
            outofstockLayout = itemView.findViewById(R.id.out_of_stock);
        //    share_product = itemView.findViewById(R.id.share_product);
        }
    }
//    public void getCartItemCount(){
//        sharedPreferences = context.getSharedPreferences("StandAlone", MODE_PRIVATE);
//        String adminToken = sharedPreferences.getString("adminToken",null);
//        String custToken = sharedPreferences.getString("customerToken",null);
//        CustomerProductCart customerProductCart = new CustomerProductCart();
//        CustomerProductCartRequest customerProductCartRequest = new CustomerProductCartRequest();
//
//        sharedPreferences = context.getSharedPreferences("StandAlone",MODE_PRIVATE);
//        String shopUrl  = sharedPreferences.getString("shop_url",null);
//        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
//        Retrofit retrofit = mposRetrofitClient.getClient("https://"+shopUrl+Constant.BASE_URL);
//        MPOSServices services = retrofit.create(MPOSServices.class);
//        String cartId = sharedPreferences.getString("ProductCartId",null);
//        customerProductCartRequest.setAdminToken(adminToken);
//        customerProductCartRequest.setUserToken(custToken);
//        customerProductCart.setRequest(customerProductCartRequest);
//        services.getCustomerProductInCart(customerProductCart).enqueue(new Callback<List<ProductCartResponse>>() {
//            @Override
//            public void onResponse(Call<List<ProductCartResponse>> call, Response<List<ProductCartResponse>> response) {
//                if(response.code() == 200 ||response.isSuccessful()){
//                    cartCount = String.valueOf(response.body().size());
//                    sharedPreferences.edit().putString("cartCount", String.valueOf(response.body().size())).apply();
//                    cartTextView.setText(cartCount);
//                    progressDialog.dismiss();
//                }else if(response.code() == 400){
//                    progressDialog.dismiss();
//                    Toast.makeText(context,"Bad Request",Toast.LENGTH_LONG).show();
//                }else if(response.code() == 401){
//                    progressDialog.dismiss();
//                    Toast.makeText(context,"Unauthorised",Toast.LENGTH_LONG).show();
//                }else if(response.code() == 500){
//                    progressDialog.dismiss();
//                    Toast.makeText(context,"Internal server error",Toast.LENGTH_LONG).show();
//                }else {
//                    progressDialog.dismiss();
//                    Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<ProductCartResponse>> call, Throwable t) {
//                progressDialog.dismiss();
//                Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
//            }
//        });
//    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productList = catList;
                } else {
                    ArrayList<Product> filteredList = new ArrayList<>();
                    for (Product row : catList) {
                        if (row.getProductName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    productList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productList = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public Product getWordAtPosition(int position) {
        return productList.get(position);
    }

    public void setOnItemClickListener(ProductAdapter.ClickListener clickListener) {
        ProductAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position,String flag);
    }



    public void deleteItemCount(final Product product,final ProductAdapter.MyProductList holder){
        sharedPreferences = context.getSharedPreferences("StandAlone",MODE_PRIVATE);
        String adminToken = sharedPreferences.getString("adminToken",null);
        String custToken = sharedPreferences.getString("customerToken",null);

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Deleting quantity of product....");
        progressDialog.show();
        progressDialog.setCancelable(false);

       String quantityVal = String.valueOf(quantity = quantity - 1);

        String shopUrl  = sharedPreferences.getString("shop_url",null);
        Integer cartId = sharedPreferences.getInt("ProductCartId",0);
        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
        Retrofit retrofit = mposRetrofitClient.getClient(Constant.REVAMP_URL1);
        MPOSServices services = retrofit.create(MPOSServices.class);

        CustomerProductCart customerProductCart = new CustomerProductCart();
        CustomerProductCartRequest customerProductCartRequest = new CustomerProductCartRequest();
        customerProductCartRequest.setQty_change(quantityVal);
        customerProductCartRequest.setQuote_id(String.valueOf(cartId));
        customerProductCartRequest.setSku(product.getSku());
        customerProductCart.setRequest(customerProductCartRequest);
        Gson gson = new Gson();
        final String cart = gson.toJson(customerProductCart);
        Log.d("Product JSON","Product Json Uniform== "+cart);
        services.deleteProductToCart(customerProductCart).enqueue(new Callback<DeleteCartResponse>() {
            @Override
            public void onResponse(Call<DeleteCartResponse> call, Response<DeleteCartResponse> response) {
                Log.d("url...", String.valueOf(call.request().url()));
                if(response.code() == 200 ||response.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(context,"Deleted product quantity successfully",Toast.LENGTH_LONG).show();
                    productsList.add(product);
                    iProduct.productAddedOrRemoved();
                    product.setChecked(true);
                    quantity = Integer.parseInt(quantityVal);
                    holder.quantityEditText.setText(String.valueOf(quantity));
                    progressDialog.dismiss();
                }else if(response.code() == 400){
                    progressDialog.dismiss();
                    Toast.makeText(context,"Bad Request",Toast.LENGTH_LONG).show();
                }else if(response.code() == 401){
                    progressDialog.dismiss();
                    Toast.makeText(context,"Unauthorised",Toast.LENGTH_LONG).show();
                }else if(response.code() == 500){
                    progressDialog.dismiss();
                    Toast.makeText(context,"Internal server error",Toast.LENGTH_LONG).show();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteCartResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }


    public void addCart(final Product product,final ProductAdapter.MyProductList holder){
        String shopUrl  = sharedPreferences.getString("shop_url",null);
        String url = Constant.REVAMP_URL1+"carts/mine_items.php";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("Url....", "" + url);


        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setTitle("Please wait....");
        progressDialog.setMessage("Adding in cart .......");
        Integer cartId = sharedPreferences.getInt("ProductCartId",0);
        ProductCartNewRequest productCartNewRequest = new ProductCartNewRequest();

        ProductCart productCart = new ProductCart();
        CartItem cItems = new CartItem();
        cItems.setQty(Integer.valueOf(product.getSelectedQuantity()));
        cItems.setSku(product.getSku());
        cItems.setUser_id(String.valueOf(cartId));
        cItems.setPrice(Double.valueOf(product.getProductPrice()));
        cItems.setType("cart");
        productCart.setCart(cItems);
        productCartNewRequest.setRequest(productCart);

        Gson gson = new Gson();
        String productGson = gson.toJson(productCartNewRequest);
        Log.d("Product JSON","Product Json == "+productGson);

        StringRequest req = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    Toast.makeText(context,"Product Added To Cart Successfully",Toast.LENGTH_LONG).show();
                    iProduct.productAddedOrRemoved();
                    quantity = quantity + 1;
                    holder.quantityEditText.setText(String.valueOf(quantity));
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Something went wrong, Please try again",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }

        }) {
            @Override
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    Log.e("Url...", "" + productGson);
                    return productGson == null ? null : productGson.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", productGson, "utf-8");
                    return null;
                }
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
        MaintainRequestQueue.getInstance(context).addToRequestQueue(req, "tag");



    }


}
