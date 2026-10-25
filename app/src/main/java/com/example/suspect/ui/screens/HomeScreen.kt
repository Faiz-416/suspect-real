package com.example.suspect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.suspect.model.GameMode
import com.example.suspect.ui.components.AppCard
import com.example.suspect.ui.components.AppHeading
import com.example.suspect.ui.components.AppSubHeading
import com.example.suspect.ui.theme.AccentBlue
import com.example.suspect.ui.theme.AccentPurple
import com.example.suspect.ui.theme.TextMuted
import com.example.suspect.ui.theme.TextPrimary

@Composable
fun HomeScreen(
    onSelectGameMode: (GameMode) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppHeading("Select Game")
            Spacer(modifier = Modifier.height(4.dp))
            AppSubHeading("Choose what you want to play")
        }

        AppCard(
            onClick = { onSelectGameMode(GameMode.NUMBER_JUSTIFY) },
            borderColor = AccentPurple.copy(alpha = 0.3f),
            modifier = Modifier.height(180.dp),
            testTag = "home_card_number_justify"
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AccentPurple.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Numbers,
                    contentDescription = "Number Justify",
                    tint = AccentPurple,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Number Justify",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Justify your answer. Find the liar.",
                color = TextMuted,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AppCard(
            onClick = { onSelectGameMode(GameMode.IMPOSTOR_WORD) },
            borderColor = AccentBlue.copy(alpha = 0.3f),
            modifier = Modifier.height(180.dp),
            testTag = "home_card_impostor_word"
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AccentBlue.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Impostor Word",
                    tint = AccentBlue,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Impostor Word",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Find the player with the different word.",
                color = TextMuted,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
