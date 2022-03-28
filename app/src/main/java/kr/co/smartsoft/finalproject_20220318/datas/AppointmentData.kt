package kr.co.smartsoft.finalproject_20220318.datas

import java.io.Serializable
import java.util.*

class AppointmentData (
    val id : Int,
    val user_id : Int,
    val title: String,
    val datetime: Date,
    val start_place : String,
    val start_latitude: Double,
    val start_longitude: Double,
    val place: String,
    val latitude: Double,
    val longitude: Double,
    val created_at: String,
    val user: UserData,
    val invited_friends: List<UserData>
) : Serializable {

}