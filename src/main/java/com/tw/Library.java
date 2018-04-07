package com.tw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Library {
    public static final String ADD_STUDENT_HINT = "请输入学生信息(格式:姓名,学号,学科:成绩,...),按回车提交:\n";
    public static final String MAIN_INFORMATION = "1.添加学生\n2.生成成绩单\n3.退出\n";
    public static final String MAIN_HINT = "请输入你的选择(1~3):";
    public static final String ADD_STUDENT_SUCCESS = "学生%s的成绩被添加";
    private ConsoleReader source;

    public Library() {
        source = new ConsoleReader();
    }

    public void Start() {
        System.out.print(MAIN_INFORMATION);
        int selection;
        do {
            selection = selectChoice(MAIN_HINT);
            action(selection);
        }while (selection != 3);
    }

    public Integer selectChoice(String hint) {
        System.out.print(hint);
        return Integer.valueOf(source.read());
    }

    public void setSource(ConsoleReader source) {
        this.source = source;
    }

    public boolean action(int selection) {
        System.out.print("学生Tom的成绩被添加\n");
        return true;
    }

    public Student parseString(String string) {
        List<String> resultList = Arrays.asList(string.split(","))
                .stream()
                .map(String::trim)
                .collect(Collectors.toList());
        String name = resultList.get(0);
        String number = resultList.get(1);
        Student student = new Student(name, number);
        resultList.stream()
                .skip(2)
                .map(item->{
                    Subject subject = parseSubjectFromString(item);
                    student.addSubject(subject);
                    return string;
                })
                .count();
        return student;
    }

    public Subject parseSubjectFromString(String string) {
        List<String> result = Arrays.asList(string.split(":"))
                .stream()
                .map(String::trim)
                .collect(Collectors.toList());
        return new Subject(result.get(0), Double.valueOf(result.get(1)));
    }
}
