package agh.ics.oop;

import javax.swing.*;
import java.util.*;

abstract public class AbstractWorldMap implements IWorldMap, IPositionObserver{
    Map<Vector2d, List<IMapElement>> elements = new HashMap<>();
    MapBoundary boundary = new MapBoundary();
    protected int width;
    protected int height;
    protected int animalCount = 0;
    protected int grassCount = 0;
    protected int grassInJungle = 0;
    protected int freeTilesInJungle = 0;
    protected int freeTiles = 0;
    protected Vector2d jungleLowerLeft;
    protected Vector2d jungleUpperRight;

    public AbstractWorldMap(int width, int height, double jungleRatio){
        if (jungleRatio < 0 || jungleRatio > 1)
            throw new IllegalArgumentException("jungle ration should be between 0 and 1");
        if (width < 1 || height < 1)
            throw new IllegalArgumentException("width and height should be greater than 0");

        // Calculating jungle surface, so that it can be as close to jungleRatio as possible

        this.width = width;
        this.height = height;
        double last = 1;
        double x = 0;
        double y = 0;
        for(double i = Math.ceil((double)width * jungleRatio); i <= Math.min(Math.ceil((double)(width * height) * jungleRatio), (double)width); i++){
            double pom = Math.floor((double) ( width * height) * jungleRatio / i);
            if (Math.abs((pom * i)/(double)(width * height) - jungleRatio) < last){
                last = Math.abs((pom * i) / (double)(width * height) - jungleRatio);
                x = i;
                y = pom;
            }
            pom = Math.ceil((double) ( width * height) * jungleRatio / i);
            if (Math.abs((pom * i)/(double)(width * height) - jungleRatio) < last){
                last = Math.abs((pom * i) / (double)(width * height) - jungleRatio);
                x = i;
                y = pom;
            }
        }
        this.jungleLowerLeft = new Vector2d((int)Math.floor(((double)width - x)/2), (int)Math.floor(((double)height - y)/2));
        this.jungleUpperRight = new Vector2d(width - 1 - (int)Math.ceil(((double)width - x)/2), height - 1 - (int)Math.ceil(((double)height - y)/2));

        this.freeTiles = width * height;
        this.freeTilesInJungle = (jungleUpperRight.x - jungleLowerLeft.x + 1) * (jungleUpperRight.y - jungleLowerLeft.y + 1);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(new Vector2d(0,0)) && position.proceeds(new Vector2d(width - 1, height - 1));
    }

    public boolean insideJungle(Vector2d position) {
        return position.follows(jungleLowerLeft) && position.proceeds(jungleUpperRight);
    }

    @Override
    public void placeGrass(Grass grass) {
        if (isOccupied(grass.getPosition()))
            throw new IllegalArgumentException("Grass cannot be placed at " + grass.getPosition());
        elements.put(grass.getPosition(), new LinkedList<>(Arrays.asList(new Grass[]{grass})));
        grassCount += 1;
        freeTiles -= 1;
        if (insideJungle(grass.getPosition())) {
            grassInJungle += 1;
            freeTilesInJungle -= 1;
        }
        grass.addObserver(this);
        grass.addObserver(boundary);
    }

    @Override
    public void placeAnimal(Animal animal) {
        if(canMoveTo(animal.getPosition())){
            if (!isOccupied(animal.getPosition())) {
                freeTiles -= 1;
                if (insideJungle(animal.getPosition()))
                    freeTilesInJungle -=1;
                elements.put(animal.getPosition(),new LinkedList<>(Arrays.asList(new Animal[]{animal})));
            }
            else {
                List<IMapElement> helper = elements.get(animal.getPosition());
                helper.add(animal);
                elements.put(animal.getPosition(), helper);
            }
            animal.addObserver(this);
            animal.addObserver(boundary);
            boundary.add(animal);
            animalCount += 1;
        }
        else
            throw new IllegalArgumentException("Animal can't be placed at " + animal.getPosition());
    }

    @Override
    public Object objectAt(Vector2d position) {
        if (elements.containsKey(position))
            return elements.get(position);
        return null;
    }

