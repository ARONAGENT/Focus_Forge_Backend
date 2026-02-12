package com.aronJourney.focus_forge.ai;

import com.aronJourney.focus_forge.dto.chat.MessageDto;
import com.aronJourney.focus_forge.services.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Tag(name = "AI Assistant", description = "Endpoints for AI chat and RAG-based question answering")
public class AiController {


    private final ChatService chatService;
    private final RagService ragService;

    @Operation(
            summary = "AI Chat for Weekly Report Analysis and improvements",
            description = "Send a free-form message to the AI model and receive a response"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "AI response generated successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/chat")
    public ResponseEntity<MessageDto> getAnalysisOfReports(@RequestBody String Msg){
        return ResponseEntity.ok(chatService.askAI(Msg));
    }

//    @Operation(
//            summary = "Ask AI About Application",
//            description = "Ask questions related to the application using RAG (Retrieval Augmented Generation)"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Answer generated successfully"),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    @PostMapping("/ask/questions")
//    public ResponseEntity<MessageDto> askAiAbout_App(@RequestBody String msg){
//        String ans= ragService.askAi(msg);
//        return ResponseEntity.ok(new MessageDto(ans));
//    }


}
