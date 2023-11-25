package ru.sinitsynme.logistapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "organization_sequence", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "organization", fetch = LAZY, cascade = CascadeType.ALL)
    private List<Warehouse> warehouses;
}
