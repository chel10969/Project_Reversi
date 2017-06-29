package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;

import java.util.Objects;
import java.util.Optional;

public class Table {
    private static Table table;
    private final Group root = new Group();

    private Label currentPlayerLabel;
    private Label blackScoreLabel;
    private Label whiteScoreLabel;
    private Label resultLabel;

    public static final double diskMargin = 5.0;

    private Table(){
        root.getChildren().addAll(makePane(), makeHorizontalLine(), makeVerticalLine(),
                makeCurrentTurn(), makeScoreLine(), makeBoarder(), makeResult());
    }

    public static Table getInstance() {
        if (Objects.isNull(table)) {
            resetInstance();
        }
        return table;
    }

    public static void resetInstance() {
        table = new Table();
    }

    public Group getRoot() {
        return root;
    }

    private Path makeBoarder() {
        return new Path(new MoveTo(60.0, 45.0), new LineTo(600.0, 45.0));
    }

    private GridPane makePane() {
        GridPane pane = new GridPane();
        pane.setStyle("-fx-background-color: #a0c000;");
        // 領域のコンテンツの上、右、下、左の余白
        pane.setPadding(new Insets(90.0, 50.0, 50.0, 50.0));
        pane.setGridLinesVisible(true);

        Disks disks = Disks.getInstance();
        for(int col = 0 ; col < 8 ; col++ ){
            for(int row = 0 ; row < 8 ; row++){
                pane.add(disks.getDisk(col, row), col, row);
                GridPane.setMargin(disks.getDisk(col, row), new Insets(diskMargin, diskMargin, diskMargin, diskMargin));
                //当たり判定アイテムの追加
                pane.add(disks.getCollisionDetectionItem(col, row), col, row);
            }
        }

        return pane;
    }

    private VBox makeVerticalLine(){
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(90.0, 0.0, 50.0, 15.0));

        double fontSize = 30.0;
        for(int i = 1 ; i <= 8 ; i++){
            Label label = makeLineLabel(String.valueOf(i), fontSize, 18.5, 0.0, 14.5, 0.0);
            vBox.getChildren().add(label);
        }
        return vBox;
    }

    private HBox makeHorizontalLine(){
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(50.0, 50.0, 0.0, 50.0));

        double fontSize = 30.0;
        for(char ch = 'a' ; ch <= 'h' ; ch++){
            Label label = makeLineLabel(String.valueOf(ch), fontSize, 0.0, 27.0, 0.0, 26.0);
            hBox.getChildren().add(label);
        }
        return hBox;
    }

    private Label makeResult(){
        double fontSize = 25.0;
        resultLabel = makeLineLabel("", fontSize);
        resultLabel.setPadding(new Insets(10.0, 0.0, 0.0, 265.0));

        return resultLabel;
    }

    private Label makeCurrentTurn(){
        double fontSize = 25.0;
        currentPlayerLabel = makeLineLabel("「" + Player.getCurrentPlayer().getName().toString() + "」のターン", fontSize);
        currentPlayerLabel.setPadding(new Insets(10.0, 0.0, 0.0, 50.0));

        return currentPlayerLabel;
    }

    private HBox makeScoreLine(){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BOTTOM_CENTER);
        hBox.setPadding(new Insets(0.0, 0.0, 0.0, 465.0));

        Player blackPlayer = Player.getInstance(Player.Name.Black);
        // 値をラップする
        Optional<Player> oBlackPlayer = Optional.ofNullable(blackPlayer);
        // 値が存在する場合のみ実行
        oBlackPlayer.ifPresent(player -> blackScoreLabel =
                makeLineLabel(player.getName().toString() + "：" + String.format("%02d", player.getScore()),
                        25.0, 10.0, 0.0, 18.5, 0.0));

        Player whitePlayer = Player.getInstance(Player.Name.White);
        // 値をラップする
        Optional<Player> oWhitePlayer = Optional.ofNullable(whitePlayer);
        // 値が存在する場合のみ実行
        oWhitePlayer.ifPresent(player -> whiteScoreLabel =
                makeLineLabel(" " + player.getName().toString() + "：" + String.format("%02d", player.getScore()),
                        25.0, 10.0, 0.0, 18.5, 0.0));

        hBox.getChildren().addAll(blackScoreLabel, whiteScoreLabel);

        return hBox;
    }

    public void showResult() {
        Optional<Player> oBlackPlayer = Optional.ofNullable(Player.getInstance(Player.Name.Black));
        Optional<Player> oWhitePlayer = Optional.ofNullable(Player.getInstance(Player.Name.White));
        
        oBlackPlayer.ifPresent(blackPlayer -> oWhitePlayer.ifPresent(whitePlayer -> {
            String result = null;
            int flag = 0;
            if (blackPlayer.getScore() > whitePlayer.getScore()) result = blackPlayer.getName().toString();
            else if (blackPlayer.getScore() < whitePlayer.getScore()) result = whitePlayer.getName().toString();
            else flag = 1;
            
            if (flag == 0) resultLabel.setText("「" + result + "」の勝ち");
            else if (flag == 1) resultLabel.setText("引き分け");
        }));
    }

    public void setCurrentPlayerLabel(String str) {
        currentPlayerLabel.setText("「" + str + "」のターン");
    }

    public void setScoreLabel(Player.Name name, int score) {
        switch (name){
            case Black:
                blackScoreLabel.setText(name.toString() + ":" + String.format("%02d", score));
                break;
            case White:
                whiteScoreLabel.setText(" " + name.toString() + ":" + String.format("%02d", score));
        }
    }

    private Label makeLineLabel(String text, double fontSize){
        return makeLineLabel(text, fontSize, 0, 0, 0, 0);
    }

    private Label makeLineLabel(String text, double fontSize, double top, double right, double bottom, double left){
        Label label = new Label(text);
        label.setFont(new Font("System", fontSize));
        label.setPadding(new Insets(top, right, bottom, left));
        return label;
    }
}
