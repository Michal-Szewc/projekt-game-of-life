package agh.ics.oop.gui;

import agh.ics.oop.Animal;
import agh.ics.oop.IMapElement;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class GuiElementBox {
    private ImageView imageView;
    private static int width = 20;
    private static int height = 20;

    static HashMap<String, Image> imageMap = new HashMap<>();

    public GuiElementBox(IMapElement element) throws FileNotFoundException {
        Image image;
        if (imageMap.containsKey(element.resourcePath()))
            image = imageMap.get(element.resourcePath());
        else {
            image = new Image(new FileInputStream(element.resourcePath()));
            imageMap.put(element.resourcePath(), image);
        }

        this.imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        if (element instanceof Animal animal){
            imageView.setOpacity((double)animal.getEnergy()/(double)animal.getMaxEnergy());
        }
    }

    public ImageView getImage(){
        return imageView;
    }

}
