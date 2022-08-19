package view;

import javax.swing.*;

public class CActionMenu  {

    //main menu prop
    private JMenuBar fileMenu;
    private JMenu fileMenuB;
    private JMenuItem loadFile;
    private JMenuItem saveFile;

    public CActionMenu(SalesInvoice salesInvoice) {

        loadFile = new JMenuItem("Load file");
        loadFile.addActionListener(salesInvoice);
        loadFile.setActionCommand("Load");

        saveFile = new JMenuItem("Save file");
        saveFile.addActionListener(salesInvoice);
        saveFile.setActionCommand("Save");

        fileMenuB = new JMenu("File");
        fileMenuB.add(loadFile);
        fileMenuB.add(saveFile);

        fileMenu = new JMenuBar();
        fileMenu.add(fileMenuB);

        salesInvoice.setJMenuBar(fileMenu);
    }
}
