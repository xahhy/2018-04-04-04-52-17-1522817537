package com.tw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Student {
    public String name;
    public String number;
    public List<Subject> subjectList;

    public Student(String name, String number) {
        this.name = name;
        this.number = number;
        this.subjectList = new ArrayList<>();
    }


    public void addSubject(Subject... subjects) {
        subjectList.addAll(Arrays.asList(subjects));
    }

    public Double averageScore() {
        return Utils.round(
                subjectList.stream()
                        .mapToDouble(Subject::getScore)
                        .average()
                        .getAsDouble()
        );
    }

    public double totalScore() {
        return subjectList.stream()
                .mapToDouble(Subject::getScore)
                .sum();
    }

    public String genScoreString(Subject... subjects) {
        List<String> scoreList = Stream.of(subjects)
                .map(subject -> {
                    if (!this.subjectList.contains(subject)) {
                        return "--";
                    }else
                        return subjectList.get(subjectList.indexOf(subject)).getScoreString();
                })
                .collect(Collectors.toList());
        String result = Stream.of(this.name, scoreList, Utils.doubleToString(this.averageScore()), Utils.doubleToString(this.totalScore()))
                .flatMap(item->{
                    if (item.getClass().equals(ArrayList.class)) {
                        return scoreList.stream();
                    }else
                        return Stream.of(item);
                })
                .map(item->{
                    return item.toString();
                })
                .collect(Collectors.joining("|"));
        return result;
    }
}
