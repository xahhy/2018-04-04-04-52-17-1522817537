package com.tw;

import java.util.Objects;

public class Subject {
    private String name;
    private Double score;

    Subject(String name, Double score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public Double getScore() {
        return score;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if(obj.getClass() != this.getClass()) return false;
        Subject subject = (Subject) obj;
        return (Objects.equals(subject.getName(), this.getName()));
    }
}
