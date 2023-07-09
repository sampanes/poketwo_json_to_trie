import os
import json

# Get the absolute path of the script
script_path = os.path.abspath(__file__)

# Navigate two directories above
parent_dir = os.path.dirname(os.path.dirname(script_path))

# Define the path to the JSON file
json_file_path = os.path.join(parent_dir, "pokemon_dict.json")
links_file_path = os.path.join(parent_dir, "png_links.json")

PKFILE_ORIG = json_file_path
LINKS_FILE = links_file_path
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


def remove_nones(pk_dict):
    # if no data just None or False then remove
    for name, data in pk_dict.items():
        if not data:
            pk_dict.pop(name)
    return pk_dict

def get_links_by_number_and_region(pk_dict):
    links_dict = {}
    for name, data in pk_dict.items():
        current_name = name.replace("Shiny ", "")
        current_number = data["Number"]
        current_url = data["URL"]
        region = next((reg for reg in REGIONAL_NAME_LIST if reg in name), False)
        event_bool = data["Rarity"] == "Event"
        if not event_bool and data["Catchable"] == False:
            continue

        if current_number in links_dict.keys():
            # This pokemon number exists already
            smallest_name_key_in_current_number = min(links_dict[current_number], key=lambda k: len(k))
            if current_name in links_dict[current_number].keys() \
                or (not region and \
                    smallest_name_key_in_current_number in current_name):
                if current_name not in links_dict[current_number].keys():
                    # print("\nNot in dict but shortest key in name, name: %s" % current_name)
                    current_name = min(links_dict[current_number], key=lambda k: len(k))
                # This name already exists for this number, must be shiny/unshiny
                if current_url in links_dict[current_number][current_name]:
                    # This URL ALREADY EXISTS! TODO this is odd and means duplicates
                    print("\nDUPLICATE URL: %s" % name)
                    print_pretty(data)
                else:
                    # This is a new url but d[#]["n"] exists, shiny/unshiny probably
                    links_dict[current_number][current_name].append(current_url)
            elif region:
                reg_name = region + " " + min(links_dict[current_number], key=lambda k: len(k))
                if reg_name in links_dict[current_number].keys():
                    # a shiny or event regional pokemon, append url list
                    links_dict[current_number][reg_name].append(current_url)
                else:
                    links_dict[current_number][reg_name] = [current_url]
            else:
                # print("This name does not exist for this number, different region than last, add dict key current name value list of urls\n%s\n" % current_name)
                all_english_names = data["Names"]["gb"]
                for n in all_english_names:
                    if smallest_name_key_in_current_number in n:
                        # print("\tso dumb but %s is actually %s\n\n" % (current_name, smallest_name_key_in_current_number))
                        links_dict[current_number][smallest_name_key_in_current_number].append(current_url)
                        break
                else:
                    # print("\n\t%s not in %s" % (current_name, links_dict[current_number].keys()))
                    for k in links_dict[current_number].keys():
                        if k[:-1] in current_name:
                            # print("\tso dumb but %s is actually %s\n\n" % (current_name, k))
                            links_dict[current_number][k].append(current_url)
                            break
        else:
            # No number yet so create
            if event_bool:
                print("\nEVENT POKEMON SHOWED UP BEFORE ORIGINAL: %s" % name)
                print_pretty(data)
            links_dict[current_number] = {current_name: [current_url]}

    return links_dict

def print_unique_png_dict_items(png_dict):
    l=[]
    for number, names in png_dict.items():
        for name, urls in names.items():
            if len(urls) in l:
                break
            else:
                l.append(len(urls))
                print("\n\n%s" % number)
                print_pretty(names)


if __name__ == "__main__":
    # Load the dict
    pk_dict_orig = load(PKFILE_ORIG)

    # get rid of names with None value
    pk_dict_no_nones = remove_nones(pk_dict_orig)
    # Change \u2728 to Shiny, print other mismatches to terminal
    no_sym_dict = shiny_decode(pk_dict_no_nones)

    # put all pokemon that are not event first
    new_dict = {
        k: v for k, v in no_sym_dict.items() if v.get("Rarity") != "Event"
    }
    new_dict.update = {
        k: v for k, v in no_sym_dict.items() if v.get("Rarity") == "Event"
    }

    # Overwrite original dict with all changes above
    save(PKFILE_ORIG, new_dict)

    # redo png links json
    png_dict = get_links_by_number_and_region(new_dict)

    print_unique_png_dict_items(png_dict)

    save(LINKS_FILE, png_dict)