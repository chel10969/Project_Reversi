package sample;

import javafx.scene.paint.Color;

public class Player {
    private static Player blackPlayer = new Player(Name.Black);
    private static Player whitePlayer = new Player(Name.White);
    private static Player currentPlayer = blackPlayer;
    private final Name name;
    private int score = 2;

    public Player(Name name) {
        this.name = name;
    }

    enum Name {
        Black("黒", Color.BLACK), White("白", Color.WHITE);

        private final Color color;
        private final String jpName;

        Name(String jpName, Color color) {
            this.jpName = jpName;
            this.color = color;
        }

        public Color getColor() {
            return this.color;
        }

        @Override
        public String toString() {
            return this.jpName;
        }
    }

    public static Player getInstance(Name name) {
        switch(name) {
            case Black:
                return blackPlayer;
            case White:
                return whitePlayer;
        }
        return null;
    }

    public static void resetInstance() {
        blackPlayer = new Player(Name.Black);
        whitePlayer = new Player(Name.White);
        currentPlayer = blackPlayer;
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    private static void next() {
        if (currentPlayer == blackPlayer) {
            currentPlayer = whitePlayer;
        } else {
            currentPlayer = blackPlayer;
        }

        Disks disks = Disks.getInstance();
        // パスの判定
        if (!disks.checkHit(currentPlayer.getName())) {
            if (currentPlayer == blackPlayer) {
                currentPlayer = whitePlayer;
            } else {
                currentPlayer = blackPlayer;
            }
        }
    }

    public void reverse(Coordinate coordinate) {
        int oldTotalScore = blackPlayer.score + whitePlayer.score;

        reverseTop(this.name, coordinate);
        reverseRight(this.name, coordinate);
        reverseBottom(this.name, coordinate);
        reverseLeft(this.name, coordinate);
        reverseUpperRight(this.name, coordinate);
        reverseUpperLeft(this.name, coordinate);
        reverseLowerRight(this.name, coordinate);
        reverseLowerLeft(this.name, coordinate);

        Disks disks = Disks.getInstance();
        int blackScore = 0;
        int whiteScore = 0;
        for (int col = 0 ; col < 8 ; col++){
            for (int row = 0 ; row < 8 ; row++){
                if (disks.getDisk(col, row).getFill() == Color.BLACK) {
                    blackScore++;
                } else if (disks.getDisk(col, row).getFill() == Color.WHITE) {
                    whiteScore++;
                }
            }
        }
        blackPlayer.score = blackScore;
        whitePlayer.score = whiteScore;

        int newTotalScore = blackScore + whiteScore;
        // tableのdiskに変化があったら次のターンへ
        if (newTotalScore != oldTotalScore) {
            next();
        }

        Table table = Table.getInstance();
        table.setCurrentPlayerLabel(Player.getCurrentPlayer().getName().toString());
        table.setScoreLabel(Name.Black, blackScore);
        table.setScoreLabel(Name.White, whiteScore);

        // ゲームの終了判定
        int sum = blackScore + whiteScore;
        if (sum == 64){
            table.showResult();
        }
        else if (blackScore == 0 || whiteScore == 0){
            table.showResult();
        }
    }

    private void reverseTop(Name name, Coordinate coordinate) {
        Disks disks = Disks.getInstance();

        int amount = disks.checkTop(name, coordinate);
        for (int i = 0, col = coordinate.col, row = coordinate.row ; i < amount && row >= 0 ; i++, row--){
            disks.getDisk(col, row).setFill(name.getColor());
        }
    }

    private void reverseRight(Name name, Coordinate coordinate) {
        Disks disks = Disks.getInstance();

        int amount = disks.checkRight(name, coordinate);
        for (int i = 0, col = coordinate.col, row = coordinate.row ; i < amount && col < 8 ; i++, col++){
            disks.getDisk(col, row).setFill(name.getColor());
        }
    }

    private void reverseBottom(Name name, Coordinate coordinate) {
        Disks disks = Disks.getInstance();

        int amount = disks.checkBottom(name, coordinate);
        for (int i = 0, col = coordinate.col, row = coordinate.row ; i < amount && row < 8 ; i++, row++){
            disks.getDisk(col, row).setFill(name.getColor());
        }
    }

    private void reverseLeft(Name name, Coordinate coordinate) {
        Disks disks = Disks.getInstance();

        int amount = disks.checkLeft(name, coordinate);
        for (int i = 0, col = coordinate.col, row = coordinate.row ; i < amount && col >= 0 ; i++, col--){
            disks.getDisk(col, row).setFill(name.getColor());
        }
    }

    private void reverseUpperRight(Name name, Coordinate coordinate) {
        Disks disks = Disks.getInstance();

        int amount = disks.checkUpperRight(name, coordinate);
        for (int i = 0, col = coordinate.col, row = coordinate.row ; i < amount && col < 8 && row >= 0 ; i++, col++, row--){
            disks.getDisk(col, row).setFill(name.getColor());
        }
    }

    private void reverseUpperLeft(Name name, Coordinate coordinate) {
        Disks disks = Disks.getInstance();

        int amount = disks.checkUpperLeft(name, coordinate);
        for (int i = 0, col = coordinate.col, row = coordinate.row ; i < amount && col >= 0 && row >= 0 ; i++, col--, row--){
            disks.getDisk(col, row).setFill(name.getColor());
        }
    }

    private void reverseLowerRight(Name name, Coordinate coordinate) {
        Disks disks = Disks.getInstance();

        int amount = disks.checkLowerRight(name, coordinate);
        for (int i = 0, col = coordinate.col, row = coordinate.row ; i < amount && col < 8 && row < 8 ; i++, col++, row++){
            disks.getDisk(col, row).setFill(name.getColor());
        }
    }

    private void reverseLowerLeft(Name name, Coordinate coordinate) {
        Disks disks = Disks.getInstance();

        int amount = disks.checkLowerLeft(name, coordinate);
        for (int i = 0, col = coordinate.col, row = coordinate.row ; i < amount && col >= 0 && row < 8 ; i++, col--, row++){
            disks.getDisk(col, row).setFill(name.getColor());
        }
    }

    public Name getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }
}