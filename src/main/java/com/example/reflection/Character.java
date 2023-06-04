package com.example.reflection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Character {
    private String name = "Anna";
    private String className = "mage";
    private String wepon = "rod";
    private Integer healingSpeed = 10;
    private int maxHealth = 250;
    private int damage = 5;

}
