package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.application.Platform;

public class Main extends Application {
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        game();
    }

    private void game(){
        stage.setTitle("Othello");
        stage.setResizable(false);

        BorderPane pane = new BorderPane();
        Table table = Table.getInstance();
        Group root = table.getRoot();
        pane.setTop(this.makeMenu());
        pane.setBottom(root);
        Scene scene = new Scene(pane, 660, 700);
        stage.setScene(scene);
        stage.show();

        // Disk4枚回転
        Disks disks = Disks.getInstance();
        disks.getDisk(3, 3).setFill(Color.WHITE);
        disks.getDisk(4, 4).setFill(Color.WHITE);
        disks.getDisk(3, 4).setFill(Color.BLACK);
        disks.getDisk(4, 3).setFill(Color.BLACK);
    }

    // メニューバーとメニュー項目作成
    private MenuBar makeMenu(){
        Menu menu = new Menu("File");
        MenuItem mnuReset = new MenuItem("Reset");
        mnuReset.setOnAction(event -> resetGame());
        MenuItem mnuExit = new MenuItem("Exit");
        mnuExit.setOnAction(event -> exitGame());
        menu.getItems().addAll(mnuReset, mnuExit);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu);
        return menuBar;
    }

    // メニューバーのResetがクリックされたときgameをResetする
    private void resetGame(){
        Player.resetInstance();
        Disks.resetInstance();
        Table.resetInstance();

        game();
    }

    // メニューバーのExitがクリックされたときgameをExitする
    private void exitGame() {
        Platform.exit();
    }

}
