package com.sgorokh.TimeSaver.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "home_images")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HomeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "content")
    private String content;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;
}
