package agh.ics.oop;

public interface IPositionObserver {

    /**
     * Updates position of element in Position Observer
     * @param element
     *          Element, whose position has changed
     * @param oldPosition
     *          Previous position of that element
     * @param newPosition
     *          New position of that element
     */

    void positionChanged(IMapElement element, Vector2d oldPosition, Vector2d newPosition);

    /**
     * Updates the Position Observer about the death of the element
     * @param element
     *          The element, which has died
     */

    void died(IMapElement element);
}