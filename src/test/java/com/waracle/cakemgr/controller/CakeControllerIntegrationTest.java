package com.waracle.cakemgr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waracle.cakemgr.config.SecurityConfig;
import com.waracle.cakemgr.dto.CakeDTO;
import com.waracle.cakemgr.model.CakeEntity;
import com.waracle.cakemgr.service.CakeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CakeController.class)
@Import(SecurityConfig.class)
class CakeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CakeService cakeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    void getAllCakes_ShouldReturn200() throws Exception {
        when(cakeService.getAllCakes()).thenReturn(
                Arrays.asList(new CakeEntity(1, "Cake", "Desc", "url"))
        );

        mockMvc.perform(get("/cakes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Cake"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getCakeById_Exists_ShouldReturn200() throws Exception {
        when(cakeService.getCakeById(1)).thenReturn(Optional.of(new CakeEntity(1, "Cake", "Desc", "url")));

        mockMvc.perform(get("/cakes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Cake"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void createCake_ShouldReturn201() throws Exception {
        CakeEntity cake = new CakeEntity(null, "New Cake", "Desc", "url");
        CakeEntity saved = new CakeEntity(1, "New Cake", "Desc", "url");
        when(cakeService.createCake(any())).thenReturn(saved);

        mockMvc.perform(post("/cakes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cake)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Cake"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void createCake_ShouldReturnBadRequest() throws Exception {
        CakeDTO cake = new CakeDTO(null, "Description", "url");
        mockMvc.perform(post("/cakes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cake)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteCake_ShouldReturn204() throws Exception {
        doNothing().when(cakeService).deleteCake(1);

        mockMvc.perform(delete("/cakes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteCake_ShouldReturn403() throws Exception {
        doNothing().when(cakeService).deleteCake(1);

        mockMvc.perform(delete("/cakes/1"))
                .andExpect(status().isForbidden());
    }
}
