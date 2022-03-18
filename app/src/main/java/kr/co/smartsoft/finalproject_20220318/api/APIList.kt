package kr.co.smartsoft.finalproject_20220318.api

import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIList {

    @FormUrlEncoded     // 파라미터중에 Field(formData)에 담아야하는 파라미터가 있다.
    @POST("/user")
    fun postRequestLogin(
        @Field("email") email: String,
        @Field("password") pw: String
    ) : Call<BasicResponse>
}