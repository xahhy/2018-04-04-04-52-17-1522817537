package com.tw;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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

    @Test
    public void should_get_title_of_all_distinct_subject_from_students() {
        fillLibraryWith3Students();
        String title = library.getScoreTableTitle();
        assertEquals("姓名|数学|语文|英语|编程|平均分|总分", title);
    }

    @Test
    public void should_generate_score_result_of_each_student() {
        Student student = new Student("Tom", "110");
        Subject math = new Subject("数学", 75d);
        Subject chinese = new Subject("语文", 95d);
        Subject english = new Subject("英语", 80d);
        Subject program = new Subject("编程", 80d);
        student.addSubject(math, chinese, english, program);
        Subject[] subjectArray = Stream.of(math, chinese, english, program)
                .toArray(Subject[]::new);
        String result = student.genScoreString(subjectArray);
        assertEquals("Tom|75|95|80|80|82.5|330", result);
    }

    @Test
    public void should_generate_score_result_when_student_dont_have() {
        Student student = new Student("Tom", "110");
        Subject math = new Subject("数学", 75d);
        Subject chinese = new Subject("语文", 95d);
        Subject english = new Subject("英语", 80d);
        Subject program = new Subject("编程", 80d);
        student.addSubject(math, chinese, program);
        Subject[] subjectArray = Stream.of(math, chinese, english, program)
                .toArray(Subject[]::new);
        String result = student.genScoreString(subjectArray);
        assertEquals("Tom|75|95|--|80|83.33|250", result);
    }

    @Test
    public void should_get_all_students_average_score() {
        fillLibraryWith2Students();
        Double allAverageScore = library.getAllTotalAverageScore();
        assertEquals(327.5d, allAverageScore, 0d);
    }

    @Test
    public void should_get_all_2_students_middle_score() {
        fillLibraryWith2Students();
        Double allAverageScore = library.getAllTotalMiddleScore();
        assertEquals(327.5d, allAverageScore, 0d);
    }

    @Test
    public void should_get_all_3_students_middle_score() {
        fillLibraryWith3Students();
        Double allAverageScore = library.getAllTotalMiddleScore();
        assertEquals(325d, allAverageScore, 0d);
    }

    @Test
    public void should_get_score_list() {
        fillLibraryWith3Students();
        String scoreListString = library.getScoreListString();
        assertEquals("成绩单\n姓名|数学|语文|英语|编程|平均分|总分\n" +
                "========================\n" +
                "Tom|75|95|80|80|82.5|330\n" +
                "Jerry|85|80|70|90|81.25|325\n" +
                "Bob|85|80|70|90|81.25|325\n" +
                "========================\n" +
                "全班总分平均数:326.67\n" +
                "全班总分中位数:325\n", scoreListString);
    }

    private void fillLibraryWith2Students() {
        Student tom = new Student("Tom", "110");
        Student jerry = new Student("Jerry", "111");
        tom.addSubject(
                new Subject("数学", 75d),
                new Subject("语文", 95d),
                new Subject("英语", 80d),
                new Subject("编程", 80d)
        );
        jerry.addSubject(
                new Subject("数学", 85d),
                new Subject("语文", 80d),
                new Subject("英语", 70d),
                new Subject("编程", 90d)
        );
        library.addStudent(tom, jerry);
    }

    private void fillLibraryWith3Students() {
        fillLibraryWith2Students();
        Student bob = new Student("Bob", "112");
        bob.addSubject(
                new Subject("数学", 85d),
                new Subject("语文", 80d),
                new Subject("英语", 70d),
                new Subject("编程", 90d)
        );
        library.addStudent(bob);
    }
}
