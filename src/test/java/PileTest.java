package java;

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
    void testConstructorWithCorrectNumberOfVisibleCards() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = Arrays.asList(card5, card6);

        Pile pile = new Pile(visibleCards, hiddenCards, 42L);
        assertNotNull(pile);
    }

    @Test
    void testConstructorWithNotFourVisibleCards() {
        visibleCards = Arrays.asList(card1, card2, card3);
        hiddenCards = Arrays.asList(card5, card6);

        assertThrows(IllegalArgumentException.class, () -> {
            new Pile(visibleCards, hiddenCards, 42L);
        });
    }


    @Test
    void testGetCardValidIndex() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = new ArrayList<>();
        Pile pile = new Pile(visibleCards, hiddenCards, 42L);

        Optional<Card> result = pile.getCard(0);
        assertTrue(result.isPresent());
        assertEquals(card1, result.get());
    }

    @Test
    void testTakeCardValidIndex() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = Arrays.asList(card5, card6);
        Pile pile = new Pile(visibleCards, hiddenCards, 42L);

        Card taken = pile.takeCard(2);
        assertEquals(card3, taken);
    }

    @Test
    void testTakeCardInvalidIndex() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = new ArrayList<>();
        Pile pile = new Pile(visibleCards, hiddenCards, 42L);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            pile.takeCard(5);
        });
    }

    @Test
    void testTakeCardRefillsFromDiscardDeck() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = Arrays.asList(card5, card6);
        Pile pile = new Pile(visibleCards, hiddenCards, 42L);

        pile.takeCard(0);

        Optional<Card> newFirst = pile.getCard(0);
        assertTrue(newFirst.isPresent());
        assertTrue(newFirst.get() == card5 || newFirst.get() == card6);
    }

    @Test
    void testTakeCardWithoutHiddenCards() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = new ArrayList<>();
        Pile pile = new Pile(visibleCards, hiddenCards, 42L);

        Card taken = pile.takeCard(0);
        assertEquals(card1, taken);

        Optional<Card> result = pile.getCard(3);
        assertFalse(result.isPresent());
    }

    @Test
    void testRemoveLastCardWithHiddenCards() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = Arrays.asList(card5);
        Pile pile = new Pile(visibleCards, hiddenCards, 42L);

        pile.removeLastCard();

        Optional<Card> first = pile.getCard(0);
        assertTrue(first.isPresent());
        assertEquals(card5, first.get());

        Optional<Card> last = pile.getCard(3);
        assertTrue(last.isPresent());
        assertEquals(card3, last.get());
    }

    @Test
    void testRemoveLastCardWithoutHiddenOrDiscard() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = new ArrayList<>();
        Pile pile = new Pile(visibleCards, hiddenCards, 42L);

        assertThrows(IllegalStateException.class, () -> {
            pile.removeLastCard();
        });
    }

    @Test
    void testRemoveLastCardUsesDiscardDeck() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = Arrays.asList(card5);
        Pile pile = new Pile(visibleCards, hiddenCards, 42L);

        pile.takeCard(0);
        pile.removeLastCard();

        Optional<Card> first = pile.getCard(0);
        assertTrue(first.isPresent());
    }

    @Test
    void testCanDiscardLastCardWithHiddenCards() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = Arrays.asList(card5);
        Pile pile = new Pile(visibleCards, hiddenCards, 42L);

        assertTrue(pile.canDiscardTheLastCard());
    }

    @Test
    void testCanDiscardLastCardWithoutHiddenOrDiscard() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = new ArrayList<>();
        Pile pile = new Pile(visibleCards, hiddenCards, 42L);

        assertFalse(pile.canDiscardTheLastCard());
    }

    @Test
    void testStateReturnsValidJSON() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = Arrays.asList(card5, card6);
        Pile pile = new Pile(visibleCards, hiddenCards, 42L);

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
        Pile pile = new Pile(visibleCards, hiddenCards, 42L);

        Card first = pile.takeCard(0);
        Card second = pile.takeCard(1);

        assertEquals(card1, first);
        assertEquals(card3, second);
    }

    @Test
    void testDiscardDeckShufflesIntoHidden() {
        visibleCards = Arrays.asList(card1, card2, card3, card4);
        hiddenCards = new ArrayList<>();
        Pile pile = new Pile(visibleCards, hiddenCards, 42L);

        Card taken1 = pile.takeCard(0);
        Card taken2 = pile.takeCard(0);
        Card taken3 = pile.takeCard(0);
        Card taken4 = pile.takeCard(0);

        Optional<Card> result = pile.getCard(0);
        assertTrue(result.isPresent());
    }

    @Test
    void testRandomSeedAffectsCardOrder() {
        List<Card> visible1 = Arrays.asList(card1, card2, card3, card4);
        List<Card> hidden1 = Arrays.asList(card5, card6, new Card(7), new Card(8));
        Pile pile1 = new Pile(visible1, hidden1, 42L);

        List<Card> visible2 = Arrays.asList(card1, card2, card3, card4);
        List<Card> hidden2 = Arrays.asList(card5, card6, new Card(7), new Card(8));
        Pile pile2 = new Pile(visible2, hidden2, 42L);

        pile1.takeCard(0);
        pile2.takeCard(0);

        assertEquals(pile1.getCard(0).get(), pile2.getCard(0).get());
    }
}

