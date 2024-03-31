package com.skillstorm.mvideoinventoryplatform.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "units")
public class Unit {

    @Id
    @Column
    @SequenceGenerator(
            name = "units_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "units_id_seq")
    private Long id;

    @Column(length = 10)
    private String series;

    @Column(length = 50)
    private String model;

    @Column
    private Integer vram;

    @Column
    private Double factoryClock;

    @Column
    private Integer videoCores;

    @Column
    private Integer aiCores;

    @Column
    private Integer powerDraw;

    @ManyToOne
    @JoinColumn(name = "wh_id_code", nullable = false)
    private Warehouse warehouse;

}
