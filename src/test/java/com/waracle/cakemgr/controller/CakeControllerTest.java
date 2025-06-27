package com.waracle.cakemgr.controller;

import com.waracle.cakemgr.dto.CakeDTO;
import com.waracle.cakemgr.model.CakeEntity;
import com.waracle.cakemgr.service.CakeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CakeControllerTest {

    @Mock
    private CakeService cakeService;

    @InjectMocks
    private CakeController cakeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCakes_ShouldReturnList() {
        List<CakeEntity> mockCakes = Arrays.asList(new CakeEntity(1, "Cake1", "Desc1", "URL1"));
        when(cakeService.getAllCakes()).thenReturn(mockCakes);

        List<CakeEntity> result = cakeController.getAllCakes();

        assertEquals(1, result.size());
        verify(cakeService, times(1)).getAllCakes();
    }

    @Test
    void getCakeById_Exists() {
        CakeEntity cake = new CakeEntity(1, "Cake1", "Desc1", "URL1");
        when(cakeService.getCakeById(1)).thenReturn(Optional.of(cake));

        ResponseEntity<CakeEntity> response = cakeController.getCakeById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Cake1", response.getBody().getTitle());
    }

    @Test
    void getCakeById_NotFound() {
        when(cakeService.getCakeById(2)).thenReturn(Optional.empty());

        ResponseEntity<CakeEntity> response = cakeController.getCakeById(2);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void createCake_ShouldReturnCreatedCake() {
        CakeDTO cake = new CakeDTO("CakeX", "Description", "url");
        CakeEntity saved = new CakeEntity(1, "CakeX", "Description", "url");
        when(cakeService.createCake(cake)).thenReturn(saved);

        ResponseEntity<CakeEntity> result = cakeController.createCake(cake);

        assertEquals("CakeX", result.getBody().getTitle());
        verify(cakeService).createCake(cake);
    }

    @Test
    void deleteCake_ShouldCallService() {
        doNothing().when(cakeService).deleteCake(1);

        ResponseEntity<Void> response = cakeController.deleteCake(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(cakeService).deleteCake(1);
    }

    @Test
    void updateCake_Successful() {
        CakeEntity updated = new CakeEntity(1, "CakeUpdated", "Desc", "url");
        when(cakeService.updateCake(eq(1), any())).thenReturn(updated);

        ResponseEntity<CakeEntity> response = cakeController.updateCake(1, updated);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("CakeUpdated", response.getBody().getTitle());
    }

    @Test
    void updateCake_NotFound() {
        when(cakeService.updateCake(eq(1), any())).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<CakeEntity> response = cakeController.updateCake(1, new CakeEntity());

        assertEquals(404, response.getStatusCodeValue());
    }
}
