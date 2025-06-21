# BiomeAnnouncer

A Minecraft plugin that announces the biome players are in with animated titles and sound effects. Compatible with both vanilla Minecraft biomes and Terra biomes.

## Features
- Animated biome name announcements
- Configurable sound effects
- Support for both vanilla and Terra biomes
- Toggle command for players
- Customizable cooldown system
- Per-world configuration

## Fork changes
 - added support to Terralith / Incendium / Nullscape

## Later plans
 - change language to Polish


## Commands & Permissions
- `/togglebiomeannouncer` - Toggles biome announcements on/off
  - Permission: `biomeannouncer.toggle`

## Configuration
```yaml
# Animation settings
animation:
  typing-speed: 3
  color: "&b"
  sound:
    enabled: true
    volume: 0.3
    pitch: 2.0

# Cooldown settings
cooldown:
  duration: 3 # seconds

# Messages
messages:
  prefix: "&8[&aBiomeAnnouncer&8] "
  enabled: "&aEnabled biome announcements!"
  disabled: "&cDisabled biome announcements!"
  no-permission: "&cYou don't have permission to use this command!"

# Worlds where the plugin is enabled
enabled-worlds:
  - "world"
```

## Installation
1. Download the plugin JAR file
2. Place it in your server's `plugins` folder
3. Restart your server
4. Configure the plugin in `plugins/BiomeAnnouncer/config.yml`

## Support
For issues and feature requests use the GitHub Issues tab.

## License
This project is licensed under the MIT License - see the LICENSE file for details.
