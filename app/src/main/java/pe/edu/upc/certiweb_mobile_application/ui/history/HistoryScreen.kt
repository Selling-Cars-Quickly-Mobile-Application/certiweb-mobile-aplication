package pe.edu.upc.certiweb_mobile_application.ui.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import pe.edu.upc.certiweb_mobile_application.data.SessionManager
import pe.edu.upc.certiweb_mobile_application.data.model.Reservation
import pe.edu.upc.certiweb_mobile_application.data.remote.ApiClient
import pe.edu.upc.certiweb_mobile_application.data.remote.ReservationApi

@Composable
fun HistoryScreen() {
    val ctx = LocalContext.current
    val session = remember { SessionManager(ctx) }
    val user = session.getCachedUser()

    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var reservations by remember { mutableStateOf<List<Reservation>>(emptyList()) }

    LaunchedEffect(user) {
        if (user == null) {
            error = "Debes iniciar sesión para ver tu historial"
            isLoading = false
            return@LaunchedEffect
        }
        try {
            val api = ApiClient.retrofit.create(ReservationApi::class.java)
            val res = api.getReservationsByUserId(user.id)
            if (res.isSuccessful) {
                reservations = res.body().orEmpty()
                error = null
            } else {
                error = "No se pudo cargar el historial (${res.code()})"
            }
        } catch (e: Exception) {
            error = "Error cargando historial: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(12.dp))
                    Text("Cargando historial...")
                }
            }
            error != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(error ?: "", color = MaterialTheme.colorScheme.error)
                }
            }
            reservations.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No tienes reservaciones aún", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    Text("Cuando reserves, aparecerán aquí")
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(reservations) { r ->
                        ReservationItem(r)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReservationItem(r: Reservation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AsyncImage(
                model = r.imageUrl ?: "",
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(160.dp),
                contentScale = ContentScale.Crop
            )
            Text(text = r.reservationName, style = MaterialTheme.typography.titleMedium)
            Text(text = "${r.brand} - ${r.model}")
            Text(text = "Placa: ${r.licensePlate}")
            Text(text = "Fecha: ${r.inspectionDateTime}")
            Text(text = "Precio: S/ ${r.price}")
            Text(text = "Estado: ${r.status}")
        }
    }
}