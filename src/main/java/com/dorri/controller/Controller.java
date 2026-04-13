package com.dorri.controller;

import com.dorri.model.Bip39Model;
import com.dorri.view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {
    private final Bip39Model model;
    private final View view;

    public Controller(Bip39Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(View.CREATE)) {

            int sheets = view.getSheets();
            int rows = view.getRows();
            int cols = view.getCols();

            if (sheets <= 0 || rows <= 0 || cols <= 0) {
                throw new NumberFormatException();
            }

            model.generateExcel(
                    view.getSheets(),
                    view.getRows(),
                    view.getCols()
            );
        }
    }
}
