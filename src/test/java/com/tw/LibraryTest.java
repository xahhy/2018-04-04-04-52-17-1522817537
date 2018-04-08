package com.tw;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LibraryTest {

    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private Library library;
    ConsoleReader mockSource;
    PrintStream console;

    @Before
    public void setup() {
        library = new Library();
        console = System.out;
        System.setOut(new PrintStream(outContent));
        mockSource = mock(ConsoleReader.class);
        library.setSource(mockSource);
    }

    private String systemOut() {
        return outContent.toString();
    }

//    @Test
//    public void should_return_student_info_hint() {
//        when(mockSource.read()).thenReturn("1");
//        library.selectChoice(Library.ADD_STUDENT_HINT);
//        System.setOut(console);
//        System.out.println(systemOut());
//        assertTrue(systemOut().endsWith(Library.ADD_STUDENT_HINT));
//    }

    @Test
    public void should_return_x_when_select_x() {
        int result;
        when(mockSource.read()).thenReturn("1");
        result = library.selectChoice(Library.ADD_STUDENT_HINT);
        assertEquals(1, result);

        when(mockSource.read()).thenReturn("2");
        result = library.selectChoice(Library.ADD_STUDENT_HINT);
        assertEquals(2, result);
    }

    @Test
    public void should_return_success_message_when_add_student() {
        int result;
        when(mockSource.read()).thenReturn("1");
        result = library.selectChoice(Library.ADD_STUDENT_HINT);
        library.action(result);
        assertTrue(systemOut().endsWith(String.format(Library.ADD_STUDENT_SUCCESS, "Tom") + "\n"));
    }

    @Test
    public void should_return_1_subject() {
        Subject subject = new Subject("数学", 100d);
        Subject result = library.parseSubjectFromString(" 数学 :  100");
        assertEquals(subject, result);
    }

    @Test
    public void should_parse_string_to_student_object() {
        Student student = library.parseString("Tom, 110, 数学:75, 语文:95, 英语:100");
        assertEquals("Tom", student.name);
        assertEquals("110", student.number);
        assertEquals(new Subject("数学", 75d), student.subjectList.get(0));
        assertEquals(new Subject("语文", 95d), student.subjectList.get(1));
        assertEquals(new Subject("英语", 100d), student.subjectList.get(2));
    }

    @Test
    public void should_get_wrong_message_if_no_name() {
        Student student = library.parseString("110, 数学:75, 语文:95, 英语:100");
        assertNull(student);
        assertTrue(systemOut().endsWith("请按正确的格式输入(格式:姓名,学号,学科:成绩,...):\n"));
    }

    @Test
    public void should_get_wrong_message_if_no_number() {
        Student student = library.parseString("Tom, 数学:75, 语文:95, 英语:100");
        assertNull(student);
        assertTrue(systemOut().endsWith("请按正确的格式输入(格式:姓名,学号,学科:成绩,...):\n"));
    }

    @Test
    public void should_get_wrong_message_if_no_subject() {
        Student student = library.parseString("Tom, 110,");
        assertNull(student);
        assertTrue(systemOut().endsWith("请按正确的格式输入(格式:姓名,学号,学科:成绩,...):\n"));
    }

    @Test
    public void should_get_wrong_message_if_wrong_subject_format() {
        Student student = library.parseString("Tom, 110, 数学, 语文:95, 英语:100");
        assertNull(student);
        assertTrue(systemOut().endsWith("请按正确的格式输入(格式:姓名,学号,学科:成绩,...):\n"));
    }

    @Test
    public void should_add_students_to_library() {
        Student tom = new Student("Tom", "110");
        Student jerry = new Student("Jerry", "111");
        Student bob = new Student("Bob", "112");
        tom.addSubject(new Subject("数学", 100d));
        tom.addSubject(new Subject("语文", 85d));
        jerry.addSubject(new Subject("数学", 90d));
        jerry.addSubject(new Subject("英语", 75d));
        bob.addSubject(new Subject("英语", 95d));
        bob.addSubject(new Subject("编程", 60d));
        library.addStudent(tom, jerry, bob);
        assertTrue(library.getStudents().contains(tom));
        assertTrue(library.getStudents().contains(jerry));
        assertTrue(library.getStudents().contains(bob));
    }

    @Test
    public void should_get_all_distinct_subject_from_students() {
        Student tom = new Student("Tom", "110");
        Student jerry = new Student("Jerry", "111");
        Student bob = new Student("Bob", "112");
        tom.addSubject(new Subject("数学", 100d));
        tom.addSubject(new Subject("语文", 85d));
        jerry.addSubject(new Subject("数学", 90d));
        jerry.addSubject(new Subject("英语", 75d));
        bob.addSubject(new Subject("英语", 95d));
        bob.addSubject(new Subject("编程", 60d));
        library.addStudent(tom, jerry, bob);
        List<Subject> subjectList = library.getDistinctSubjects();
        List<Subject> expectSubjects = Arrays.asList(
                new Subject("数学", 0d),
                new Subject("语文", 0d),
                new Subject("英语", 0d),
                new Subject("编程", 0d)
        );
        assertTrue(subjectList.stream().allMatch(subject -> expectSubjects.stream()
                .anyMatch(expectSubject -> expectSubject.getName() == subject.getName())
        ));
    }
}
