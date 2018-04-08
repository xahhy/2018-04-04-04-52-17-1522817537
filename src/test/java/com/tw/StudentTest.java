package com.tw;

import org.junit.Test;

import static org.junit.Assert.*;

public class StudentTest {
    @Test
    public void create_1_student() {
        Student student = new Student("Tom", "110");
        assertEquals("Tom", student.name);
        assertEquals("110", student.number);
    }

    @Test
    public void should_calculate_average_number() {
        Student student = new Student("Tom", "110");
        Subject math = new Subject("数学", 75d);
        Subject chinese = new Subject("语文", 95d);
        Subject english = new Subject("英语", 80d);
        student.addSubject(math);
        student.addSubject(chinese);
        student.addSubject(english);
        assertEquals(83.33d, student.averageScore(), 0.0d);
    }

    @Test
    public void should_calculate_total_number() {
        Student student = new Student("Tom", "110");
        Subject math = new Subject("数学", 75d);
        Subject chinese = new Subject("语文", 95d);
        Subject english = new Subject("英语", 80d);
        student.addSubject(math);
        student.addSubject(chinese);
        student.addSubject(english);
        assertEquals(250d, student.totalScore(), 0.0d);
    }

}