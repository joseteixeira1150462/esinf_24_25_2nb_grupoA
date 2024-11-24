package esinf.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateResult {
    final Map<Integer, Double> updates;
    final List<String> messages;

    UpdateResult() {
        this.updates = new HashMap<>();
        this.messages = new ArrayList<>();
    }
}