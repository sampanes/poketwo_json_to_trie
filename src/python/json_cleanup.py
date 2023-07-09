import os
import json

# Get the absolute path of the script
script_path = os.path.abspath(__file__)

# Navigate two directories above
parent_dir = os.path.dirname(os.path.dirname(script_path))

# Define the path to the JSON file
json_file_path = os.path.join(parent_dir, "pokemon_dict.json")

PKFILE_ORIG = json_file_path
REGIONAL_NAME_LIST = ["Alolan", "Galarian"]


def load(file):
    # Open the input file and read its contents into a dictionary
    with open(file, 'r') as file:
        return json.load(file)
    

def save(file, dict):
    # Open the output file and write the updated dictionary with pretty formatting
    with open(file, 'w') as file:
        json.dump(dict, file, indent=4)


def print_pretty(data, indent=4):
    if isinstance(data, dict):
        print(json.dumps(data, indent=indent))
    elif isinstance(data, list):
        print('\n'.join(data))


def shiny_decode(pk_dict):
    # change each occurance of \u2728 with the word Shiny, print when names don't match for other reasons
    for name, data in pk_dict.items():
        if name != data["Name"]:
            if '\u2728' in data["Name"]:
                data["Name"] = data["Name"].replace('\u2728', 'Shiny')
            else:
                print("\n%s" % name)
                print_pretty(data)
    return pk_dict


if __name__ == "__main__":
    # Load the dict
    pk_dict_orig = load(PKFILE_ORIG)

    # Change \u2728 to Shiny, print other mismatches to terminal
    new_dict = shiny_decode(pk_dict_orig)

    # Overwrite original dict with all changes above
    save(PKFILE_ORIG, new_dict)

