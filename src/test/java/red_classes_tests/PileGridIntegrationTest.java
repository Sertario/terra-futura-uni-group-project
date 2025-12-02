package red_classes_tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.uniba.fmph.dcs.terra_futura.Card;
import sk.uniba.fmph.dcs.terra_futura.red_classes.Grid;
import sk.uniba.fmph.dcs.terra_futura.red_classes.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.red_classes.Pile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PileGridIntegrationTest {
    private Pile pile;
    private Grid grid;
    private Card card1, card2, card3, card4, card5;

    @BeforeEach
    void setUp() {
        card1 = new Card(1);
        card2 = new Card(2);
        card3 = new Card(3);
        card4 = new Card(4);
        card5 = new Card(5);

        List<Card> visibleCards = Arrays.asList(card1, card2, card3, card4);
        List<Card> hiddenCards = Arrays.asList(card5);

        pile = new Pile(visibleCards, hiddenCards, 123L);
        grid = new Grid();
    }

    @Test
    void testTakeCardAndPutOnGrid() {
        Card takenCard = pile.takeCard(0);
        assertNotNull(takenCard);

        GridPosition coordinate = new GridPosition(0, 0);
        assertTrue(grid.canPutCard(coordinate));
        grid.putCard(coordinate, takenCard);

        Optional<Card> gridCard = grid.getCard(coordinate);
        assertTrue(gridCard.isPresent());
        assertEquals(takenCard, gridCard.get());

        String pileState = pile.state();
        assertTrue(pileState.contains("visible_count"));
        assertTrue(pileState.contains("hidden_count"));
    }
}

