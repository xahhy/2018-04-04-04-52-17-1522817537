package com.tw;

import java.io.Console;
import java.util.Scanner;

public class ConsoleReader {
    private final Scanner source;

    public ConsoleReader() { source = new Scanner(System.in);}
    public String read(){
        return source.next();
    }
}
