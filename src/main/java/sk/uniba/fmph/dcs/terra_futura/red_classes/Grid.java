package sk.uniba.fmph.dcs.terra_futura.red_classes;

import org.json.JSONArray;
import org.json.JSONObject;
import sk.uniba.fmph.dcs.terra_futura.Card;

import java.util.*;

public class Grid {
    private final Map<GridPosition, Card> cells;
    private final Set<GridPosition> isActivated;
    private List<GridPosition> activationPattern;

    public Grid () {
        cells = new HashMap<>();
        isActivated = new HashSet<>();
        activationPattern = new ArrayList<>();
    }

    public Optional<Card> getCard(GridPosition coordinate) {
        return Optional.ofNullable(cells.get(coordinate));
    }

    public boolean canPutCard(GridPosition coordinate) {
        if (coordinate == null) return false;
        return !cells.containsKey(coordinate);
    }

    public void putCard(GridPosition coordinate, Card card) {
        if (coordinate == null || card == null) {
            throw new IllegalArgumentException("Coordinate and card should be defined");
        }

        if (!canPutCard(coordinate)) {
            throw new IllegalStateException(
                    "It is impossible to put card on occupied coordinate"
            );
        }

        cells.put(coordinate, card);
    }

    public boolean canBeActivated(GridPosition coordinate) {
        if (coordinate == null) return false;
        return cells.containsKey(coordinate) && !isActivated.contains(coordinate);
    }

    public void setActivated(GridPosition coordinate) {
        if (!canBeActivated(coordinate)) {
            throw new IllegalStateException("Coordinate cannot be activated");
        }
        isActivated.add(coordinate);
    }

    public void setActivationPattern(List<GridPosition> pattern) {
        this.activationPattern = (pattern == null) ?
                Collections.emptyList() : new ArrayList<>(pattern);
    }

    public void endTurn() {
        isActivated.clear();
    }

    public String state() {
        JSONObject cardsObject = new JSONObject();
        for (Map.Entry<GridPosition, Card> cur : cells.entrySet()) {
            String key = String.valueOf(cur.getKey());

            Card card = cur.getValue();
            cardsObject.put(key, (card == null) ?
                    JSONObject.NULL : card.state());
        }

        JSONArray patternArray = new JSONArray();
        for (GridPosition gp : activationPattern) {
            patternArray.put((gp == null) ? JSONObject.NULL : gp.toString());
        }

        JSONObject result = new JSONObject();
        result.put("cards", cardsObject);
        result.put("card_count", cells.size());
        result.put("activation_pattern", patternArray);
        return result.toString();
    }
}

