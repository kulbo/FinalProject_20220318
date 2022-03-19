package kr.co.smartsoft.finalproject_20220318.api

import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import retrofit2.Call
import retrofit2.http.*

interface APIList {

    @FormUrlEncoded     // 파라미터중에 Field(formData)에 담아야하는 파라미터가 있다.
    @POST("/user")
    fun postRequestLogin(
        @Field("email") email: String,
        @Field("password") pw: String
    ) : Call<BasicResponse>

    @GET("/user/check") // 중복검사
    fun getDupulicateCheck(
        @Query("type") type:String,
        @Query("value") value:String
    ) : Call<BasicResponse>

//    회원가입하기
    @FormUrlEncoded
    @PUT("/user")
    fun putRequestSignUp(
        @Field("email") email: String,
        @Field("password") pw: String,
        @Field("nick_name") nick: String
    ) : Call<BasicResponse>


}