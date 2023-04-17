package standalone.eduqfix.qfixinfo.com.app.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by darshan on 27/2/19.
 */

public class MPOSRetrofitClient {

    private Retrofit retrofit;

    public Retrofit getClient(String baseUrl) {
        OkHttpClient okHttpClient = new OkHttpClient();

// set your desired log level
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        okHttpClient.newBuilder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60,TimeUnit.SECONDS);
        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .client(okHttpClient)
                    .client(httpClient.build())
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }
}
