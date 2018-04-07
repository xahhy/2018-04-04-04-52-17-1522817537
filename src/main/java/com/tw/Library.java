package com.tw;

public class Library {
    public static final String ADD_STUDENT_HINT = "请输入学生信息(格式:姓名,学号,学科:成绩,...),按回车提交:\n";
    public static final String MAIN_INFORMATION = "1.添加学生\n2.生成成绩单\n3.退出\n";
    public static final String MAIN_HINT = "请输入你的选择(1~3):";
    public static final String ADD_STUDENT_SUCCESS = "学生%s的成绩被添加";
    private ConsoleReader source;

    public boolean someLibraryMethod() {
        return true;
    }

    public Library() {
        source = new ConsoleReader();
    }

    public void Start() {
        System.out.print(MAIN_INFORMATION);
        selectChoice(MAIN_HINT);
    }

    public Integer selectChoice(String hint) {
        System.out.print(ADD_STUDENT_HINT);
        return Integer.valueOf(source.read());
    }

    public void setSource(ConsoleReader source) {
        this.source = source;
    }

    public boolean action(int selection) {
        System.out.print("学生Tom的成绩被添加");
        return true;
    }
}
