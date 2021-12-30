package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

public class App extends Application implements ICycleObserver{
    private int height = 30;
    private int width = 30;
    private double jungleRatio = 0.25;
    private int animalCount = 30;
    private int startEnergy = 100;
    private int moveEnergy = 1;
    private int plantEnergy = 100;
    private int delay = 30;

    LinkedList<TextField> textFields = new LinkedList<>();
    private GridPane gridL;
    private GridPane gridR;
    private GridPane infoL;
    private GridPane infoR;

    private boolean paused1 = false;
    private boolean paused2 = false;

    private AbstractWorldMap mapL;
    private AbstractWorldMap mapR;
    private SimulationEngine engine1;
    private SimulationEngine engine2;
    private Scene scene = null;
    private Stage primaryStage;
    private boolean menu = true;

    private int days1 = 0;
    private int days2 = 0;

    private XYChart.Series animalSeries1;
    private XYChart.Series animalSeries2;
    private LineChart<Number,Number> animalChart;

    private XYChart.Series plantSeries1;
    private XYChart.Series plantSeries2;
    private LineChart<Number,Number> plantChart;

    private XYChart.Series energySeries1;
    private XYChart.Series energySeries2;
    private LineChart<Number,Number> energyChart;

    private XYChart.Series ageSeries1;
    private XYChart.Series ageSeries2;
    private LineChart<Number,Number> ageChart;

    private void startSimulation(){
        this.height = Integer.parseInt(textFields.get(0).getText());
        this.width = Integer.parseInt(textFields.get(1).getText());
        this.jungleRatio = Double.parseDouble(textFields.get(2).getText());
        this.animalCount = Integer.parseInt(textFields.get(3).getText());
        this.startEnergy = Integer.parseInt(textFields.get(4).getText());
        this.moveEnergy = Integer.parseInt(textFields.get(5).getText());
        this.plantEnergy = Integer.parseInt(textFields.get(6).getText());
        this.delay = Integer.parseInt(textFields.get(7).getText());

        this.mapL = new SoftBoundsMap(width, height, jungleRatio);
        this.mapR = new HardBoundsMap(width, height, jungleRatio);



        this.engine1 = new SimulationEngine(mapL, animalCount, startEnergy, moveEnergy, plantEnergy, this, delay);
        Thread engineThread1 = new Thread(engine1);
        engineThread1.start();

        this.engine2 = new SimulationEngine(mapR, animalCount, startEnergy, moveEnergy, plantEnergy, this, delay);
        Thread engineThread2 = new Thread(engine2);
        engineThread2.start();
    }
    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;

        GridPane menuPane = new GridPane();

        Label heightLabel = new Label("Height");
        menuPane.add(heightLabel,0,0);
        TextField heightField = new TextField("30");
        textFields.add(heightField);
        menuPane.add(heightField,1,0);

        Label widthLabel = new Label("Width");
        menuPane.add(widthLabel,0,1);
        TextField widthField = new TextField("30");
        textFields.add(widthField);
        menuPane.add(widthField,1,1);

        Label ratioLabel = new Label("Jungle ratio");
        menuPane.add(ratioLabel,0,2);
        TextField ratioField = new TextField("0.25");
        textFields.add(ratioField);
        menuPane.add(ratioField,1,2);

        Label animalLabel = new Label("Animal number");
        menuPane.add(animalLabel,0,3);
        TextField animalField = new TextField("30");
        textFields.add(animalField);
        menuPane.add(animalField,1,3);

        Label startELabel = new Label("Start energy");
        menuPane.add(startELabel,0,4);
        TextField startEField = new TextField("100");
        textFields.add(startEField);
        menuPane.add(startEField,1,4);

        Label moveELabel = new Label("Move energy");
        menuPane.add(moveELabel,0,5);
        TextField moveEField = new TextField("1");
        textFields.add(moveEField);
        menuPane.add(moveEField,1,5);

        Label plantELabel = new Label("Plant energy");
        menuPane.add(plantELabel,0,6);
        TextField plantEField = new TextField("100");
        textFields.add(plantEField);
        menuPane.add(plantEField,1,6);

        Label delayLabel = new Label("Delay");
        menuPane.add(delayLabel,0,7);
        TextField delayField = new TextField("30");
        textFields.add(delayField);
        menuPane.add(delayField,1,7);

        Button startButton = new Button("start");
        startButton.setOnAction(e -> startSimulation());
        menuPane.add(startButton, 0, 8);

        scene = new Scene(menuPane);
        primaryStage.setScene(scene);
        primaryStage.show();

        this.animalSeries1 = new XYChart.Series();
        animalSeries1.setName("wrapping animal number");
        this.animalSeries2 = new XYChart.Series();
        animalSeries2.setName("not wrapping animal number");

        NumberAxis animalxAxis = new NumberAxis();
        NumberAxis animalyAxis = new NumberAxis();
        this.animalChart = new LineChart<Number,Number>(animalxAxis,animalyAxis);
        animalChart.getData().add(animalSeries1);
        animalChart.getData().add(animalSeries2);

        this.plantSeries1 = new XYChart.Series();
        plantSeries1.setName("wrapping plant number");
        this.plantSeries2 = new XYChart.Series();
        plantSeries2.setName("not wrapping plant number");

        NumberAxis plantxAxis = new NumberAxis();
        NumberAxis plantyAxis = new NumberAxis();
        this.plantChart = new LineChart<Number,Number>(plantxAxis,plantyAxis);
        plantChart.getData().add(plantSeries1);
        plantChart.getData().add(plantSeries2);

