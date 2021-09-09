package com.sgorokh.TimeSaver.models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sessions")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;

    @Column(name = "type")
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "client_session",
            joinColumns = {@JoinColumn(name = "clients_id")},
            inverseJoinColumns = {@JoinColumn(name = "sessions_id")})
    private Client client;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "session_image",
            joinColumns = {@JoinColumn(name = "image_id")},
            inverseJoinColumns = {@JoinColumn(name = "session_id")})
    private List<Image> images;

    public enum Type {
        Wedding(1), Portrait(2);

        private final int code;

        private Type(int code) {
            this.code = code;
        }

        public int toInt() {
            return code;
        }

        public String toString() {
            return String.valueOf(code);
        }
    }
}
