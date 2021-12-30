package agh.ics.oop;

import java.util.*;

public class MapBoundary implements IPositionObserver{
    Comparator<Vector2d> compX = (a, b) -> {
        if (a.x == b.x && a.y == b.y)
            return 0;
        if (a.x == b.x) {
            if (a.y > b.y)
                return 1;
            return -1;
        }
        if (a.x > b.x)
            return 1;
        return -1;
    };

    Comparator<Vector2d> compY = (a, b) -> {
        if (a.y == b.y && a.x == b.x)
            return 0;
        if (a.y == b.y) {
            if (a.x > b.x)
                return 1;
            return -1;
        }
        if (a.y > b.y)
            return 1;
        return -1;
    };

    SortedMap<Vector2d,List<IMapElement>> x = new TreeMap<>(compX);
    SortedMap<Vector2d,List<IMapElement>> y = new TreeMap<>(compY);

    public void add(IMapElement element){
        if (x.containsKey(element.getPosition())) {
            List<IMapElement> helper = x.get(element.getPosition());
            helper.add(element);
            x.put(element.getPosition(), helper);
        }
        else
            x.put(element.getPosition(), new LinkedList<>(Arrays.asList(new IMapElement[]{element})));
        if (y.containsKey(element.getPosition())) {
            List<IMapElement> helper = y.get(element.getPosition());
            helper.add(element);
            y.put(element.getPosition(), helper);
        }
        else
            y.put(element.getPosition(), new LinkedList<>(Arrays.asList(new IMapElement[]{element})));
    }

    @Override
    public void positionChanged(IMapElement element, Vector2d oldPosition, Vector2d newPosition) {
        x.get(oldPosition).remove(element);
        if (x.containsKey(newPosition))
            x.get(newPosition).add(element);
        else
            x.put(newPosition,  new LinkedList<>(Arrays.asList(new IMapElement[]{element})));
        y.get(oldPosition).remove(element);
        if (y.containsKey(newPosition))
            y.get(newPosition).add(element);
        else
            y.put(newPosition,  new LinkedList<>(Arrays.asList(new IMapElement[]{element})));
    }

    public void died(IMapElement element){
        x.get(element.getPosition()).remove(element);
        y.get(element.getPosition()).remove(element);
    }

    public Vector2d getLowerLeft(){
        return new Vector2d(x.firstKey().x,y.firstKey().y);
    }

    public Vector2d getUpperRight(){
        return new Vector2d(x.lastKey().x,y.lastKey().y);
    }
}