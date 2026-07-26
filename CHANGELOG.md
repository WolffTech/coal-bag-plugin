# Changelog

All notable changes to Coal Bag are documented in this file.

## 1.6 - 2026-07-25

### Added

- Track coal mined directly into an open bag, eligible bonus ore, and bank container-emptying.

Thanks to [nanopink](https://github.com/nanopink) for contributing this release.

## 1.5 - 2022-05-24

### Fixed

- Check coal bag dialogue on each client tick so dialogue closed within the same game tick is not missed ([#4](https://github.com/WolffTech/coal-bag-plugin/issues/4)).

## 1.4 - 2021-09-14

### Added

- Add configurable colors for known, empty, and unknown coal amounts.
- Support all coal bag item variants.

### Changed

- Evaluate both chat and widget messages when updating the counter.
- Refactor the counter logic and configuration.

### Fixed

- Recognize variations of the empty coal bag message.

Thanks to [keyosk](https://github.com/keyosk) for contributing this release.

## 1.3 - 2021-07-24

### Changed

- Update the empty coal bag message to match the game.
- Clean up the counter logic.

## 1.2 - 2021-07-21

### Fixed

- Ignore unrelated messages that previously caused errors during Temple Trekking and clue scrolls ([#2](https://github.com/WolffTech/coal-bag-plugin/issues/2)).
- Correctly update the counter when one piece of coal remains while emptying the bag.

## 1.1 - 2021-06-24

### Changed

- Rename the plugin to Coal Bag.

### Fixed

- Account for the Smithing cape's increased coal bag capacity ([#1](https://github.com/WolffTech/coal-bag-plugin/issues/1)).
- Display the correct amount after emptying the bag.

## 1.0 - 2021-03-05

### Added

- Initial release.
