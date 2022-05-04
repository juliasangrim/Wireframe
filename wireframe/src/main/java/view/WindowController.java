package view;

import tools.BSpline;
import utils.Converter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class WindowController extends View {

    public WindowController() {
        super();
        try {
            createButtons();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    private void createButtons() throws NoSuchMethodException {
        addSubMenu("File", KeyEvent.VK_F);

        addMenuButton("File/Open...", "Open wireframe", KeyEvent.VK_O, "open.png", "onOpen");
        addToolBarButton("File/Open...");
        addMenuButton("File/Save...", "Save your wireframe", KeyEvent.VK_S, "save.png", "onSave");
        addToolBarButton("File/Save...");
        addSubMenu("B-Spline", KeyEvent.VK_F);
        addMenuButton("B-Spline/Rotation reset...", "Reset rotate to initial value", KeyEvent.VK_S, "rotate.png", "onRotateReset");
        addToolBarButton("B-Spline/Rotation reset...");
        addMenuButton("B-Spline/Settings...", "Make your b-spline", KeyEvent.VK_S, "b-spline.png", "onBSplineSettings");
        addToolBarButton("B-Spline/Settings...");


        addSubMenu("Help", KeyEvent.VK_H);
        addMenuButton("Help/About...", "Shows program version and copyright information", KeyEvent.VK_A, "help.png", "onInfo");
        addToolBarButton("Help/About...");

    }

    public void onInfo(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Wireframe, version 1.0\n Hello! This is simple wireframe!\n" +
                "The list of instrument presented below: \n " +
                "You can make your personal wireframe by point in special editor\n" +
                "Also you can rotate wireframe and reset rotate, save your wireframe(supported extension: txt) \n" +
                "and open your work(supported extension: txt)!\n" +
                "Copyright \u00a9 2022 Julia Trubitsyna, FIT, group 19202", "About Wireframe", JOptionPane.INFORMATION_MESSAGE);

    }


    public void onBSplineSettings(ActionEvent e) {
        var copyBSpline = new BSpline();
        var bSplinePanel = dialogPanel.getBsplinePanel();
        var oldGeneretrixAmount = wireframePanel.getGeneratrixAmount();
        var oldCircleSegmentAmount = wireframePanel.getCircleSegmentAmount();

        copyBSpline.copy(bSplinePanel.getBSpline());

        var clickedValue = JOptionPane.showOptionDialog(this, dialogPanel, "Change b-spline",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (clickedValue == JOptionPane.OK_OPTION) {
            wireframePanel.setBSpline(bSplinePanel.getBSpline());
            wireframePanel.repaint();
        } else {
            var settingsPanel = dialogPanel.getSettingPanel();

            bSplinePanel.setIndexActivePoint(0);
            bSplinePanel.setBSpline(copyBSpline);
            wireframePanel.setGeneratrixAmount(oldGeneretrixAmount);
            wireframePanel.setCircleSegmentAmount(oldCircleSegmentAmount);
            settingsPanel.changePoint(bSplinePanel.getBSpline().getAnchorPoints().get(bSplinePanel.getIndexActivePoint()));

            settingsPanel.setSpinnerValueK(bSplinePanel.getBSpline().getAnchorPointsAmount());
            settingsPanel.setSpinnerValueN(bSplinePanel.getBSpline().getAmountSegments());
            settingsPanel.setSpinnerValueM1(oldGeneretrixAmount);
            settingsPanel.setSpinnerValueM2(oldCircleSegmentAmount);

            wireframePanel.setBSpline(bSplinePanel.getBSpline());

        }
    }

    public void onRotateReset(ActionEvent e) {
        wireframePanel.initRotate();
    }


    public void onOpen(ActionEvent event) {
        FileDialog fd = new FileDialog(this, "Open wireframe", FileDialog.LOAD);
        fd.setVisible(true);
        if (fd.getFile() != null) {
            if (fd.getFile().endsWith(".txt")) {
                try {
                    var isFileValid = Converter.fromFileToWireframe(fd.getDirectory() + fd.getFile(), dialogPanel.getBsplinePanel().getBSpline(), wireframePanel);
                    if (!isFileValid) {
                        JOptionPane.showMessageDialog(this, "Application can't open file because its data invalid.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        wireframePanel.initRotate();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Application can't open file with such extension.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void onSave(ActionEvent event) {
        FileDialog fd = new FileDialog(this, "Save wireframe", FileDialog.SAVE);
        fd.setFile("Untitled");
        fd.setVisible(true);
        try {
            Converter.fromWireframeToFile(fd.getDirectory() + fd.getFile() + ".txt", dialogPanel.getBsplinePanel().getBSpline(), wireframePanel);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Application can't save file.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }


    }
}
