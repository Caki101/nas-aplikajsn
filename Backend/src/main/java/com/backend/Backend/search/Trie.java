package com.backend.Backend.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    Map<Character, Trie> children = new HashMap<>();
    List<Long> tiket_ids = new ArrayList<>();

    public Map<Character, Trie> getChildren() {
        return children;
    }

    public void setChildren(Map<Character, Trie> children) {
        this.children = children;
    }

    public List<Long> getTiket_ids() {
        return tiket_ids;
    }

    public void setTiket_ids(List<Long> tiket_ids) {
        this.tiket_ids = tiket_ids;
    }
}