        this.energySeries1 = new XYChart.Series();
        energySeries1.setName("wrapping average energy");
        this.energySeries2 = new XYChart.Series();
        energySeries2.setName("not wrapping average energy");

        NumberAxis energyxAxis = new NumberAxis();
        NumberAxis energyyAxis = new NumberAxis();
        this.energyChart = new LineChart<Number,Number>(energyxAxis,energyyAxis);
        energyChart.getData().add(energySeries1);
        energyChart.getData().add(energySeries2);

        this.ageSeries1 = new XYChart.Series();
        ageSeries1.setName("wrapping average lifespan");
        this.ageSeries2 = new XYChart.Series();
        ageSeries2.setName("not wrapping average lifespan");

        NumberAxis agexAxis = new NumberAxis();
        NumberAxis ageyAxis = new NumberAxis();
        this.ageChart = new LineChart<Number,Number>(agexAxis,ageyAxis);
        ageChart.getData().add(ageSeries1);
        ageChart.getData().add(ageSeries2);
    }

    @Override
    public void newCycle() {
        if(!paused1){
            this.gridL = new GridPane();
            gridL.setGridLinesVisible(true);
            gridL.getRowConstraints().add(new RowConstraints(height));
            gridL.getColumnConstraints().add(new ColumnConstraints(width));
            gridL.setMaxSize(800,800);

            days1 += 1;

            for (int y = this.mapL.lowerLeft().y; y <= this.mapL.upperRight().y; y++) {
                for (int x = this.mapL.lowerLeft().x; x <= this.mapL.upperRight().x; x++) {
                    if (mapL.isOccupied(new Vector2d(x, y))) {
                        try {
                            IMapElement element = mapL.randomObjectAt(new Vector2d(x, y));
                            if (element != null)
                                gridL.add(new GuiElementBox(element).getImage(), x, y);
                        } catch (FileNotFoundException e) {
                            System.out.println("File not found");
                        }
                    }
                }
            }

            this.infoL = new GridPane();
            Button pause1Button = new Button("pause");
            pause1Button.setOnAction(e1 -> pause1());
            infoL.add(pause1Button,0, 0);
            infoL.add(new Label("Average animal lifespan " + (this.engine1.averageLifespan())), 0, 4);
            infoL.add(new Label("Average number of children " + (this.engine1.getChildrenAverage())), 0, 5);

            infoL.add(new Label("Dominating genotype"), 0, 6);

            List<Gene> genes = this.mapL.dominantGenes();
            int row = 1;
            for (Gene gene : genes) {
                infoL.add(new Label(gene.toString()), 0, 6 + row);
                row++;
            }
            this.animalSeries1.getData().add(new XYChart.Data(days1,this.engine1.getAnimals()));
            this.plantSeries1.getData().add(new XYChart.Data(days1,this.mapL.getGrassCount()));
            this.energySeries1.getData().add(new XYChart.Data(days1,this.engine1.getEnergyAverage()));
            this.ageSeries1.getData().add(new XYChart.Data(days1,this.engine1.averageLifespan()));
        }
        if (!paused2){
            days2 += 1;
            this.infoR = new GridPane();
            Button pause2Button = new Button("pause");
            pause2Button.setOnAction(e -> pause2());
            infoR.add(pause2Button,0, 0);
            infoR.add(new Label("Average animal lifespan " + (this.engine2.averageLifespan())),0, 4);
            infoR.add(new Label("Average number of children " + (this.engine2.getChildrenAverage())),0, 5);

            infoR.add(new Label("Dominating genotype"), 0, 6);

            List<Gene> genes = this.mapR.dominantGenes();
            int row =1;
            for(Gene gene: genes){
                infoR.add(new Label(gene.toString()),0, 6 + row);
                row++;
            }



            this.gridR = new GridPane();
            gridR.setGridLinesVisible(true);
            gridR.getRowConstraints().add(new RowConstraints(height));
            gridR.getColumnConstraints().add(new ColumnConstraints(width));
            gridR.setMaxSize(800,800);

            for(int y = this.mapR.lowerLeft().y; y <= this.mapR.upperRight().y; y++){
                for(int x = this.mapR.lowerLeft().x; x <= this.mapR.upperRight().x; x++){
                    if(mapR.isOccupied(new Vector2d(x, y))){
                        try {
                            IMapElement element = mapR.randomObjectAt(new Vector2d(x, y));
                            if (element != null)
                                gridR.add(new GuiElementBox(element).getImage(), x, y);
                        } catch (FileNotFoundException e) {
                            System.out.println("File not found");
                        }
                    }
                }
            }


            this.animalSeries2.getData().add(new XYChart.Data(days2,this.engine2.getAnimals()));
            this.plantSeries2.getData().add(new XYChart.Data(days2,this.mapR.getGrassCount()));
            this.energySeries2.getData().add(new XYChart.Data(days2,this.engine2.getEnergyAverage()));
            this.ageSeries2.getData().add(new XYChart.Data(days2,this.engine2.averageLifespan()));
        }

        GridPane grid = new GridPane();
        grid.add(gridL,0,0);
        grid.add(gridR,2,0);
        GridPane chartGrid = new GridPane();
        chartGrid.add(animalChart,0,0);
        chartGrid.add(plantChart,0,1);
        chartGrid.add(energyChart, 0, 2);
        chartGrid.add(ageChart, 0, 3);
        grid.add(infoL,0,1);
        grid.add(infoR,2,1);
        grid.add(chartGrid,1,0,1,2);
        grid.setMaxSize(1900,1000);

        scene = new Scene(grid);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void pause1(){
        paused1 = !paused1;
        engine1.pause();
    }


    public void pause2(){
        paused2 = !paused2;
        engine2.pause();
    }
}