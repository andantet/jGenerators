package io.github.andantedevs.jgenerators.util;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class CrashDialog {
    public CrashDialog(Throwable throwable) {
        JFrame frame = new JFrame("jGenerators has crashed!");
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());

        JTextArea text = new JTextArea();
        text.setTabSize(2);
        pane.add(new JScrollPane(text), BorderLayout.CENTER);

        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        String stackTrace = stringWriter.toString();
        text.setText(stackTrace);

        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.doLayout();
        frame.setVisible(true);
    }
}
