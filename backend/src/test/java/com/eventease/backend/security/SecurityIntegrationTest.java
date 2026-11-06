package com.eventease.backend.security;

import com.eventease.backend.dto.AuthRequestDTO;
import com.eventease.backend.dto.RegisterRequestDTO;
import com.eventease.backend.dto.EventRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Logs in as the seeded admin user (created by DataInitializer: username=admin, password=password).
     */
    private String asAdmin() throws Exception {
        return login("admin", "password");
    }

    /**
     * Registers a new regular user and logs in.
     */
    private String asUser() throws Exception {
        String username = "student" + System.currentTimeMillis();
        RegisterRequestDTO register = new RegisterRequestDTO(username, username + "@example.com", "password123");
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        return login(username, "password123");
    }

    private String login(String username, String password) throws Exception {
        AuthRequestDTO login = new AuthRequestDTO(username, password);
        String response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JsonNode json = objectMapper.readTree(response);
        return json.get("token").asText();
    }

    @Test
    void publishedEventsEndpointIsPublic() throws Exception {
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk());
    }

    @Test
    void adminEventsEndpointRequiresAdminRole() throws Exception {
        String userToken = asUser();

        // A USER token cannot access admin endpoints
        mockMvc.perform(get("/api/admin/events")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());

        // No token at all
        mockMvc.perform(get("/api/admin/events"))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanAccessAdminEndpoints() throws Exception {
        String adminToken = asAdmin();

        mockMvc.perform(get("/api/admin/events")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/admin/dashboard")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void adminCanCreateEvent() throws Exception {
        String adminToken = asAdmin();

        EventRequestDTO request = new EventRequestDTO();
        request.setTitle("Test Event");
        request.setDescription("A test event");
        request.setCategoryId(1L); // "Music" category seeded by DataInitializer
        request.setEventDate(LocalDate.of(2026, 12, 1));
        request.setEventTime(LocalTime.of(10, 0));
        request.setVenue("Hall A");
        request.setCapacity(50);
        request.setOrganizerName("Test Org");
        request.setStatus("PUBLISHED");

        mockMvc.perform(post("/api/admin/events")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void userCannotCreateAdminEvent() throws Exception {
        String userToken = asUser();

        mockMvc.perform(post("/api/admin/events")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"x\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void registrationsEndpointRequiresAuthentication() throws Exception {
        // No token — Spring Security denies access (403 with anonymous auth)
        mockMvc.perform(get("/api/registrations/my"))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"eventId\":1}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void userCanAccessOwnRegistrations() throws Exception {
        String userToken = asUser();

        mockMvc.perform(get("/api/registrations/my")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }

    @Test
    void userDashboardRequiresAuthentication() throws Exception {
        // No token
        mockMvc.perform(get("/api/user/dashboard"))
                .andExpect(status().isForbidden());

        // With user token
        String userToken = asUser();
        mockMvc.perform(get("/api/user/dashboard")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }
}
