package ru.sinitsynme.logistapi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PackagedProduct extends Product {
    private int quantityInPackage;
    @OneToOne
    private Product childProduct;
}
