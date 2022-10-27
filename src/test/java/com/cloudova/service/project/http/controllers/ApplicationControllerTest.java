package com.cloudova.service.project.http.controllers;

import com.cloudova.service.UserRequiredTest;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("testing")
@AutoConfigureMockMvc
class ApplicationControllerTest extends UserRequiredTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateApplication() throws Exception {
        this.mockMvc.perform(post("/api/v1/applications")
                        .header("Authorization", "Bearer %s".formatted(this.token))
                        .contentType("application/json")
                        .content(String.format("{\n" +
                                "  \"name\": \"%s\",\n" +
                                "  \"subdomain\": \"%s\",\n" +
                                "  \"description\": \"%s\"\n" +
                                "}", this.faker.name().title(), this.faker.name().username(), this.faker.lorem().paragraph())))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    void testListOfApplications() throws Exception {
        this.mockMvc.perform(get("/api/v1/applications")
                        .header("Authorization", "Bearer %s".formatted(this.token))
                )
                .andExpect(status().isOk());

    }

    @Test
    void testDeleteApplication() throws Exception {
        this.mockMvc.perform(post("/api/v1/applications")
                        .header("Authorization", "Bearer %s".formatted(this.token))
                        .contentType("application/json")
                        .content(String.format("{\n" +
                                "  \"name\": \" %s \",\n" +
                                "  \"subdomain\": \"%s\",\n" +
                                "  \"description\": \"%s\"\n" +
                                "}", this.faker.name().title(), this.faker.name().username(), this.faker.lorem().paragraph())))
                .andExpect(status().isOk())
                .andDo(result -> {
                    JSONObject object = new JSONObject(result.getResponse().getContentAsString());
                    long id = object.getJSONObject("data").getLong("id");
                    this.mockMvc.perform(delete("/api/v1/applications/" + id)
                                    .header("Authorization", "Bearer %s".formatted(this.token))
                            )
                            .andExpect(status().isOk());
                });
    }
}