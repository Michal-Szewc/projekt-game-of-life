package agh.ics.oop;

import java.util.Arrays;
import java.util.Random;

public class Gene {
    private final int[] genes = new int[32];

    public Gene(){
        Random random = new Random();
        for(int i = 0; i < 32; i++)
            genes[i] = random.nextInt(8);
        Arrays.sort(genes);
    }

    // new Gene is a from [0, split) + b from [split,32)

    public Gene(Gene a, Gene b, int split){
        if (split < 0 || split > 31)
            throw new IllegalArgumentException("split should be between 0 and 31");
        for(int i = 0; i < split; i++)
            genes[i] = a.genes[i];
        for(int i = split; i < 32; i++)
            genes[i] = b.genes[i];
        Arrays.sort(genes);
    }

    public int getRandomGeneRange(int min, int max){
        if (min < 0)
            throw new IllegalArgumentException("min should be greater than 0");
        if (min > max)
            throw new IllegalArgumentException("min should be lower than max");
        if (max > 31)
            throw new IllegalArgumentException("max should be less than 32");
        Random random = new Random();
        return genes[random.nextInt(max - min) + min];
    }

    public int getRandomGene(){
        return getRandomGeneRange(0, 31);
    }

    @Override
    public String toString() {
        return Arrays.toString(genes);
    }
}
