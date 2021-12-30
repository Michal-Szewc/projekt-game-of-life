package agh.ics.oop;

import java.util.List;

public class OptionsParser {
    static public MoveDirection[] parse(List<String> strings){
        MoveDirection[] commands = new MoveDirection[strings.size()];
        int i = 0;
        for(String string : strings){
            switch (string) {
                case "f","forward"->commands[i]=MoveDirection.FORWARD;
                case "b","backward"->commands[i]=MoveDirection.BACKWARD;
                case "l","left"->commands[i]=MoveDirection.LEFT;
                case "r","right"->commands[i]=MoveDirection.RIGHT;
                default -> throw new IllegalArgumentException(string + " is not a legal move specification");
            }
            i++;
        }
        return commands;
    }

    static public MoveDirection[] parse(String[] strings){
        MoveDirection[] commands = new MoveDirection[strings.length];
        for(int i = 0; i < strings.length; i++){
            switch (strings[i]) {
                case "f","forward"->commands[i]=MoveDirection.FORWARD;
                case "b","backward"->commands[i]=MoveDirection.BACKWARD;
                case "l","left"->commands[i]=MoveDirection.LEFT;
                case "r","right"->commands[i]=MoveDirection.RIGHT;
                default -> throw new IllegalArgumentException(strings[i] + " is not a legal move specification");
            }
            i++;
        }
        return commands;
    }
}