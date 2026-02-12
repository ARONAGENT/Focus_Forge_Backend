package com.aronJourney.focus_forge.services.impl;


import com.aronJourney.focus_forge.dto.chat.MessageDto;
import com.aronJourney.focus_forge.entities.User;
import com.aronJourney.focus_forge.services.ChatService;
import com.aronJourney.focus_forge.tool.GetProfileTool;
import com.aronJourney.focus_forge.tool.WeeklyReportToolCall;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;


import static com.aronJourney.focus_forge.utils.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

   private final ChatClient chatClient;
   private final WeeklyReportToolCall weeklyReportToolCall;
   private final VectorStore vectorStore;
   private final GetProfileTool getProfileTool;



    @Override
    public MessageDto askAI(String msg) {
        User user = getCurrentUser();

        String tasks = chatClient.prompt()
                .system(sp -> sp
                        .text("""
                      USER CONTEXT:
                      - Name: {name}
                      - Email: {email}
                      - User ID: {userId}
                      
                      You are a personal study assistant. Use the above context when addressing the user.
                      
                      TOOL USAGE RULES:
                      1. For study analytics (time, streaks, performance) → Weekly Report Tool
                      2. For user profile (goals, preferences) → Profile Tool
                      3. For casual chat or greetings → Use USER CONTEXT above + conversation memory
                      4. Never call tools for simple questions you can answer from context
                      """)
                        .param("name", user.getUsername())
                        .param("email", user.getEmail())
                        .param("userId", user.getId())
                )
                .user(msg)
                .tools(weeklyReportToolCall, getProfileTool)
                .advisors(
                        new SimpleLoggerAdvisor(),
                        VectorStoreChatMemoryAdvisor.builder(vectorStore)
                                .conversationId(String.valueOf(user.getId()))
                                .defaultTopK(6)
                                .build()
                )
                .call()
                .content();

        return new MessageDto(tasks);
    }
}
