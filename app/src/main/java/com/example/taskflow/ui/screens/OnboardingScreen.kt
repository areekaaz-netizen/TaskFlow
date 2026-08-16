package com.example.taskflow.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.R
import com.example.taskflow.model.OnboardingPage
import com.example.taskflow.ui.theme.TaskFlowTheme
import kotlinx.coroutines.launch

private val OnboardingPurple = Color(0xFF5B5FEF)
private val OnboardingGray = Color(0xFF6B7280)
private val InactiveIndicator = Color(0xFFD6D6E5)

@Composable
fun OnboardingScreen(
    onSkip: () -> Unit = {},
    onGetStarted: () -> Unit = {}
) {
    val pages = listOf(
        OnboardingPage(
            image = R.drawable.onboarding1,
            title = "Stay Organized",
            description = "Organize your daily tasks and never miss an important deadline."
        ),
        OnboardingPage(
            image = R.drawable.onboarding2,
            title = "Boost Productivity",
            description = "Prioritize important work and stay focused on what matters."
        ),
        OnboardingPage(
            image = R.drawable.onboarding3,
            title = "Ready to Get Started?",
            description = "Manage work and personal life effortlessly with TaskFlow."
        )
    )

    val pagerState = rememberPagerState {
        pages.size
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 18.dp,
                    end = 20.dp
                ),
            horizontalArrangement = Arrangement.End
        ) {
            if (pagerState.currentPage != pages.lastIndex) {
                TextButton(
                    onClick = onSkip
                ) {
                    Text(
                        text = "Skip",
                        color = OnboardingPurple,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Image(
                    painter = painterResource(
                        id = pages[page].image
                    ),
                    contentDescription = pages[page].title,
                    modifier = Modifier
                        .fillMaxWidth(0.94f)
                        .height(420.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = pages[page].title,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = OnboardingPurple
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = pages[page].description,
                    modifier = Modifier.fillMaxWidth(0.88f),
                    textAlign = TextAlign.Center,
                    fontSize = 17.sp,
                    lineHeight = 27.sp,
                    color = OnboardingGray
                )

                Spacer(modifier = Modifier.height(34.dp))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    bottom = 30.dp
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pages.size) { index ->
                val isSelected =
                    pagerState.currentPage == index

                Box(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .height(10.dp)
                        .width(
                            if (isSelected) {
                                34.dp
                            } else {
                                10.dp
                            }
                        )
                        .clip(CircleShape)
                        .background(
                            if (isSelected) {
                                OnboardingPurple
                            } else {
                                InactiveIndicator
                            }
                        )
                )
            }
        }

        Button(
            onClick = {
                if (
                    pagerState.currentPage <
                    pages.lastIndex
                ) {
                    scope.launch {
                        pagerState.animateScrollToPage(
                            pagerState.currentPage + 1
                        )
                    }
                } else {
                    onGetStarted()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = OnboardingPurple
            )
        ) {
            Text(
                text = if (
                    pagerState.currentPage ==
                    pages.lastIndex
                ) {
                    "Get Started"
                } else {
                    "Next"
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun OnboardingPreview() {
    TaskFlowTheme(
        dynamicColor = false
    ) {
        OnboardingScreen()
    }
}