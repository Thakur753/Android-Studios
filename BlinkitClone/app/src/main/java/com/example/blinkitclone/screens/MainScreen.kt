package com.example.blinkitclone.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.blinkitclone.navigation.Routes
import com.example.blinkitclone.viewmodels.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.blinkitclone.models.Order
import com.example.blinkitclone.models.Product
import com.example.blinkitclone.models.ChatMessage

@Composable
fun MainScreen(parentNavController: NavHostController, viewModel: HomeViewModel = viewModel()) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val cartItems by viewModel.cart.collectAsState()
    
    val items = listOf(
        Triple("Home", Routes.Home.route, Icons.Default.Home),
        Triple("Categories", "categories", Icons.AutoMirrored.Filled.List),
        Triple("Print", "print", Icons.Default.Print),
        Triple("Profile", "profile", Icons.Default.Person)
    )

    Scaffold(
        bottomBar = {
            Column {
                if (cartItems.isNotEmpty() && selectedItem != 2) {
                    CartBarUI(cartItems.size, cartItems.sumOf { it.price!! * it.itemCount }) {
                        selectedItem = 2 // Switch to Cart/Orders
                    }
                }
                NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { 
                                BadgedBox(badge = {
                                    if (item.first == "Print" && cartItems.isNotEmpty()) {
                                        Badge(containerColor = Color(0xFF318616)) { Text(cartItems.size.toString(), color = Color.White) }
                                    }
                                }) {
                                    Icon(item.third, contentDescription = item.first)
                                }
                            },
                            label = { Text(item.first, fontSize = 10.sp, fontWeight = if(selectedItem == index) FontWeight.Bold else FontWeight.Normal) },
                            selected = selectedItem == index,
                            onClick = { selectedItem = index },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Black,
                                selectedTextColor = Color.Black,
                                indicatorColor = Color.Transparent,
                                unselectedIconColor = Color.DarkGray,
                                unselectedTextColor = Color.DarkGray
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedItem) {
                0 -> HomeScreen(parentNavController, viewModel)
                1 -> CategoriesScreen(viewModel)
                2 -> CartScreen(viewModel)
                3 -> ProfileScreen(parentNavController)
                4 -> SupportChatScreen(parentNavController, viewModel)
            }
        }
    }
}

@Composable
fun SupportChatScreen(navController: NavHostController, viewModel: HomeViewModel) {
    val messages by viewModel.chatMessages.collectAsState()
    val isBotTyping by viewModel.isBotTyping.collectAsState()
    var textState by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size, isBotTyping) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF1F1F1))) {
        Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shadowElevation = 2.dp) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = null) }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = "Blinkit Assistant", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = if (isBotTyping) "Bot is typing..." else "Online", color = if(isBotTyping) Color(0xFF318616) else Color.DarkGray, fontSize = 12.sp)
                }
            }
        }

        LazyColumn(state = listState, modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
            items(messages) { message ->
                ChatBubble(message)
            }
            if (isBotTyping) {
                item {
                    Text(text = "typing...", color = Color.DarkGray, fontSize = 12.sp, modifier = Modifier.padding(8.dp))
                }
            }
        }

        Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shadowElevation = 16.dp) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = textState,
                    onValueChange = { textState = it },
                    placeholder = { Text("Ask about order, refund, coupon...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF318616),
                        unfocusedBorderColor = Color.LightGray
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                FloatingActionButton(
                    onClick = { 
                        if (textState.isNotBlank()) {
                            viewModel.sendMessage(textState)
                            textState = ""
                        }
                    },
                    modifier = Modifier.size(48.dp),
                    containerColor = Color(0xFF318616),
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val bubbleColor = if (message.isUser) Color(0xFF318616) else Color.White
    val textColor = if (message.isUser) Color.White else Color.Black
    val shape = if (message.isUser) {
        RoundedCornerShape(16.dp, 16.dp, 2.dp, 16.dp)
    } else {
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 2.dp)
    }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalAlignment = alignment) {
        Surface(color = bubbleColor, shape = shape, shadowElevation = 1.dp) {
            Text(text = message.message, color = textColor, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), fontSize = 14.sp)
        }
    }
}

@Composable
fun CartBarUI(itemCount: Int, totalPrice: Int, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
        color = Color(0xFF318616),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(text = "$itemCount ITEMS", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = "₹$totalPrice", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "View Cart", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null, tint = Color.White)
            }
        }
    }
}

@Composable
fun CategoriesScreen(viewModel: HomeViewModel) {
    val categories by viewModel.categories.collectAsState()
    val displayCategories = if (categories.isNotEmpty()) categories else getDemoCategories()

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Text(text = "All Categories", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(16.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(displayCategories) { category ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(50.dp).clip(CircleShape).background(Color(0xFFF3F9F3)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Category, contentDescription = null, tint = Color(0xFF318616))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = category.name ?: "", fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color(0xFFEEEEEE))
            }
        }
    }
}

