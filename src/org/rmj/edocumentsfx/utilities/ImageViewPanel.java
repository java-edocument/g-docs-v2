package org.rmj.edocumentsfx.utilities;

import javax.swing.*;
import java.awt.*;
import java.beans.*;
import java.io.File;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
 
public class ImageViewPanel extends JPanel
        implements PropertyChangeListener {
    
    private int width, height;
    private ImageIcon icon;
    private Image image;
    private static final int ACCSIZE = 400;
    private Color bg;
    final DoubleProperty zoomProperty = new SimpleDoubleProperty(200);
    
    public ImageViewPanel() {
        setPreferredSize(new Dimension(ACCSIZE, 400));
        bg = getBackground();
    }
    
    public void propertyChange(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        
        // Make sure we are responding to the right event.
        if (propertyName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
            File selection = (File)e.getNewValue();
            String name;
            
            if (selection == null)
                return;
            else
                name = selection.getAbsolutePath();
            
            /*
             * Make reasonably sure we have an image format that AWT can
             * handle so we don't try to draw something silly.
             */
            if ((name != null) &&
                    name.toLowerCase().endsWith(".jpg")) {
                icon = new ImageIcon(name);
                image = icon.getImage();
                scaleImage();
                repaint();
            }
        }
    }
    
    private void scaleImage() {
        width = image.getWidth(this);
        height = image.getHeight(this);
        double ratio = 1;
       
        /* 
         * Determine how to scale the image. Since the accessory can expand
         * vertically make sure we don't go larger than 150 when scaling
         * vertically.
         */
        if (width >= height) {
            ratio = (double)(ACCSIZE -1) / width;
            width = ACCSIZE -1;
            height = (int)(height * ratio);
        }
        else {
            if (getHeight() > 150) {
                ratio = (double)(ACCSIZE-1) / height;
                height = ACCSIZE-1;
                width = (int)(width * ratio);
            }
            else {
                ratio = (double)getHeight() / height;
                height = getHeight();
                width = (int)(width * ratio);
            }
        }
                
        image = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }
    
    public void paintComponent(Graphics g) {
        g.setColor(bg);
        
        /*
         * If we don't do this, we will end up with garbage from previous
         * images if they have larger sizes than the one we are currently
         * drawing. Also, it seems that the file list can paint outside
         * of its rectangle, and will cause odd behavior if we don't clear
         * or fill the rectangle for the accessory before drawing. This might
         * be a bug in JFileChooser.
         */
        g.fillRect(0, 0, ACCSIZE, getHeight());
        g.drawImage(image, getWidth() / 2 - width / 2 + 5,
                getHeight() / 2 - height / 2, this);
    }
    
//    public void fileChooser(){
//        String dir = poTrans.getDefaultPath();
//        
//        File dirFile = new File(dir);
//        
//        TableModel fsImage =  table.getSelectionModel().getSelectedItem();
//        fileChooser = new FileChooser();
//        fileChooser.setTitle("Select an Image");
//        
//        //Set extension filter
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image File", "*.jpg", "*.png");
//        fileChooser.getExtensionFilters().add(extFilter);
//        
//        //set user directory
//         fileChooser.setInitialDirectory(dirFile);
//        
//        this.filePath = fileChooser.showOpenDialog(null);
//        
//        if (filePath != null){
//            try{
//                BufferedImage bufferedImage = ImageIO.read(filePath);
//                    if(bufferedImage !=null){
//                        javafx.scene.image.Image image = SwingFXUtils.toFXImage(bufferedImage, null);
//                        imageview.setImage(image);
//                        fsImage.setIndex05(filePath.getName());
//                        poTrans.setDetail(pnRow, "sFileName", filePath.getName());
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
    
}