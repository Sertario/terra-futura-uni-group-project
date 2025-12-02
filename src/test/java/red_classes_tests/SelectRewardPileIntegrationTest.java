package red_classes_tests;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.uniba.fmph.dcs.terra_futura.red_classes.*;
import sk.uniba.fmph.dcs.terra_futura.Card;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class SelectRewardPileIntegrationTest {
    private SelectReward selectReward;
    private Pile pile;

    @BeforeEach
    void setUp() {
        selectReward = new SelectReward();
        pile = new Pile(
                Arrays.asList(new Card(1), new Card(2), new Card(3), new Card(4)),
                Arrays.asList(new Card(5)),
                47L
        );
    }

    @Test
    void testRewardSelectionUpdatesState() {
        Card takenCard = pile.takeCard(0);
        assertNotNull(takenCard);

        Grid grid = new Grid();
        GridPosition coordinate = new GridPosition(0, 0);
        grid.putCard(coordinate, takenCard);
        assertEquals(takenCard, grid.getCard(coordinate).get());

        assertTrue(grid.canBeActivated(coordinate));
        grid.setActivated(coordinate);
        assertFalse(grid.canBeActivated(coordinate));

        selectReward.setReward(1, Arrays.asList(Resource.Green, Resource.Red));

        boolean selected = selectReward.selectReward(1, Resource.Red);
        assertTrue(selected);
        assertEquals(Resource.Red, selectReward.getSelectedResource().get());

        assertFalse(selectReward.getPlayer().isPresent());
        JSONObject stateJson = new JSONObject(selectReward.state());
        assertEquals(0, stateJson.getJSONArray("available").length());
        assertTrue(pile.state().contains("visible_count"));
        assertTrue(grid.state().contains("cards"));
    }
}