@Composable
fun CartScreen(viewModel: HomeViewModel) {
    val cartItems by viewModel.cart.collectAsState()
    var orderStatus by remember { mutableStateOf("Cart") } // Cart or Tracking

    if (orderStatus == "Tracking") {
        OrderTrackingUI(viewModel) { orderStatus = "Cart" }
    } else if (cartItems.isEmpty()) {
        EmptyCartUI()
    } else {
        Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF1F1F1))) {
            Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shadowElevation = 2.dp) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Checkout", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text(text = "${cartItems.size} items", color = Color.DarkGray)
                }
            }
            
            LazyColumn(modifier = Modifier.weight(1f).padding(16.dp)) {
                item {
                    Text(text = "Items in Cart", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
                }
                items(cartItems) { product ->
                    CartItemUI(product, viewModel)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    BillSummaryUI(cartItems)
                }
            }

            // Place Order Button
            Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shadowElevation = 16.dp) {
                Button(
                    onClick = { 
                        viewModel.placeOrder()
                        orderStatus = "Tracking" 
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp).height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF318616)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Place Order • ₹${cartItems.sumOf { it.price!! * it.itemCount }}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CartItemUI(product: Product, viewModel: HomeViewModel) {
    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFF8F8F8)), contentAlignment = Alignment.Center) {
                Image(painter = painterResource(id = com.example.blinkitclone.R.drawable.image_prod), contentDescription = null)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.name ?: "", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = product.unit ?: "", fontSize = 12.sp, color = Color.DarkGray)
                Text(text = "₹${product.price}", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
            }
            // Quantity Controls
            Surface(shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, Color(0xFF318616)), color = Color(0xFFF1F9F1)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.removeFromCart(product) }, modifier = Modifier.size(30.dp)) {
                        Text("-", color = Color(0xFF318616), fontWeight = FontWeight.Bold)
                    }
                    Text(text = product.itemCount.toString(), fontWeight = FontWeight.Bold, color = Color(0xFF318616), modifier = Modifier.padding(horizontal = 8.dp))
                    IconButton(onClick = { viewModel.addToCart(product) }, modifier = Modifier.size(30.dp)) {
                        Text("+", color = Color(0xFF318616), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun BillSummaryUI(items: List<Product>) {
    val total = items.sumOf { it.price!! * it.itemCount }
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Bill Summary", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Item Total", color = Color.DarkGray)
                Text(text = "₹$total")
            }
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Delivery Charge", color = Color.DarkGray)
                Text(text = "FREE", color = Color(0xFF318616), fontWeight = FontWeight.Bold)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Grand Total", fontWeight = FontWeight.Bold)
                Text(text = "₹$total", fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
fun OrderTrackingUI(viewModel: HomeViewModel, onBack: () -> Unit) {
    val remainingTime by viewModel.remainingTime.collectAsState()
    val minutes = remainingTime / 60
    val seconds = remainingTime % 60
    val timeString = String.format("%02d:%02d", minutes, seconds)

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Surface(modifier = Modifier.fillMaxWidth().height(300.dp), color = Color(0xFFE8F5E9)) {
            Box(contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color(0xFF318616))
                    Text("Order Placed Successfully!", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Arriving in $timeString", color = if(remainingTime < 60) Color.Red else Color.DarkGray, fontWeight = FontWeight.Bold)
                }
            }
        }
        Column(modifier = Modifier.padding(24.dp)) {
            Text(text = "Order Status", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(20.dp))
            StatusStep("Order Received", "10:05 AM", true)
            StatusStep("Packed by Store", "10:07 AM", true)
            StatusStep("Delivery Partner Assigned", "10:08 AM", true)
            StatusStep("Out for Delivery", "Pending", false)
            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) {
                Text("Back to Shopping")
            }
        }
    }
}

@Composable
fun StatusStep(title: String, time: String, isDone: Boolean) {
    Row(modifier = Modifier.padding(bottom = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(if (isDone) Color(0xFF318616) else Color.LightGray))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, modifier = Modifier.weight(1f), fontWeight = if(isDone) FontWeight.Bold else FontWeight.Normal)
        Text(text = time, color = Color.DarkGray, fontSize = 12.sp)
    }
}

