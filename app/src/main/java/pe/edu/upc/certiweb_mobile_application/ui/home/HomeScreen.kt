package pe.edu.upc.certiweb_mobile_application.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
// ArrowBack como filled (evitamos import AutoMirrored por compatibilidad de BOM)
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavHostController
import pe.edu.upc.certiweb_mobile_application.ui.theme.*
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Brush
// clickable ya importado arriba

data class Car(val brand: String, val model: String)

interface CarRepository {
    suspend fun getAllCars(): List<Car>
}

private class LocalCarRepository : CarRepository {
    override suspend fun getAllCars(): List<Car> = sampleCars
}

private val predefinedBrands = listOf(
    "Toyota", "Hyundai", "Kia", "Chevrolet", "Suzuki", "Mitsubishi",
    "Honda", "Volkswagen", "Ford", "Mercedes-Benz", "BMW", "Audi"
)

private val sampleCars = listOf(
    Car("Toyota", "Corolla"),
    Car("Toyota", "RAV4"),
    Car("Hyundai", "Elantra"),
    Car("Hyundai", "Tucson"),
    Car("Kia", "Rio"),
    Car("Kia", "Sportage"),
    Car("Chevrolet", "Onix"),
    Car("Chevrolet", "Tracker"),
    Car("Suzuki", "Swift"),
    Car("Mitsubishi", "Outlander"),
    Car("Honda", "Civic"),
    Car("Honda", "CR-V"),
    Car("Volkswagen", "Gol"),
    Car("Ford", "Focus"),
    Car("Mercedes-Benz", "C-Class"),
    Car("BMW", "X3"),
    Car("Audi", "A4"),
)

// Dataset de autos certificados (frontend) para el carrusel móvil
private data class CertifiedCar(
    val id: Int,
    val name: String,
    val image: String,
    val color: String,
    val price: String,
    val route: String
)

private val certifiedCars = listOf(
    CertifiedCar(
        id = 1,
        name = "BMW Serie 4",
        image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSYVcLx4pGvnOpKwlHUU49s8jkRkJDGVxaiDw&s",
        color = "Azul metálico",
        price = "S/4,399.00",
        route = "/car-detail/4"
    ),
    CertifiedCar(
        id = 2,
        name = "Ford Mustang GT",
        image = "https://www.vdm.ford.com/content/dam/na/ford/en_us/images/mustang/2025/jellybeans/Ford_Mustang_2025_200A_PJS_883_89W_13B_COU_64F_99H_44U_EBST_YZTAC_DEFAULT_EXT_4.png",
        color = "Gris",
        price = "S/4,599.00",
        route = "/car-detail/5"
    ),
    CertifiedCar(
        id = 3,
        name = "Kia Niro",
        image = "https://cdn.motor1.com/images/mgl/ojyBzq/s3/kia-niro-2025.jpg",
        color = "Rojo",
        price = "S/3,900.00",
        route = "/car-detail/2"
    ),
    CertifiedCar(
        id = 4,
        name = "Kia Sportage",
        image = "https://s3.amazonaws.com/kia-greccomotors/Sportage_blanca_01_9a1ad740c7.png",
        color = "Blanco perlado",
        price = "S/3,299.00",
        route = "/car-detail/3"
    ),
    CertifiedCar(
        id = 5,
        name = "Audi A5 Sportback",
        image = "https://hips.hearstapps.com/hmg-prod/images/2025-audi-a5-137-669583e0eda6e.jpg?crop=0.638xw:0.479xh;0.207xw,0.312xh&resize=1200:*",
        color = "Gris Quantum",
        price = "S/4,799.00",
        route = "/car-detail/6"
    ),
    CertifiedCar(
        id = 6,
        name = "Mercedes Clase C",
        image = "https://images.coches.com/_vn_/mercedes/Clase-C/0b325a581bbefb9994d94efc91277ba9.jpg?w=1920&ar=16:9",
        color = "Plata Iridio",
        price = "S/4,670.00",
        route = "/car-detail/7"
    )
)

private fun generateBrandOptions(predefined: List<String>, cars: List<Car>): List<String> {
    return (predefined + cars.map { it.brand })
        .map { it.trim() }
        .map { it.lowercase() }
        .distinct()
        .map { it.replaceFirstChar { c -> c.uppercase() } }
        .sorted()
}

