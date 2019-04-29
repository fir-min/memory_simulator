package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import simulator.BestFit;
import simulator.FirstFit;
import simulator.NextFit;
import simulator.Simulator;

import java.io.File;

public class UI extends Application {
    private Label inputFile;
    private Label currentTime;
    private Label totalMem;
    private Label freeMem;
    private Label waitingProcesses;
    private TextArea programOutput;
    private TextArea programInput;
    private Simulator simulator;
    private File selectedFile;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("COSC 519 - Memory Simulation");
        Group root = new Group();
        Label programOutputLabel = new Label("Program Output");
        programOutput = TextAreaBuilder.create()
                .prefWidth(400)
                .prefHeight(400)
                .wrapText(true)
                .build();

        ScrollPane programOutputScrollPane = new ScrollPane();
        programOutputScrollPane.setContent(programOutput);
        programOutputScrollPane.setFitToWidth(true);
        programOutputScrollPane.setPrefWidth(400);
        programOutputScrollPane.setPrefHeight(400);
        programOutputScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Label programInputLabel = new Label("Program Input");

        programInput = TextAreaBuilder.create()
                .prefWidth(150)
                .prefHeight(400)
                .wrapText(true)
                .build();
        //programInput.setDisable(true);

        ScrollPane programInputScrollPane = new ScrollPane();
        programInputScrollPane.setContent(programInput);
        programInputScrollPane.setFitToWidth(true);
        programInputScrollPane.setPrefWidth(150);
        programInputScrollPane.setPrefHeight(400);
        programInputScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox vbox2 = new VBox(programOutputLabel, programOutputScrollPane);
        vbox2.setSpacing(5);
        vbox2.setPadding(new Insets(5, 5, 5, 5));

        VBox vbox3 = new VBox(programInputLabel, programInputScrollPane);
        vbox3.setSpacing(5);
        vbox3.setPadding(new Insets(5, 5, 5, 5));

        HBox hbox5 = new HBox(vbox2, vbox3);
        hbox5.setSpacing(5);
        hbox5.setPadding(new Insets(5, 5, 5, 5));

        totalMem = new Label("Total Memory: ");
        freeMem = new Label("Free Memory: ");
        waitingProcesses = new Label("Waiting Processes: ");

        FileChooser fileChooser = new FileChooser();
        inputFile =  new Label();
        Button button = new Button("Pick Input File");
        button.setOnAction(e -> {
            selectedFile = fileChooser.showOpenDialog(primaryStage);
            inputFile.setText(selectedFile.getName());
        });

        Label simTypes = new Label("Simulation Type: ");
        ToggleGroup tg = new ToggleGroup();

        // create radiobuttons
        RadioButton r1 = new RadioButton("First");
        RadioButton r2 = new RadioButton("Next");
        RadioButton r3 = new RadioButton("Best");
        r1.setToggleGroup(tg);
        r2.setToggleGroup(tg);
        r3.setToggleGroup(tg);

        CheckBox defrag = new CheckBox("Defragment Memory?");
        defrag.setDisable(true);

        HBox hbox1 = new HBox(button, inputFile);
        hbox1.setSpacing(10);
        hbox1.setPadding(new Insets(5, 12, 5, 12));

        TextField time = new TextField("0");
        currentTime = new Label("Current time: -1");
        Button end = new Button("End Simulation");
        end.setOnAction(e -> System.exit(0));
        Button goToTime = new Button("Go to time");
        goToTime.setOnAction(e -> simulator.incrementThroughTime(Integer.parseInt(time.getText())));

        HBox hbox3 = new HBox(time, goToTime, end);
        hbox3.setSpacing(30);
        hbox3.setPadding(new Insets(5, 12, 5, 12));
        hbox3.setDisable(true);

        HBox hbox2 = new HBox(totalMem, freeMem, waitingProcesses, currentTime);
        hbox2.setSpacing(20);
        hbox2.setPadding(new Insets(5, 12, 5, 12));

        HBox hbox4 = new HBox(simTypes, r1, r2, r3);
        hbox4.setSpacing(10);
        hbox4.setPadding(new Insets(5, 12, 5, 12));

        Button startSim = new Button("Start the simulation");
        HBox hbox6 = new HBox(startSim);
        hbox6.setPadding(new Insets(0, 0, 0, 12));
        startSim.setOnAction(e -> {
            /* start simulation*/
            RadioButton type = (RadioButton)tg.getSelectedToggle();

            switch (type.getText()) {
                case "First":
                    simulator = new FirstFit(selectedFile, this, defrag.isSelected());
                    break;
                case "Next":
                    simulator = new NextFit(selectedFile, this, defrag.isSelected());
                    break;
                case "Best":
                    simulator = new BestFit(selectedFile, this, defrag.isSelected());
                    break;
            }
            simulator.start();
            button.setDisable(true);
            hbox3.setDisable(false);
            hbox4.setDisable(true);
            startSim.setVisible(false);
        });

        VBox vbox1 = new VBox(hbox1, hbox4, hbox5, hbox2, hbox6, hbox3);
        vbox1.setSpacing(10);
        vbox1.setPadding(new Insets(5, 5, 5, 5));

        root.getChildren().addAll(vbox1);
        Scene scene = new Scene(root, 595, 680);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void append(String data) {
        programOutput.appendText(data);
    }

    public void setInputData(String data) {
        programInput.setText(data);
    }

    public void setFreeMemory(int num) {
        freeMem.setText("Free Memory: " + num);
    }

    public void setTotalMemory(int num) {
        totalMem.setText("Total Memory: " + num);
    }

    public void setWaitingProcesses(int num) {
        waitingProcesses.setText("Waiting Processes: " + num);
    }

    public void setCurrentTime(int num) {
        currentTime.setText("CurrentTime: " + num);
    }
}
