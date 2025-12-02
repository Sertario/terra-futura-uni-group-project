package red_classes_tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.uniba.fmph.dcs.terra_futura.Card;
import sk.uniba.fmph.dcs.terra_futura.red_classes.Grid;
import sk.uniba.fmph.dcs.terra_futura.red_classes.GridPosition;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    private Grid grid;
    private GridPosition pos00;
    private GridPosition pos11;
    private GridPosition pos22;
    private Card card;

    @BeforeEach
    void setUp() {
        grid = new Grid();
        pos00 = new GridPosition(0, 0);
        pos11 = new GridPosition(1, 1);
        pos22 = new GridPosition(2, 2);

        card = new Card();
    }

    @Test
    void testPutCardOnEmptyCell() {
        assertTrue(grid.canPutCard(pos00));
        grid.putCard(pos00, card);

        Optional<Card> result = grid.getCard(pos00);
        assertTrue(result.isPresent());
        assertEquals(card, result.get());
    }

    @Test
    void testCannotPutCardOnOccupiedCell() {
        grid.putCard(pos11, card);

        assertFalse(grid.canPutCard(pos11));

        assertThrows(IllegalStateException.class, () -> {
            grid.putCard(pos11, new Card());
        });
    }

    @Test
    void testGetCardOnUnknownPosition() {
        Optional<Card> result = grid.getCard(pos22);
        assertFalse(result.isPresent());
    }

    @Test
    void testCanBeActivated() {
        grid.putCard(pos00, card);

        assertTrue(grid.canBeActivated(pos00));
        grid.setActivated(pos00);

        assertFalse(grid.canBeActivated(pos00));
    }

    @Test
    void testCannotActivateEmptyCell() {
        assertFalse(grid.canBeActivated(pos11));

        assertThrows(IllegalStateException.class, () -> {
            grid.setActivated(pos11);
        });
    }

    @Test
    void testEndTurnResetsActivation() {
        grid.putCard(pos00, card);
        grid.setActivated(pos00);

        assertFalse(grid.canBeActivated(pos00));

        grid.endTurn();

        assertTrue(grid.canBeActivated(pos00));
    }

    @Test
    void testActivationPatternSaved() {
        grid.setActivationPattern(Arrays.asList(pos00, pos11));

        String state = grid.state();
        assertTrue(state.contains("(0, 0)"));
        assertTrue(state.contains("(1, 1)"));
    }
}

