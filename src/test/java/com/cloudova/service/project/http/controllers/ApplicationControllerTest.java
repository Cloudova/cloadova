package com.cloudova.service.project.http.controllers;

import com.cloudova.service.UserRequiredTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("testing")
class ApplicationControllerTest extends UserRequiredTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateApplication() throws Exception {
        this.mockMvc.perform(post("/api/v1/applications")
                .content("application/json")
                .content(""))
                .andExpect(status().isOk());

    }
}