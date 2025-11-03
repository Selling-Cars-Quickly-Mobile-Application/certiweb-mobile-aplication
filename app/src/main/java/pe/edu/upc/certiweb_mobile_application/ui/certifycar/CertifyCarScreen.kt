package pe.edu.upc.certiweb_mobile_application.ui.certifycar

import android.app.DatePickerDialog
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.certiweb_mobile_application.data.SessionManager
import pe.edu.upc.certiweb_mobile_application.data.model.User
import pe.edu.upc.certiweb_mobile_application.data.ReservationRepository
import pe.edu.upc.certiweb_mobile_application.data.model.ReservationRequest
import io.flutter.embedding.android.FlutterActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CertifyCarScreen() {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val cachedUser: User? = sessionManager.getCachedUser()

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var brand by remember { mutableStateOf("") }
    var isBrandMenuExpanded by remember { mutableStateOf(false) }
    val brands = listOf("Toyota", "Honda", "Nissan", "Hyundai", "Kia", "Ford")

    var model by remember { mutableStateOf("") }
    var licensePlate by remember { mutableStateOf("") }
    var ownerEmail by remember { mutableStateOf(cachedUser?.email ?: "") }
    var sellingPrice by remember { mutableStateOf("") }

    var selectedDateText by remember { mutableStateOf("Select a date") }
    var selectedTime by remember { mutableStateOf<String?>(null) }

    val timeSlots = listOf("09:00", "11:00", "13:00", "15:00", "17:00")

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }

    val calendar = remember { Calendar.getInstance() }
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val c = Calendar.getInstance().apply { set(year, month, dayOfMonth) }
            val dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
            val isBusinessDay = dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY
            if (!isBusinessDay) {
                Toast.makeText(context, "Only business days (Monday to Friday)", Toast.LENGTH_SHORT).show()
            }
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDateText = sdf.format(c.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val repository = remember { ReservationRepository() }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Upload Vehicle Photo",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        )

        Column(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFEFF7ED), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Text(text = "Vehicle Photo", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = { pickImageLauncher.launch("image/*") }) {
                    Text("+Select JPG File")
                }
                Text(text = imageUri?.lastPathSegment ?: "No file chosen", modifier = Modifier.align(Alignment.CenterVertically))
            }
            Spacer(Modifier.height(12.dp))
            TextButton(onClick = { Toast.makeText(context, "Image selection ready", Toast.LENGTH_SHORT).show() }) {
                Text("Upload Image")
            }
            Spacer(Modifier.height(8.dp))
            Text(text = "JPG files only. Max ~2MB", fontSize = 12.sp, color = Color.Gray)
        }

        Text(text = "Vehicle Data", style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp, fontWeight = FontWeight.SemiBold))

        Column(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFEFF7ED), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Text("Brand", fontWeight = FontWeight.Bold)
            Box(Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { isBrandMenuExpanded = true }, modifier = Modifier.fillMaxWidth()) {
                    Text(if (brand.isEmpty()) "Select a brand" else brand)
                }
                DropdownMenu(expanded = isBrandMenuExpanded, onDismissRequest = { isBrandMenuExpanded = false }) {
                    brands.forEach { b ->
                        DropdownMenuItem(text = { Text(b) }, onClick = {
                            brand = b
                            isBrandMenuExpanded = false
                        })
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Text("Model", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = model,
                onValueChange = { model = it },
                placeholder = { Text("E.g.: Corolla, Yaris, Hilux") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))
            Text("License Plate", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = licensePlate,
                onValueChange = { licensePlate = it.uppercase(Locale.getDefault()) },
                placeholder = { Text("Format: ABC-123") },
                supportingText = { Text("Format: ABC-123") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))
            Text("Owner's Email", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = ownerEmail,
                onValueChange = { ownerEmail = it },
                placeholder = { Text("Enter your email address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))
            Text("Selling Price (PEN)", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = sellingPrice,
                onValueChange = { sellingPrice = it.filter { ch -> ch.isDigit() } },
                placeholder = { Text("E.g: 50000") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Text(text = "BOOK INSPECTION TIME", style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp, fontWeight = FontWeight.SemiBold))

        Column(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFEFF7ED), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Text("Select a date", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = { datePickerDialog.show() }, modifier = Modifier.fillMaxWidth()) {
                Text(selectedDateText)
            }
            Spacer(Modifier.height(8.dp))
            Text("Only business days (Monday to Friday)", fontSize = 12.sp, color = Color.Gray)
        }

        Column(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFEFF7ED), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Text("Select a time", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            timeSlots.forEach { slot ->
                val isSelected = selectedTime == slot
                Button(
                    onClick = { selectedTime = slot },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (isSelected) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = SimpleDateFormat("h:mm a", Locale.getDefault()).format(
                            SimpleDateFormat("HH:mm", Locale.getDefault()).parse(slot)!!
                        ),
                        color = if (isSelected) Color.White else Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("Available since Monday to Friday", fontSize = 12.sp, color = Color.Gray)
        }

        if (errorMessage != null) {
            Text(text = errorMessage!!, color = Color.Red)
        }

        Button(
            onClick = {
                // validations
                val plateRegex = Regex("^[A-Z]{3}-\\d{3}$")
                if (brand.isBlank()) {
                    errorMessage = "Please select a brand"
                    return@Button
                }
                if (model.isBlank()) {
                    errorMessage = "Please enter model"
                    return@Button
                }
                if (!plateRegex.matches(licensePlate)) {
                    errorMessage = "Invalid license plate format (ABC-123)"
                    return@Button
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(ownerEmail).matches()) {
                    errorMessage = "Invalid email"
                    return@Button
                }
                if (sellingPrice.isBlank()) {
                    errorMessage = "Enter selling price"
                    return@Button
                }
                if (selectedDateText == "Select a date") {
                    errorMessage = "Please select a date"
                    return@Button
                }
                if (selectedTime == null) {
                    errorMessage = "Please select a time"
                    return@Button
                }
                if (cachedUser == null) {
                    errorMessage = "Login required to confirm reservation"
                    return@Button
                }
                // Navegar a la pantalla Flutter (ReservationPage es la home en main.dart)
                errorMessage = null
                val intent = FlutterActivity.createDefaultIntent(context)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
        ) {
            Text(if (isLoading) "Processing..." else "✓ Confirm Reservation", color = Color.White)
        }
    }
}