    public IMapElement randomObjectAt(Vector2d position) {
        if (isOccupied(position)) {
            LinkedList<Animal> animals = new LinkedList<>();
            Grass grass = null;
            for(IMapElement element : elements.get(position)){
                if (element instanceof Animal animal)
                    animals.add(animal);
                else if (element instanceof Grass _grass)
                    grass = _grass;
            }
            if (animals.isEmpty())
                return grass;
            Random rd = new Random();
            return animals.get(rd.nextInt(animals.size()));
        }
        return null;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        if (objectAt(position) == null)
            return false;
        return elements.get(position).size() > 0;
    }

    public String toString() {
        return new MapVisualizer(this).draw(boundary.getLowerLeft(),boundary.getUpperRight());
    }

    @Override
    public Vector2d emptyPosition(){
        if (freeTiles <= 0)
            return null;
        Random rd = new Random();
        Vector2d position = new Vector2d(rd.nextInt(width),rd.nextInt(height));
        while (isOccupied(position)){
            position = new Vector2d(rd.nextInt(width),rd.nextInt(height));
        }
        return position;
    }

    @Override
    public void positionChanged(IMapElement element, Vector2d oldPosition, Vector2d newPosition){
        elements.get(oldPosition).remove(element);
        if (!isOccupied(oldPosition)) {
            freeTiles += 1;
            if (insideJungle(oldPosition))
                freeTilesInJungle += 1;
        }
        if (isOccupied(newPosition))
            elements.get(newPosition).add(element);
        else {
            elements.put(newPosition, new LinkedList<>(Arrays.asList(new IMapElement[]{element})));
            freeTiles -= 1;
            if (insideJungle(newPosition))
                freeTilesInJungle -= 1;
        }
    }

    @Override
    public void died(IMapElement element){
        elements.get(element.getPosition()).remove(element);
        if (element instanceof Animal animal) {
            animalCount -= 1;
            if (!isOccupied(animal.getPosition())) {
                freeTiles += 1;
                if (insideJungle(animal.getPosition()))
                    freeTilesInJungle +=1;
            }
        }
        else if (element instanceof Grass grass){
            grassCount -=1;
            if (insideJungle(grass.getPosition()))
                grassInJungle -=1;
        }
    }

    public Vector2d upperRight(){
        return boundary.getUpperRight();
    }

    public Vector2d lowerLeft(){
        return boundary.getLowerLeft();
    }

    public int getGrassCount() {
        return grassCount;
    }

    public List<Gene> dominantGenes(){
        HashMap<Gene, Integer> count = new HashMap<>();
        ArrayList<Gene> genes = new ArrayList<>();
        for(List<IMapElement> e: elements.values()){
            for(IMapElement element: e){
                if (element instanceof Animal){
                    if (count.containsKey(((Animal) element).getGenes())){
                        Integer helper = count.get(((Animal) element).getGenes());
                        count.put(((Animal) element).getGenes(), helper + 1);
                    }
                    else
                        count.put(((Animal) element).getGenes(), 1);
                }
            }
        }
        Integer max = 0;
        for (Integer i: count.values()){
            max = Math.max(max,i);
        }
        for (Map.Entry<Gene, Integer> entry : count.entrySet()){
            if (entry.getValue().equals(max))
                genes.add(entry.getKey());
        }
        return genes;
    }

    @Override
    public void addPlants(){
        Random rd = new Random();
        if (freeTilesInJungle > 0){
            Vector2d grass_pos = new Vector2d(rd.nextInt(jungleUpperRight.x - jungleLowerLeft.x + 1) + jungleLowerLeft.x,
                                                 rd.nextInt(jungleUpperRight.y - jungleLowerLeft.y + 1) + jungleLowerLeft.y);
            while (isOccupied(grass_pos)){
                grass_pos = new Vector2d(rd.nextInt(jungleUpperRight.x - jungleLowerLeft.x + 1) + jungleLowerLeft.x,
                        rd.nextInt(jungleUpperRight.y - jungleLowerLeft.y + 1) + jungleLowerLeft.y);
            }
            placeGrass(new Grass(grass_pos));
        }
        if (freeTiles - freeTilesInJungle > 0){
            Vector2d grass_pos = new Vector2d(rd.nextInt(width), rd.nextInt(height));

            while (isOccupied(grass_pos) || insideJungle(grass_pos))
                grass_pos = new Vector2d(rd.nextInt(width), rd.nextInt(height));

            placeGrass(new Grass(grass_pos));
        }
    }
}