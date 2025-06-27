package com.waracle.cakemgr.config;

import com.waracle.cakemgr.dto.CakeDTO;
import com.waracle.cakemgr.model.CakeEntity;
import com.waracle.cakemgr.repository.CakeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Configuration
@AllArgsConstructor
@Log4j2
public class CakeDataLoader implements CommandLineRunner {

    private CakeRepository cakeRepository;
    private RestTemplate restTemplate;

    private static final String CAKE_JSON_URL = "https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json";

    @Override
    public void run(String... args) throws Exception {
        try {
            if (cakeRepository.count() == 0) {
                log.info("Fetching cake data from URL...");

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(headers);

                ResponseEntity<CakeDTO[]> responseEntity = restTemplate.exchange(CAKE_JSON_URL, HttpMethod.GET, entity, CakeDTO[].class);
                Set<CakeEntity> cakes = new HashSet<>();
                if (responseEntity != null && responseEntity.getBody() != null) {
                    CakeDTO[] cakeDTOS = responseEntity.getBody();
                    for (CakeDTO dto : cakeDTOS) {
                        CakeEntity build = CakeEntity.builder()
                                .title(dto.getTitle())
                                .desc(dto.getDesc())
                                .image(dto.getImage())
                                .build();
                        cakes.add(build);
                    }
                    if (!CollectionUtils.isEmpty(cakes)) {
                        cakeRepository.saveAll(cakes);
                    }
                }
                log.info("Saved {} cakes to the database", CollectionUtils.isEmpty(cakes) ? 0 : cakes.size());
            } else {
                log.warn("No data fetched from URL");
            }
        } catch (Exception e) {
            log.error("Some error occurred while saving cakes into database: ", e);
        }
    }
}
