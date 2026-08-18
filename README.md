<p align="center">
  <img src="icon.png" width="160" alt="Coal Bag logo">
</p>

<h1 align="center">Coal Bag</h1>

<p align="center">
  Keep your coal bag count visible right in your RuneLite inventory.
</p>

Coal Bag adds a small counter to every coal bag variant in Old School
RuneScape. It learns the bag's contents from in-game messages, then keeps the
count up to date as you fill, empty, and mine into it.

## Features

- Displays the known coal amount directly on the bag in your inventory
- Shows `0` for a known empty bag and `?` until the amount is known
- Tracks coal mined directly into an open bag, including eligible bonus ore
  from the Celestial ring and Varrock platebody
- Accounts for the increased capacity of the Smithing cape and max cape
- Provides configurable colors for filled, empty, and unknown amounts

## Install

1. Open RuneLite's **Configuration** panel and select **Plugin Hub**.
2. Search for **Coal Bag**.
3. Select **Install**.

After the plugin starts, check, fill, or empty your coal bag once so RuneLite
can learn its current contents. The counter remains `?` until the game reports
an amount.

> [!NOTE]
> Coal picked up from the ground directly into an open bag cannot be tracked
> reliably because the game does not expose a safe event for that action.

### Configuration

Open **Configuration → Coal Bag** to choose the colors used for filled, empty,
and unknown counters.

## Build from source

Clone the repository and run the test suite:

```sh
./gradlew test
```

To launch RuneLite in developer mode with the plugin loaded:

```sh
./gradlew run
```

## Support

If you find a bug, [open an issue](https://github.com/WolffTech/coal-bag-plugin/issues/new)
with a description of the problem and the steps needed to reproduce it.

## Credits

Coal Bag is based on Adam's
[Essence Pouch plugin](https://github.com/Adam-/runelite-plugins/blob/esspouch/src/main/java/info/sigterm/plugins/esspouch/EssPouchPlugin.java).
Automatic update handling is adapted from
[Item Charges Improved](https://github.com/TicTac7x/runelite-plugins/tree/plugin-charges).

## License

Coal Bag is licensed under the [BSD 2-Clause License](LICENSE). Release history
is available in [CHANGELOG.md](CHANGELOG.md).
