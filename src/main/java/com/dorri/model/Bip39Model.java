package com.dorri.model;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileOutputStream;
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

    public void generateExcel(int sheets, int rows, int cols, String filePath) {

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            if (sheets <= 0 || rows <= 0 || cols <= 0) {
                throw new NumberFormatException("Sheets and rows and cols must be greater than 0");
            }

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
            }

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
                String information = sheets + " sheets × " + rows + "×" + cols + " = " +
                                     (sheets * rows * cols) + " random BIP-39 words";
                this.result = new Result(
                        new Result.Data(filePath),
                        true,
                        information
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
            this.result = new Result(
                    null,
                    false,
                    e.getMessage()
            );
        }
        notifyObservers();
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

    public void setResult(Result result) {
        this.result = result;
    }
}
