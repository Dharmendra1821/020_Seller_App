package standalone.eduqfix.qfixinfo.com.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddNewProductModel;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.AddProductRequest;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MainRequest;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MyOrderListModel;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.MyOrderListModelNew;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.RefreshToken;
import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.Request;
import standalone.eduqfix.qfixinfo.com.app.customer.model.AddAddressRequest;
import standalone.eduqfix.qfixinfo.com.app.customer.model.AddAddressResponse;
import standalone.eduqfix.qfixinfo.com.app.customer.model.Customers;
import standalone.eduqfix.qfixinfo.com.app.customer.model.CustomerDetails;
import standalone.eduqfix.qfixinfo.com.app.customer.model.CustomerResponse;
import standalone.eduqfix.qfixinfo.com.app.customer.model.Data;
import standalone.eduqfix.qfixinfo.com.app.customer.model.SearchCustomerRequest;
import standalone.eduqfix.qfixinfo.com.app.customer.model.State;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.EntryModel;
import standalone.eduqfix.qfixinfo.com.app.invoices.model.MPOSResponse;
import standalone.eduqfix.qfixinfo.com.app.invoices.model.OfflinePaymentRequest;
import standalone.eduqfix.qfixinfo.com.app.invoices.model.SearchInvoiceRequest;
import standalone.eduqfix.qfixinfo.com.app.invoices.model.SearchInvoiceResponse;
import standalone.eduqfix.qfixinfo.com.app.login.model.Address;
import standalone.eduqfix.qfixinfo.com.app.login.model.CustomerCartId;
import standalone.eduqfix.qfixinfo.com.app.login.model.ForgotPassword;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginRequest;
import standalone.eduqfix.qfixinfo.com.app.login.model.LoginResponse;
import standalone.eduqfix.qfixinfo.com.app.login.model.ProductCartResponse;
import standalone.eduqfix.qfixinfo.com.app.qpos_login.model.RegisterDeviceRequest;
import standalone.eduqfix.qfixinfo.com.app.qpos_login.model.RegisterDeviceResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.AddressInformation;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Categories;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ConfigDetails;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ConfigProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ConfigProductCartResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.CustomerProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.DeleteCartResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.EstimateShippingCharge;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.EstimateShippingResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewCartRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewLoginResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.NewPlaceOrderResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.OrderSummary;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.OtpRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.PlaceOrderRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.PlacedOrderResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.Product;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductCart;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductCartNewRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ProductSKU;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SKURequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SMSContactModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SendPaymentLinkRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SendPaymentLinkResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ShippingChargeResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ShippingCharges;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ShippingRateRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.ShippingRates;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StoreCreditDetailResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StoreCreditListResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.StoreCreditRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.model.SubCategories;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.AcknowledgeRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.AcknowledgeResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.FeeInitiateToken;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.InitiatePaymentRequest;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.InitiatePaymentResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.TokenResponse;
import standalone.eduqfix.qfixinfo.com.app.seller_app.pine.Model.UpdatePaymentRequest;

/**
 * Created by darshan on 27/2/19.
 */

public interface MPOSServices {

    @GET("customapi/getShopUrl")
    Call<String> getShopUrl(@Query("email") String email);

    @POST("customapi/mposLogin.php")
    Call<NewLoginResponse> login(@Body standalone.eduqfix.qfixinfo.com.app.login.model.Request request);

    @POST("customapi/getVerifyotp")
    Call<LoginResponse> verifyOtp(@Body OtpRequest request);

    @POST("customapi/registerDevice.php")
    Call<RegisterDeviceResponse> registerDevice(@Body RegisterDeviceRequest request);

    @POST("customapi/searchInvoice.php")
    Call<List<SearchInvoiceResponse>> searchInvoiceList(@Body SearchInvoiceRequest request);

