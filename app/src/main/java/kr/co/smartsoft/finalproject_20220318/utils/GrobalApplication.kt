package kr.co.smartsoft.finalproject_20220318.utils

import android.app.Application
import com.facebook.FacebookSdk
import com.kakao.sdk.common.KakaoSdk

class GrobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 네이티브 앱카를 입력한다.
        KakaoSdk.init(this, "d78ccb9e2ef5d58d8deda8b3c1ce266e")
    }
}