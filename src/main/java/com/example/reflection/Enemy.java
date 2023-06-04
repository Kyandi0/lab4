package com.example.reflection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enemy {
    private String type = "ground";
    private String fightStyle = "magic";
    private String textPrize = "bones";
    private double power = 23.6;
    private int health = 1000;
    private boolean aggresive = true;
    public boolean getAggresive() {
        return aggresive;
    }

}
