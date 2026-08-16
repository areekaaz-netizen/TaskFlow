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
import androidx.compose.material.icons.filled.ArrowBack
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

private val ForgotPurple = Color(0xFF5B5FEF)
private val ForgotDark = Color(0xFF20243A)
private val ForgotGray = Color(0xFF6B7280)
private val ForgotBackground = Color(0xFFF8F7FF)
private val ForgotBorder = Color(0xFFD8DAE5)
private val ForgotPlaceholder = Color(0xFF9CA3AF)
private val ForgotError = Color(0xFFDC2626)
private val ForgotSuccess = Color(0xFF16A34A)

@Composable
fun ForgotPasswordScreen(
    onPasswordUpdated: () -> Unit = {},
    onBackToLogin: () -> Unit = {}
) {
    val context = LocalContext.current

    val sessionManager = remember(context) {
        SessionManager(context.applicationContext)
    }

    var email by remember {
        mutableStateOf("")
    }

    var newPassword by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var emailVerified by remember {
        mutableStateOf(false)
    }

    var emailError by remember {
        mutableStateOf<String?>(null)
    }

    var passwordError by remember {
        mutableStateOf<String?>(null)
    }

    var confirmPasswordError by remember {
        mutableStateOf<String?>(null)
    }

    var successMessage by remember {
        mutableStateOf<String?>(null)
    }

    var newPasswordVisible by remember {
        mutableStateOf(false)
    }

    var confirmPasswordVisible by remember {
        mutableStateOf(false)
    }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = ForgotDark,
        unfocusedTextColor = ForgotDark,
        disabledTextColor = ForgotGray,

        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        disabledContainerColor = Color.White,

        focusedBorderColor = ForgotPurple,
        unfocusedBorderColor = ForgotBorder,
        errorBorderColor = ForgotError,

        focusedLabelColor = ForgotPurple,
        unfocusedLabelColor = ForgotGray,
        errorLabelColor = ForgotError,

        focusedLeadingIconColor = ForgotPurple,
        unfocusedLeadingIconColor = ForgotGray,
        errorLeadingIconColor = ForgotError,

        focusedTrailingIconColor = ForgotPurple,
        unfocusedTrailingIconColor = ForgotGray,
        errorTrailingIconColor = ForgotError,

        focusedPlaceholderColor = ForgotPlaceholder,
        unfocusedPlaceholderColor = ForgotPlaceholder,

        cursorColor = ForgotPurple,
        errorCursorColor = ForgotError,

        focusedSupportingTextColor = ForgotGray,
        unfocusedSupportingTextColor = ForgotGray,
        errorSupportingTextColor = ForgotError
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFDFDFF),
                        ForgotBackground
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(horizontal = 26.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier.height(46.dp)
            )

            Image(
                painter = painterResource(
                    id = R.drawable.taskflow_logo
                ),
                contentDescription = "TaskFlow logo",
                modifier = Modifier.size(84.dp)
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text = "Task",
                    fontSize = 30.sp,
                    fontWeight =
                        FontWeight.ExtraBold,
                    color = ForgotDark
                )

                Text(
                    text = "Flow",
                    fontSize = 30.sp,
                    fontWeight =
                        FontWeight.ExtraBold,
                    color = ForgotPurple
                )
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Text(
                text = "Reset Password",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = ForgotDark
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = if (emailVerified) {
                    "Enter and confirm your new password."
                } else {
                    "Enter the email address registered on this device."
                },
                modifier = Modifier.fillMaxWidth(0.9f),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                lineHeight = 23.sp,
                color = ForgotGray
            )

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                    successMessage = null
                    emailVerified = false
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
                        imageVector =
                            Icons.Default.Email,
                        contentDescription = null
                    )
                },
                singleLine = true,
                enabled = !emailVerified,
                isError = emailError != null,
                supportingText = {
                    emailError?.let {
                        Text(it)
                    }
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Email
                    ),
                shape =
                    RoundedCornerShape(16.dp),
                colors = fieldColors
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            if (!emailVerified) {
                Button(
                    onClick = {
                        val cleanedEmail =
                            email.trim().lowercase()

                        val validFormat =
                            cleanedEmail.isNotBlank() &&
                                    Patterns.EMAIL_ADDRESS
                                        .matcher(
                                            cleanedEmail
                                        )
                                        .matches()

                        emailError = null
                        successMessage = null

                        when {
                            !validFormat -> {
                                emailError =
                                    "Enter a valid email address."
                            }

                            !sessionManager
                                .isAccountRegistered() -> {
                                emailError =
                                    "No account exists on this device."
                            }

                            !sessionManager
                                .isRegisteredEmail(
                                    cleanedEmail
                                ) -> {
                                emailError =
                                    "This email is not registered."
                            }

                            else -> {
                                emailVerified = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape =
                        RoundedCornerShape(18.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                ForgotPurple
                        )
                ) {
                    Text(
                        text = "Verify Email",
                        fontSize = 18.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            if (emailVerified) {
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        passwordError = null
                        confirmPasswordError = null
                        successMessage = null
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    label = {
                        Text("New password")
                    },
                    placeholder = {
                        Text("Minimum 6 characters")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector =
                                Icons.Default.Lock,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                newPasswordVisible =
                                    !newPasswordVisible
                            }
                        ) {
                            Icon(
                                imageVector =
                                    if (
                                        newPasswordVisible
                                    ) {
                                        Icons.Default
                                            .VisibilityOff
                                    } else {
                                        Icons.Default
                                            .Visibility
                                    },
                                contentDescription =
                                    if (
                                        newPasswordVisible
                                    ) {
                                        "Hide password"
                                    } else {
                                        "Show password"
                                    }
                            )
                        }
                    },
                    singleLine = true,
                    isError =
                        passwordError != null,
                    supportingText = {
                        passwordError?.let {
                            Text(it)
                        }
                    },
                    visualTransformation =
                        if (newPasswordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Password
                        ),
                    shape =
                        RoundedCornerShape(16.dp),
                    colors = fieldColors
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = null
                        successMessage = null
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    label = {
                        Text("Confirm new password")
                    },
                    placeholder = {
                        Text("Re-enter new password")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector =
                                Icons.Default.Lock,
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
                                    if (
                                        confirmPasswordVisible
                                    ) {
                                        Icons.Default
                                            .VisibilityOff
                                    } else {
                                        Icons.Default
                                            .Visibility
                                    },
                                contentDescription =
                                    if (
                                        confirmPasswordVisible
                                    ) {
                                        "Hide password"
                                    } else {
                                        "Show password"
                                    }
                            )
                        }
                    },
                    singleLine = true,
                    isError =
                        confirmPasswordError != null,
                    supportingText = {
                        confirmPasswordError?.let {
                            Text(it)
                        }
                    },
                    visualTransformation =
                        if (
                            confirmPasswordVisible
                        ) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Password
                        ),
                    shape =
                        RoundedCornerShape(16.dp),
                    colors = fieldColors
                )

                if (successMessage != null) {
                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(
                        text =
                            successMessage.orEmpty(),
                        modifier =
                            Modifier.fillMaxWidth(),
                        textAlign =
                            TextAlign.Center,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight =
                            FontWeight.SemiBold,
                        color = ForgotSuccess
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(22.dp)
                )

                Button(
                    onClick = {
                        val validPassword =
                            newPassword.length >= 6

                        val passwordsMatch =
                            confirmPassword
                                .isNotBlank() &&
                                    newPassword ==
                                    confirmPassword

                        passwordError =
                            if (validPassword) {
                                null
                            } else {
                                "Password must contain at least 6 characters."
                            }

                        confirmPasswordError =
                            if (passwordsMatch) {
                                null
                            } else {
                                "Passwords do not match."
                            }

                        successMessage = null

                        if (
                            validPassword &&
                            passwordsMatch
                        ) {
                            sessionManager
                                .updatePassword(
                                    newPassword
                                )

                            successMessage =
                                "Password updated successfully."

                            onPasswordUpdated()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape =
                        RoundedCornerShape(18.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                ForgotPurple
                        )
                ) {
                    Text(
                        text = "Update Password",
                        fontSize = 18.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            TextButton(
                onClick = onBackToLogin
            ) {
                Icon(
                    imageVector =
                        Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = ForgotPurple
                )

                Spacer(
                    modifier = Modifier.size(8.dp)
                )

                Text(
                    text = "Back to Sign In",
                    color = ForgotPurple,
                    fontSize = 16.sp,
                    fontWeight =
                        FontWeight.SemiBold
                )
            }

            Spacer(
                modifier = Modifier.height(30.dp)
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun ForgotPasswordScreenPreview() {
    TaskFlowTheme(
        dynamicColor = false
    ) {
        ForgotPasswordScreen()
    }
}