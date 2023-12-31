# poketwo_json_to_trie

The Pokémon Trie Maker is a Java application that creates a trie data structure for storing Pokémon names and URLs. It allows efficient searching of Pokémon names based on user queries, handling variations in names and providing accurate results. The program has a command-line interface (CLI) for user interaction, supports serialization and deserialization of the trie, and offers customization of the data source. Overall, it simplifies Pokémon data storage and retrieval for nerds.

## Table of Contents

- [Usage](#usage)
- [Features](#features)
- [Contributing](#contributing)
- [License](#license)

## Usage

This is all just a clever way of turning all of poketwo's pokemon data (so far) into a serialized TRIE tree, so that one can easily look up pokemon hints:
- p_k_ch_ = pikachu
- esp___ = Espurr and Espeon (two image url's)
![alt text](https://cdn.poketwo.net/images/25.png?v=26)

## Features

- Create Trie from JSON: Parse a JSON file containing Pokémon data and create a trie data structure to store the Pokémon names and their associated URLs.
- Search Pokémon: Perform prefix-based searches on the trie to retrieve Pokémon names that match a given query. Display the prefixes and corresponding URLs for the matching Pokémon.
- Handle Pokémon Name Variations: Account for variations in Pokémon names, such as replacing symbols or handling special characters, to ensure accurate search results.
- Serialization: Serialize the trie data structure to a file (trie.ser) for efficient retrieval and reusability of the trie across multiple program executions. (this will hopefully be used by my android app that I'm working on)
- Deserialization: Deserialize the trie from the file (trie.ser) if it exists, allowing for the reuse of the previously created trie instead of recreating it from scratch.
- Error Handling: Handle exceptions and error conditions gracefully, providing informative error messages to users when invalid input or unexpected scenarios occur.
- Command-Line Interface: Utilize a command-line interface (CLI) to interact with the program, accepting user input for Pokémon name queries and displaying the results.
- Data Source Customization: Allow users to specify a different JSON data source containing Pokémon information, enabling the usage of alternative datasets.
- Efficient Trie Construction: Optimize the creation of the trie data structure by implementing efficient algorithms and data structures, ensuring fast performance even with large datasets.

## Contributing

I am not 100% sure about the accuracy of the json file, I found that kabutops randomly had the name and number for aerodactyl but the rest of the info like stats and url were correct... so I corrected it by hand but that doesn't make me super warm and fuzzy. If you like parsing through huge jsons and gathering insights (maybe comparing key (name) to data member {key "name" : "value",...} for each pokemon would be a good start) then feel free to let me know about any errors or missing things. Or you can do it the pro way:

1. Fork the repository.
2. Create a new branch.
3. Make your changes.
4. Submit a pull request.

## License

free use?

Copyright (c) 2023, Sampanes