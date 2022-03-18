package kr.co.smartsoft.finalproject_20220318.utils

import android.content.Context

class ContextUtil {

    companion object {

        private val prefName = "FinalProjectPref"

        private val LOGIN_USER_TOKEN = "LOGIN_USER_TOKEN"

        // token을 저장하는 함수
        public fun setLoginUserToken(context: Context,token: String) {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putString(LOGIN_USER_TOKEN, token).apply()
        }

        // token을 가져오는 함수
        public fun getLoginUserToken(context: Context) : String {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getString(LOGIN_USER_TOKEN, "")!!
        }
    }

}