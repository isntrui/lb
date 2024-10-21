package ru.isntrui.lb.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

@Entity
@Table(name = "texts")
@Data
@EqualsAndHashCode(of = {"id"})
public class Text {

    @Id
    @GeneratedValue
    private Long id;

    private String wave;
    private String body;
    private String title;

    @ManyToOne
    private User madeBy;

    private Date madeOn;

    private Boolean isApproved;

    @ManyToOne
    private User approvedBy;

    private Date approvedOn;

}