package java;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sk.uniba.fmph.dcs.terra_futura.red_classes.Resource;
import sk.uniba.fmph.dcs.terra_futura.red_classes.SelectReward;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SelectRewardTest {
    private SelectReward selectReward;
    private List<Resource> testResources;

    @BeforeEach
    void setUp() {
        selectReward = new SelectReward();
        testResources = Arrays.asList(Resource.Green, Resource.Red, Resource.Yellow);
    }

    @Test
    void testConstructor() {
        assertFalse(selectReward.getPlayer().isPresent());
        assertFalse(selectReward.isSelected());
        assertFalse(selectReward.getSelectedResource().isPresent());
    }

    @Test
    void testSetRewardWithEmptyList() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> selectReward.setReward(1, Collections.emptyList())
        );
        assertEquals("There's no available resources", exception.getMessage());
    }

    @Test
    void testSetRewardSuccess() {
        selectReward.setReward(1, testResources);

        assertTrue(selectReward.getPlayer().isPresent());
        assertEquals(1, selectReward.getPlayer().get());
        assertFalse(selectReward.isSelected());
        assertFalse(selectReward.getSelectedResource().isPresent());
    }

    @Test
    void testSelectRewardTwice() {
        selectReward.setReward(1, testResources);
        selectReward.selectReward(1, Resource.Green);
        boolean result = selectReward.selectReward(1, Resource.Red);
        assertFalse(result);
    }

    @Test
    void testSelectRewardWrongPlayer() {
        selectReward.setReward(1, testResources);
        boolean result = selectReward.selectReward(2, Resource.Green);
        assertFalse(result);
        assertFalse(selectReward.isSelected());
    }

    @Test
    void testCanSelectRewardCorrectPlayer() {
        selectReward.setReward(1, testResources);
        assertTrue(selectReward.canSelectReward(1));
    }

    @Test
    void testCanSelectRewardWrongPlayer() {
        selectReward.setReward(1, testResources);
        assertFalse(selectReward.canSelectReward(2));
    }

    @Test
    void testCanSelectRewardAfterSelection() {
        selectReward.setReward(1, testResources);
        selectReward.selectReward(1, Resource.Money);
        assertFalse(selectReward.canSelectReward(1));
    }


    @Test
    void testSelectRewardResourceNotInSelection() {
        selectReward.setReward(1, Arrays.asList(Resource.Green, Resource.Red));
        boolean result = selectReward.selectReward(1, Resource.Yellow);

        assertFalse(result);
        assertFalse(selectReward.isSelected());
    }

    @Test
    void testSelectRewardNullResource() {
        selectReward.setReward(1, testResources);
        boolean result = selectReward.selectReward(1, null);

        assertFalse(result);
        assertFalse(selectReward.isSelected());
    }

    @Test
    void testSelectRewardSuccess() {
        selectReward.setReward(1, testResources);
        boolean result = selectReward.selectReward(1, Resource.Green);

        assertTrue(result);
        assertTrue(selectReward.isSelected());
        assertTrue(selectReward.getSelectedResource().isPresent());
        assertEquals(Resource.Green, selectReward.getSelectedResource().get());
        assertFalse(selectReward.getPlayer().isPresent());
    }


    @Test
    void testMultipleSetReward() {
        selectReward.setReward(1, testResources);
        selectReward.selectReward(1, Resource.Green);

        selectReward.setReward(2, Arrays.asList(Resource.Red));

        assertTrue(selectReward.getPlayer().isPresent());
        assertEquals(2, selectReward.getPlayer().get());
        assertFalse(selectReward.isSelected());
        assertTrue(selectReward.canSelectReward(2));
    }

    @Test
    void testStateAfterSetReward() {
        selectReward.setReward(1, testResources);
        String stateStr = selectReward.state();
        JSONObject state = new JSONObject(stateStr);

        assertEquals(1, state.getInt("player"));
        assertEquals(3, state.getJSONArray("available").length());
        assertFalse(state.getBoolean("selected"));
        assertTrue(state.isNull("selected_resource"));
    }

    @Test
    void testStateAfterSelectReward() {
        selectReward.setReward(1, testResources);
        selectReward.selectReward(1, Resource.Green);
        String stateStr = selectReward.state();
        JSONObject state = new JSONObject(stateStr);

        assertTrue(state.isNull("player"));
        assertEquals(0, state.getJSONArray("available").length());
        assertTrue(state.getBoolean("selected"));
        assertEquals("Green", state.getString("selected_resource"));
    }

    @Test
    void testStateAllResources() {
        List<Resource> allResources = Arrays.asList(Resource.values());
        selectReward.setReward(5, allResources);
        JSONObject state = new JSONObject(selectReward.state());

        assertEquals(allResources.size(), state.getJSONArray("available").length());
    }

    @Test
    void testGetPlayerAfterSelection() {
        selectReward.setReward(1, testResources);
        selectReward.selectReward(1, Resource.Green);
        assertFalse(selectReward.getPlayer().isPresent());
    }

    @Test
    void testIsSelectedInitial() {
        assertFalse(selectReward.isSelected());
    }

    @Test
    void testIsSelectedPersists() {
        selectReward.setReward(1, testResources);
        selectReward.selectReward(1, Resource.Green);

        assertTrue(selectReward.isSelected());
        assertTrue(selectReward.isSelected());
    }

    @Test
    void testGetSelectedResourceCorrect() {
        selectReward.setReward(1, testResources);
        selectReward.selectReward(1, Resource.Red);

        assertTrue(selectReward.getSelectedResource().isPresent());
        assertEquals(Resource.Red, selectReward.getSelectedResource().get());
    }
}

