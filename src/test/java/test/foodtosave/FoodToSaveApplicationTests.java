package test.foodtosave;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import test.foodtosave.records.PersonRecord;
import test.foodtosave.utils.PersonPayload;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc()
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FoodToSaveApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldPostAndCheckValueThroughGet() throws Exception {
        final String payload = objectMapper.writeValueAsString(new PersonPayload("Andre"));

        mockMvc.perform(post("/person").contentType("application/json")
                                       .content(payload))
               .andExpect(status().isOk())
               .andExpect(content().string("true"));

        final String result = mockMvc.perform(get("/person"))
                                     .andExpect(status().isOk())
                                     .andReturn()
                                     .getResponse()
                                     .getContentAsString();

        final List<PersonRecord> list = objectMapper.readValue(result, new TypeReference<List<PersonRecord>>() {
        });

        assertEquals(1, list.size());

        final Long id = list.stream()
                            .findFirst()
                            .get()
                            .id();

        final String resultFromGetOne = mockMvc.perform(get(String.format("/person/%s", id)))
                                               .andExpect(status().isOk())
                                               .andReturn()
                                               .getResponse()
                                               .getContentAsString();

        final PersonRecord person = objectMapper.readValue(resultFromGetOne, PersonRecord.class);
        assertNotNull(person.id());
        assertNotNull(person.createdAt());
        assertEquals("Andre", person.name());
    }
}