    @POST("customapi/markAsPaid.php")
    Call<MPOSResponse> markAsPaid(@Body OfflinePaymentRequest request);

    @GET("customapi/getShippingAddressByOrderId")
    Call<List<Address>> getShippingAddressByOrderId(@Query("orderId") Integer orderId);

    @POST("user/token")
    Call<TokenResponse> getUserToken(@Body FeeInitiateToken request);
//
    @POST("payment/shopping/initiate")
    Call<InitiatePaymentResponse> initiatePayment(@Body InitiatePaymentRequest request, @Header("token") String token);
//
    @POST("payment/payment/mposAcknowledgePayment")
    Call<AcknowledgeResponse> acknowledgePaymentRequest(@Body AcknowledgeRequest request, @Header("token") String token);

    @POST("customapi/getProductList.php")
    Call<ArrayList<Product>> getProductList(@Body ProductRequest request);

    @GET("customapi/getStanaloneSellerCategory.php")
    Call<List<Categories>> getSellercategories(@Query("sellerId") Integer sellerId);

   /* @GET("customapi/getCategoriesBySeller")
      Call<List<Categories>> getSellercategories(@Query("sellerId") Integer sellerId);
   */
    @GET("categories/list?searchCriteria[filter_groups][0][filters][0][field]=parent_id")
    Call<SubCategories> getSellerSubcategories(@Query("searchCriteria[filter_groups][0][filters][0][value]") Integer sellerId, @Header("Authorization") String authHeader);

    @POST("integration/admin/token")
    Call<String> getAdminToken(@Body ConfigDetails configDetails);

    @POST("marketplaceproduct/mobileTokens")
    Call<RefreshToken> getCustomerToken(@Body Request configDetails);

    @POST("customapi/sendpaymentlink-token")
    Call<String> getPaymentToken(@Body Request configDetails);

    @POST("store-credit/seller_store_credit_report.php")
    Call<StoreCreditListResponse> getStoreCredit(@Query("seller_id") String sellerIdId, @Query("customer_id") String customerId, @Header("Authorization") String authHeader);

     @GET("store-credit/seller_store_credit_report.php")
     Call<StoreCreditDetailResponse> getStoreCreditDetail(@Query("seller_id") String sellerIdId, @Query("customer_id") String customerId, @Header("Authorization") String authHeader);

//    @POST("index.php/rest/V1/integration/customer/token")
//    Call<String> getUserToken(@Body ConfigDetails configDetails);

     @GET("customapi/check_sku.php")
     Call<Boolean> checkSku(@Query("sku") String sku);

    @GET("carts/mine.php")
    Call<String> getProductCartParentId(@Query("customer_id") Integer customerId, @Header("Authorization") String authHeader);

    @GET("carts/mine/items")
    Call<Integer> getProductCartId(@Body ProductCart productCart);

    @GET("customapi/getCountryRegionsId.php")
    Call<List<State>> getStateList();

    @POST("carts/mine_items.php")
    Call<ProductCartResponse> addProductToCart(@Body ProductCartNewRequest productCart);

    @POST("customapi/decreseProductInCart.php")
    Call<DeleteCartResponse> deleteProductToCart(@Body CustomerProductCart productCart);

    @POST("carts/mine/items")
    Call<ConfigProductCartResponse> addProductToCartConfig(@Body ConfigProductCart configProductCart, @Header("Authorization") String authHeader);

    @POST("carts/mine/shipping-information")
    Call<ShippingChargeResponse> setShippingCharges(@Body ShippingCharges shippingCharges, @Header("Authorization") String authHeader);

    @POST("carts/shipping-methods.php")
    Call<List<EstimateShippingResponse>> estimateShippingCharges(@Body EstimateShippingCharge estimateShippingCharge, @Header("Authorization") String authHeader);

    @POST("customapi/getProductInCart.php")

    Call<List<ProductCartResponse>> getCustomerProductInCart(@Body NewCartRequest productCart);

