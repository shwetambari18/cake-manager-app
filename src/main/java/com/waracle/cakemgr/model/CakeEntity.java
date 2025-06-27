package com.waracle.cakemgr.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "cakes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CakeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;

    @Column(name = "TITLE",unique = true, nullable = false, length = 100)
    private String title;

    @Column(name = "DESCRIPTION", unique = false, nullable = false, length = 100)
    private String desc;

    @Column(name = "IMAGE", unique = false, nullable = false, length = 300)
    private String image;

}