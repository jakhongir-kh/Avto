package com.uztest.avto.presentation.screen.aichat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIChatScreen() {
    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    
    LaunchedEffect(Unit) {
        messages.add(
            ChatMessage(
                text = "Hello! I'm your AI driving instructor. Ask me anything about traffic rules, road signs, or driving techniques!",
                isFromUser = false
            )
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "AI Assistant",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Ask me anything about driving",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Normal
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
        
        // Messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(messages) { message ->
                MessageBubble(message = message)
            }
        }
        
        // Input field
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { 
                        Text(
                            "Ask about driving rules...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        ) 
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            if (messageText.isNotBlank()) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            messages.add(ChatMessage(messageText, true))
                            val userMessage = messageText
                            messageText = ""
                            
                            // Simulate AI response
                            messages.add(
                                ChatMessage(
                                    text = getAIResponse(userMessage),
                                    isFromUser = false
                                )
                            )
                        }
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Send,
                            contentDescription = "Send",
                            modifier = Modifier.size(20.dp),
                            tint = if (messageText.isNotBlank()) Color.White
                                   else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier.widthIn(max = 300.dp),
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp,
                bottomStart = if (message.isFromUser) 20.dp else 6.dp,
                bottomEnd = if (message.isFromUser) 6.dp else 20.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isFromUser) 
                    Color(0xFF90CAF9) // Soft Blue
                else 
                    Color(0xFFF5F7FA) // Light Gray
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(16.dp),
                color = if (message.isFromUser) 
                    Color.White
                else 
                    MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
            )
        }
    }
}

private fun getAIResponse(userMessage: String): String {
    val lowerMessage = userMessage.lowercase()
    
    return when {
        lowerMessage.contains("speed limit") -> 
            "Speed limits vary by area: 50 km/h in residential areas, 80-100 km/h on highways. Always check local signs as they may override general limits."
        
        lowerMessage.contains("stop sign") -> 
            "At a stop sign, you must come to a complete stop, check for traffic and pedestrians, then proceed when safe. Rolling stops are illegal."
        
        lowerMessage.contains("right of way") -> 
            "Right of way rules: yield to traffic already in intersection, vehicles on your right at 4-way stops, pedestrians in crosswalks, and emergency vehicles."
        
        lowerMessage.contains("parking") -> 
            "When parking: stay 5m from fire hydrants, don't block driveways, park in direction of traffic, and always check for parking signs and restrictions."
        
        lowerMessage.contains("yellow light") -> 
            "Yellow light means prepare to stop if you can do so safely. Only proceed if you're too close to stop safely without hard braking."
        
        lowerMessage.contains("merge") || lowerMessage.contains("highway") -> 
            "When merging onto highways: match the speed of traffic, use your signal, check blind spots, and merge when there's a safe gap."
        
        lowerMessage.contains("rain") || lowerMessage.contains("weather") -> 
            "In bad weather: reduce speed, increase following distance, use headlights, avoid sudden movements, and pull over if visibility is too poor."
        
        else -> 
            "That's a great question! For specific driving rules, I recommend checking your local traffic laws. Remember: always drive defensively, follow speed limits, and stay alert. Is there a specific traffic situation you'd like to know more about?"
    }
}

data class ChatMessage(
    val text: String,
    val isFromUser: Boolean
)