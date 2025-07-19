package com.uztest.avto.presentation.screen.aichat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                Text(
                    text = "AI Driving Instructor",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        // Messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                MessageBubble(message = message)
            }
        }
        
        // Input field
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
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
                    placeholder = { Text("Ask about driving rules...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
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
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = MaterialTheme.colorScheme.primary
                    )
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
            modifier = Modifier.widthIn(max = 280.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isFromUser) 16.dp else 4.dp,
                bottomEnd = if (message.isFromUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isFromUser) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = if (message.isFromUser) 
                    MaterialTheme.colorScheme.onPrimary 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
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