package com.ratherbeembed;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class JsonReader {
    public static void createTrieFromJson(TrieNode root, String jsonFilePath) {
        Gson gson = new Gson();

        try (FileReader fileReader = new FileReader(jsonFilePath)) {
            JsonObject jsonObject = gson.fromJson(fileReader, JsonObject.class);
            int skipped_pks = 0;

            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String pokemonName = entry.getKey();
                JsonElement jsonValue = entry.getValue();

                if (jsonValue.isJsonObject()) {
                    JsonObject pokemonDataJson = jsonValue.getAsJsonObject();
                    JsonElement nameElement = pokemonDataJson.get("Name");
                    JsonElement urlElement = pokemonDataJson.get("URL");

                    if (skipped_pks == 0) {
                        //System.out.println("First Skipped Pokemon Data: " + pokemonDataJson);
                    } 

                    if (nameElement != null && nameElement.isJsonPrimitive() && urlElement != null && urlElement.isJsonPrimitive()) {
                        String name = nameElement.getAsString();
                        String url = urlElement.getAsString();

                        // Replace the symbol if present in the Pokemon name
                        if (name.contains("\u2728")) {
                            name = name.replace("\u2728", "shiny");
                        }
                        // Replace the symbol if present in the Pokemon name
                        if (name.contains("&")) {
                            name = name.replace("&", "and");
                        }
                        
                        // System.out.println("Name: " + name); // Add this line
                        // System.out.println("URL: " + url); // Add this line

                        TrieUpdater.updateTrie(root, name, url);
                    } else {
                        skipped_pks += 1;
                        if (skipped_pks < 5){
                            System.out.println("Skipping Pokemon: " + pokemonName);
                            System.out.println("nameElement: " + nameElement);
                            System.out.println("urlElement: " + urlElement);
                        }
                    }
                } else {
                    System.out.println("Not Object.. Invalid JSON entry for Pokemon: " + pokemonName);
                }
            }

            if (skipped_pks > 0) {
                System.out.println("Lots of non-pokemon, probably shinys with funky star: " + skipped_pks);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TrieNode root;

        // Try to deserialize the trie if it exists
        try {
            FileInputStream fileIn = new FileInputStream("trie.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            root = (TrieNode) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Trie deserialized.");
        } catch (IOException | ClassNotFoundException e) {
            // Create a new trie if deserialization fails
            root = new TrieNode();
            System.out.println("New trie created.");
        }

        String jsonFilePath = "pkjsontrie/src/pokemon_dict.json";
        createTrieFromJson(root, jsonFilePath);

        try (Scanner scanner = new Scanner(System.in)) {
            String pokemonName;

            while (true) {
                System.out.print("Enter a Pokemon name (or 'exit' to quit): ");
                pokemonName = scanner.nextLine();

                if (pokemonName.equalsIgnoreCase("exit")) {
                    break; // Exit the loop if the user enters "exit"
                }

                List<SearchResult> urls = searchInTrie(root, pokemonName);

                if (urls != null && !urls.isEmpty()) {
                    System.out.println("Results for Pokemon: " + pokemonName);
                    for (SearchResult result : urls) {
                        System.out.println("Prefix: " + result.getPrefix());
                        System.out.println("URL: " + result.getUrl());
                        System.out.println("------------------------");
                    }
                } else {
                    System.out.println("Pokemon not found: " + pokemonName);
                }
            }
        }

        // Serialize the trie
        try {
            FileOutputStream fileOut = new FileOutputStream("trie.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(root);
            out.close();
            fileOut.close();
            System.out.println("Trie serialized.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static List<SearchResult> searchInTrie(TrieNode root, String prefix) {
        prefix = prefix.toLowerCase(); // Convert prefix to lowercase

        // Perform wildcard search if the prefix contains underscore character
        if (prefix.contains("*")) {
            prefix.replace("*", "_");
        }
        return root.searchTrie(root, prefix);
    }
    
}
