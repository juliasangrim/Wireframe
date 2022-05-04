package utils;

import tools.BSpline;
import view.SettingPanel;
import view.WireframePanel;

import java.io.*;
import java.util.ArrayList;


public class Converter {

    public static boolean fromFileToWireframe(String fileName, BSpline bSpline, WireframePanel wireframePanel) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            int anchorPointsAmount;
            int segmentsAmount;
            int generatrixAmount;
            int circleSegmentsAmount;

            ArrayList<Point> anchorPoints = new ArrayList<>();
            var parseLine = br.readLine().split(":");

            if (parseLine.length != 2) {
                return false;
            }
            if (parseLine[0].equals("K")) {
                anchorPointsAmount = Integer.parseInt(parseLine[1]);
                if (anchorPointsAmount < SettingPanel.MIN_ANCHOR_POINTS_AMOUNT
                        || anchorPointsAmount > SettingPanel.MAX_ANCHOR_POINTS_AMOUNT) {
                    return false;
                }
            } else {
                return false;
            }

            parseLine = br.readLine().split(":");

            if (parseLine.length != 2) {
                return false;
            }
            if (parseLine[0].equals("N")) {
                segmentsAmount = Integer.parseInt(parseLine[1]);
                if (segmentsAmount < SettingPanel.MIN_SEGMENTS_AMOUNT
                        || segmentsAmount > SettingPanel.MAX_SEGMENTS_AMOUNT) {
                    return false;
                }
            } else {
                return false;
            }

            parseLine = br.readLine().split(":");

            if (parseLine.length != 2) {
                return false;
            }
            if (parseLine[0].equals("M1")) {
                generatrixAmount = Integer.parseInt(parseLine[1]);
                if (generatrixAmount < SettingPanel.MIN_GENERATRIX_AMOUNT || generatrixAmount > SettingPanel.MAX_GENERATRIX_AMOUNT) {
                    return false;
                }
            } else {
                return false;
            }

            parseLine = br.readLine().split(":");

            if (parseLine.length != 2) {
                return false;
            }
            if (parseLine[0].equals("M2")) {
                circleSegmentsAmount = Integer.parseInt(parseLine[1]);
                if (circleSegmentsAmount < SettingPanel.MIN_CIRCLE_SEGMENTS_AMOUNT || circleSegmentsAmount > SettingPanel.MAX_CIRCLE_SEGMENTS_AMOUNT) {
                    return false;
                }
            } else {
                return false;
            }

            parseLine = br.readLine().split(":");

            if (parseLine.length != 2) {
                return false;
            }
            if (parseLine[0].equals("Points")) {
                var points = parseLine[1].split(";");
                for (var point : points) {
                    var parsePoint = point.split(",");
                    if (parsePoint.length == 2) {
                        anchorPoints.add(new Point(Integer.parseInt(parsePoint[0]), Integer.parseInt(parsePoint[1])));
                    } else {
                        return false;
                    }
                }
                if (anchorPoints.size() != anchorPointsAmount) {
                    return false;
                }
            } else {
                return false;
            }

            bSpline.setParamBSpline(anchorPointsAmount, segmentsAmount, anchorPoints);
            wireframePanel.setCircleSegmentAmount(circleSegmentsAmount);
            wireframePanel.setGeneratrixAmount(generatrixAmount);
            return true;
        }


    }


    public static void fromWireframeToFile(String fileName, BSpline bSpline, WireframePanel wireframePanel) throws IOException {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(fileName))) {
            printWriter.print("K:" + bSpline.getAnchorPointsAmount() + "\n");
            printWriter.print("N:" + bSpline.getAmountSegments() + "\n");
            printWriter.print("M1:" + wireframePanel.getGeneratrixAmount() + "\n");
            printWriter.print("M2:" + wireframePanel.getCircleSegmentAmount() + "\n");
            printWriter.print("Points:");
            for (var point : bSpline.getAnchorPoints()) {
                printWriter.print(point.getX() + "," + point.getY() + ";");
            }
        }
    }
}
