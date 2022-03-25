package kr.co.smartsoft.finalproject_20220318.datas

import java.io.Serializable

class PlaceData(
    val id: Int,
    val user_id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val is_primary: Boolean,
) : Serializable {
}