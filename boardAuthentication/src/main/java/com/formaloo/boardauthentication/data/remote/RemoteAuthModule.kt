package com.formaloo.boardauthentication.data.remote




import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun createRemoteAuthModule(
    baseUrl: String,
    appToken: String
) = module {

    factory(named(remoteAuthModulConstant.InterceptorName)) {
        Interceptor { chain ->
            val original = chain.request()

            val request =
                original.newBuilder()
//                    .header("x-api-key", appToken)
                    .header("x-api-key", appToken)
                    .method(original.method, original.body)
                    .build()

            chain.proceed(request)
        }
    }


    single(named(remoteAuthModulConstant.ClientName)) {
        OkHttpClient.Builder()
            .addInterceptor(get(named(remoteAuthModulConstant.InterceptorName)) as Interceptor)
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .build()
    }

    factory(named(remoteAuthModulConstant.RetrofitName)) {
        Retrofit.Builder()
            .client(get(named(remoteAuthModulConstant.ClientName)))
//            .baseUrl(baseUrl)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    factory(named(remoteAuthModulConstant.ServiceName)) {
        get<Retrofit>(named(remoteAuthModulConstant.RetrofitName)).create(
            AuthService::class.java
        )
    }

    factory(named(remoteAuthModulConstant.DataSourceName)) { AuthDatasource(get(named(remoteAuthModulConstant.ServiceName))) }
}

object remoteAuthModulConstant {
    const val DataSourceName="authDatasource"
    const val ServiceName="boardService"
    const val ClientName="boardClient"
    const val RetrofitName="boardRetrofit"
    const val InterceptorName="boardInterceptor"
}
