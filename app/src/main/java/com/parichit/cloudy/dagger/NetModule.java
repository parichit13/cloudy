package com.parichit.cloudy.dagger;

import android.arch.lifecycle.ViewModelProvider;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parichit.cloudy.BuildConfig;
import com.parichit.cloudy.data.network.RetrofitService;
import com.parichit.cloudy.ui.MainViewModelFactory;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(subcomponents = ViewModelSubComponent.class)
public class NetModule {
    private static final String TAG = "NetModule";
    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson) {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();

                        Log.d(TAG, "Original: " + originalHttpUrl.toString());

                        HttpUrl url = originalHttpUrl.newBuilder()
                                .addQueryParameter("key", BuildConfig.APIXUKey)
                                .build();

                        Log.d(TAG, url.toString());

                        Request.Builder requestBuilder = original.newBuilder()
                                .url(url);

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .build();

        Log.d(TAG, BuildConfig.BASEURL);

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASEURL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    public RetrofitService providesRetrofitService(Retrofit retrofit) {
        return retrofit.create(RetrofitService.class);
    }

    @Singleton
    @Provides
    ViewModelProvider.Factory provideViewModelFactory(ViewModelSubComponent.Builder viewModelSubComponent) {
        return new MainViewModelFactory(viewModelSubComponent.build());
    }
}
