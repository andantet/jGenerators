package io.github.andantedevs.jgenerators;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static io.github.andantedevs.jgenerators.Main.log;

public class GeneratorGUI extends JFrame implements ActionListener {
    public static GeneratorGUI instance;

    public GeneratorGUI() {
        JButton generateButton = new JButton("Generate");
        generateButton.addActionListener(this);
        this.add(generateButton, BorderLayout.CENTER);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        /*setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource("icon.png")));*/
    }

    public static void start() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        GeneratorGUI dialog = new GeneratorGUI();
        instance = dialog;
        dialog.pack();
        dialog.setTitle("jGenerators");
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Generate":
                log("AYYY");
                break;
        }
    }
}
