package ru.bacca.bikerun.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.*;

public class PdfBoxFinder extends PDFGraphicsStreamEngine {
    /**
     * Supply the page to analyze here; to analyze multiple pages
     * create multiple {@link PdfBoxFinder} instances.
     */
    final List<PathElement> path = new ArrayList<>();
    final List<Interval> horizontalLines = new ArrayList<>();
    final List<Interval> verticalLines = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(PdfBoxFinder.class);


    public PdfBoxFinder(PDPage page) {
        super(page);
    }

    /**
     * The boxes ({@link Rectangle2D} instances with coordinates according to
     * the PDF coordinate system, e.g. for decorating the table cells) the
     * {@link PdfBoxFinder} has recognized on the current page.
     */
    public Map<String, Rectangle2D> getBoxes() {
        consolidateLists();
        Map<String, Rectangle2D> result = new HashMap<>();
        if (!horizontalLines.isEmpty() && !verticalLines.isEmpty()) {
            Interval top = horizontalLines.get(horizontalLines.size() - 1);
            char rowLetter = 'A';
            for (int i = horizontalLines.size() - 2; i >= 0; i--, rowLetter++) {
                Interval bottom = horizontalLines.get(i);
                Interval left = verticalLines.get(0);
                int column = 1;
                for (int j = 1; j < verticalLines.size(); j++, column++) {
                    Interval right;
                    if (((rowLetter == 'A' || rowLetter == 'B' || rowLetter == 'D' || rowLetter == 'K' || rowLetter == 'L' || rowLetter == 'M' || rowLetter == 'N') && j == 2)
                            || (rowLetter == 'J' && j == 1)) {
                        right = verticalLines.get(4);
                        putInResult(result, top, rowLetter, bottom, left, column, right);
                        left = verticalLines.get(j);
                    } else if ((rowLetter == 'F' || rowLetter == 'G' || rowLetter == 'H' || rowLetter == 'I') && j == 1) {
                        right = verticalLines.get(2);
                        putInResult(result, top, rowLetter, bottom, left, column, right);
                        left = verticalLines.get(j);
                    } else {
                        right = verticalLines.get(j);
                        putInResult(result, top, rowLetter, bottom, left, column, right);
                        left = right;
                    }


                }
                top = bottom;
            }
        }
        return result;
    }

    private void putInResult(Map<String, Rectangle2D> result, Interval top, char rowLetter, Interval bottom, Interval left, int column, Interval right) {
        String name = String.format("%s%s", rowLetter, column);
        Rectangle2D rectangle = new Rectangle2D.Float(left.from, bottom.from, right.to - left.from, top.to - bottom.from);
        result.put(name, rectangle);
    }

    /**
     * The regions ({@link Rectangle2D} instances with coordinates according
     * to the PDFBox text extraction API, e.g. for initializing the regions of
     * a {@link PDFTextStripperByArea}) the {@link PdfBoxFinder} has recognized
     * on the current page.
     */
    public Map<String, Rectangle2D> getRegions() {
        PDRectangle cropBox = getPage().getCropBox();
        float xOffset = cropBox.getLowerLeftX();
        float yOffset = cropBox.getUpperRightY();
        Map<String, Rectangle2D> result = getBoxes();
        for (Map.Entry<String, Rectangle2D> entry : result.entrySet()) {
            Rectangle2D box = entry.getValue();
            Rectangle2D region = new Rectangle2D.Float(xOffset + (float) box.getX(), yOffset - (float) (box.getY() + box.getHeight()), (float) box.getWidth(), (float) box.getHeight());
            entry.setValue(region);
        }
        return result;
    }

    /**
     * <p>
     * Processes the path elements currently in the {@link #path} list and
     * eventually clears the list.
     * </p>
     * <p>
     * Currently only elements are considered which
     * </p>
     * <ul>
     * <li>are {@link Rectangle} instances;
     * <li>are filled fairly black;
     * <li>have a thin and long form; and
     * <li>have sides fairly parallel to the coordinate axis.
     * </ul>
     */
    void processPath() throws IOException {
        PDColor color = getGraphicsState().getNonStrokingColor();
        if (!isBlack(color)) {
            logger.debug("Dropped path due to non-black fill-color.");
            return;
        }

        for (PathElement pathElement : path) {
            if (pathElement instanceof Rectangle) {
                Rectangle rectangle = (Rectangle) pathElement;

                double p0p1 = rectangle.p0.distance(rectangle.p1);
                double p1p2 = rectangle.p1.distance(rectangle.p2);
                boolean p0p1small = p0p1 < 3;
                boolean p1p2small = p1p2 < 3;

                if (p0p1small) {
                    if (p1p2small) {
                        logger.debug("Dropped rectangle too small on both sides.");
                    } else {
                        processThinRectangle(rectangle.p0, rectangle.p1, rectangle.p2, rectangle.p3);
                    }
                } else if (p1p2small) {
                    processThinRectangle(rectangle.p1, rectangle.p2, rectangle.p3, rectangle.p0);
                } else {
                    logger.debug("Dropped rectangle too large on both sides.");
                }
            }
        }
        path.clear();
    }

