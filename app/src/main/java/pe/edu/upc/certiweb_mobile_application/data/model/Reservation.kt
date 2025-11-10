package pe.edu.upc.certiweb_mobile_application.data.model

// Mantener Request antiguo si se requiere en algún flujo anterior (no usado actualmente)
data class ReservationRequest(
    val userId: String,
    val date: String,      // yyyy-MM-dd
    val time: String,      // HH:mm
    val serviceType: String,
    val notes: String
)

// Modelo alineado con el backend y Flutter
data class Reservation(
    val id: String?,
    val userId: String,
    val reservationName: String,
    val reservationEmail: String,
    val imageUrl: String?,
    val brand: String,
    val model: String,
    val licensePlate: String,
    val inspectionDateTime: String, // ISO8601 con Z
    val price: String,
    val status: String
)