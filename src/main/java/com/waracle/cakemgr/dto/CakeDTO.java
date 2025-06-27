package com.waracle.cakemgr.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CakeDTO {
    @NotBlank(message = "Title must not be blank")
    private String title;
    @NotBlank(message = "Description must not be blank")
    private String desc;
    @NotBlank(message = "Image URL must not be blank")
    private String image;
}
