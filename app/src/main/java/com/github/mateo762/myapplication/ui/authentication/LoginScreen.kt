import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.github.mateo762.myapplication.authentication.RegisterActivity

@Composable
fun LoginScreen(googleSignIn: () -> Unit, signInUser: (String, String) -> Unit) {
    val context = LocalContext.current
    val email = remember { mutableStateOf("") }
    val emailFiller by remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordFiller by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = emailFiller,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text(text = "Email") },
            modifier = Modifier
                .padding(16.dp)
                .testTag("text_email")
        )
        Text(
            text = passwordFiller,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .padding(16.dp)
                .testTag("text_password")
        )
        Button(
            onClick = {
                signInUser(email.value, password.value)
            },
            modifier = Modifier
                .padding(16.dp)
                .testTag("btn_login")
        ) {
            Text(text = "Login")
        }
        Button(
            onClick = {
                val intent = Intent(context, RegisterActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .padding(16.dp)
                .testTag("btn_not_registered")
        ) {
            Text(text = "Still don't have an account? Register now")
        }
        Button(
            onClick = {
                googleSignIn()
            },
            modifier = Modifier
                .padding(16.dp)
                .testTag("btn_login_google")
        ) {
            Text(text = "Sign in with Google")
        }
    }
}