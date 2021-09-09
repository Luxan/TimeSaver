package com.sgorokh.TimeSaver.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "portfolio_images")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;
}
