package pe.edu.upc.certiweb_mobile_application.data.remote

import pe.edu.upc.certiweb_mobile_application.data.model.Reservation
import pe.edu.upc.certiweb_mobile_application.data.model.ReservationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ReservationApi {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/reservations")
    suspend fun createReservation(@Body request: ReservationRequest): Response<Reservation>

    @GET("/reservations")
    suspend fun getAllReservations(): Response<List<Reservation>>

    @GET("/reservations/{id}")
    suspend fun getReservationById(@Path("id") id: String): Response<Reservation>
}