@Composable
fun EmptyCartUI() {
    Column(modifier = Modifier.fillMaxSize().background(Color.White), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Image(painter = painterResource(id = com.example.blinkitclone.R.drawable.cart), contentDescription = null, modifier = Modifier.size(150.dp))
        Text("You don't have anything in your cart", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text("Search and add items to your cart", color = Color.DarkGray)
    }
}

@Composable
fun ProfileScreen(navController: NavHostController, viewModel: HomeViewModel = viewModel()) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    var activeTab by remember { mutableStateOf("Main") }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F8F8))) {
        if (activeTab == "Main") {
            Surface(modifier = Modifier.fillMaxWidth(), color = Color.White) {
                Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(60.dp).clip(CircleShape).background(Color(0xFFF1F1F1)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(40.dp), tint = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = user?.phoneNumber ?: "Guest User", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                        Text(text = "My Account", fontSize = 14.sp, color = Color(0xFF318616), fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Surface(modifier = Modifier.fillMaxWidth(), color = Color.White) {
                Column {
                    ProfileOptionUI("My Orders", Icons.Default.ShoppingBag) { activeTab = "Orders" }
                    ProfileOptionUI("Address Book", Icons.Default.LocationOn) { activeTab = "Address" }
                    ProfileOptionUI("Refund & Returns", Icons.Default.History) { activeTab = "Refunds" }
                    ProfileOptionUI("Customer Support", Icons.Default.SupportAgent) { activeTab = "Support" }
                    ProfileOptionUI("Developer Settings", Icons.Default.Settings) { activeTab = "Dev" }
                    TextButton(onClick = { auth.signOut(); navController.navigate(Routes.Auth.route) { popUpTo(0) } }, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text("Logout", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shadowElevation = 2.dp) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { activeTab = "Main" }) { Icon(Icons.Default.ArrowBack, contentDescription = null) }
                        Text(text = activeTab, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
                when (activeTab) {
                    "Orders" -> MyOrdersScreen(viewModel)
                    "Address" -> AddressBookScreen()
                    "Refunds" -> RefundStatusScreen()
                    "Support" -> CustomerSupportScreen()
                    "Dev" -> DeveloperSettingsScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun DeveloperSettingsScreen(viewModel: HomeViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Developer Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        Text("If your Firebase data is empty, you can use Demo Data to see the UI.", fontSize = 14.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.toggleDemoMode() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            val isDemo by viewModel.isDemoMode.collectAsState()
            Text(if (isDemo) "Switch to Firebase Mode" else "Switch to Demo Mode")
        }
    }
}

@Composable
fun MyOrdersScreen(viewModel: HomeViewModel) {
    val orders by viewModel.pastOrders.collectAsState()
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(orders) { order ->
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = order.orderId ?: "", fontWeight = FontWeight.Bold)
                        Text(text = order.status, color = if(order.status == "Delivered") Color(0xFF318616) else Color(0xFFFFA000), fontWeight = FontWeight.Bold)
                    }
                    Text(text = "₹${order.totalPrice}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(vertical = 4.dp))
                    Text(text = "Items: ${order.items.joinToString { it.name ?: "" }}", fontSize = 12.sp, color = Color.DarkGray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(text = "Delivered to: ${order.address}", fontSize = 12.sp, color = Color.DarkGray)
                }
            }
        }
    }
}

@Composable
fun AddressBookScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        AddressCard("Home", "H.No 123, Sector 45, Gurgaon, Haryana - 122003", true)
        AddressCard("Office", "Plot 44, Cyber City, Phase 2, Gurgaon - 122002", false)
        Button(onClick = {}, modifier = Modifier.fillMaxWidth().padding(top = 16.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF318616))) {
            Text("Add New Address")
        }
    }
}

@Composable
fun AddressCard(label: String, address: String, isDefault: Boolean) {
    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = if(isDefault) BorderStroke(1.dp, Color(0xFF318616)) else null) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(if(label == "Home") Icons.Default.Home else Icons.Default.Work, contentDescription = null, tint = Color.Gray)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = label, fontWeight = FontWeight.Bold)
                    if(isDefault) Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(4.dp), modifier = Modifier.padding(start = 8.dp)) {
                        Text("DEFAULT", color = Color(0xFF318616), fontSize = 8.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                    }
                }
                Text(text = address, fontSize = 13.sp, color = Color.DarkGray)
            }
        }
    }
}

@Composable
fun RefundStatusScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        RefundItem("ORD12345", "₹450", "Refund Processed", "Money will reach your bank in 3-5 days")
        RefundItem("ORD09876", "₹120", "Refund Initiated", "Waiting for bank confirmation")
    }
}

@Composable
fun RefundItem(orderId: String, amount: String, status: String, subtext: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Order $orderId", fontWeight = FontWeight.Bold)
                Text(text = amount, fontWeight = FontWeight.ExtraBold)
            }
            Text(text = status, color = Color(0xFF318616), fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
            Text(text = subtext, fontSize = 12.sp, color = Color.DarkGray)
        }
    }
}

@Composable
fun CustomerSupportScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("How can we help you?", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        SupportOption("Query regarding an order", Icons.Default.ShoppingBag)
        SupportOption("Payment or Refund related", Icons.Default.Payments)
        SupportOption("Feedback or Suggestions", Icons.Default.RateReview)
        SupportOption("Other issues", Icons.Default.Info)
        
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF318616))) {
            Text("Chat with us")
        }
    }
}

@Composable
fun SupportOption(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color(0xFF318616))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}

@Composable
fun ProfileOptionUI(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color.DarkGray, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 16.sp, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color(0xFFEEEEEE))
}
