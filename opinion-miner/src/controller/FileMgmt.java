package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileMgmt {

    public static ArrayList<String> readDataFile(String fname) {
        String line;
        ArrayList file = new ArrayList();
        try {
            BufferedReader in = new BufferedReader(new FileReader(fname));
            while ((line = in.readLine()) != null) {
                line = line.trim();
                file.add(line);
            }
            in.close();
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
        return file;
    }

    public static String printArrayList(ArrayList<String> str) {
        String res1 = "";
        for (int i = 0; i < str.size(); i++) {
            //System.out.println(str.get(i));
            res1 += str.get(i) + "\n";
        }
        return res1;
    }

    public static void writeDataFile(String fname, String res) {
        try {
            FileWriter fstream = new FileWriter(fname);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(res);
            out.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void readwriteArrayList(String fname, String addLine) {
        String line, newLine;
        ArrayList file = new ArrayList();
        try {
            BufferedReader in = new BufferedReader(new FileReader(fname));
            while ((line = in.readLine()) != null) {
                line = line.trim();
                file.add(line);
            }
            in.close();
            newLine = addLine;
            file.add(addLine);
            FileWriter fstream = new FileWriter(fname);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(printArrayList(file));
            out.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
