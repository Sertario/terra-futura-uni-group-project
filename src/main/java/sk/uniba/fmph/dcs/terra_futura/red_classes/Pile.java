package sk.uniba.fmph.dcs.terra_futura.red_classes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class Pile {
    private final List<Card> visibleCards;
    private final List<Card> hiddenCards;
    private final List<Card> discardDeck;
    private final Random random;

    public Pile(List<Card> visibleCards, List<Card> hiddenCards, Long seed) {
        if (visibleCards.size() != 4) {
            throw new IllegalArgumentException("There should be exactly 4 visible cards");
        }
        this.visibleCards = new ArrayList<>(visibleCards);
        this.hiddenCards = new ArrayList<>(hiddenCards);
        this.discardDeck = new ArrayList<>();
        this.random = new Random(seed);
    }

    public Optional<Card> getCard(int index) {
        if (index < 0 || index >= visibleCards.size()) return Optional.empty();
        return Optional.ofNullable(visibleCards.get(index));
    }

    public Card takeCard(int index) {
        if (index < 0 || index >= visibleCards.size()) {
            throw new IndexOutOfBoundsException("Index is out of range");
        }
        Card card = visibleCards.get(index);
        renewVisibleCards(index);
        return card;
    }

    public void removeLastCard() {
        if (!canDiscardTheLastCard()) {
            throw new IllegalStateException("Cannot discard the last one card:" +
                    "there is no hidden or discard cards available");
        }
        Card oldest = visibleCards.remove(3);
        discardDeck.add(oldest);
        visibleCards.add(0, drawHiddenCards());
    }

    public String state() {
        JSONObject result = new JSONObject();

        result.put("visible_count", visibleCards.size());
        result.put("hidden_count", hiddenCards.size());
        result.put("discard_count", discardDeck.size());

        JSONArray visibleCardsArray = new org.json.JSONArray();
        for (Card card : visibleCards) {
            try {
                visibleCardsArray.put(card == null ?
                        JSONObject.NULL : card.state());
            } catch (Exception e) {
                visibleCardsArray.put(JSONObject.NULL);
            }
        }
        result.put("visible_cards", visibleCardsArray);
        return result.toString();
    }

    public boolean canDiscardTheLastCard() {
        return !hiddenCards.isEmpty() || !discardDeck.isEmpty();
    }

    private void renewVisibleCards(int index) {
        visibleCards.remove(index);
        if (!hiddenCards.isEmpty() || !discardDeck.isEmpty()) {
            visibleCards.add(0, drawHiddenCards());
        }
    }

    private Card drawHiddenCards() {
        if (hiddenCards.isEmpty()) {
            if (discardDeck.isEmpty()) {
                throw new IllegalStateException("No cards available to draw");
            }

            hiddenCards.addAll(discardDeck);
            discardDeck.clear();
            Collections.shuffle(hiddenCards, random);
        }
        int index = random.nextInt(hiddenCards.size());
        return hiddenCards.remove(index);
    }
}

