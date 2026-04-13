package com.dorri;

import com.dorri.model.Bip39Model;
import com.dorri.view.View;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            View view = new View(new Bip39Model());

            JFrame frame = new JFrame("My App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(view);

            frame.setSize(800, 600);
            frame.setResizable(false);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
