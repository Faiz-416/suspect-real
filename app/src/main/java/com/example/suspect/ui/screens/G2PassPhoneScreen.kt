package com.example.suspect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.suspect.ui.components.AppHeading
import com.example.suspect.ui.components.AppSubHeading
import com.example.suspect.ui.components.PrimaryButton
import com.example.suspect.ui.theme.AccentBlue
import com.example.suspect.ui.theme.DarkCard

@Composable
fun G2PassPhoneScreen(
    nextPlayerName: String,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(DarkCard),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Pass phone",
                tint = AccentBlue,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AppSubHeading("Pass the phone to", textAlign = TextAlign.Center)
        AppHeading(nextPlayerName, textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 32.dp))

        PrimaryButton(
            text = "Continue",
            onClick = onContinue,
            testTag = "pass_phone_continue_button"
        )
    }
}
