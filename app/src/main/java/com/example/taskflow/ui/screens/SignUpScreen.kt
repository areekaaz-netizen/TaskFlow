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
import androidx.compose.material.icons.filled.Person
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

private val SignUpPurple = Color(0xFF5B5FEF)
private val SignUpDark = Color(0xFF20243A)
private val SignUpGray = Color(0xFF6B7280)
private val SignUpBackground = Color(0xFFF8F7FF)
private val SignUpBorder = Color(0xFFD8DAE5)
private val SignUpPlaceholder = Color(0xFF9CA3AF)
private val SignUpError = Color(0xFFDC2626)

@Composable
fun SignUpScreen(
    onAccountCreated: () -> Unit = {},
    onSignInClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val sessionManager = remember(context) {
        SessionManager(context.applicationContext)
    }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var fullNameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }

    var accountErrorMessage by remember {
        mutableStateOf<String?>(null)
    }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = SignUpDark,
        unfocusedTextColor = SignUpDark,
        disabledTextColor = SignUpGray,

        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        disabledContainerColor = Color.White,

        focusedBorderColor = SignUpPurple,
        unfocusedBorderColor = SignUpBorder,
        errorBorderColor = SignUpError,

        focusedLabelColor = SignUpPurple,
        unfocusedLabelColor = SignUpGray,
        errorLabelColor = SignUpError,

        focusedLeadingIconColor = SignUpPurple,
        unfocusedLeadingIconColor = SignUpGray,
        errorLeadingIconColor = SignUpError,

        focusedTrailingIconColor = SignUpPurple,
        unfocusedTrailingIconColor = SignUpGray,
        errorTrailingIconColor = SignUpError,

        focusedPlaceholderColor = SignUpPlaceholder,
        unfocusedPlaceholderColor = SignUpPlaceholder,

        cursorColor = SignUpPurple,
        errorCursorColor = SignUpError,

        focusedSupportingTextColor = SignUpGray,
        unfocusedSupportingTextColor = SignUpGray,
        errorSupportingTextColor = SignUpError
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFDFDFF),
                        SignUpBackground
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
            Spacer(modifier = Modifier.height(38.dp))

            Image(
                painter = painterResource(
                    id = R.drawable.taskflow_logo
                ),
                contentDescription = "TaskFlow logo",
                modifier = Modifier.size(82.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Task",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SignUpDark
                )

                Text(
                    text = "Flow",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SignUpPurple
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = SignUpDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Start organizing your life with TaskFlow.",
                modifier = Modifier.fillMaxWidth(0.88f),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = SignUpGray
            )

            Spacer(modifier = Modifier.height(26.dp))

            OutlinedTextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                    fullNameError = false
                    accountErrorMessage = null
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Full name")
                },
                placeholder = {
                    Text("Enter your full name")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                },
                singleLine = true,
                isError = fullNameError,
                supportingText = {
                    if (fullNameError) {
                        Text("Enter at least 2 characters.")
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = fieldColors
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = false
                    accountErrorMessage = null
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
                        Text("Enter a valid email address.")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                shape = RoundedCornerShape(16.dp),
                colors = fieldColors
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = false
                    confirmPasswordError = false
                    accountErrorMessage = null
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Password")
                },
                placeholder = {
                    Text("Minimum 6 characters")
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
                            "Password must contain at least 6 characters."
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = false
                    accountErrorMessage = null
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Confirm password")
                },
                placeholder = {
                    Text("Re-enter your password")
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
                            confirmPasswordVisible =
                                !confirmPasswordVisible
                        }
                    ) {
                        Icon(
                            imageVector =
                                if (confirmPasswordVisible) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                            contentDescription =
                                if (confirmPasswordVisible) {
                                    "Hide password"
                                } else {
                                    "Show password"
                                }
                        )
                    }
                },
                singleLine = true,
                isError = confirmPasswordError,
                supportingText = {
                    if (confirmPasswordError) {
                        Text("Passwords do not match.")
                    }
                },
                visualTransformation =
                    if (confirmPasswordVisible) {
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

            if (accountErrorMessage != null) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = accountErrorMessage.orEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = SignUpError
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = {
                    val cleanedName = fullName.trim()
                    val cleanedEmail =
                        email.trim().lowercase()

                    val validName =
                        cleanedName.length >= 2

                    val validEmail =
                        cleanedEmail.isNotBlank() &&
                                Patterns.EMAIL_ADDRESS
                                    .matcher(cleanedEmail)
                                    .matches()

                    val validPassword =
                        password.length >= 6

                    val validConfirmation =
                        confirmPassword.isNotBlank() &&
                                password == confirmPassword

                    fullNameError = !validName
                    emailError = !validEmail
                    passwordError = !validPassword
                    confirmPasswordError = !validConfirmation
                    accountErrorMessage = null

                    if (
                        validName &&
                        validEmail &&
                        validPassword &&
                        validConfirmation
                    ) {
                        if (sessionManager.isAccountRegistered()) {
                            accountErrorMessage =
                                "An account already exists on this device. Please sign in instead."
                        } else {
                            sessionManager.registerUser(
                                fullName = cleanedName,
                                email = cleanedEmail,
                                password = password
                            )

                            // Option B:
                            // Account is created, but the user must sign in.
                            sessionManager.setLoggedIn(false)

                            onAccountCreated()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SignUpPurple
                )
            ) {
                Text(
                    text = "Create Account",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account?",
                    fontSize = 15.sp,
                    color = SignUpGray
                )

                TextButton(
                    onClick = onSignInClick
                ) {
                    Text(
                        text = "Sign In",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SignUpPurple
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
private fun SignUpScreenPreview() {
    TaskFlowTheme(
        dynamicColor = false
    ) {
        SignUpScreen()
    }
}