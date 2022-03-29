package kr.co.smartsoft.finalproject_20220318.api

import kr.co.smartsoft.finalproject_20220318.datas.BasicResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
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

    @GET("/user") // 중복검사
    fun getRequestMyInfo(
    ) : Call<BasicResponse>

    @GET("/appointment")
    fun getRequestAppointmentList() : Call<BasicResponse>

    @GET("/user/place")
    fun getRequestMyPlacesList() : Call<BasicResponse>

    @FormUrlEncoded
    @POST("/appointment")
    fun postRequestAddAppointment(
        @Field("title") title : String,
        @Field("datetime") datetime : String,
        @Field("start_place") stPlaceName : String,
        @Field("start_latitude") stLat : Double,
        @Field("start_longitude") stLng : Double,
        @Field("place") place : String,
        @Field("latitude") lat : Double,
        @Field("longitude") lng : Double,
    ) : Call<BasicResponse>

    @GET("/user/friend")
    fun getRequestMyFriendsList(
        @Query("type") type : String,   // all, my, requested 세 문구만 입력    
    ) : Call<BasicResponse>

    //    친구 요청 수락/거절
    @FormUrlEncoded
    @PUT("/user/friend")
    fun putRequestFriendAcceptDeny(
        @Field("user_id") id: Int,
        @Field("type") type: String     // 수락,거절 만 가능
    ) : Call<BasicResponse>

    @GET("/search/user")
    fun getRequestUserList(
        @Query("nickname") nickName : String,   // 검색할 닉네임 2자이상
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @POST("/user/friend")
    fun postRequestAddFriend(
        @Field("user_id") userId: Int,
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @POST("/user/place")
    fun postRequestAddPlace(
        @Field("name") name: String,
        @Field("latitude") lat: Double,
        @Field("longitude") lon: Double,
        @Field("is_primary") isp: Boolean,
    ) : Call<BasicResponse>

//    프로필 사진 첨부 => 파라미터에 파일이 있다 : Field 대신, Multipart 활용
    @Multipart
    @PUT("/user/image")
    fun putRequestProfileImg(
        @Part img : MultipartBody.Part
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @POST("/user/social")
    fun postRequestSocialLogin(
        @Field("provider") provider : String,
        @Field("uid") uid : String,
        @Field("nick_name") nick_name : String,
    ) : Call<BasicResponse>
}