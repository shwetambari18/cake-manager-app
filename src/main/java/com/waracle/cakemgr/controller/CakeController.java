package com.waracle.cakemgr.controller;

import com.waracle.cakemgr.dto.CakeDTO;
import com.waracle.cakemgr.model.CakeEntity;
import com.waracle.cakemgr.service.CakeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cakes")
@AllArgsConstructor
@Log4j2
public class CakeController {

    private CakeService cakeService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Fetch all cake details from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cake details fetched sucessfully")})
    public List<CakeEntity> getAllCakes() {
        log.info("Fetching all cakes");
        return cakeService.getAllCakes();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get cake by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cake details fetched sucessfully"),
            @ApiResponse(responseCode = "404", description = "Cake details not found")})
    public ResponseEntity<CakeEntity> getCakeById(@PathVariable("id") Integer id) {
        log.info("Fetching cake with id {}", id);
        return cakeService.getCakeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Adds a new cake to database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cake added successfully"),
            @ApiResponse(responseCode = "403", description = "Admin users can add a new cake")})
    public ResponseEntity<CakeEntity> createCake(@Valid @RequestBody CakeDTO cake) {
        log.info("Creating cake: {}", cake.getTitle());
        try {
            CakeEntity createdCake = cakeService.createCake(cake);
            return ResponseEntity.ok(createdCake);
        }catch(Exception e){
            log.error("Some error occurred while creating error: ",e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a cake from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cake deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Admin users can delete a cake")})
    public ResponseEntity<Void> deleteCake(@PathVariable("id") Integer id) {
        log.info("Deleting cake with id {}", id);
        cakeService.deleteCake(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a cake by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cake updated successfully"),
            @ApiResponse(responseCode = "404", description = "Cake not found in the database"),
            @ApiResponse(responseCode = "403", description = "Users other than Admins cannot add new cake")})
    public ResponseEntity<CakeEntity> updateCake(@PathVariable("id") Integer id, @RequestBody CakeEntity cake) {
        log.info("Updating cake with id {}", id);
        try {
            CakeEntity updated = cakeService.updateCake(id, cake);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}