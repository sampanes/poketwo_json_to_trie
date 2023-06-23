package com.ratherbeembed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TrieNode implements Serializable {
    private static final int ALPHABET_SIZE = 45; // 26 lowercase letters + 10 numbers + . - ♂ ♀ space ' %
    private TrieNode[] children;
    private boolean isEndOfWord;
    private String url; // The URL as a string actually, images/ and shiny/ :(

    public TrieNode() {
        this.children = new TrieNode[ALPHABET_SIZE]; // Assuming lowercase English alphabet
        this.isEndOfWord = false;
        this.url = "https://via.placeholder.com/475x475.png"; // Default URL value
    }

    public TrieNode[] getChildren() {
        return children;
    }

    public void setChildren(TrieNode[] children) {
        this.children = children;
    }

    public boolean isEndOfWord() {
        return isEndOfWord;
    }

    public void setEndOfWord(boolean endOfWord) {
        isEndOfWord = endOfWord;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }

    private char getChildCharacter(TrieNode parentNode, TrieNode childNode) {
        for (int i = 0; i < parentNode.getChildren().length; i++) {
            if (parentNode.getChildren()[i] == childNode) {
                return CharUtils.getCharacter(i);
            }
        }
        throw new IllegalArgumentException("Child node not found in parent's children");
    }

    public List<SearchResult> searchTrie(TrieNode root, String query) {
        List<SearchResult> results = new ArrayList<>();
        searchTrieHelper(root, query.toLowerCase(), new StringBuilder(), results);
        return results;
    }

    private void searchTrieHelper(TrieNode node, String query, StringBuilder prefix, List<SearchResult> results) {
        if (query.isEmpty()) {
            if (node.isEndOfWord()) {
                SearchResult result = new SearchResult(prefix.toString(), node.getURL());
            
                results.add(result);
                System.out.println("end of word, heres prefix: " + prefix.toString() +" "+ node.getURL());
            }
            return;
        }

        char nextChar = query.charAt(0);
        String remainingQuery = query.substring(1);

        if (nextChar == '_') {
            // Perform wildcard search for each child node
            for (TrieNode child : node.getChildren()) {
                if (child != null) {
                    char ch = getChildCharacter(node, child);
                    prefix.append(ch);
                    searchTrieHelper(child, remainingQuery, prefix, results);
                    prefix.setLength(prefix.length() - 1);
                }
            }
        } else {
            int index = CharUtils.getIndex(nextChar);
            TrieNode childNode = node.getChildren()[index];
            if (childNode != null) {
                char ch = getChildCharacter(node, childNode);
                prefix.append(ch);
                searchTrieHelper(childNode, remainingQuery, prefix, results);
                prefix.setLength(prefix.length() - 1);
            }
        }
    }
}