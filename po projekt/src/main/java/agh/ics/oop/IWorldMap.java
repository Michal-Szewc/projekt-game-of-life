package agh.ics.oop;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo
 *
 */
public interface IWorldMap {
    /**
     * Indicate if any object can move to the given position.
     *
     * @param position
     *            The position checked for the movement possibility.
     * @return True if the object can move to that position.
     */
    boolean canMoveTo(Vector2d position);

    /**
     * Place a grass on the map.
     *
     * @param grass
     *           The grass to place on the map
     */

    void placeGrass(Grass grass);

    /**
     * Place an animal on the map.
     *
     * @param animal
     *            The animal to place on the map.
     */
    void placeAnimal(Animal animal);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position
     *            Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return an object at a given position.
     *
     * @param position
     *            The position of the object.
     * @return Object or null if the position is not occupied.
     */
    Object objectAt(Vector2d position);

    /**
     * Return position that is inside map.
     *
     * @param position
     *              The position to fit in the map
     * @return position % map boundaries
     */

    Vector2d fit(Vector2d position);

    /**
     * Return random position inside map, that isn't occupied
     *
     * @return not occupied position
     */

    Vector2d emptyPosition();

    /**
     * Return point in lower left of the map
     *
     * @return Vector2d which is the lower left point of the map
     */

    Vector2d lowerLeft();

    /**
     * Return point in upper right of the map
     *
     * @return Vector2d which is the upper right point of the map
     */

    Vector2d upperRight();

    /**
     * Add new plants to map
     */

    void addPlants();
}