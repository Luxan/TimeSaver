package com.sgorokh.TimeSaver.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "image")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "original_image")
    private Byte[] originalImage;

    @Lob
    @Column(name = "medium_image")
    private Byte[] mediumImage;

    @Lob
    @Column(name = "small_image")
    private Byte[] smallImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "session_image",
            joinColumns = {@JoinColumn(name = "session_id")},
            inverseJoinColumns = {@JoinColumn(name = "image_id")})
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "post_image",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "image_id")})
    private Post post;
}