private fun modelOptionsForBrand(brand: String?, cars: List<Car>): List<String> {
    if (brand.isNullOrBlank()) return emptyList()
    val b = brand.lowercase()
    return cars
        .filter { it.brand.lowercase() == b }
        .map { it.model }
        .distinct()
        .sorted()
}

private fun getModelCategory(model: String): String {
    val m = model.lowercase()
    return when {
        m.contains("suv") || m.contains("x") -> "SUV"
        m.contains("sedan") || m.contains("series") -> "Sedán"
        m.contains("hatch") -> "Hatchback"
        else -> "Sedán"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, onLogout: () -> Unit = {}) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Mostrar el drawer en el lado derecho usando RTL alrededor del drawer
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = CardBackground,
                    drawerTonalElevation = 2.dp
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        DrawerHeader(onClose = { scope.launch { drawerState.close() } })
                        DrawerContent(
                            onNavigate = { route ->
                                navController.navigate(route)
                                scope.launch { drawerState.close() }
                            },
                            onLogout = {
                                onLogout()
                                scope.launch { drawerState.close() }
                            }
                        )
                    }
                }
            },
            scrimColor = Color.Black.copy(alpha = 0.2f)
        ) {
            // Restaurar LTR para el contenido principal de la app
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = GreenPrimary,
                                titleContentColor = Color.White
                            ),
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(Color.White),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AsyncImage(
                                            model = "https://i.ibb.co/ZpSpH21m/certiweb.png",
                                            contentDescription = "Certiweb logo",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(4.dp),
                                            contentScale = ContentScale.Fit
                                        )
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Text("CertiWeb", fontWeight = FontWeight.SemiBold)
                                }
                            },
                            actions = {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                                }
                            }
                        )
                    },
                    containerColor = CreamBackground
                ) { inner ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(inner)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        item { BannerSection() }
                        item { SearchCard() }
                        item { WelcomeBackSection() }
                        item { CarouselSection() }
                        item { CertificateCTASection() }
                        item { MostSearchedBrandsSection() }
                        item { FooterSection() }
                    }
                }
            }
        }
    }
}

@Composable
private fun BannerSection() {
    // Banner superior con placeholder
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(170.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardBackground),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = "https://i.ibb.co/JRsXJD6k/brand-Phrase.png",
            contentDescription = "Brand phrase",
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchCard() {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Search, contentDescription = null, tint = GreenPrimary)
                Spacer(Modifier.width(8.dp))
                Text("Find your perfect car", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }

            var loading by remember { mutableStateOf(false) }
            var carsData by remember { mutableStateOf<List<Car>>(emptyList()) }
            val repository = remember { LocalCarRepository() }

            LaunchedEffect(Unit) {
                loading = true
                carsData = repository.getAllCars()
                loading = false
            }

            // Brand dropdown
            Text("Brand", color = TextSecondary, fontSize = 14.sp)
            var brandExpanded by remember { mutableStateOf(false) }
            var selectedBrand by remember { mutableStateOf<String?>(null) }
            var selectedModel by remember { mutableStateOf<String?>(null) }
            val brandOptions = remember(carsData) { generateBrandOptions(predefinedBrands, carsData) }
            ExposedDropdownMenuBox(expanded = brandExpanded, onExpandedChange = { brandExpanded = !brandExpanded }) {
                TextField(
                    value = selectedBrand ?: "Select brand",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = brandExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF7F7F7),
                        unfocusedContainerColor = Color(0xFFF7F7F7)
                    )
                )
                ExposedDropdownMenu(expanded = brandExpanded, onDismissRequest = { brandExpanded = false }) {
                    brandOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedBrand = option
                                selectedModel = null
                                brandExpanded = false
                            }
                        )
                    }
                }
            }

            // Model dropdown
            Text("Model", color = TextSecondary, fontSize = 14.sp)
            var modelExpanded by remember { mutableStateOf(false) }
            val modelOptions = remember(selectedBrand, carsData) { modelOptionsForBrand(selectedBrand, carsData) }
            ExposedDropdownMenuBox(expanded = modelExpanded, onExpandedChange = { modelExpanded = !modelExpanded }) {
                TextField(
                    value = selectedModel ?: "Select model",
                    onValueChange = {},
                    readOnly = true,
                    enabled = selectedBrand != null,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = modelExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF7F7F7),
                        unfocusedContainerColor = Color(0xFFF7F7F7)
                    )
                )
                ExposedDropdownMenu(expanded = modelExpanded, onDismissRequest = { modelExpanded = false }) {
                    modelOptions.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(option)
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        getModelCategory(option),
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            },
                            onClick = {
                                selectedModel = option
                                modelExpanded = false
                            }
                        )
                    }
                }
            }

            // Buttons
            val isFormValid = selectedBrand != null
            Button(
                onClick = {
                    println("Search brand=" + (selectedBrand ?: "-") + ", model=" + (selectedModel ?: "-"))
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid && !loading,
                colors = ButtonDefaults.buttonColors(containerColor = BlueAccent)
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Search", color = Color.White)
            }

            OutlinedButton(onClick = {
                selectedBrand = null
                selectedModel = null
            }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary)
                Spacer(Modifier.width(8.dp))
                Text("Clear filters", color = TextSecondary)
            }
        }
    }
}

