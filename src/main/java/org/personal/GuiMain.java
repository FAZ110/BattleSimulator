package org.personal;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration; // NEW IMPORT

import org.personal.engine.Arena;
import org.personal.engine.Simulation;
import org.personal.model.Combatant;
import org.personal.model.CombatantFactory;
import org.personal.model.Team;

import java.util.Random;

public class GuiMain extends Application {

    private Arena arena;
    private Simulation sim;
    private GridPane gridPane;

    private final int TILE_SIZE = 40;
    private final int WIDTH = 10;
    private final int HEIGHT = 10;

    @Override
    public void start(Stage primaryStage) {
        // 1. Setup Game Engine
        long seed = System.currentTimeMillis();
        arena = new Arena(HEIGHT, WIDTH, seed);
        arena.generateEnvironment(7, 3);
        sim = new Simulation(arena, 50);

        spawnInitialUnits(seed);

        // 2. Setup the UI Layout
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(2);
        gridPane.setVgap(2);

        // 3. Setup the Auto-Player (Timeline)
        Button autoPlayBtn = new Button("Start Auto-Play");
        autoPlayBtn.setFont(Font.font(16));

        // Create a Timeline that triggers every 500 milliseconds (0.5 seconds)
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), e -> {
            sim.playOneTurn();
            updateGrid();
        }));
        // Tell it to repeat indefinitely
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Make the button toggle Play and Pause
        autoPlayBtn.setOnAction(e -> {
            if (timeline.getStatus() == Animation.Status.RUNNING) {
                timeline.pause();
                autoPlayBtn.setText("Resume Auto-Play");
            } else {
                timeline.play();
                autoPlayBtn.setText("Pause Auto-Play");
            }
        });

        VBox root = new VBox(20, gridPane, autoPlayBtn);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2b2b2b;");

        updateGrid();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Battle Simulator - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateGrid() {
        gridPane.getChildren().clear();

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {

                Rectangle tileBg = new Rectangle(TILE_SIZE, TILE_SIZE);
                tileBg.setFill(Color.DARKGREEN);
                tileBg.setStroke(Color.BLACK);

                Text overlay = new Text("");
                overlay.setFont(Font.font("Arial", FontWeight.BOLD, 20));

                if (arena.isOccupied(x, y)) {
                    Combatant fighter = arena.getFighterAt(x, y);
                    overlay.setText(String.valueOf(fighter.getSymbol()));
                    overlay.setFill(fighter.getTeam() == Team.BLUE ? Color.LIGHTBLUE : Color.SALMON);
                }
                else if (arena.getObstacleAt(x, y) != null) {
                    overlay.setText("#");
                }
                else if (arena.getConsumableAt(x, y) != null) {
                    overlay.setText("+");
                }

                StackPane stack = new StackPane(tileBg, overlay);
                gridPane.add(stack, x, y);
            }
        }
    }

    private void spawnInitialUnits(long seed) {
        Random random = new Random(seed);
        // Spawn 3 Blue Humans
        for (int i = 0; i < 3; i++) {
            int rx, ry;
            int attempts = 0;
            do {
                rx = random.nextInt(WIDTH / 2);
                ry = random.nextInt(HEIGHT);
                attempts++;
            } while (arena.isTileTaken(rx, ry) && attempts < 100);
            if (attempts < 100) sim.addFighter(CombatantFactory.createHuman(Team.BLUE, rx, ry));

            // Spawn a Mage for Blue too!
            do {
                rx = random.nextInt(WIDTH / 2);
                ry = random.nextInt(HEIGHT);
                attempts++;
            } while (arena.isTileTaken(rx, ry) && attempts < 200);
            if (attempts < 200) sim.addFighter(CombatantFactory.createMage(Team.BLUE, rx, ry));
        }

        // Spawn 3 Red Zombies & Archers
        for (int i = 0; i < 3; i++) {
            int rx, ry;
            int attempts = 0;
            do {
                rx = random.nextInt(WIDTH / 2, WIDTH);
                ry = random.nextInt(HEIGHT);
                attempts++;
            } while (arena.isTileTaken(rx, ry) && attempts < 100);
            if (attempts < 100) sim.addFighter(CombatantFactory.createZombie(Team.RED, rx, ry));

            do {
                rx = random.nextInt(WIDTH / 2, WIDTH);
                ry = random.nextInt(HEIGHT);
                attempts++;
            } while (arena.isTileTaken(rx, ry) && attempts < 200);
            if (attempts < 200) sim.addFighter(CombatantFactory.createArcher(Team.RED, rx, ry));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}