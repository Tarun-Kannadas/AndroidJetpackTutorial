package com.example.firstjetpackapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firstjetpackapp.model.AppDatabase
import com.example.firstjetpackapp.repository.AuthRepository
import com.example.firstjetpackapp.ui.theme.FirstJetpackAppTheme
import com.example.firstjetpackapp.viewmodel.AuthViewModel
import com.example.firstjetpackapp.viewmodel.AuthViewModelFactory

class LoginActivity : ComponentActivity() {

    // 1. Initialize DB and Repository
    private val db by lazy { AppDatabase.getDatabase(applicationContext) }
    private val repo by lazy { AuthRepository(db.userDao()) }

    // 2. Initialize ViewModel
    private val viewmodel: AuthViewModel by viewModels {
        AuthViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstJetpackAppTheme {

                // 3. Observe the login state from the ViewModel
                val isLoginSuccess by viewmodel.loginSuccess

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        modifier = Modifier.padding(innerPadding),
                        isLoginSuccess = isLoginSuccess, // Pass the state down
                        onLoginAttempt = { username, password ->
                            // Tell the ViewModel to attempt login
                            viewmodel.loginUser(username, password)
                        },
                        onLoginSuccessNavigate = { username ->
                            // This runs when isLoginSuccess becomes true
                            Toast.makeText(this, "Welcome $username", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, HomeActivity::class.java).apply {
                                putExtra("EXTRA_USERNAME", username) // "EXTRA_USERNAME" is our key
                            }
                            startActivity(intent)
                            finish()
                        },
                        onRegisterClick = {
                            startActivity(Intent(this, RegisterActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    isLoginSuccess: Boolean = false,
    onLoginAttempt: (String, String) -> Unit = { _, _ -> },
    onLoginSuccessNavigate: (String) -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    val buttonModifier = Modifier.width(200.dp).height(48.dp)

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 4. Listen for the success state to change!
    // Whenever isLoginSuccess changes to true, this block executes.
    LaunchedEffect(isLoginSuccess) {
        if (isLoginSuccess) {
            onLoginSuccessNavigate(username)
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login Page", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // 5. Trigger the ViewModel function via our callback
                if (username.isNotBlank() && password.isNotBlank()) {
                    onLoginAttempt(username, password)
                }
            },
            modifier = buttonModifier
        ) {
            Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRegisterClick, modifier = buttonModifier) {
            Text("Register", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    FirstJetpackAppTheme {
        LoginScreen()
    }
}