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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.LayoutDirection
import pe.edu.upc.certiweb_mobile_application.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onLogout: () -> Unit = {}) {
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
                    DrawerHeader(onClose = { scope.launch { drawerState.close() } })
                    DrawerContent(onLogout = {
                        onLogout()
                        scope.launch { drawerState.close() }
                    })
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
                                        Text("CW", fontSize = 12.sp, color = GreenPrimary, fontWeight = FontWeight.Bold)
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
        Text(
            text = "TU CAMINO CERTIFICADO\nHACIA UNA VENTA DE\nAUTO SEGURA",
            textAlign = TextAlign.Center,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
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

            // Brand dropdown
            Text("Brand", color = TextSecondary, fontSize = 14.sp)
            var brandExpanded by remember { mutableStateOf(false) }
            var selectedBrand by remember { mutableStateOf("Select brand") }
            ExposedDropdownMenuBox(expanded = brandExpanded, onExpandedChange = { brandExpanded = !brandExpanded }) {
                TextField(
                    value = selectedBrand,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = brandExpanded) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF7F7F7),
                        unfocusedContainerColor = Color(0xFFF7F7F7)
                    )
                )
                ExposedDropdownMenu(expanded = brandExpanded, onDismissRequest = { brandExpanded = false }) {
                    listOf("Audi", "Mercedes-Benz", "BMW", "Volkswagen").forEach { option ->
                        DropdownMenuItem(text = { Text(option) }, onClick = {
                            selectedBrand = option
                            brandExpanded = false
                        })
                    }
                }
            }

            // Model dropdown
            Text("Model", color = TextSecondary, fontSize = 14.sp)
            var modelExpanded by remember { mutableStateOf(false) }
            var selectedModel by remember { mutableStateOf("Select model") }
            ExposedDropdownMenuBox(expanded = modelExpanded, onExpandedChange = { modelExpanded = !modelExpanded }) {
                TextField(
                    value = selectedModel,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = modelExpanded) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF7F7F7),
                        unfocusedContainerColor = Color(0xFFF7F7F7)
                    )
                )
                ExposedDropdownMenu(expanded = modelExpanded, onDismissRequest = { modelExpanded = false }) {
                    listOf("Model A", "Model B", "Model C").forEach { option ->
                        DropdownMenuItem(text = { Text(option) }, onClick = {
                            selectedModel = option
                            modelExpanded = false
                        })
                    }
                }
            }

            // Buttons
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BlueAccent)
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Search", color = Color.White)
            }

            OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
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
        items(3) { _ ->
            CarCard()
        }
    }
}

@Composable
private fun CarCard() {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(260.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Imagen del auto en blanco (placeholder)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.White),
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Kia Niro", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Text("S/3,900.00", color = TextPrimary, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text("View details", color = TextSecondary)
            }
        }
    }
}

@Composable
private fun CertificateCTASection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        // Imagen amplia
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
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

        listOf(
            "Audi" to Color(0xFFDE6B76),
            "Mercedes-Benz" to Color(0xFF79C7E7),
            "BMW" to Color(0xFF84B9D9),
            "Volkswagen" to Color(0xFF8AA9C1)
        ).forEach { (brand, color) ->
            BrandCard(brand = brand, ringColor = color)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun BrandCard(brand: String, ringColor: Color) {
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
                // Logo circular placeholder
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(Color(0xFFF5F5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(brand.take(1), fontWeight = FontWeight.Bold, color = TextPrimary)
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
private fun DrawerContent(onLogout: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(8.dp))
        SectionTitle("NAVEGACIÓN")
        DrawerItem(
            icon = { Icon(Icons.Outlined.DirectionsCar, contentDescription = null, tint = GreenPrimary) },
            title = "Certified Cars for Sale",
            subtitle = "Explora vehículos certificados"
        )
        DrawerItem(
            icon = { Icon(Icons.Outlined.AddCircleOutline, contentDescription = null, tint = GreenPrimary) },
            title = "Certify your Car",
            subtitle = "Certifica tu vehículo"
        )

        HorizontalDivider(color = DividerLight, modifier = Modifier.padding(vertical = 12.dp))

        SectionTitle("CUENTA")
        DrawerItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null, tint = GreenPrimary) },
            title = "Profile",
            subtitle = "Gestiona tu perfil"
        )
        DrawerItem(
            icon = { Icon(Icons.Outlined.History, contentDescription = null, tint = GreenPrimary) },
            title = "History",
            subtitle = "Historial de actividades"
        )
        DrawerItem(
            icon = { Icon(Icons.AutoMirrored.Outlined.HelpOutline, contentDescription = null, tint = GreenPrimary) },
            title = "Support",
            subtitle = "Ayuda y soporte"
        )
        DrawerItem(
            icon = { Icon(Icons.Outlined.Article, contentDescription = null, tint = GreenPrimary) },
            title = "Terms of Use",
            subtitle = "Términos y condiciones"
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
private fun DrawerItem(icon: @Composable () -> Unit, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
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
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextSecondary)
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
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color(0xFFD32F2F))
    }
}