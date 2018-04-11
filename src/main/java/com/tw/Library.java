package com.tw;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Library {
    public static final String ADD_STUDENT_HINT = "请输入学生信息(格式:姓名,学号,学科:成绩,...),按回车提交:\n";
    public static final String MAIN_INFORMATION = "1.添加学生\n2.生成成绩单\n3.退出\n";
    public static final String MAIN_HINT = "请输入你的选择(1~3):";
    public static final String ADD_STUDENT_SUCCESS = "学生%s的成绩被添加";
    public static final String ADD_STUDENT_WRONG_MESSAGE = "请按正确的格式输入(格式:姓名,学号,学科:成绩,...):\n";
    public static final String SPLITER = "========================";
    public static final String TITLE = "成绩单";

    private ConsoleReader source;
    private List<Student> students;

    public Library() {
        source = new ConsoleReader();
        students = new ArrayList<Student>();
    }

    public void Start() {
        System.out.print(MAIN_INFORMATION);
        int selection;
        do {
            selection = selectChoice(MAIN_HINT);
            action(selection);
        } while (selection != 3);
    }

    public Integer selectChoice(String hint) {
        System.out.print(hint);
        return Integer.valueOf(source.read());
    }

    public void setSource(ConsoleReader source) {
        this.source = source;
    }

    public boolean action(int selection) {
        switch (selection) {
            case 1:
                System.out.print(ADD_STUDENT_HINT);
                Student newStudent = parseString(source.read());
                if (!Objects.equals(null, newStudent)) {
                    addStudent(newStudent);
                    System.out.print(String.format(ADD_STUDENT_SUCCESS, newStudent.name) + "\n");
                }
                break;
            case 2:
                System.out.print(getScoreListString());
                break;
            default:
                break;
        }
        return true;
    }

    public Student parseString(String string) {
        try {
            List<String> resultList = Arrays.asList(string.split(","))
                    .stream()
                    .map(String::trim)
                    .collect(Collectors.toList());
            String name = resultList.get(0);
            String number = resultList.get(1);
            if (Stream.of(name, number)
                    .anyMatch(item -> item.contains(":"))) {
                throw new Exception("学号不能包含特殊符号");
            }
            Student student = new Student(name, number);
            long subjectNumber = resultList.stream()
                    .skip(2)
                    .map(item -> {
                        Subject subject = parseSubjectFromString(item);
                        student.addSubject(subject);
                        return string;
                    })
                    .count();
            if (subjectNumber == 0) {
                throw new Exception("没有输入学科信息");
            }
            return student;
        } catch (Exception e) {
            System.out.print(ADD_STUDENT_WRONG_MESSAGE);
            return null;
        }
    }

    public Subject parseSubjectFromString(String string) {
        List<String> result = Arrays.asList(string.split(":"))
                .stream()
                .map(String::trim)
                .collect(Collectors.toList());
        return new Subject(result.get(0), Double.valueOf(result.get(1)));
    }

    public void addStudent(Student... students) {
        this.students.addAll(Arrays.asList(students));
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Subject> getDistinctSubjects() {
        Set<String> subjectSet = new HashSet<>();
        return students.stream()
                .flatMap(student -> student.subjectList.stream())
                .filter(subject -> subjectSet.add(subject.getName()))
                .collect(Collectors.toList());
    }

    public Double getAllTotalAverageScore() {
        return students.stream()
                .mapToDouble(Student::totalScore)
                .average()
                .orElse(0d);
    }

    public Double getAllTotalMiddleScore() {
        List<Double> resultList = students.stream()
                .mapToDouble(Student::totalScore)
                .sorted()
                .boxed()
                .collect(Collectors.toList());
        int size = resultList.size();
        if (size == 0)return 0d;
        if (size % 2 == 0) {
            return (resultList.get(size / 2 - 1) + resultList.get(size / 2)) / 2d;
        } else {
            return resultList.get(size / 2);
        }

    }

    public String getScoreListString() {
        String bodyString = students.stream()
                .map(student -> student.genScoreString(
                        getDistinctSubjects().stream().toArray(Subject[]::new))
                )
                .collect(Collectors.joining("\n"));
        return TITLE + "\n" +
                getScoreTableTitle() + "\n" +
                SPLITER + "\n" +
                bodyString + "\n" +
                SPLITER + "\n" +
                "全班总分平均数:" + Utils.doubleToString(getAllTotalAverageScore()) + "\n" +
                "全班总分中位数:" + Utils.doubleToString(getAllTotalMiddleScore()) + "\n"
                ;
    }

    public String getScoreTableTitle() {
        String subjectListString = getDistinctSubjects().stream()
                .map(Subject::getName)
                .collect(Collectors.joining("|"));
        return Stream.of("姓名", subjectListString, "平均分", "总分")
                .collect(Collectors.joining("|"));
    }
}
