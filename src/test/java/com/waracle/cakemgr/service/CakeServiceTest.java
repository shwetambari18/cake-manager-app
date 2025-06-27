package com.waracle.cakemgr.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waracle.cakemgr.dto.CakeDTO;
import com.waracle.cakemgr.model.CakeEntity;
import com.waracle.cakemgr.repository.CakeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CakeServiceTest {

    private CakeRepository cakeRepository;
    private CakeService cakeService;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        cakeRepository = mock(CakeRepository.class);
        cakeService = new CakeService(cakeRepository);
    }

    @Test
    void getAllCakes_shouldReturnListOfCakes() {
        List<CakeEntity> mockCakes = Arrays.asList(
                new CakeEntity(1, "Cake1", "Desc1", "img1"),
                new CakeEntity(2, "Cake2", "Desc2", "img2")
        );
        when(cakeRepository.findAll()).thenReturn(mockCakes);

        List<CakeEntity> result = cakeService.getAllCakes();

        assertEquals(2, result.size());
        verify(cakeRepository, times(1)).findAll();
    }

    @Test
    void getCakeById_whenExists_shouldReturnCake() {
        CakeEntity cake = new CakeEntity(1, "Cake", "Desc", "img");
        when(cakeRepository.findById(1)).thenReturn(Optional.of(cake));

        Optional<CakeEntity> result = cakeService.getCakeById(1);

        assertTrue(result.isPresent());
        assertEquals("Cake", result.get().getTitle());
    }

    @Test
    void getCakeById_whenNotExists_shouldReturnEmpty() {
        when(cakeRepository.findById(99)).thenReturn(Optional.empty());

        Optional<CakeEntity> result = cakeService.getCakeById(99);

        assertFalse(result.isPresent());
    }

    @Test
    void createCake_shouldSaveAndReturnCake() {

        CakeDTO cake = new CakeDTO("Cake", "Desc", "img");
        CakeEntity savedCake = new CakeEntity(1, "Cake", "Desc", "img");
        CakeEntity cakeEntity = mapper.convertValue(cake, CakeEntity.class);

        when(cakeRepository.save(cakeEntity)).thenReturn(savedCake);

        CakeEntity result = cakeService.createCake(cake);

        assertEquals(1, result.getId());
        verify(cakeRepository, times(1)).save(cakeEntity);
    }

    @Test
    void deleteCake_shouldCallDeleteById() {
        cakeService.deleteCake(5);

        verify(cakeRepository, times(1)).deleteById(5);
    }

    @Test
    void updateCake_whenExists_shouldUpdateAndSave() {
        CakeEntity existing = new CakeEntity(1, "Old", "OldDesc", "OldImg");
        CakeEntity updated = new CakeEntity(null, "New", "NewDesc", "NewImg");

        when(cakeRepository.findById(1)).thenReturn(Optional.of(existing));
        when(cakeRepository.save(any(CakeEntity.class))).thenAnswer(i -> i.getArgument(0));

        CakeEntity result = cakeService.updateCake(1, updated);

        assertEquals("New", result.getTitle());
        assertEquals("NewDesc", result.getDesc());
        assertEquals("NewImg", result.getImage());
    }

    @Test
    void updateCake_whenNotExists_shouldThrowException() {
        when(cakeRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cakeService.updateCake(99, new CakeEntity());
        });

        assertTrue(exception.getMessage().contains("Cake not found with id: 99"));
    }
}
