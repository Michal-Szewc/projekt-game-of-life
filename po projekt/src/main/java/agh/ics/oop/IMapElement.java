package agh.ics.oop;

public interface IMapElement {

    /**
     * Returns copy of the position
     *
     * @return copy of position
     */

    Vector2d getPosition();

    /**
     * Returns true if element is at given position
     *
     * @param position
     *          Position to check for the element
     * @return true if element is at position
     */

    boolean isAt(Vector2d position);

    String resourcePath();
}