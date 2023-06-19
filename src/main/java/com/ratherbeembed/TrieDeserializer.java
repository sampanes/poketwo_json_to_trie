package com.ratherbeembed;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class TrieDeserializer {
    public static TrieNode deserializeTrie(String filePath) {
        TrieNode root = null;

        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

            root = (TrieNode) objectIn.readObject();

            System.out.println("Trie deserialized from: " + filePath);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return root;
    }
}
