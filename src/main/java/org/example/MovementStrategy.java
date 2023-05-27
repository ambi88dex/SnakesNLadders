package org.example;

public enum MovementStrategy {

    SUM("SUM"),
    MAX("MAX"),
    MIN("MIN");

    final String name;
    MovementStrategy(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}
