package ru.itis.Tyshenko.entity;

import lombok.*;

@Builder
@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Cottage {

    public Cottage(){}

    private String address;
    private Integer price;
    private Integer id;
}
