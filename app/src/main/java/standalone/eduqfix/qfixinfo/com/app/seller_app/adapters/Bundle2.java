package standalone.eduqfix.qfixinfo.com.app.seller_app.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import standalone.eduqfix.qfixinfo.com.app.R;
import standalone.eduqfix.qfixinfo.com.app.login.model.ProductCartResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.interfaces.datachanged;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.BundleProductOption;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CartItem;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.MediaGalleryEntry;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductLink;
import standalone.eduqfix.qfixinfo.com.app.services.MPOSServices;
import standalone.eduqfix.qfixinfo.com.app.util.Constant;
import standalone.eduqfix.qfixinfo.com.app.util.MPOSRetrofitClient;

import static android.content.Context.MODE_PRIVATE;

public class Bundle2 extends RecyclerView.Adapter<Bundle2.ViewHolder>


{
    List<BundleProductOption> bundleProductOptionList;
    List<MediaGalleryEntry> mediaGalleryEntries;
    List<ProductLink> productLinkList ;


    private Context context1;
    List<String> Dropdown = new ArrayList<>();

    datachanged datachanged1;

    Integer n =1;
    RadioButton radioButton;
    String smallimage = null;
    String Productimage=null;
    String thumbnailimage = null;
    Integer count = 0,select=0,c=0;
    ArrayList<String> name = new ArrayList<>();

    Integer quantity;
    ArrayList<Integer> QTY = new ArrayList<>();
    ArrayList<Integer> Index = new ArrayList<>();

    ArrayList<String> namelist = new ArrayList<>();
    ArrayList<String> skulist = new ArrayList<>();
    ArrayList<String> pricelist = new ArrayList<>();

    String CartName;
    String CartSKU;
    String CartPrice;
  String typecheck ;

    public  int mSelectedItem = -1;

    List<RadioButton> allRadioButons = new ArrayList<>();

List<Integer> selectindex = new ArrayList<>();
Integer a=0;
Integer spinner=0;


    List<List<String>> lists = new ArrayList<List<String>>();
    List<String> dropdown = new ArrayList<>();
    List<String> dropdown1 = new ArrayList<String>();
    List<String> dropdown2 = new ArrayList<>();
    List<String> dropdown3 = new ArrayList<String>();




