package pe.edu.upc.certiweb_mobile_application.data

import pe.edu.upc.certiweb_mobile_application.data.model.Reservation
import pe.edu.upc.certiweb_mobile_application.data.model.ReservationRequest
import pe.edu.upc.certiweb_mobile_application.data.remote.ApiClient
import retrofit2.HttpException

class ReservationRepository {
    private val api = ApiClient.retrofit.create(pe.edu.upc.certiweb_mobile_application.data.remote.ReservationApi::class.java)

    suspend fun createReservation(request: ReservationRequest): Result<Reservation> = try {
        val res = api.createReservation(request)
        if (res.isSuccessful) {
            val body = res.body()
            if (body != null) Result.success(body) else Result.failure(Exception("Empty response"))
        } else Result.failure(HttpException(res))
    } catch (e: Exception) {
        Result.failure(e)
    }
}