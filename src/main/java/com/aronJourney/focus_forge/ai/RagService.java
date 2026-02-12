package com.aronJourney.focus_forge.ai;

import com.aronJourney.focus_forge.entities.User;
import com.aronJourney.focus_forge.tool.GetProfileTool;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.aronJourney.focus_forge.utils.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
public class RagService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final GetProfileTool getProfileTool;

    @Value("classpath:my-info-pdf.pdf")
    Resource pdfFile;

    public void putPdfDataToVectorStore() {
        PagePdfDocumentReader pagePdfDocumentReader= new PagePdfDocumentReader(pdfFile);

        List<Document> pages=pagePdfDocumentReader.get();

        TokenTextSplitter tokenTextSplitter= TokenTextSplitter.builder()
                .withChunkSize(200)
                .build();

        List<Document> chunks=tokenTextSplitter.apply(pages);
        vectorStore.add(chunks);
    }

//    public String askAi(String prompt){
//
//        User user = getCurrentUser();
//
//        String template = """
//              You are an AI assistant helping a developer.
//
//              Follow these rules strictly:
//
//              1. Use ONLY the information provided in the <context>.
//              2. You may rephrase, summarize, and explain the information in natural language.
//              3. Do NOT introduce any new facts, assumptions, or external knowledge.
//              4. If multiple context sections are relevant, combine them into a single clear explanation.
//              5. If the answer is not present in the context, respond with: "I don't know".
//
//              {context}
//
//              Answer in a friendly, conversational tone.
//              """;
//        List<Document> documents= vectorStore.similaritySearch(SearchRequest.builder()
//                .query(prompt)
//                .topK(4)
//                .similarityThreshold(0.5)
//                .filterExpression("file_name == 'my-info-pdf.pdf'")
//                .build());
//
//        String  context = documents.stream()
//                .map(Document::getText)
//                .collect(Collectors.joining("\n\n"));
//
//        PromptTemplate promptTemplate=new PromptTemplate(template);
//        String systemPrompt=promptTemplate.render(Map.of("context",context));
//
//        return chatClient.prompt()
//                .user(prompt)
//                .system(systemPrompt)
//                .tools(getProfileTool)
//                .advisors(
//                        new SimpleLoggerAdvisor(),
//                        VectorStoreChatMemoryAdvisor.builder(vectorStore)
//                                .conversationId(String.valueOf(user.getId()))
//                                .defaultTopK(4)
//                                .build()
//                )
//                .call()
//                .content();
//    }
}
