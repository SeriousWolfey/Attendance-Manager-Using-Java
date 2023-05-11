/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */


/**
 *
 * @author Dell
 */
package com.mycompany.attendancemanager;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.io.File; 
import java.io.IOException; 
import java.io.OutputStream;  
import java.io.FileOutputStream;
import java.io.FileNotFoundException;  
import org.apache.poi.hssf.usermodel.HSSFWorkbook;  
import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.Row;  
import org.apache.poi.ss.usermodel.Sheet;  
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FillPatternType;  
import org.apache.poi.ss.usermodel.IndexedColors;    
import java.util.Scanner;
import org.apache.poi.hssf.util.HSSFColor;



class student{
    String name;
    String batch;
    char lab;
    boolean a;
    student(String Name,String Batch,char Lab){
        name=Name;
        batch=Batch;
        lab=Lab;
    }
    void display(){
        System.out.println(name);
        System.out.println(batch);
        System.out.println(lab);
    }
    void attend(){
        a=!a;
    }
    void attend(char c){
        switch (c) {
            case 'p':
                a=true;
                break;
            case 'a':
                a=false;
                break;
            default:
                System.out.println("Invalid Attend Value");
                break;
        }
    }
    void status(){
        System.out.println(name+": "+(a?"Present":"Absent"));
    }
}

public class AttendanceManager {
    static HashMap<String,student> Studentdb=new HashMap<>();
    static HashMap<String,List<String>> Batch=new HashMap<>();
    static Scanner sc=new Scanner(System.in);
    static void ScanFile() throws Exception{
        File file=new File("E:\\Documents\\Programs\\Sem3\\Java\\AttendanceManager\\src\\main\\java\\com\\mycompany\\attendancemanager\\studentDatabase.txt");
        Scanner fileio=new Scanner(file);
        String erno,name,batch;
        char lab;
        
        while(fileio.hasNextLine()){
            erno=fileio.nextLine();
            name=fileio.nextLine();
            batch=fileio.nextLine();
            lab=fileio.nextLine().charAt(0);
            
            if (!Batch.containsKey(batch))
                Batch.put(batch,new ArrayList<>());
            if (!Batch.containsKey(batch+lab))
                Batch.put(batch+lab,new ArrayList<>());
            
            Batch.get(batch).add(erno);
            Batch.get(batch+lab).add(erno);
            
            Studentdb.put(erno, new student(name,batch,lab));
            System.out.println(erno);
        }
        for (String er : Studentdb.keySet()){
            System.out.println(er);
            Studentdb.get(er).display();
        }   
        for (String b : Batch.keySet()){
            System.out.println(b);
            System.out.println(Batch.get(b));
        }
    }

    static void takeAttendance(){
        String batch, choice;
        System.out.println("Enter From the following Class: ");
        for (String b:Batch.keySet())
            System.out.println(b);
        batch=sc.nextLine();
        if (Batch.containsKey(batch)){
            for(String s:Batch.get(batch)){
                System.out.print(s+": ");
                do{
                    choice=sc.nextLine();
                    
                }while (!(choice.equalsIgnoreCase("p") || choice.equalsIgnoreCase("a")));
                Studentdb.get(s).attend(choice.toLowerCase().charAt(0));
            }
            displayAttendance(batch);
            System.out.println("Do you Want to save Attendance? (y/n): ");
            do{choice=sc.nextLine();}while (!(choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("n")));
            if (choice.equalsIgnoreCase("y")){
                System.out.println("Enter Name of the file to save in: ");
                choice=sc.nextLine();
                try{
                    SaveFile(batch,choice+".xls");
                    System.out.println("File Saved Sucessfully");
                }
                catch(Exception e) {  
                    System.out.println(e.getMessage());  
                }
            }
        }
        else
            System.out.println("Invalid Batch");
    }
    static void displayAttendance(){
        System.out.println("Enter Your Choice from the following Batches:  ");
        for (String b:Batch.keySet())
            System.out.println(b);
        String batch=sc.nextLine();
        if (Batch.containsKey(batch)){
            displayAttendance(batch);
        }
        else
            System.out.println("Invalid Batch");
    }
    static void displayAttendance(String batch){
        for (String s:Batch.get(batch)){
            System.out.print(s+": ");
            Studentdb.get(s).status();
        }
    }
    static void SaveFile(String batch,String filename) throws Exception{
        Workbook wb=new HSSFWorkbook();
        OutputStream fileOut = new FileOutputStream(filename);
        
        Sheet sheet = wb.createSheet("New Sheet");
        Row row     = sheet.createRow(0);  
        Cell cell   = row.createCell(0);  
        

        Font font1 = wb.createFont();
        Font font2 = wb.createFont();
        font1.setColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
        font2.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        CellStyle StylePresent = wb.createCellStyle(); 
        CellStyle StyleAbsent = wb.createCellStyle(); 
        StylePresent.setFont(font1);
        StyleAbsent.setFont(font2);
        cell.setCellValue("Hello World"); //Listen, don't ask me why I did this
        sheet.setColumnWidth(0, 3000);
        sheet.setColumnWidth(1, 10000);
		
        for (int i=0;i<Batch.get(batch).size();i++){
            row=sheet.createRow(i);
            cell=row.createCell(0);
            cell.setCellValue(Long.parseLong(Batch.get(batch).get(i)));
            cell=row.createCell(1);
            cell.setCellValue(Studentdb.get(Batch.get(batch).get(i)).name);
            cell=row.createCell(2);
            cell.setCellValue(Studentdb.get(Batch.get(batch).get(i)).a?"Present":"Absent");
            cell.setCellStyle(Studentdb.get(Batch.get(batch).get(i)).a?StylePresent:StyleAbsent);  
        }
        wb.write(fileOut);
    }

    public static void main(String[] args) throws Exception{
        ScanFile();
        int x;

        while (true){
            System.out.print("\033[H\033[2J");  
            System.out.flush();
            System.out.print("1. Take Attendance\n2. Show Attendance\n3. Display\n4. Exit\n> ");
            x=sc.nextInt();
            sc.nextLine();//To eliminate newline in input stream
            switch (x) {
                case 1:
                    takeAttendance();
                    break;
                case 2:
                    displayAttendance();
                    break;
                case 3:
                    break;
                case 4:
                    System.exit(0);
                default:
                    System.out.println("Invalid Input");
                    break;
            }
            System.out.println("Enter anything to Continue");
            sc.nextLine();
        }
    }
}
