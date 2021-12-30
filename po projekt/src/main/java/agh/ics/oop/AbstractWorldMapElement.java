package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractWorldMapElement implements IMapElement{
    protected Vector2d position;
    protected List<IPositionObserver> positionObserverList = new ArrayList<>();

    public void addObserver(IPositionObserver observer){
        this.positionObserverList.add(observer);
    }

    public void removeObserver(IPositionObserver observer){
        this.positionObserverList.remove(observer);
    }

    public Vector2d getPosition(){
        return position;
    }

    @Override
    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    public void positionChange(Vector2d oldPosition, Vector2d newPosition){
        for( IPositionObserver observer: positionObserverList){
            observer.positionChanged(this, oldPosition, newPosition);
        }
    }

    public void die(){
        for( IPositionObserver observer: positionObserverList)
            observer.died(this);
        positionObserverList.clear();
    }
}