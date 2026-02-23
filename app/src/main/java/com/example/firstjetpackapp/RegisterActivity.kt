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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.firstjetpackapp.viewmodel.AuthViewModelHilt
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // 1. Tell Hilt to inject into this Activity
class RegisterActivity : ComponentActivity() {

//    // 1. Initialize DB, Repo, and ViewModel
//    private val db by lazy { AppDatabase.getDatabase(applicationContext) }
//    private val repo by lazy { AuthRepository(db.userDao()) }
//    private val viewmodel: AuthViewModel by viewModels {
//        AuthViewModelFactory(repo)
//    }

    // 2. Hilt Magic! No factory, no DB, no repo needed. Just this one line:
    private val viewmodel: AuthViewModelHilt by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstJetpackAppTheme {

                // 2. Observe the registration success state
                val isRegistrationSuccess by viewmodel.registrationSuccessHilt

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterScreen(
                        modifier = Modifier.padding(innerPadding),
                        isRegistrationSuccess = isRegistrationSuccess,

                        // FIX: Now accepts all 4 strings from the UI
                        onRegisterAttempt = { username, password, phoneNumber, email ->
                            // 3. Ask ViewModel to save the user to the Room DB
                            viewmodel.registerUserHilt(username, password, phoneNumber, email)
                        },
                        onNavigateToLogin = {
                            // 4. Handle navigation up here in the Activity
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    isRegistrationSuccess: Boolean = false,

    // FIX: Updated signature to expect 4 Strings instead of 2
    onRegisterAttempt: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onNavigateToLogin: () -> Unit = {}
) {
    val context = LocalContext.current
    val buttonModifier = Modifier.width(200.dp).height(48.dp)

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 5. LaunchedEffect listens for the ViewModel's state change
    LaunchedEffect(isRegistrationSuccess) {
        if (isRegistrationSuccess) {
            Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
            onNavigateToLogin()
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Register Page", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username, onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phoneNumber, onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Password") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // 6. Trigger the hoisted register function with all 4 fields
                if (username.isNotBlank() && password.isNotBlank() && phoneNumber.isNotBlank() && email.isNotBlank()) {
                    onRegisterAttempt(username, password, phoneNumber, email)
                } else {
                    Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = buttonModifier
        ) {
            Text("Register", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNavigateToLogin, // 7. Trigger the hoisted navigation function
            modifier = buttonModifier
        ) {
            Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    FirstJetpackAppTheme { RegisterScreen() }
}