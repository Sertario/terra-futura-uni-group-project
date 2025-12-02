package red_classes_tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import sk.uniba.fmph.dcs.terra_futura.red_classes.Pile;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class PileTest {
    private Card card1, card2, card3, card4, card5, card6;
    private List<Card> visibleCards;
    private List<Card> hiddenCards;

    @BeforeEach
    void setUp() {
        card1 = new Card(1);
        card2 = new Card(2);
        card3 = new Card(3);
        card4 = new Card(4);
        card5 = new Card(5);
        card6 = new Card(6);
    }

    @Test
    void testConstructor() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = Arrays.asList(card5, card6);

        Pile pile = new Pile(visibleCards, hiddenCards, 123L);
        assertNotNull(pile);
    }

    @Test
    void testGetCardValidIndex() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = new ArrayList<>();
        Pile pile = new Pile(visibleCards, hiddenCards, 47L);

        Optional<Card> result = pile.getCard(0);
        assertTrue(result.isPresent());
        assertEquals(card1, result.get());
    }

    @Test
    void testTakeCardValidIndex() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = Arrays.asList(card5, card6);
        Pile pile = new Pile(visibleCards, hiddenCards, 456L);

        Card taken = pile.takeCard(2);
        assertEquals(card3, taken);
    }

    @Test
    void testTakeCardInvalidIndex() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = new ArrayList<>();
        Pile pile = new Pile(visibleCards, hiddenCards, 789L);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            pile.takeCard(5);
        });
    }

    @Test
    void testTakeCardRefillsFromDiscardDeck() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = Arrays.asList(card5, card6);
        Pile pile = new Pile(visibleCards, hiddenCards, 47L);

        pile.takeCard(0);

        Optional<Card> newFirst = pile.getCard(0);
        assertTrue(newFirst.isPresent());
        assertTrue(newFirst.get() == card5 || newFirst.get() == card6);
    }

    @Test
    void testTakeCardWithoutHiddenCards() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = new ArrayList<>();
        Pile pile = new Pile(visibleCards, hiddenCards, 24L);

        Card taken = pile.takeCard(0);
        assertEquals(card1, taken);

        Optional<Card> result = pile.getCard(3);
        assertFalse(result.isPresent());
    }

    @Test
    void testRemoveLastCardWithHiddenCards() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = Arrays.asList(card5);
        Pile pile = new Pile(visibleCards, hiddenCards, 123L);

        pile.removeLastCard();

        Optional<Card> first = pile.getCard(0);
        assertTrue(first.isPresent());
        assertEquals(card5, first.get());

        Optional<Card> last = pile.getCard(3);
        assertTrue(last.isPresent());
        assertEquals(card3, last.get());
    }

    @Test
    void testRemoveLastCardUsesDiscardDeck() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = Arrays.asList(card5);
        Pile pile = new Pile(visibleCards, hiddenCards, 47L);

        pile.takeCard(0);
        pile.removeLastCard();

        Optional<Card> first = pile.getCard(0);
        assertTrue(first.isPresent());
    }

    @Test
    void testCanDiscardLastCardWithHiddenCards() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = Arrays.asList(card5);
        Pile pile = new Pile(visibleCards, hiddenCards, 47L);

        assertTrue(pile.canDiscardTheLastCard());
    }

    @Test
    void testStateReturnsValidJSON() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = Arrays.asList(card5, card6);
        Pile pile = new Pile(visibleCards, hiddenCards, 47L);

        String state = pile.state();
        assertNotNull(state);
        assertTrue(state.contains("visible_count"));
        assertTrue(state.contains("hidden_count"));
        assertTrue(state.contains("discard_count"));
    }

    @Test
    void testMultipleTakeCardOperations() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = Arrays.asList(card5, card6);
        Pile pile = new Pile(visibleCards, hiddenCards, 47L);

        Card first = pile.takeCard(0);
        Card second = pile.takeCard(1);

        assertEquals(card1, first);
        assertEquals(card3, second);
    }
}

