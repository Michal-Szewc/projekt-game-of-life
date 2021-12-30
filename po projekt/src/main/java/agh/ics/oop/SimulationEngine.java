package agh.ics.oop;

import javafx.application.Platform;

import java.util.*;

public class SimulationEngine implements IEngine, Runnable{
    private IWorldMap map;
    private ArrayList<Animal> animals = new ArrayList<>();
    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;
    private int delay = 100;

    protected int childrenCount = 0;
    protected int energySum = 0;
    protected int collectiveLifespan = 0;
    protected int deadAnimals = 0;

    private boolean pasued = false;

    private ICycleObserver cycleObserver;

    public SimulationEngine(IWorldMap map, int animal_count, int startEnergy, int moveEnergy, int plantEnergy, ICycleObserver cycleObserver, int delay){
        this.map = map;
        this.startEnergy = startEnergy;
        this.moveEnergy = -moveEnergy;
        this.plantEnergy = plantEnergy;
        this.cycleObserver = cycleObserver;
        this.delay = delay;
        for (int i = 0; i < animal_count; i++) {
            Animal animal = new Animal(this.map, startEnergy);
            this.map.placeAnimal(animal);
            animals.add(animal);
        }
        System.out.println(this.map);
    }

    public double averageLifespan(){
        return (double)collectiveLifespan / (double)(animals.size() + deadAnimals);
    }

    public int getAnimals(){
        return animals.size();
    }

    public double getEnergyAverage() {
        return (double)energySum/(double)animals.size();
    }

    public double getChildrenAverage() {
        return (double)childrenCount/(double)animals.size();
    }

    @Override
    public void run() {
        while (animals.size() > 0) {

            if (pasued)
                continue;
            // Add plants

            this.map.addPlants();

            // Move animals and reduce their energy

            for (Animal animal : animals) {
                animal.move(animal.getRandomMove());
                animal.changeEnergyBy(moveEnergy);
            }

            // Check positions of animals for eating grass and reproduction

            List<Animal> checked = new LinkedList<>();
            List<Animal> animals_to_add = new LinkedList<>();
            Random rd = new Random();
            energySum = 0;
            childrenCount = 0;
            for (Animal animal : animals) {

                // Find animal with highest energy

                energySum += animal.getEnergy();
                childrenCount += animal.getChildren();
                animal.age();

                if (checked.contains(animal))
                    continue;
                Grass grass = null;
                List<Animal> max_energy_animals = new LinkedList<>();
                int max_energy = 0;
                Animal lucky = null;
                for (IMapElement element : (List<IMapElement>) this.map.objectAt(animal.getPosition())) {
                    if (element instanceof Animal _animal) {
                        checked.add(_animal);
                        if (_animal.getEnergy() > max_energy) {
                            max_energy = _animal.getEnergy();
                            if (max_energy_animals.size() > 0)
                                lucky = max_energy_animals.get(rd.nextInt(max_energy_animals.size()));
                            max_energy_animals = new LinkedList<>(Arrays.asList(new Animal[]{_animal}));
                        } else if (_animal.getEnergy() == max_energy)
                            max_energy_animals.add(_animal);
                        lucky = null;
                    } else if (element instanceof Grass _grass) {
                        grass = _grass;
                    }
                }

                // eat grass

                if (grass != null) {
                    for (Animal _animal : max_energy_animals)
                        _animal.changeEnergyBy(plantEnergy / max_energy_animals.size());
                    grass.die();
                }

                // reproduce animals

                if (max_energy_animals.size() >0 && (max_energy_animals.get(0).getEnergy() * 2 >= startEnergy)) {
                    if (lucky != null || max_energy_animals.size() > 1) {
                        if (max_energy_animals.size() == 1) {
                            Animal _animal = new Animal(this.map, lucky.getPosition(), lucky.getEnergy() / 4 + max_energy_animals.get(0).getEnergy() / 4, max_energy_animals.get(0).cross(lucky));
                            lucky.changeEnergyBy(lucky.getEnergy() / 4);
                            max_energy_animals.get(0).changeEnergyBy(max_energy_animals.get(0).getEnergy() / 4);
                            this.map.placeAnimal(_animal);
                            animals_to_add.add(_animal);
                            lucky.hasChild();
                            max_energy_animals.get(0).hasChild();
                        } else {
                            int random1 = rd.nextInt(max_energy_animals.size());
                            int random2 = rd.nextInt(max_energy_animals.size());
                            while (random1 == random2)
                                random2 = rd.nextInt(max_energy_animals.size());
                            Animal _animal = new Animal(this.map, max_energy_animals.get(random1).getPosition(), max_energy_animals.get(random1).getEnergy() / 4 + max_energy_animals.get(random2).getEnergy() / 4, max_energy_animals.get(random1).cross(max_energy_animals.get(random2)));
                            this.map.placeAnimal(_animal);
                            animals_to_add.add(_animal);
                            max_energy_animals.get(random1).hasChild();
                            max_energy_animals.get(random2).hasChild();
                        }
                    }
                }
            }
            animals.addAll(animals_to_add);

            // remove dead animals

            ListIterator<Animal> it = animals.listIterator();
            while(it.hasNext()){
                Animal animal = it.next();
                if(animal.getEnergy() <= 0){
                    animal.die();
                    collectiveLifespan += animal.getAge();
                    it.remove();
                }
            }

            // update statistics

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                System.out.println("Why would you kill me :(");
            }
            Platform.runLater(cycleObserver::newCycle);
        }
    }

    public void pause(){
        pasued = !pasued;
    }
}