package org.example.dto.weather;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Weather {
    private int id;
    private String main;
    private String description;
    private String icon;
}
