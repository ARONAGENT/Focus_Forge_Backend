package com.aronJourney.focus_forge.services;

import com.aronJourney.focus_forge.ai.RagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RagServiceTest {
    @Autowired
    RagService ragService;

    @Test
    public void putPdfDataToVectorStore(){
        ragService.putPdfDataToVectorStore();
    }

    @Test
    void askAi() {
        String ans=ragService.askAi("what is my app about and who is creator give me in 2 lines ");
        System.out.println(ans);
    }
}
