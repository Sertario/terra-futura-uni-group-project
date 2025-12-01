package sk.uniba.fmph.dcs.terra_futura.red_classes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SelectReward {
    private Optional<Integer> player;
    private List<Resource> selection;
    private boolean selected;
    private Optional<Resource> selectedResource;

    public SelectReward() {
        player = Optional.empty();
        selection = Collections.emptyList();
        selected = false;
        selectedResource = Optional.empty();
    }

    public void setReward(int playerId, List<Resource> availableResources) {
        if (availableResources.isEmpty()) {
            throw new IllegalArgumentException("There's no available resources");
        }

        this.player = Optional.of(playerId);
        this.selection = new ArrayList<>(availableResources);
        this.selected = false;
        this.selectedResource = Optional.empty();
    }

    public boolean canSelectReward(int playerId) {
        return player.isPresent() && player.get().intValue() == playerId && !selected;
    }

    public boolean selectReward(int playerId, Resource resource) {
        if (!canSelectReward(playerId)) return false;
        if (!selection.contains(resource)) return false;
        if (resource == null) return false;

        this.selectedResource = Optional.of(resource);
        this.selected = true;

        this.player = Optional.empty();
        this.selection = Collections.emptyList();
        return true;
    }

    public Optional<Integer> getPlayer() {
        return player;
    }

    public Optional<Resource> getSelectedResource() {
        return selectedResource;
    }

    public boolean isSelected() {
        return selected;
    }

    public String state() {
        JSONObject result = new JSONObject();
        result.put("player", player.isPresent() ? player.get() : JSONObject.NULL);

        JSONArray available = new JSONArray();
        for (Resource resource : selection) {
            available.put(resource == null ? JSONObject.NULL : resource.name());
        }

        result.put("available", available);
        result.put("selected", this.selected);
        result.put("selected_resource", selectedResource.isPresent() ?
                selectedResource.get().name() : JSONObject.NULL);
        return result.toString();
    }
}

