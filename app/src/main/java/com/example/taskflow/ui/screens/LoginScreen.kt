package com.example.taskflow.ui.screens

import android.util.Patterns
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.R
import com.example.taskflow.ui.theme.TaskFlowTheme
import com.example.taskflow.utils.SessionManager

private val LoginPurple = Color(0xFF5B5FEF)
private val LoginDarkText = Color(0xFF20243A)
private val LoginGrayText = Color(0xFF6B7280)
private val LoginBackground = Color(0xFFF8F7FF)
private val LoginBorder = Color(0xFFD8DAE5)
private val LoginPlaceholder = Color(0xFF9CA3AF)
private val LoginError = Color(0xFFDC2626)

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onCreateAccount: () -> Unit = {},
    onForgotPassword: () -> Unit = {}
) {
    val context = LocalContext.current

    val sessionManager = remember(context) {
        SessionManager(context.applicationContext)
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var emailError by remember {
        mutableStateOf(false)
    }

    var passwordError by remember {
        mutableStateOf(false)
    }

    var loginErrorMessage by remember {
        mutableStateOf<String?>(null)
    }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = LoginDarkText,
        unfocusedTextColor = LoginDarkText,
        disabledTextColor = LoginGrayText,

        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        disabledContainerColor = Color.White,

        focusedBorderColor = LoginPurple,
        unfocusedBorderColor = LoginBorder,
        errorBorderColor = LoginError,

        focusedLabelColor = LoginPurple,
        unfocusedLabelColor = LoginGrayText,
        errorLabelColor = LoginError,

        focusedLeadingIconColor = LoginPurple,
        unfocusedLeadingIconColor = LoginGrayText,
        errorLeadingIconColor = LoginError,

        focusedTrailingIconColor = LoginPurple,
        unfocusedTrailingIconColor = LoginGrayText,
        errorTrailingIconColor = LoginError,

        focusedPlaceholderColor = LoginPlaceholder,
        unfocusedPlaceholderColor = LoginPlaceholder,

        cursorColor = LoginPurple,
        errorCursorColor = LoginError,

        focusedSupportingTextColor = LoginGrayText,
        unfocusedSupportingTextColor = LoginGrayText,
        errorSupportingTextColor = LoginError
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFDFDFF),
                        LoginBackground
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(52.dp))

            Image(
                painter = painterResource(
                    id = R.drawable.taskflow_logo
                ),
                contentDescription = "TaskFlow logo",
                modifier = Modifier.size(88.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Task",
                    fontSize = 31.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LoginDarkText
                )

                Text(
                    text = "Flow",
                    fontSize = 31.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LoginPurple
                )
            }
            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Welcome back",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = LoginDarkText
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sign in to continue managing your tasks.",
                modifier = Modifier.fillMaxWidth(0.88f),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = LoginGrayText
            )
            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = false
                    loginErrorMessage = null
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Email address")
                },
                placeholder = {
                    Text("name@example.com")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null
                    )
                },
                singleLine = true,
                isError = emailError,
                supportingText = {
                    if (emailError) {
                        Text(
                            text = "Enter a valid email address."
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                shape = RoundedCornerShape(16.dp),
                colors = fieldColors
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = false
                    loginErrorMessage = null
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Password")
                },
                placeholder = {
                    Text("Enter your password")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {
                        Icon(
                            imageVector = if (passwordVisible) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = if (passwordVisible) {
                                "Hide password"
                            } else {
                                "Show password"
                            }
                        )
                    }
                },
                singleLine = true,
                isError = passwordError,
                supportingText = {
                    if (passwordError) {
                        Text(
                            text = "Password must contain at least 6 characters."
                        )
                    }
                },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                shape = RoundedCornerShape(16.dp),
                colors = fieldColors
            )

            if (loginErrorMessage != null) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = loginErrorMessage.orEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = LoginError
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onForgotPassword
                ) {
                    Text(
                        text = "Forgot password?",
                        color = LoginPurple,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    val cleanedEmail =
                        email.trim().lowercase()

                    val validEmail =
                        cleanedEmail.isNotBlank() &&
                                Patterns.EMAIL_ADDRESS
                                    .matcher(cleanedEmail)
                                    .matches()

                    val validPassword =
                        password.length >= 6

                    emailError = !validEmail
                    passwordError = !validPassword
                    loginErrorMessage = null

                    if (validEmail && validPassword) {
                        if (!sessionManager.isAccountRegistered()) {
                            loginErrorMessage =
                                "No account exists on this device. Create an account first."
                        } else {
                            val credentialsAreCorrect =
                                sessionManager.validateCredentials(
                                    email = cleanedEmail,
                                    password = password
                                )

                            if (credentialsAreCorrect) {
                                sessionManager.setLoggedIn(true)
                                onLoginSuccess()
                            } else {
                                loginErrorMessage =
                                    "Incorrect email or password."
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LoginPurple
                )
            ) {
                Text(
                    text = "Sign In",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    fontSize = 15.sp,
                    color = LoginGrayText
                )

                TextButton(
                    onClick = onCreateAccount
                ) {
                    Text(
                        text = "Create account",
                        color = LoginPurple,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun LoginScreenPreview() {
    TaskFlowTheme(
        dynamicColor = false
    ) {
        LoginScreen()
    }
}