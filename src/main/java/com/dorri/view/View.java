package com.dorri.view;

import com.dorri.controller.Controller;
import com.dorri.model.Bip39Model;
import com.dorri.model.Observer;

import javax.swing.*;
import java.awt.*;

public class View extends JPanel implements Observer {

    public final static String CREATE = "create";

    private final Bip39Model model;
    private Controller controller;

    private JTextField sheetsField;
    private JTextField rowsField;
    private JTextField colsField;
    private JTextArea resultArea;
    private JButton createButton;

    public View(Bip39Model model) {
        createUI();
        this.model = model;
        model.registerObserver(this);
        attachController(makeController());
    }

    public void attachController(Controller controller) {
        if (this.controller != null) {
            createButton.removeActionListener(this.controller);
        }
        this.controller = controller;
        this.createButton.addActionListener(this.controller);
    }

    protected Controller makeController() {
        return new Controller(model, this);
    }

    public void createUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JLabel sheetsLabel = new JLabel("Sheets:");
        this.sheetsField = new JTextField();

        JLabel rowsLabel = new JLabel("Rows:");
        this.rowsField = new JTextField();

        JLabel colsLabel = new JLabel("Cols:");
        this.colsField = new JTextField();

        inputPanel.add(sheetsLabel);
        inputPanel.add(sheetsField);
        inputPanel.add(rowsLabel);
        inputPanel.add(rowsField);
        inputPanel.add(colsLabel);
        inputPanel.add(colsField);

        this.createButton = new JButton("Create");
        this.createButton.setActionCommand(CREATE);

        this.resultArea = new JTextArea(8, 20);
        this.resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(createButton, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public int getSheets() {
        return Integer.parseInt(sheetsField.getText());
    }

    public int getRows() {
        return Integer.parseInt(rowsField.getText());
    }

    public int getCols() {
        return Integer.parseInt(colsField.getText());
    }

    @Override
    public void update() {
        Bip39Model.Result result = model.getResult();
        resultArea.setText(
                result.fileInfo() + "\n" + result.information()
        );
    }
}
