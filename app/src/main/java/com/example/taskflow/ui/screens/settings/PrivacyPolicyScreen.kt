package com.example.taskflow.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PrivacyPurple = Color(0xFF6C4FF8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit
) {

    Scaffold(

        containerColor = MaterialTheme.colorScheme.background,

        topBar = {

            CenterAlignedTopAppBar(

                title = {
                    Text(
                        "Privacy Policy",
                        fontWeight = FontWeight.Bold
                    )
                },

                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            Icons.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }

            )

        }

    ) { innerPadding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {

            Card(

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(18.dp),

                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )

            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Shield,
                        contentDescription = null,
                        tint = PrivacyPurple
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Your Privacy Matters",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    PolicyItem(
                        "• TaskFlow stores your tasks locally on your device."
                    )

                    PolicyItem(
                        "• We do not collect personal information."
                    )

                    PolicyItem(
                        "• Your reminders are only used to notify you about your own tasks."
                    )

                    PolicyItem(
                        "• No task data is shared with third parties."
                    )

                    PolicyItem(
                        "• You have complete control over your notifications and reminder settings."
                    )

                }

            }

        }

    }

}

@Composable
private fun PolicyItem(
    text: String
) {

    Column {
        Text(
            text = text,
            fontSize = 15.sp,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(
            modifier = Modifier.height(14.dp)
        )

    }

}