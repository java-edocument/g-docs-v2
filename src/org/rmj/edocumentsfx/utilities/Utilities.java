package org.rmj.edocumentsfx.utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;

public class Utilities {
    public final static String jpg = "jpg";
    private static final int MIN_PIXELS = 10;
    
    public static void runDesktopBrowser(){
        Desktop desktop;
    if (Desktop.isDesktopSupported() 
        && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
            try {
                URI mailto = new URI("mailto:jovancasilang_20@hotmail.com?subject=Report_Encountered");
                desktop.mail(mailto);
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
            }
    } else {
      throw new RuntimeException("desktop doesn't support mailto; mail is dead anyway ;)");
    }
    
    }
    
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    public static String getName(File f) {
        String fname = null;
        String s = f.getName();
        int i = s.length() - s.lastIndexOf('.');
        fname = s.substring(0,s.length()-i);

        return fname;
    }
    
    public static void reset(ImageView imageView, double width, double height) {
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
    }
    
    public static void ImageTransaction(ImageView imageview, double width, double height){
        reset(imageview, width / 1.1, height / 1.1);
        
        ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

        imageview.setOnMousePressed(e -> {
            
            Point2D mousePress = imageViewToImage(imageview, new Point2D(e.getX(), e.getY()));
            mouseDown.set(mousePress);
        });

        imageview.setOnMouseDragged(e -> {
            Point2D dragPoint = imageViewToImage(imageview, new Point2D(e.getX(), e.getY()));
            shift(imageview, dragPoint.subtract(mouseDown.get()));
            mouseDown.set(imageViewToImage(imageview, new Point2D(e.getX(), e.getY())));
        });

        imageview.setOnScroll(e -> {
            double delta = e.getDeltaY();
            Rectangle2D viewport = imageview.getViewport();

            double scale = clamp(Math.pow(1.01, delta),

                // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
                Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),

                // don't scale so that we're bigger than image dimensions:
                Math.max(width / viewport.getWidth(), height / viewport.getHeight())

            );

            Point2D mouse = imageViewToImage(imageview, new Point2D(e.getX(), e.getY()));

            double newWidth = viewport.getWidth() * scale;
            double newHeight = viewport.getHeight() * scale;

            // To keep the visual point under the mouse from moving, we need
            // (x - newViewportMinX) / (x - currentViewportMinX) = scale
            // where x is the mouse X coordinate in the image

            // solving this for newViewportMinX gives

            // newViewportMinX = x - (x - currentViewportMinX) * scale 

            // we then clamp this value so the image never scrolls out
            // of the imageview:

            double newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale, 
                    0, width - newWidth);
            double newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale, 
                    0, height - newHeight);

            imageview.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
        });

            imageview.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                   reset(imageview, width, height);
            }
        });
    
    }
    
    
    // shift the viewport of the imageView by the specified delta, clamping so
    // the viewport does not move off the actual image:
    private static void shift(ImageView imageView, Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();

        double width = imageView.getImage().getWidth() ;
        double height = imageView.getImage().getHeight() ;

        double maxX = width - viewport.getWidth();
        double maxY = height - viewport.getHeight();
        
        double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }

    private static double clamp(double value, double min, double max) {

        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    // convert mouse coordinates in the imageView to coordinates in the actual image:
    private static Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
        double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(
                viewport.getMinX() + xProportion * viewport.getWidth(), 
                viewport.getMinY() + yProportion * viewport.getHeight());
    }

    
    
    
}