@Composable
private fun WelcomeBackSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("Welcome back!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(Modifier.height(4.dp))
        Text("Check out these recently certified cars", color = TextSecondary)
    }
}

@Composable
private fun CarouselSection() {
    LazyRow(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(certifiedCars) { car ->
            CarCard(car)
        }
    }
}

@Composable
private fun CarCard(car: CertifiedCar) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(260.dp)
            .clickable { println("Open car: ${car.route}") },
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Imagen del auto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                AsyncImage(
                    model = car.image,
                    contentDescription = car.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Overlay inferior con precio y "View details"
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0x99000000), Color(0x33000000), Color(0x00000000))
                            )
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(car.price, color = Color.White, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                            Spacer(Modifier.width(6.dp))
                            Text("View details", color = Color.White)
                        }
                    }
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(car.name, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Text(car.price, color = TextPrimary, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(car.color, color = TextSecondary)
            }
        }
    }
}

@Composable
private fun CertificateCTASection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        // Imagen amplia
        AsyncImage(
            model = "https://img.freepik.com/foto-gratis/coche-lujoso-estacionado-carretera-faro-iluminado-al-atardecer_181624-60607.jpg?semt=ais_hybrid&w=740",
            contentDescription = "Featured car",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))
        Text("We know your car is\nimportant", textAlign = TextAlign.Center, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(Modifier.height(8.dp))
        Text(
            "Certify your car and get an inspection that will add more value and confidence when selling it.",
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center,
            color = TextSecondary
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = YellowAccent)
        ) {
            Text("Get your certificate!", color = TextPrimary)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = TextPrimary)
        }
    }
}

@Composable
private fun MostSearchedBrandsSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("Most searched brands", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(Modifier.height(8.dp))
        Text("Discover vehicles from the most popular brands among our users", color = TextSecondary)

        Spacer(Modifier.height(16.dp))

        // Usamos las imágenes de logos definidas en el frontend (brand-search.component.vue)
        listOf(
            Triple("Audi", Color(0xFFDE6B76), "https://1000marcas.net/wp-content/uploads/2019/12/Audi-logo.png"),
            Triple("Mercedes-Benz", Color(0xFF79C7E7), "https://www.pngarts.com/files/3/Mercedes-Benz-Logo-PNG-Photo.png"),
            Triple("BMW", Color(0xFF84B9D9), "https://images.icon-icons.com/1834/PNG/512/iconfinderbmwlogo4140436-115966_115915.png"),
            Triple("Volkswagen", Color(0xFF8AA9C1), "https://e7.pngegg.com/pngimages/570/523/png-clipart-volkswagen-logo-volkswagen-golf-car-porsche-cayenne-volkswagen-beetle-creative-car-car-standard-volkswagen-logo-emblem-free-logo-design-template.png")
        ).forEach { (brand, color, logoUrl) ->
            BrandCard(brand = brand, ringColor = color, logoUrl = logoUrl)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun BrandCard(brand: String, ringColor: Color, logoUrl: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Óvalo decorativo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke = 3.dp.toPx()
                    drawOval(color = ringColor.copy(alpha = 0.6f), style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke))
                }
                // Logo circular real (usando AsyncImage)
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(Color(0xFFF5F5F5))
                ) {
                    AsyncImage(
                        model = logoUrl,
                        contentDescription = "$brand logo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(brand, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary)
                Spacer(Modifier.width(6.dp))
                Text("View vehicles", color = TextSecondary)
            }
            Spacer(Modifier.height(6.dp))
            HorizontalDivider(color = ringColor.copy(alpha = 0.6f))
        }
    }
}

