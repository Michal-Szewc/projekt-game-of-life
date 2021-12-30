package agh.ics.oop;

public class HardBoundsMap extends AbstractWorldMap{
    public HardBoundsMap(int width, int height, double jungleRatio) {
        super(width, height, jungleRatio);
    }

    @Override
    public Vector2d fit(Vector2d position){
        return position;
    }
}
