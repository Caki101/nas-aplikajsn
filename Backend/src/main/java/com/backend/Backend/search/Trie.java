package com.backend.Backend.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    Map<Character, Trie> children = new HashMap<>();
    List<Long> ids = new ArrayList<>();

    public Map<Character, Trie> getChildren() {
        return children;
    }

    public void setChildren(Map<Character, Trie> children) {
        this.children = children;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setTiket_ids(List<Long> ids) {
        this.ids = ids;
    }
}
