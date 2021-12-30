package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal extends AbstractWorldMapElement{
    private MapDirection direction;
    private final IWorldMap map;
    private Gene genes;
    private int energy;
    private int maxEnergy;
    private int children = 0;
    private int descendants = 0;
    private int age = 0;

    public int getChildren() {
        return children;
    }

    public void age(){
        age += 1;
    }

    public int getAge() {
        return age;
    }

    public Animal(IWorldMap map, int energyMax){
        this.position = map.emptyPosition();
        this.direction = MapDirection.random();
        this.map = map;
        this.genes = new Gene();
        this.energy = energyMax;
        this.maxEnergy = energyMax;
    }

    public Animal(IWorldMap map, Vector2d initialPosition, int energyMax, Gene genes){
        this.position = initialPosition;
        this.direction = MapDirection.random();
        this.map = map;
        this.genes = genes;
        this.energy = energyMax;
    }

    public String toString(){
        return direction.toString();
    }

    @Override
    public String resourcePath(){
        return "src/main/resources/animal.png";
    }

    public void move(MoveDirection direction){
        Vector2d newPosition = position;
        switch (direction) {
            case LEFT:
                this.direction = this.direction.previous();
                break;
            case LEFT2:
                this.direction = this.direction.previous().previous();
                break;
            case LEFT3:
                this.direction = this.direction.previous().previous().previous();
                break;
            case RIGHT:
                this.direction = this.direction.next();
                break;
            case RIGHT2:
                this.direction = this.direction.next().next();
                break;
            case RIGHT3:
                this.direction = this.direction.next().next().next();
            case FORWARD:
                if(map.canMoveTo(map.fit(this.position.add(this.direction.toUnitVector())))){
                    newPosition = map.fit(this.position.add(this.direction.toUnitVector()));
                }
                break;
            case BACKWARD:
                if(map.canMoveTo(map.fit(this.position.add(this.direction.toUnitVector().opposite())))){
                    newPosition = map.fit(this.position.add(this.direction.toUnitVector().opposite()));
                }
                break;
        }
        if(!position.equals(newPosition)) {
            positionChange(position, newPosition);
            this.position = newPosition;
        }
    }

    public int getEnergy() {
        return energy;
    }

    public Gene getGenes(){
        return genes;
    }

    public MoveDirection getRandomMove(){
        return switch(this.genes.getRandomGene()){
            case 0 -> MoveDirection.FORWARD;
            case 1 -> MoveDirection.RIGHT;
            case 2 -> MoveDirection.RIGHT2;
            case 3 -> MoveDirection.RIGHT3;
            case 4 -> MoveDirection.BACKWARD;
            case 5 -> MoveDirection.LEFT3;
            case 6 -> MoveDirection.LEFT2;
            case 7 -> MoveDirection.LEFT;
            default -> throw new Error("gene is not in range [0,8)");
        };
    }

    public Gene cross(Animal other){
        Random random = new Random();

        if (random.nextInt(2) == 0){
            return new Gene(this.getGenes(), other.getGenes(),(int)(32 * this.getEnergy())/(this.getEnergy() + other.getEnergy()));
        }
        else
            return new Gene(other.getGenes(), this.getGenes(),(int)(32 * other.getEnergy())/(this.getEnergy() + other.getEnergy()));
    }

    public void hasChild(){
        this.children += 1;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public void changeEnergyBy(int lost_energy){
        this.energy = Math.min(maxEnergy, energy + lost_energy);
    }
}