    public Bundle2(Context context,  List<BundleProductOption> bundleProductOptionList ,List<MediaGalleryEntry> mediaGalleryEntries,List<ProductLink> productLinkList,List<Integer> selectindex , datachanged datachanged)
    {


        this.datachanged1=datachanged;
        this.context1=context;

        this.mediaGalleryEntries=mediaGalleryEntries;

        this.bundleProductOptionList = bundleProductOptionList;

        this.productLinkList=productLinkList;
        this.selectindex=selectindex;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context1).inflate(R.layout.product_bundle,parent,false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final Bundle2.ViewHolder viewHolder, final int pos) {


        typecheck = "select";
        SharedPreferences sharedPreferences = context1.getSharedPreferences("GROUPVALUE", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("KEY", true);


        quantity = sharedPreferences.getInt("QUANTITY", 0);


        //QUANTITY CHECK

        if (quantity <= 0) {
            viewHolder.editTextqty.setText("1");
            quantity = 1;
            count++;
        } else {
            //viewHolder.editTextqty.setText(quantity.toString());
            viewHolder.editTextqty.setText("1");
            quantity = 1;
            count++;

        }
        //END OF QUANTITY CHECK


        viewHolder.editTextqty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int m, KeyEvent keyEvent) {

                Integer check = (Integer) Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText()));
                if (check == 0) {
                    count = 0;

                } else {

                    namelist.add( bundleProductOptionList.get(0).getProductLinks().get(pos).getProductName());
                    QTY.add(check);
                    Index.add(c, pos);
                    c++;
                    quantity = check;
                    count++;

                }
                return false;
            }
        });


        if (selectindex.size() > 1) {


            if (pos == 0) {
                n = 1;
                lists.add(dropdown3);

                lists.get(pos).add(0, "SELECT");
                viewHolder.spinner.setVisibility(View.VISIBLE);
                a = selectindex.get(pos);
                viewHolder.label.setText(bundleProductOptionList.get(a).getTitle());
                for (int k = 0; k < bundleProductOptionList.get(a).getProductLinks().size(); k++) {

                    lists.get(pos).add(n, bundleProductOptionList.get(a).getProductLinks().get(k).getProductName());
                    n++;

                    viewHolder.editTextqty.setText("1");

                }
                a = 0;


                ArrayAdapter arrayAdapter = new ArrayAdapter(context1, R.layout.support_simple_spinner_dropdown_item, lists.get(pos));
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                // viewHolder.spinner.setPrompt("SELECT");
                viewHolder.spinner.setAdapter(arrayAdapter);








                viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position >= 1) {


                            Integer Checkfor = Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText()));

                            if (Checkfor == 0) {
                                count = 0;
                                QTY.add(Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText())));
                            }
                            else {

                                for (int i = 0; i < selectindex.size(); i++) {
                                    a = selectindex.get(i);

                                    for (int k = 0; k < bundleProductOptionList.get(a).getProductLinks().size(); k++) {

                                        if (lists.get(pos).get(position).equals(bundleProductOptionList.get(a).getProductLinks().get(k).getProductName())) {
                                            Double price = Double.parseDouble(bundleProductOptionList.get(a).getProductLinks().get(k).getProductPrice());
                                            viewHolder.Price.setText(price.toString());
                                            namelist.add(bundleProductOptionList.get(a).getProductLinks().get(k).getProductName());
                                            skulist.add(bundleProductOptionList.get(a).getProductLinks().get(k).getSku());
                                            pricelist.add(bundleProductOptionList.get(a).getProductLinks().get(k).getProductPrice());
                                            QTY.add(Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText())));
                                            viewHolder.productvalue.setVisibility(View.VISIBLE);
                                            select++;

                                        } else {
                                            //
                                        }

                                    }
                                    a = 0;

                                }

                            }

                        } else {
                            select = 0;
                            viewHolder.productvalue.setVisibility(View.INVISIBLE);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        count = 0;
                    }
                });

            }



            if (pos == 1) {
                n = 1;
                lists.add(dropdown2);
                lists.get(pos).add(0, "SELECT");
                viewHolder.spinner2.setVisibility(View.VISIBLE);
                a = selectindex.get(pos);
                viewHolder.label.setText(bundleProductOptionList.get(a).getTitle());
                for (int k = 0; k < bundleProductOptionList.get(a).getProductLinks().size(); k++) {

                    lists.get(pos).add(n, bundleProductOptionList.get(a).getProductLinks().get(k).getProductName());
                    n++;

                    viewHolder.editTextqty.setText("1");

                }
                a = 0;


                ArrayAdapter arrayAdapter = new ArrayAdapter(context1, R.layout.support_simple_spinner_dropdown_item, lists.get(pos));
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                // viewHolder.spinner.setPrompt("SELECT");
                viewHolder.spinner2.setAdapter(arrayAdapter);







                viewHolder.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position >= 1) {

                            for (int i = 0; i < selectindex.size(); i++) {
                                a = selectindex.get(i);

                                for (int k = 0; k < bundleProductOptionList.get(a).getProductLinks().size(); k++) {

                                    if (lists.get(pos).get(position).equals(bundleProductOptionList.get(a).getProductLinks().get(k).getProductName())) {
                                        Double price = Double.parseDouble(bundleProductOptionList.get(a).getProductLinks().get(k).getProductPrice());
                                        viewHolder.Price.setText(price.toString());
                                        namelist.add( bundleProductOptionList.get(a).getProductLinks().get(k).getProductName());
                                        skulist.add(bundleProductOptionList.get(a).getProductLinks().get(k).getSku());
                                        pricelist.add(bundleProductOptionList.get(a).getProductLinks().get(k).getProductPrice());
                                        QTY.add(Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText())));
                                        viewHolder.productvalue.setVisibility(View.VISIBLE);
                                        select++;

                                    } else {
                                        //
                                    }

                                }
                                a = 0;

                            }


                        } else {
                            select = 0;
                            viewHolder.productvalue.setVisibility(View.INVISIBLE);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        count = 0;
                    }
                });

            }
            if (pos == 2) {
                n = 1;
                lists.add(dropdown1);
                lists.get(pos).add(0, "SELECT");
                viewHolder.spinner3.setVisibility(View.VISIBLE);
                a = selectindex.get(pos);
                viewHolder.label.setText(bundleProductOptionList.get(a).getTitle());
                for (int k = 0; k < bundleProductOptionList.get(a).getProductLinks().size(); k++) {

                    lists.get(pos).add(n, bundleProductOptionList.get(a).getProductLinks().get(k).getProductName());
                    n++;

                    viewHolder.editTextqty.setText("1");

                }
                a = 0;


                ArrayAdapter arrayAdapter = new ArrayAdapter(context1, R.layout.support_simple_spinner_dropdown_item, lists.get(pos));
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                // viewHolder.spinner.setPrompt("SELECT");
                viewHolder.spinner3.setAdapter(arrayAdapter);







                viewHolder.spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position >= 1) {

                            for (int i = 0; i < selectindex.size(); i++) {
                                a = selectindex.get(i);

                                for (int k = 0; k < bundleProductOptionList.get(a).getProductLinks().size(); k++) {

                                    if (lists.get(pos).get(position).equals(bundleProductOptionList.get(a).getProductLinks().get(k).getProductName())) {
                                        Double price = Double.parseDouble(bundleProductOptionList.get(a).getProductLinks().get(k).getProductPrice());
                                        viewHolder.Price.setText(price.toString());
                                        namelist.add( bundleProductOptionList.get(a).getProductLinks().get(k).getProductName());
                                        skulist.add(bundleProductOptionList.get(a).getProductLinks().get(k).getSku());
                                        pricelist.add(bundleProductOptionList.get(a).getProductLinks().get(k).getProductPrice());
                                        QTY.add(Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText())));
                                        viewHolder.productvalue.setVisibility(View.VISIBLE);
                                        select++;

                                    } else {
                                        //
                                    }

                                }
                                a = 0;

                            }


                        } else {
                            select = 0;
                            viewHolder.productvalue.setVisibility(View.INVISIBLE);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        count = 0;
                    }
                });

            }




            if (pos == 3) {
                n = 1;
                lists.add(dropdown);
                lists.get(pos).add(0, "SELECT");
                viewHolder.spinner4.setVisibility(View.VISIBLE);
                a = selectindex.get(pos);
                viewHolder.label.setText(bundleProductOptionList.get(a).getTitle());
                for (int k = 0; k < bundleProductOptionList.get(a).getProductLinks().size(); k++) {

                    lists.get(pos).add(n, bundleProductOptionList.get(a).getProductLinks().get(k).getProductName());
                    n++;

                    viewHolder.editTextqty.setText("1");

                }
                a = 0;


                ArrayAdapter arrayAdapter = new ArrayAdapter(context1, R.layout.support_simple_spinner_dropdown_item, lists.get(pos));
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                // viewHolder.spinner.setPrompt("SELECT");
                viewHolder.spinner4.setAdapter(arrayAdapter);
                viewHolder.spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position >= 1) {

                            for (int i = 0; i < selectindex.size(); i++) {
                                a = selectindex.get(i);

                                for (int k = 0; k < bundleProductOptionList.get(a).getProductLinks().size(); k++) {

                                    if (lists.get(pos).get(position).equals(bundleProductOptionList.get(a).getProductLinks().get(k).getProductName())) {
                                        Double price = Double.parseDouble(bundleProductOptionList.get(a).getProductLinks().get(k).getProductPrice());
                                        viewHolder.Price.setText(price.toString());
                                        namelist.add( bundleProductOptionList.get(a).getProductLinks().get(k).getProductName());
                                        skulist.add(bundleProductOptionList.get(a).getProductLinks().get(k).getSku());
                                        pricelist.add(bundleProductOptionList.get(a).getProductLinks().get(k).getProductPrice());
                                        QTY.add(Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText())));
                                        viewHolder.productvalue.setVisibility(View.VISIBLE);
                                        select++;

                                    } else {
                                        //
                                    }

                                }
                                a = 0;

                            }


                        } else {
                            select = 0;
                            viewHolder.productvalue.setVisibility(View.INVISIBLE);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        count = 0;
                    }
                });

            }




        }

        //SINGLE SPINNER
        else {


            Dropdown.add(0, "SELECT");
            n=1;
            viewHolder.spinner.setVisibility(View.VISIBLE);

            a = selectindex.get(pos);
            viewHolder.label.setText(bundleProductOptionList.get(a).getTitle());
            for (int k = 0; k < bundleProductOptionList.get(a).getProductLinks().size(); k++) {

                Dropdown.add(n, bundleProductOptionList.get(a).getProductLinks().get(k).getProductName());
                n++;

                viewHolder.editTextqty.setText("1");


            }
            a = 0;


            ArrayAdapter arrayAdapter = new ArrayAdapter(context1, R.layout.support_simple_spinner_dropdown_item, Dropdown);
            arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            // viewHolder.spinner.setPrompt("SELECT");
            viewHolder.spinner.setAdapter(arrayAdapter);


            viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position >= 1) {

                        for (int i = 0; i < selectindex.size(); i++) {
                            a = selectindex.get(i);

                            for (int k = 0; k < bundleProductOptionList.get(a).getProductLinks().size(); k++) {

                                if (Dropdown.get(position).equals(bundleProductOptionList.get(a).getProductLinks().get(k).getProductName())) {
                                    Double price = Double.parseDouble(bundleProductOptionList.get(a).getProductLinks().get(k).getProductPrice());
                                    viewHolder.Price.setText(price.toString());
                                    CartName = bundleProductOptionList.get(a).getProductLinks().get(k).getProductName();
                                    CartSKU = bundleProductOptionList.get(a).getProductLinks().get(k).getSku();
                                    CartPrice = bundleProductOptionList.get(a).getProductLinks().get(k).getProductPrice();
                                    quantity = (Integer) Integer.parseInt(String.valueOf(viewHolder.editTextqty.getText()));
                                    viewHolder.productvalue.setVisibility(View.VISIBLE);
                                    select++;

                                } else {
                                    //
                                }

                            }
                            a = 0;

                        }


                    } else {
                        select = 0;
                        viewHolder.productvalue.setVisibility(View.INVISIBLE);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    count = 0;
                }


            });

        }

    }






