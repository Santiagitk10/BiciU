package ui;

import java.util.Scanner;

public class Reader {

    public String scannerText(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public int scannerInt(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

}
