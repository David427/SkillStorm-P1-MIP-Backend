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
    private String vram;

    @Column
    private String factoryClock;

    @Column
    private String videoCores;

    @Column
    private String aiCores;

    @Column
    private String powerDraw;

    @ManyToOne
    @JoinColumn(name = "wh_id_code", nullable = false)
    private Warehouse warehouse;

}
