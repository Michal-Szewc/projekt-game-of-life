package agh.ics.oop;

public class Grass extends AbstractWorldMapElement{
    public Grass(Vector2d initialPosition){
        this.position = initialPosition;
    }

    public Vector2d getPosition(){
        return position;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public String resourcePath(){
        return "src/main/resources/grass.png";
    }
}