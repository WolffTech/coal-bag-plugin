# Coal Bag

Coal Bag displays the amount of coal in your coal bag directly in your inventory.

## Features

- Displays the known coal amount on the coal bag.
- Shows `0` when the bag is known to be empty.
- Shows `?` when the amount is unknown.
- Tracks coal mined directly into an open bag, including eligible bonus ore.
- Provides configurable colors for known, empty, and unknown amounts.

## How It Works

The counter updates when the game reports the coal bag's contents, such as when you check, fill, or empty the bag. Once known, the counter also follows coal mined directly into an open bag, bonus ore from the Celestial ring and Varrock platebody, and bank container-emptying.

Coal picked up from the ground directly into an open bag cannot be tracked reliably because the game does not expose a safe event for that action.

## Configuration

The following counter colors can be configured in RuneLite:

- **Filled Color**: Used when the bag contains a known amount of coal.
- **Empty Color**: Used when the bag is known to be empty.
- **Unknown Color**: Used before the bag's contents are known.

## Installation

1. Open RuneLite.
2. Open the Configuration panel and select **Plugin Hub**.
3. Search for **Coal Bag**.
4. Select **Install**.

## Support

If you encounter a problem, [open an issue](https://github.com/WolffTech/coal-bag-plugin/issues/new) with a description of the problem and the steps needed to reproduce it.

## Credits

Coal Bag is based on Adam's [Essence Pouch plugin](https://github.com/Adam-/runelite-plugins/blob/esspouch/src/main/java/info/sigterm/plugins/esspouch/EssPouchPlugin.java).

Automatic update handling is adapted from [Item Charges Improved](https://github.com/TicTac7x/runelite-plugins/tree/plugin-charges).

## Project Information

- See [CHANGELOG.md](CHANGELOG.md) for release history.
- This project is licensed under the [BSD 2-Clause License](LICENSE).
