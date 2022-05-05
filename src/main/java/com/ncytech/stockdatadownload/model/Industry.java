package com.ncytech.stockdatadownload.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Industry {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String name;

    @ManyToOne
    private Sector sector;

    @CreationTimestamp
    private LocalDateTime createDateTime;
    @UpdateTimestamp
    private LocalDateTime updateDateTime;
}
