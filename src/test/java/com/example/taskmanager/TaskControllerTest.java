package com.example.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class TaskControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void createTask_returns201() throws Exception {
        mvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Etudier DevSecOps\"}"))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.title").value("Etudier DevSecOps"));
    }

    @Test
    void createTask_blankTitle_returns400() throws Exception {
        mvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"\"}"))
           .andExpect(status().isBadRequest());
    }

    @Test
    void getUnknownTask_returns404() throws Exception {
        mvc.perform(get("/api/tasks/9999"))
           .andExpect(status().isNotFound());
    }

    @Test
    void listTasks_returns200() throws Exception {
        mvc.perform(get("/api/tasks"))
           .andExpect(status().isOk());
    }
}
