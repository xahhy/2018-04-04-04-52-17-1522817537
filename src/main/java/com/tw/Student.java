package com.tw;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Student {
    public String name;
    public String number;
    public List<Subject> subjectList;

    public Student(String name, String number) {
        this.name = name;
        this.number = number;
        this.subjectList = new ArrayList<>();
    }


    public void addSubject(Subject subject) {
        subjectList.add(subject);
    }

    public double averageScore() {
        return round(
                subjectList.stream()
                        .mapToDouble(Subject::getScore)
                        .average()
                        .getAsDouble()
        );
    }

    private double round(double number) {
        return (int) (number * 100) / 100d;
    }

    public double totalScore() {
        return subjectList.stream()
                .mapToDouble(Subject::getScore)
                .sum();
    }

}
