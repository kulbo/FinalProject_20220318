package kr.co.smartsoft.finalproject_20220318.datas

class DataResponse (
    val user : UserData,
    val token : String,
    val friends: List<UserData>,

    val users: List<UserData>,

    val appointments: List<AppointmentData>,

    val places: List<PlaceData>,

) {

}