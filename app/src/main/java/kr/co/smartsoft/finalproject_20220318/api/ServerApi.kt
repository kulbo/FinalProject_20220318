package kr.co.smartsoft.finalproject_20220318.api

import android.content.Context
import com.google.gson.GsonBuilder
import kr.co.smartsoft.finalproject_20220318.utils.ContextUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ServerApi {

    companion object {
        private var retrofit: Retrofit? = null
        private val BASE_URL = "https://keepthetime.xyz"

        fun getRerofit(context: Context) : Retrofit {

            if (retrofit == null) {
                val intercept = Interceptor {
                    with(it) {
                        val newRequest = request().newBuilder()
                            .addHeader("X-Http-Token", ContextUtil.getLoginUserToken(context))
                            .build()

                        proceed(newRequest)
                    }
                }

                val myClient = OkHttpClient.Builder()
                    .addInterceptor(intercept)
                    .build()

                val gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .registerTypeAdapter(
                        Date::class.java,
                        DateDeseralizer()
                    )
                    .create()
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(myClient)
                    .build()
            }
            return retrofit!!
        }
    }
}