@Composable
private fun FooterSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(onClick = {}, modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Back to home")
        }
        Spacer(Modifier.height(8.dp))
        Text("© 2025 Certiweb.com. All rights reserved.", color = TextSecondary)
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = {}, shape = RoundedCornerShape(20.dp)) {
            Text("Terms and Conditions")
        }
        Spacer(Modifier.height(24.dp))
    }
}
@Composable
private fun DrawerHeader(onClose: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(GreenPrimary)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("User", color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text("Menu", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                }
            }
        }
    }
}

@Composable
private fun DrawerContent(onNavigate: (String) -> Unit, onLogout: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(8.dp))
        SectionTitle("NAVEGACIÓN")
        DrawerItem(
            icon = { Icon(Icons.Outlined.DirectionsCar, contentDescription = null, tint = GreenPrimary) },
            title = "Certified Cars for Sale",
            subtitle = "Explora vehículos certificados",
            onClick = { onNavigate("certifiedCars") }
        )
        DrawerItem(
            icon = { Icon(Icons.Outlined.AddCircleOutline, contentDescription = null, tint = GreenPrimary) },
            title = "Certify your Car",
            subtitle = "Certifica tu vehículo",
            onClick = { onNavigate("certifyCar") }
        )

        HorizontalDivider(color = DividerLight, modifier = Modifier.padding(vertical = 12.dp))

        SectionTitle("CUENTA")
        DrawerItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null, tint = GreenPrimary) },
            title = "Profile",
            subtitle = "Gestiona tu perfil",
            onClick = { onNavigate("profile") }
        )
        DrawerItem(
            icon = { Icon(Icons.Outlined.History, contentDescription = null, tint = GreenPrimary) },
            title = "History",
            subtitle = "Historial de actividades",
            onClick = { onNavigate("history") }
        )
        DrawerItem(
            icon = { Icon(Icons.AutoMirrored.Outlined.HelpOutline, contentDescription = null, tint = GreenPrimary) },
            title = "Support",
            subtitle = "Ayuda y soporte",
            onClick = { onNavigate("support") }
        )
        DrawerItem(
            icon = { Icon(Icons.Outlined.Article, contentDescription = null, tint = GreenPrimary) },
            title = "Terms of Use",
            subtitle = "Términos y condiciones",
            onClick = { onNavigate("termsOfUse") }
        )

        HorizontalDivider(color = DividerLight, modifier = Modifier.padding(vertical = 12.dp))

        SectionTitle("IDIOMA")
        LanguageToggle()

        HorizontalDivider(color = DividerLight, modifier = Modifier.padding(vertical = 12.dp))

        LogoutItem(onLogout = onLogout)
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, modifier = Modifier.padding(horizontal = 16.dp), color = TextSecondary, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun DrawerItem(icon: @Composable () -> Unit, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(42.dp).clip(RoundedCornerShape(12.dp)).background(GreenSurfaceLow),
            contentAlignment = Alignment.Center
        ) { icon() }

        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = TextSecondary, fontSize = 12.sp)
        }
        Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = TextSecondary)
    }
}

@Composable
private fun LanguageToggle() {
    Row(modifier = Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = true,
            onClick = {},
            label = { Text("English", color = Color.White) },
            leadingIcon = { Icon(Icons.Outlined.Language, contentDescription = null, tint = Color.White) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = BlueAccent,
                selectedLabelColor = Color.White,
                selectedLeadingIconColor = Color.White
            )
        )
        FilterChip(
            selected = false,
            onClick = {},
            label = { Text("Spanish") },
            leadingIcon = { Icon(Icons.Outlined.Language, contentDescription = null, tint = TextSecondary) },
            colors = FilterChipDefaults.filterChipColors(
                containerColor = Color(0xFFF2F2F2),
                labelColor = TextSecondary,
                iconColor = TextSecondary
            )
        )
    }
}

@Composable
private fun LogoutItem(onLogout: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFFEEEE))
            .padding(12.dp)
            .clickable { onLogout() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(42.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFFFF6F6)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Logout, contentDescription = null, tint = Color(0xFFD32F2F))
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("Logout", color = Color(0xFFD32F2F), fontWeight = FontWeight.SemiBold)
            Text("Cerrar sesión", color = Color(0xFFD32F2F), fontSize = 12.sp)
        }
        Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = Color(0xFFD32F2F))
    }
}