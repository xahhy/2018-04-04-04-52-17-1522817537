package com.tw;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LibraryTest {

    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private Library library;
    ConsoleReader mockSource;

    @Before
    public void setup() {
        library = new Library();
        System.setOut(new PrintStream(outContent));
        mockSource = mock(ConsoleReader.class);
        library.setSource(mockSource);
    }

    private String systemOut() {
        return outContent.toString();
    }

    @Test
    public void should_return_student_info_hint() {
        when(mockSource.read()).thenReturn("1");
        library.selectChoice(Library.ADD_STUDENT_HINT);
        assertTrue(systemOut().endsWith("请输入学生信息(格式:姓名,学号,学科:成绩,...),按回车提交:\n"));
    }

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
        assertTrue(systemOut().endsWith(String.format(Library.ADD_STUDENT_SUCCESS, "Tom")));
    }

    @Test
    public void should_parse_student_information() {

    }

    @Test
    public void testSomeLibraryMethod() {
        Library classUnderTest = new Library();
        assertTrue("someLibraryMethod should return 'true'", classUnderTest.someLibraryMethod());
    }

    @Test
    public void testMockClass() throws Exception {
        // you can mock concrete classes, not only interfaces
        LinkedList mockedList = mock(LinkedList.class);

        // stubbing appears before the actual execution
        String value = "first";
        when(mockedList.get(0)).thenReturn(value);

        assertEquals(mockedList.get(0), value);

    }

}
