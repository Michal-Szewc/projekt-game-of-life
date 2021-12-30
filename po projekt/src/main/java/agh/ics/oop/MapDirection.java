package agh.ics.oop;

import java.util.Random;

public enum MapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    static public MapDirection random(){
        Random rd = new Random();
        return switch (rd.nextInt(8)){
            case 0 -> MapDirection.NORTH;
            case 1 -> MapDirection.NORTH_EAST;
            case 2 -> MapDirection.EAST;
            case 3 -> MapDirection.SOUTH_EAST;
            case 4 -> MapDirection.SOUTH;
            case 5 -> MapDirection.SOUTH_WEST;
            case 6 -> MapDirection.WEST;
            case 7 -> MapDirection.NORTH_WEST;
            default -> throw new InternalError("Random.nextInt(8) created integer out of [0,8)");
        };
    }

    public String toString(){
        return switch(this){
            case NORTH -> "N";
            case NORTH_EAST -> "NE";
            case EAST -> "E";
            case SOUTH_EAST -> "SE";
            case SOUTH -> "S";
            case SOUTH_WEST -> "SW";
            case WEST -> "W";
            case NORTH_WEST -> "NW";
        };
    }

    public MapDirection next(){
        return switch (this){
            case NORTH -> NORTH_EAST;
            case NORTH_EAST -> EAST;
            case EAST -> SOUTH_EAST;
            case SOUTH_EAST -> SOUTH;
            case SOUTH -> SOUTH_WEST;
            case SOUTH_WEST -> WEST;
            case WEST -> NORTH_WEST;
            case NORTH_WEST -> NORTH;
        };
    }

    public MapDirection previous(){
        return switch (this){
            case NORTH -> NORTH_WEST;
            case NORTH_WEST -> WEST;
            case WEST -> SOUTH_WEST;
            case SOUTH_WEST -> SOUTH;
            case SOUTH -> SOUTH_EAST;
            case SOUTH_EAST -> EAST;
            case EAST -> NORTH_EAST;
            case NORTH_EAST -> NORTH;
        };
    }

    public Vector2d toUnitVector(){
        return switch (this){
            case NORTH -> new Vector2d(0,1);
            case NORTH_EAST -> new Vector2d(1,1);
            case EAST -> new Vector2d(1,0);
            case SOUTH_EAST -> new Vector2d(1,-1);
            case SOUTH -> new Vector2d(0,-1);
            case SOUTH_WEST -> new Vector2d(-1,-1);
            case WEST -> new Vector2d(-1,0);
            case NORTH_WEST -> new Vector2d(-1,1);
        };
    }
}