# Coal Bag Plugin

Adds a counter to the coal bag.

## Description

The plugin is simple, it adds a counter to the coal bag to tell you how much coal is in the coal bag. The counter only updates when you add coal manually into the bag. It will not update when coal is added automatically while the bag is opened.

This plugin is a modified version of [Adam's esspouch](https://github.com/Adam-/runelite-plugins/blob/esspouch/src/main/java/info/sigterm/plugins/esspouch/EssPouchPlugin.java) plugin.

## Help

If you find a issue please [open a issue](https://github.com/WolffTech/coal-bag-plugin/issues/new) on this repository with a description of what's happening and how to reproduce the issue.

## Version History
* 1.5
  * Fix Issue #4
    * Check for the widget on `onClientTick` instead of `onGameTick` as it's possible to close the widget on the same game tick that it opens.
* 1.4 (Thank you to [keyosk](https://github.com/keyosk) for this update!)
  * Added a configurable color for the counter
  * Both chat messages and widget messages are now evaluated as they can be different.
  * Cleaned up code and bug fixes
* 1.3
  * Updated coal bag empty message as it changed in one of the latest game updates
  * Cleaned up code
* 1.2
  * Fix Issue #2
    * Game will no longer error when completing temple trekks or when receiving a new cluescroll step.
  * Counter updates properly when one piece of coal is left in the bag when emptying.
* 1.1
  * Fix Issue #1
    * Counter should account for Smithing Cape of Accomplishment.
    * Counter now displays the correct amount of coal in bag when emptied.
  * Changed name to "Coal Bag"
* 1.0
    * Initial Release