//    public void addcartitem(String authtoken,Integer listquantity) {
//
//
//        final ProgressDialog progressDialog = new ProgressDialog(context1);
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        MPOSRetrofitClient mposRetrofitClient = new MPOSRetrofitClient();
//        Retrofit retrofit = mposRetrofitClient.getClient(Constant.BASE_URL);
//        SharedPreferences sharedPreferences = context1.getSharedPreferences("MPOSShopping", MODE_PRIVATE);
//        Integer cartId = sharedPreferences.getInt("ProductCartId", 0);
//        MPOSServices services = retrofit.create(MPOSServices.class);
//        boolean IsAllExecuted = true;
//
//        ProductCart productCart = new ProductCart();
//        CartItem cItems = new CartItem();
//
//
//
//           if (selectindex.size()==1) {
//
//               if (count > 0) {
//                       if (select > 0) {
//                           for (int Q = 0; Q < listquantity; Q++) {
//
//                               cItems.setSku(CartSKU);
//                               cItems.setQty(quantity);
//                               cItems.setName(CartName);
//                               cItems.setPrice(Double.valueOf(CartPrice));
//                               cItems.setQuote_id(String.valueOf(cartId));
//                               cItems.setProductType(typecheck);
//                               productCart.setCartItem(cItems);
//
//                               Gson gson = new Gson();
//                               String productGson = gson.toJson(productCart);
//                               Log.d("Product JSON", "Product Json == " + productGson);
//
//                               services.addProductToCart(productCart, authtoken).enqueue(new Callback<ProductCartResponse>() {
//                                   @Override
//                                   public void onResponse(Call<ProductCartResponse> call, Response<ProductCartResponse> response) {
//                                       if (response.code() == 200 || response.isSuccessful()) {
//
//
//                                           progressDialog.dismiss();
//
//
//                                           Handler handler = new Handler();
//                                           handler.postDelayed(new Runnable() {
//                                               @Override
//                                               public void run() {
//                                                   Intent intent = ((Activity) context1).getIntent();
//                                                   ((Activity) context1).finish();
//                                                   context1.startActivity(intent);
//                                                   Toast.makeText(context1, "Product Added To Cart Successfully", Toast.LENGTH_LONG).show();
//
//                                               }
//                                           }, 80000);
//
//
//
//                                       } else if (response.code() == 400) {
//                                           progressDialog.dismiss();
//                                           Toast.makeText(context1, "Bad Request Or Product that you are trying to add is not available", Toast.LENGTH_LONG).show();
//                                       } else if (response.code() == 401) {
//                                           progressDialog.dismiss();
//                                           Toast.makeText(context1, "Unauthorised", Toast.LENGTH_LONG).show();
//                                       } else if (response.code() == 500) {
//                                           progressDialog.dismiss();
//                                           Toast.makeText(context1, "Internal server error", Toast.LENGTH_LONG).show();
//                                       } else {
//                                           progressDialog.dismiss();
//                                           Toast.makeText(context1, "Product that you are trying to add is not available.", Toast.LENGTH_LONG).show();
//                                       }
//                                       Intent intent = ((Activity) context1).getIntent();
//                                       ((Activity) context1).finish();
//
//                                       context1.startActivity(intent);
//                                   }
//
//                                   @Override
//                                   public void onFailure(Call<ProductCartResponse> call, Throwable t) {
//                                       progressDialog.dismiss();
//                                       Toast.makeText(context1, "Something went wrong", Toast.LENGTH_LONG).show();
//                                   }
//                               });
//
//
//                           }
//                       }
//
//
//
//
//
//               }
//
//
//           }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//           else {
//
//
//               if (count > 0) {
//
//                   if (typecheck.equals("select")) {
//
//                       if (select > 0) {
//
//                           if (namelist.size() == 0) {
//
//                               progressDialog.dismiss();
//
//                               Toast.makeText(context1, "SELECT ITEM  ", Toast.LENGTH_SHORT).show();
//
//
//                           } else {
//
//
//                               for (int a = 0; a < namelist.size(); a++) {
//
//
//                                   cItems.setQty(QTY.get(a));
//                                   cItems.setProductType(typecheck);
//
//                                   cItems.setQuote_id(String.valueOf(cartId));
//                                   cItems.setPrice(Double.valueOf(pricelist.get(a)));
//                                   cItems.setName(namelist.get(a));
//                                   cItems.setSku(skulist.get(a));
//                                   productCart.setCartItem(cItems);
//
//
//                                   Gson gson = new Gson();
//                                   String productGson = gson.toJson(productCart);
//                                   Log.d("Product JSON", "Product Json == " + productGson);
//
//                                   services.addProductToCart(productCart, authtoken).enqueue(new Callback<ProductCartResponse>() {
//                                       @Override
//                                       public void onResponse(Call<ProductCartResponse> call, Response<ProductCartResponse> response) {
//                                           if (response.code() == 200 || response.isSuccessful()) {
//
//
//                                               progressDialog.dismiss();
//
//                                               Toast.makeText(context1, "Product Added To Cart Successfully", Toast.LENGTH_SHORT).show();
//
//
//                                           } else if (response.code() == 400) {
//                                               progressDialog.dismiss();
//                                               Toast.makeText(context1, "Bad Request Or Product that you are trying to add is not available", Toast.LENGTH_LONG).show();
//                                           } else if (response.code() == 401) {
//                                               progressDialog.dismiss();
//                                               Toast.makeText(context1, "Unauthorised", Toast.LENGTH_LONG).show();
//                                           } else if (response.code() == 500) {
//                                               progressDialog.dismiss();
//                                               Toast.makeText(context1, "Internal server error", Toast.LENGTH_LONG).show();
//                                           } else {
//                                               progressDialog.dismiss();
//                                               Toast.makeText(context1, "Product that you are trying to add is not available.", Toast.LENGTH_LONG).show();
//                                           }
//                                           Intent intent = ((Activity) context1).getIntent();
//                                           ((Activity) context1).finish();
//
//                                           context1.startActivity(intent);
//                                       }
//
//                                       @Override
//                                       public void onFailure(Call<ProductCartResponse> call, Throwable t) {
//                                           progressDialog.dismiss();
//                                           Toast.makeText(context1, "Something went wrong", Toast.LENGTH_LONG).show();
//                                       }
//                                   });
//                               }
//                           }
//                       } else {
//                           progressDialog.dismiss();
//
//                           Toast.makeText(context1, " Select Something  ", Toast.LENGTH_SHORT).show();
//                       }
//
//
//                   } else {
//                       progressDialog.dismiss();
//                       Toast.makeText(context1, "Product type is Wrong ", Toast.LENGTH_SHORT).show();
//                   }
//               }
//
//
//               //ELSE OF COUNT CONDITION
//               else {
//                   progressDialog.dismiss();
//
//                   Toast.makeText(context1, "Quantity of Count Cannot Be  0 ", Toast.LENGTH_SHORT).show();
//
//
//               }
//
//
//           }
//
//        }
//



    @Override
    public int getItemCount() {

return selectindex.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder
    {
        Spinner spinner,spinner2,spinner3,spinner4;
        LinearLayout radioGroup,productvalue;
        CheckBox checkBox1;
        RelativeLayout relativeLayout;
        EditText editTextqty;
        TextView Price,label;

        ViewHolder(final View view)
        {
            super(view);


            editTextqty = view.findViewById(R.id.EDITQTY1);
            label=view.findViewById(R.id.productlabel);
            Price = view.findViewById(R.id.productprice);
            editTextqty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    String value = editTextqty.getText().toString();
                    //  datachanged1.quantity_bundle(value);
                    return false;
                }
            });



            spinner=view.findViewById(R.id.spinner1);
            spinner2=view.findViewById(R.id.spinner2);
            spinner3=view.findViewById(R.id.spinner3);
            spinner4 = view.findViewById(R.id.spinner4);
            radioGroup=view.findViewById(R.id.Radiogroup);
            radioButton= view. findViewById(R.id.radiobutton);
            checkBox1 =view.findViewById(R.id.COD);
            relativeLayout =view.findViewById(R.id.checkboxes);
            productvalue = view.findViewById(R.id.productvalue);



        }


    }
}




