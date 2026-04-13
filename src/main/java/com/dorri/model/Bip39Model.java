package com.dorri.model;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Bip39Model {
    private final List<String> WORD_LIST;
    private final List<Observer> observers;
    private Result result;

    public Bip39Model() {
        WORD_LIST = loadWordList();
        observers = new ArrayList<>();
    }

    private List<String> loadWordList() {
        try (var is = Bip39Model.class.getResourceAsStream("/bip39-english.txt");
             var reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .map(String::trim)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Cannot load BIP-39 wordlist", e);
        }
    }

    public void generateExcel(int sheets, int rows, int cols) {

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            SecureRandom random = new SecureRandom();

            for (int s = 1; s <= sheets; s++) {
                XSSFSheet sheet = workbook.createSheet("Seed " + s);

                for (int r = 0; r < rows; r++) {
                    var row = sheet.createRow(r);

                    for (int c = 0; c < cols; c++) {
                        // Pick random word from the 2048-word list
                        String word = WORD_LIST.get(random.nextInt(WORD_LIST.size()));
                        row.createCell(c).setCellValue(word);
                    }
                }

                // Optional: auto-size columns (can be slow with many sheets)
                // for (int c = 0; c < COLS; c++) sheet.autoSizeColumn(c);
            }

            String filename = String.format("bip39-%s(%d_sheets_%dx%d).xlsx",
                    System.currentTimeMillis(),
                    sheets,
                    rows,
                    cols
            );

            try (FileOutputStream out = new FileOutputStream(filename)) {
                workbook.write(out);
                String fileInfo = "File Created: " + filename;
                String information = "Information: " + sheets + " sheets × " + rows + "×" + cols + " = " +
                                (sheets * rows * cols) + " random BIP-39 words";
                this.result = new Result(fileInfo, information);
                notifyObservers();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public record Result(
            String fileInfo,
            String information
    ) {
    }

    public Result getResult() {
        return result;
    }

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
