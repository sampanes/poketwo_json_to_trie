package com.ratherbeembed;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class TrieSerializer {
    public static void serializeTrie(TrieNode root, String filePath) {
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(root);

            System.out.println("Trie serialized and saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
