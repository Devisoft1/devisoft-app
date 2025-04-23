package com.example.devisoft.activities

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devisoft.R
import com.example.devisoft.models.OutstandingItem
import com.example.devisoft.network.RetrofitInstance
import com.example.devisoft.ui.theme.MyApplicationTheme
import com.example.devisoft.utils.PrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.text.format

class OutstandingReceivableActivity : ComponentActivity() {

    private val userId = "6798ce99ea2f43a4322f5f6c"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefManager = PrefManager(this)
        val accessToken = "Bearer ${prefManager.getAccessToken() ?: ""}"
        val userId = prefManager.getUserId() ?: ""


        if (accessToken.isEmpty()) {
            Log.e(
                "MainActivity",
                "Access Token is missing! Redirect to login or handle this properly."
            )
            // You could redirect to login or show a message here
        }
        setContent {
            MyApplicationTheme {
                var dataList by remember { mutableStateOf<List<OutstandingItem>>(emptyList()) }
                var errorMessage by remember { mutableStateOf<String?>(null) }
                var isLoading by remember { mutableStateOf(true) }
                val expandedState = remember { mutableStateMapOf<String, Boolean>() }
                val billExpandedState = remember { mutableStateMapOf<String, Boolean>() }
                var searchQuery by remember { mutableStateOf("") }
                var showSearchField by remember { mutableStateOf(false) }

                val itemsPerPage = 25
                var currentPage by remember { mutableStateOf(1) }

                val filteredDataList =
                    dataList.filter { it.field5.contains(searchQuery, ignoreCase = true) }
                val groupedData = filteredDataList.groupBy { it.field5 }
                val totalPages = (groupedData.size + itemsPerPage - 1) / itemsPerPage


                // ✅ Launch API call with retrieved accessToken
                LaunchedEffect(true) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = RetrofitInstance.api.getOutstandingReceivables(
                                token = accessToken,
                                userId = userId,
                                date = "2025-04-08",
                                compCode = "HRXX20242025"
                            )
                            if (response.isSuccessful) {
                                response.body()?.let {
                                    dataList = it
                                }
                            } else {
                                errorMessage = "Error: ${response.code()} - ${response.message()}"
                            }
                        } catch (e: Exception) {
                            errorMessage = "Exception: ${e.localizedMessage}"
                        } finally {
                            isLoading = false
                        }
                    }
                }

                // Reset pagination when search query changes
                LaunchedEffect(searchQuery) {
                    currentPage = 1
                }

                Scaffold(modifier = Modifier.Companion.fillMaxSize()) { padding ->
                    Box(
                        modifier = Modifier.Companion
                            .fillMaxSize()
                            .padding(padding)
                            .background(Color.Companion.White)
                    ) {
                        // Loading Spinner
                        if (isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0x66000000)) // Semi-transparent background
                                    .wrapContentSize(Alignment.Center)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator(color = Color.Black)
                                    Spacer(modifier = Modifier.height(16.dp)) // Optional space between spinner and text
                                    Text(
                                        text = "Synchronizing....",
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        // Show error message if available
                        if (errorMessage != null) {
                            Box(
                                modifier = Modifier.Companion
                                    .fillMaxSize()
                                    .wrapContentSize(align = Alignment.Companion.Center)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = errorMessage!!,
                                    color = Color.Companion.Red,
                                    fontSize = 16.sp,
                                    modifier = Modifier.Companion
                                        .padding(16.dp)
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.Companion
                                .fillMaxSize()
                                .padding(12.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                verticalAlignment = Alignment.CenterVertically // This ensures vertical centering
                            ) {
                                // Back Button on the left (Fixed width)
                                IconButton(
                                    onClick = { onBackPressedDispatcher.onBackPressed() },
                                    modifier = Modifier.width(50.dp) // fixed width for the back button
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.Black
                                    )
                                }

                                // Title (Centered)
                                Text(
                                    text = "Outstanding Receivables",
                                    fontSize = 20.sp,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .weight(1f) // This will make the title centered and take remaining space
                                        .align(Alignment.CenterVertically), // Ensure vertical centering of the text
                                    textAlign = TextAlign.Center
                                )

                                // Search Icon Button on the right (Fixed width)
                                IconButton(
                                    onClick = { showSearchField = !showSearchField },
                                    modifier = Modifier.width(50.dp) // fixed width for the search icon
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = Color.Black
                                    )
                                }
                            }


                            // Show Search TextField if visible
                            if (showSearchField) {
                                TextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    placeholder = {
                                        Text(
                                            text = "Search Party Name...",
                                            color = Color.Companion.Black
                                        )
                                    },
                                    singleLine = true,
                                    modifier = Modifier.Companion
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFD3D3D3),
                                        unfocusedContainerColor = Color(0xFFD3D3D3),
                                        disabledContainerColor = Color(0xFFD3D3D3),
                                        focusedIndicatorColor = Color.Companion.Transparent,
                                        unfocusedIndicatorColor = Color.Companion.Transparent,
                                        cursorColor = Color.Companion.Black,
                                        focusedTextColor = Color.Companion.Black,
                                        unfocusedTextColor = Color.Companion.Black
                                    )
                                )
                            }

                            // Use filtered data list to display the results
                            val paginatedData = groupedData.toList()
                                .drop((currentPage - 1) * itemsPerPage)
                                .take(itemsPerPage)

                            paginatedData.forEach { (partyName, items) ->
                                val isExpanded = expandedState[partyName] ?: false

                                Column(
                                    modifier = Modifier.Companion
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp)
                                ) {
                                    // Party Header Row
                                    Row(
                                        modifier = Modifier.Companion
                                            .fillMaxWidth()
                                            .background(Color(0xFFE0E0E0))
                                            .clickable {
                                                expandedState[partyName] = !isExpanded
                                            }
                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.Companion.CenterVertically
                                    ) {
                                        // Party Name
                                        Text(
                                            text = partyName,
                                            fontSize = 15.sp,
                                            color = Color.Black,
                                            modifier = Modifier.weight(5f)
                                        )

                                        // Total Amount + Arrow
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.End,
                                            modifier = Modifier.weight(2f)
                                        ) {
                                            // Calculate the total amount
                                            val totalAmount = items.sumOf {
                                                it.field11.toDoubleOrNull() ?: 0.0
                                            }

                                            // Display the total amount with color change for negative values
                                            Text(
                                                text = "%.2f  ".format(totalAmount),
                                                fontSize = 12.sp,
                                                color = if (totalAmount < 0) Color.Red else Color.Black // Red if negative
                                            )

                                            Spacer(modifier = Modifier.Companion.width(4.dp)) // Slight spacing between text and arrow

                                            Icon(
                                                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                                contentDescription = "Toggle expand",
                                                tint = Color.Companion.Black
                                            )
                                        }
                                    }

                                    // Show Bills if Party is Expanded
                                    if (isExpanded) {
                                        // Show Bill Table Headers
                                        TableHeader()

                                        items.forEachIndexed { index, item ->
                                            val billKey = "${partyName}_${item.field3}"
                                            val isBillExpanded = billExpandedState[billKey] ?: false

                                            TableRow(item, index, billExpandedState, billKey)

                                            if (isBillExpanded) {
                                                val Receivedamt =
                                                    item.field10.toDoubleOrNull() ?: 0.0
                                                val Returnamt =
                                                    item.field8.toDoubleOrNull() ?: 0.0
                                                val Balanceamt =
                                                    item.field11.toDoubleOrNull() ?: 0.0
                                                DisplayTotal(
                                                    Receivedamt,
                                                    Returnamt,
                                                    Balanceamt
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Pagination Control
                            if (!isLoading && errorMessage == null && groupedData.isNotEmpty()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    // Previous Button
                                    Button(
                                        onClick = { if (currentPage > 1) currentPage-- },
                                        modifier = Modifier.padding(end = 8.dp),
                                        enabled = currentPage > 1,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Blue,
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text("Previous")
                                    }

                                    // Page Number Display
                                    Text(
                                        text = "Page $currentPage of $totalPages",
                                        fontSize = 14.sp,
                                        color = Color.Black,
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        textAlign = TextAlign.Center
                                    )

                                    // Next Button
                                    Button(
                                        onClick = { if (currentPage < totalPages) currentPage++ },
                                        modifier = Modifier.padding(start = 8.dp),
                                        enabled = currentPage < totalPages,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Blue,
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text("Next")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TableHeader() {
        val lightBorderColor = Color.Companion.LightGray
        val headerHeight = 30.dp
        val textStyle = LocalTextStyle.current.copy(color = Color(0xFF000000), fontSize = 15.sp)

        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .background(Color(0xFF70A8F8))
                .height(headerHeight)
                .border(BorderStroke(0.2.dp, lightBorderColor)) // Use light gray
        ) {
            listOf("Invc No", "Invc Date", "Invc Amt", "Over Due Days").forEach { title ->
                Box(
                    modifier = Modifier.Companion
                        .weight(1f)
                        .fillMaxHeight()
                        .border(0.2.dp, lightBorderColor) // Use light gray
                        .padding(4.dp),
                    contentAlignment = Alignment.Companion.Center
                ) {
                    Text(
                        text = title,
                        style = textStyle,
                        textAlign = TextAlign.Companion.Center
                    )
                }
            }
        }
    }

    @Composable
    fun TableRow(
        item: OutstandingItem,
        index: Int,
        billExpandedState: MutableMap<String, Boolean>,
        billKey: String
    ) {
        val rowHeight = 30.dp
        val backgroundColor = if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White
        val cellStyle = LocalTextStyle.current.copy(fontSize = 12.sp, color = Color.Black)
        val isExpanded = billExpandedState[billKey] ?: false
        val lightBorderColor = Color.LightGray

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(rowHeight)
                .background(backgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .weight(1.5f)
                    .fillMaxHeight()
                    .border(0.2.dp, lightBorderColor)
                    .padding(horizontal = 4.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { billExpandedState[billKey] = !isExpanded },
                        modifier = Modifier.size(14.dp)
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (isExpanded) "Hide details" else "Show details",
                            tint = Color.Black
                        )
                    }

                    Text(
                        text = item.field3,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .weight(1f),
                        style = cellStyle,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Helper function to determine if the value should be red (negative)
            fun getTextColor(value: String): Color {
                return try {
                    if (value.toDouble() < 0) Color.Red else Color.Black
                } catch (e: Exception) {
                    Color.Black // Default color if conversion fails
                }
            }

            listOf(item.field4, item.field7, item.field18).forEach { text ->
                Text(
                    text = text,
                    modifier = Modifier
                        .weight(1.5f)
                        .fillMaxHeight()
                        .border(0.2.dp, lightBorderColor)
                        .padding(4.dp),
                    style = cellStyle.copy(color = getTextColor(text)),
                    textAlign = TextAlign.Center
                )
            }
        }
    }


    @Composable
    fun DisplayTotal(
        Receivedamt: Double,
        Returnamt: Double,
        balanceamt: Double
    ) {
        val lightBorderColor = Color.LightGray

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, lightBorderColor)
                .padding(5.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Received Amt: %.2f".format(Receivedamt),
                    fontSize = 12.sp,
                    color = if (Receivedamt < 0.0) Color.Red else Color.Black
                )
                Text(
                    text = "Return Amt: %.2f".format(Returnamt),
                    fontSize = 12.sp,
                    color = if (Returnamt < 0.0) Color.Red else Color.Black
                )
                Text(
                    text = "Balance Amt: %.2f".format(balanceamt),
                    fontSize = 12.sp,
                    color = if (balanceamt < 0.0) Color.Red else Color.Black
                )
            }
        }
    }
}