package pe.edu.upc.certiweb_mobile_application.data.model

data class ReservationRequest(
    val userId: String,
    val date: String,      // yyyy-MM-dd
    val time: String,      // HH:mm
    val serviceType: String,
    val notes: String
)

data class Reservation(
    val id: String?,
    val userId: String,
    val date: String,
    val time: String,
    val serviceType: String,
    val notes: String
)