    /**
     * The argument points shall be sorted to have (p0, p1) and (p2, p3) be the small
     * edges and (p1, p2) and (p3, p0) the long ones.
     */
    void processThinRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) {
        float longXDiff = (float) Math.abs(p2.getX() - p1.getX());
        float longYDiff = (float) Math.abs(p2.getY() - p1.getY());
        boolean longXDiffSmall = longXDiff * 10 < longYDiff;
        boolean longYDiffSmall = longYDiff * 10 < longXDiff;

        if (longXDiffSmall) {
            verticalLines.add(new Interval(p0.getX(), p1.getX(), p2.getX(), p3.getX()));
        } else if (longYDiffSmall) {
            horizontalLines.add(new Interval(p0.getY(), p1.getY(), p2.getY(), p3.getY()));
        } else {
            logger.debug("Dropped rectangle too askew.");
        }
    }

    /**
     * Sorts the {@link #horizontalLines} and {@link #verticalLines} lists and
     * merges fairly identical entries.
     */
    void consolidateLists() {
        for (List<Interval> intervals : Arrays.asList(horizontalLines, verticalLines)) {
            intervals.sort(null);
            for (int i = 1; i < intervals.size(); ) {
                if (intervals.get(i - 1).combinableWith(intervals.get(i))) {
                    Interval interval = intervals.get(i - 1).combineWith(intervals.get(i));
                    intervals.set(i - 1, interval);
                    intervals.remove(i);
                } else {
                    i++;
                }
            }
        }
    }

    /**
     * Checks whether the given color is black'ish.
     */
    boolean isBlack(PDColor color) throws IOException {
        int value = color.toRGB();
        for (int i = 0; i < 2; i++) {
            int component = value & 0xff;
            if (component > 5)
                return false;
            value /= 256;
        }
        return true;
    }

    //
    // PDFGraphicsStreamEngine overrides
    //
    @Override
    public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) throws IOException {
        path.add(new Rectangle(p0, p1, p2, p3));
    }

    @Override
    public void endPath() throws IOException {
        path.clear();
    }

    @Override
    public void strokePath() throws IOException {
        path.clear();
    }

    @Override
    public void fillPath(int windingRule) throws IOException {
        processPath();
    }

    @Override
    public void fillAndStrokePath(int windingRule) throws IOException {
        processPath();
    }

    @Override
    public void drawImage(PDImage pdImage) throws IOException {
    }

    @Override
    public void clip(int windingRule) throws IOException {
    }

    @Override
    public void moveTo(float x, float y) throws IOException {
    }

    @Override
    public void lineTo(float x, float y) throws IOException {
    }

    @Override
    public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) throws IOException {
    }

    @Override
    public Point2D getCurrentPoint() throws IOException {
        return null;
    }

    @Override
    public void closePath() throws IOException {
    }

    @Override
    public void shadingFill(COSName shadingName) throws IOException {
    }

    //
    // inner classes
    //
    class Interval implements Comparable<Interval> {
        final float from;
        final float to;

        Interval(float... values) {
            Arrays.sort(values);
            this.from = values[0];
            this.to = values[values.length - 1];
        }

        Interval(double... values) {
            Arrays.sort(values);
            this.from = (float) values[0];
            this.to = (float) values[values.length - 1];
        }

        boolean combinableWith(Interval other) {
            if (this.from > other.from)
                return other.combinableWith(this);
            if (this.to < other.from)
                return false;
            float intersectionLength = Math.min(this.to, other.to) - other.from;
            float thisLength = this.to - this.from;
            float otherLength = other.to - other.from;
            return (intersectionLength >= thisLength * .9f) || (intersectionLength >= otherLength * .9f);
        }

        Interval combineWith(Interval other) {
            return new Interval(this.from, this.to, other.from, other.to);
        }

        @Override
        public int compareTo(Interval o) {
            return this.from == o.from ? Float.compare(this.to, o.to) : Float.compare(this.from, o.from);
        }

        @Override
        public String toString() {
            return String.format("[%3.2f, %3.2f]", from, to);
        }
    }

    interface PathElement {
    }

    class Rectangle implements PathElement {
        final Point2D p0, p1, p2, p3;

        Rectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) {
            this.p0 = p0;
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
        }
    }
}
