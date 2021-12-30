package agh.ics.oop;

public class SoftBoundsMap extends AbstractWorldMap {
    public SoftBoundsMap(int width, int height, double jungleRatio) {
        super(width, height, jungleRatio);
    }

    @Override
    public Vector2d fit(Vector2d position) {
        Vector2d temp = new Vector2d(position.x % this.width, position.y % this.height);
        temp = temp.add(new Vector2d(this.width, this.height));
        return new Vector2d(temp.x % (this.width), temp.y % (this.height));
    }
}