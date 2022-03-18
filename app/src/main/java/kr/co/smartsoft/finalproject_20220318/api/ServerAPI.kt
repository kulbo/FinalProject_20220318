package kr.co.smartsoft.finalproject_20220318.api

import android.content.Context
import com.google.android.material.internal.ContextUtils
import okhttp3.Interceptor
import retrofit2.Retrofit

class ServerAPI {

    companion object {
        private var retrofit : Retrofit? = null
        private val BASE_URL = "https://keepthetime.xyz"

        fun getRetrofit(context: Context) : Retrofit {
            if (retrofit == null) {
            }
        }
    }
}