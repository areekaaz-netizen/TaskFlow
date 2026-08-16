package com.example.taskflow.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Purple = Color(0xFF6C4FF8)
private val PurpleLight = Color(0xFFF2EEFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBackClick: () -> Unit
) {

    Scaffold(

        topBar = {

            CenterAlignedTopAppBar(

                title = {
                    Text("About TaskFlow")
                },

                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            Icons.Outlined.ArrowBack,
                            null
                        )
                    }
                }
            )
        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Box(

                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Purple,
                                Color(0xFF9B7BFF)
                            )
                        )
                    ),

                contentAlignment = Alignment.Center

            ) {

                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "TaskFlow",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Version 1.0.0",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))

            Card(

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(20.dp),

                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )

            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        "About",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "TaskFlow is a modern productivity application that helps you organize daily tasks, manage deadlines, and receive smart reminders so you never miss important work."
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            Icons.Outlined.Android,
                            null,
                            tint = Purple
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {

                            Text(
                                "Built With",
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                "Jetpack Compose\nRoom Database\nWorkManager\nMaterial 3"
                            )

                        }

                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            Icons.Outlined.Code,
                            null,
                            tint = Purple
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {

                            Text(
                                "Developer",
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                "Areeka Zahid"
                            )

                        }

                    }

                }

            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                "© 2026 TaskFlow",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }

    }

}