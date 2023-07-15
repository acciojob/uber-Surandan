package com.driver.model;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;

@Entity
public class Cab {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    int perKmRate;

    boolean available;

    @OneToOne
    @JoinColumn
    Driver driver;

}