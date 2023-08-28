package com.soroosh.auth.project.http.controllers;

import com.soroosh.auth.UserRequiredTest;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
                        .content(String.format("""
                                {
                                  "name": "%s",
                                  "subdomain": "%s",
                                  "description": "%s"
                                }""", this.faker.name().title(), this.faker.name().username(), this.faker.lorem().paragraph())))
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
    void testUpdateApplication() throws Exception {
        this.mockMvc.perform(post("/api/v1/applications")
                        .header("Authorization", "Bearer %s".formatted(this.token))
                        .contentType("application/json")
                        .content(String.format("""
                                {
                                  "name": " %s ",
                                  "subdomain": "%s",
                                  "description": "%s"
                                }""", this.faker.name().title(), this.faker.name().username(), this.faker.lorem().paragraph())))
                .andExpect(status().isOk())
                .andDo(result -> {
                    JSONObject object = new JSONObject(result.getResponse().getContentAsString());
                    String id = object.getJSONObject("data").getString("id");
                    String newName = this.faker.name().title();
                    String newSubdomain = this.faker.name().username();
                    String newDescription = this.faker.lorem().paragraph();
                    this.mockMvc.perform(put("/api/v1/applications/" + id)
                                    .header("Authorization", "Bearer %s".formatted(this.token))
                                    .contentType("application/json")
                                    .content(String.format("""
                                            {
                                              "name": "%s",
                                              "subdomain": "%s",
                                              "description": "%s"
                                            }""", newName, newSubdomain, newDescription))
                            ).andDo(updateResult -> {
                                JSONObject updatedApp = (new JSONObject(updateResult.getResponse().getContentAsString())).getJSONObject("data");
                                assertEquals(newName, updatedApp.getString("name"));
                                assertEquals(newSubdomain, updatedApp.getString("subdomain"));
                            })
                            .andExpect(status().isOk());
                });
    }

    @Test
    void testDeleteApplication() throws Exception {
        this.mockMvc.perform(post("/api/v1/applications")
                        .header("Authorization", "Bearer %s".formatted(this.token))
                        .contentType("application/json")
                        .content(String.format("""
                                {
                                  "name": " %s ",
                                  "subdomain": "%s",
                                  "description": "%s"
                                }""", this.faker.name().title(), this.faker.name().username(), this.faker.lorem().paragraph())))
                .andExpect(status().isOk())
                .andDo(result -> {
                    JSONObject object = new JSONObject(result.getResponse().getContentAsString());
                    String id = object.getJSONObject("data").getString("id");
                    this.mockMvc.perform(delete("/api/v1/applications/" + id)
                                    .header("Authorization", "Bearer %s".formatted(this.token))
                            )
                            .andExpect(status().isOk());
                });
    }
}