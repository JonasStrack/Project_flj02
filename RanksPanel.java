import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class RanksPanel extends VBox {
    public RanksPanel() {
        setPrefSize(200, 800);
        setSpacing(10);

        String[] ranks = {
            "Flag (0)",
            "Spy (1)",
            "Scout (2)",
            "Miner (3)",
            "Sergeant (4)",
            "Lieutenant (5)",
            "Captain (6)",
            "Major (7)",
            "Colonel (8)",
            "General (9)",
            "Marshal (10)",
            "Bomb (B)"
        };

        for (String rank : ranks) {
            Text rankText = new Text(rank);
            rankText.setFont(Font.font("Arial", 16));
            getChildren().add(rankText);
        }
    }
}