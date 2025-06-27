package com.waracle.cakemgr.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waracle.cakemgr.dto.CakeDTO;
import com.waracle.cakemgr.model.CakeEntity;
import com.waracle.cakemgr.repository.CakeRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CakeService {

    private CakeRepository cakeRepository;

    public List<CakeEntity> getAllCakes() {
        return cakeRepository.findAll();
    }

    public Optional<CakeEntity> getCakeById(Integer id) {
        return cakeRepository.findById(id);
    }

    public CakeEntity createCake(CakeDTO cake) {
        ObjectMapper mapper = new ObjectMapper();
        CakeEntity cakeEntity = mapper.convertValue(cake,CakeEntity.class);
        return cakeRepository.save(cakeEntity);
    }

    public void deleteCake(Integer id) {
        cakeRepository.deleteById(id);
    }

    public CakeEntity updateCake(Integer id, CakeEntity updatedCake) {
        return cakeRepository.findById(id).map(existingCake -> {
            if(StringUtils.isNotEmpty(updatedCake.getTitle()))
                    existingCake.setTitle(updatedCake.getTitle());
            if(StringUtils.isNotEmpty(updatedCake.getDesc()))
                    existingCake.setDesc(updatedCake.getDesc());
            if(StringUtils.isNotEmpty(updatedCake.getImage()))
                    existingCake.setImage(updatedCake.getImage());
            return cakeRepository.save(existingCake);
        }).orElseThrow(() -> new RuntimeException("Cake not found with id: " + id));
    }
}
