package com.example.firstjetpackapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstjetpackapp.ui.theme.FirstJetpackAppTheme
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Unpack the Intent to get the username.
        // If it's missing for some reason, we default to "User".
        val loggedInUsername = intent.getStringExtra("EXTRA_USERNAME") ?: "User"

        enableEdgeToEdge()
        setContent {
            FirstJetpackAppTheme {
                // We no longer need the outer Scaffold here, the inner one is enough
                HomeScreen(
                    username = loggedInUsername // 2. Pass it to the Composable
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    username: String, // 3. Accept the username as a parameter
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "App Menu",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                NavigationDrawerItem(
                    label = { Text("Home") },
                    selected = true,
                    onClick = {
                        coroutineScope.launch {
                            Toast.makeText(context, "Home Clicked", Toast.LENGTH_SHORT).show()
                            drawerState.close()
                        }
                    },
                    icon = {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Jetpack App")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ) { padding ->
            // I combined your two Columns into one clean Column so the text and button stack nicely!
            Column(
                modifier = modifier
                    .padding(padding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center, // Centers everything vertically
                horizontalAlignment = Alignment.CenterHorizontally // Centers everything horizontally
            ) {
                // 4. Display the dynamic username!
                Text(
                    text = "Welcome\n$username",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.padding(16.dp)) // Adds some breathing room

                Button(
                    onClick = {
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    }
                ) {
                    Text("Logout")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    FirstJetpackAppTheme {
        // We pass a dummy name here just so the preview looks good
        HomeScreen(username = "Tarun")
    }
}