    @GET("customers/search")
    Call<CustomerDetails> SearchCustomer(@Query("searchCriteria[filter_groups][0][filters][0][field]") String searchType, @Query("searchCriteria[filter_groups][0][filters][0][value]") String filter, @Header("Authorization") String authHeader);

    @POST("payment/update-payment-request-call")
    Call<InitiatePaymentResponse> updatePayment(@Body UpdatePaymentRequest request, @Header("token") String token);

    @POST("customapi/searchCustomerByMobileNumber.php")
    Call<CustomerDetails> SearchCustomerByMobile(@Body SearchCustomerRequest searchCustomerRequest);

    @PUT("customers/password")
    Call<Boolean> sendForgotPasswordLink(@Body ForgotPassword forgotPassword , @Header("Authorization") String authHeader);

    @GET("carts/payment-information.php")
    Call<OrderSummary> getPaymentInformation(@Query("cart_id") String authToken);

    @GET("customapi/getStandaloneProductBySellerId")
    Call<List<AddNewProductModel>> getNewAddProduct(@Query("sellerId") Integer sellerId);

    @POST("customapi/getShippingRates")
    Call<List<ShippingRates>> getShippingRateForSeller(@Body ShippingRateRequest shippingRateRequest);

    @POST("customapi/getOrderplaced.php")
    Call<NewPlaceOrderResponse> placeOrder(@Body PlaceOrderRequest placeOrderRequest, @Header("Authorization") String authHeader);

    @POST("customapi/sendpaymentlink-token.php")
    Call<SendPaymentLinkResponse> sendPaymentLinkForForOrder(@Body SendPaymentLinkRequest paymentLinkRequest);

    @POST("customapi/createCustomer.php")
    Call<CustomerResponse> createCustomer(@Body Data customer);

    @POST("customers/customers.php")
    Call<AddAddressResponse> addAddressForCustomer( @Body AddAddressRequest addAddressRequest ,@Header("Authorization") String authToken);

    @POST("products/{sku}/media") 
    Call<ResponseBody> sendImages(@Path("sku") String sku, @Body EntryModel entryModel , @Header("Authorization") String authToken);

    @GET("products/delete_image.php")
    Call<ResponseBody> deleteImages(@Query("entity_id") Integer entity_id,@Query("is_cover") Integer is_cover,@Query("product_id") Integer product_id);

    @DELETE("carts/carts-items.php")
    Call<Void> deleteProductFromCart(@Query("cart_id") String cartId,@Query("items_id") String itemId);

    @GET("customers/productcategoryset.php")
    Call<String[]> getDisplayCategories(@Query("InstituteCode") Integer instituteCode, @Query("BranchCode") Integer branchCode , @Query("CategoryId") Integer categoryId ,@Header("Authorization") String authToken);

    @POST("customapi/getProductDetailsBySku")
    Call <ProductSKU> productsku(@Body SKURequest skuRequest);

    @POST("customapi/checkSku")
    Call <Boolean> checksku(@Body SKURequest skuRequest);

    @POST("customapi/getOrdersList")
    Call <ArrayList<MyOrderListModelNew>> getOrdersList(@Body MainRequest skuRequest);

    @POST("products")
    Call <String> addNewProduct(@Body AddProductRequest addProductRequest, @Header("Authorization") String authToken);


    @Multipart
    @POST("products/{sku}/media")
    Call<String> uploadFileWithPartMap(
            @Path("sku") String sku,
            @Header("authorization") String authToken,
            @Part("entry[media_type]") RequestBody requestBody,
            @Part("entry[label]") RequestBody requestBody2,
            @Part("entry[position]") RequestBody requestBody3,
            @Part("entry[types][]") RequestBody requestBody4,
            @Part("entry[disabled]") RequestBody requestBody5,
            @Part("entry[content][type]") RequestBody requestBody6,
            @Part("entry[content][name]") RequestBody requestBody7,
            @Part MultipartBody.Part file);

}
