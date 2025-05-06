/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lucabortolamei.FindNumber;

import java.io.*;
import java.util.*;

public class CsvHandler {
    private static final String FILE_PATH = "players.csv";

    public static void savePlayer(String name, int attempts) {
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(name + "," + attempts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> loadRanking() {
        List<String[]> ranking = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                ranking.add(line.split(","));
            }
            ranking.sort(Comparator.comparingInt(a -> Integer.parseInt(a[1]))); // Sort by attempts (ascending)
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ranking;
    }
}