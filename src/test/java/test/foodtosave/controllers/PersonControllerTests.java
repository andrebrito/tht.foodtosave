package test.foodtosave.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import test.foodtosave.utils.PersonPayload;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import test.foodtosave.services.PersonService;

@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService personService;

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get("/person"))
               .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        mockMvc.perform(get("/person/1"))
               .andExpect(status().isOk());
    }

    @Test
    void testPost() throws Exception {
        final String payload = objectMapper.writeValueAsString(new PersonPayload("Andrew"));

        mockMvc.perform(post("/person").contentType("application/json")
                                       .content(payload))
               .andExpect(status().isOk());

    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/person/1"))
               .andExpect(status().isOk());

    }
}
