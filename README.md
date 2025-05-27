# Xime (MCGamer Club Core)

**Status: Archived / No longer maintained**  
This plugin was created for MCGamer Club, which is now retired & renamed to SGHQ
This plugin is no longer in use and is being open-sourced for learning purposes and preservation. It was developed for MCGamerClub, a revival of the original MCGamer Network.

---

## Overview

Xime is a 1:1 recreation of MCGamer’s proprietary plugin framework. It was designed to support extremely efficient virtualization of a single Minecraft server into multiple isolated instances. These instances ran concurrently inside the same JVM using Hypixel's Slime World Format.

This system allowed me to run over 40 virtual Minecraft servers at the same time, on a single Spigot instance, while maintaining 20.0 TPS and sub-5ms tick times. It was optimized to run on only 2 CPU cores and 12 GB of RAM, powering over **60+ concurrent players on average** during peak times.

---

## Key Features

- 1.7 and 1.8.8 client support
- Slime World Format for efficient world management
- Internal virtualization of Minecraft "instances" inside one server
- Custom `Serverable` class for easily creating and managing new game modes
- Fully modular and isolated systems for:
  - Chat
  - Nametags
  - Tablists
  - Scoreboards
  - Holograms
  - NPCs
  - Bossbars
- Multithreaded logic for key UI elements like sidebars and tablists
- Designed for performance and scalability without external containers or proxies

---

## Virtualized Server System

At the core of this plugin is the concept of **virtual servers**, which are isolated environments running inside a single Spigot server. Each of these virtual servers is backed by a `.slime` world file and controlled by a class that extends the custom `Serverable` base class.

### Serverable Class

`Serverable` acts as an abstraction layer for virtual servers. Each minigame or mode (e.g., Survival Games, Duels, SkyWars) extends this class to handle its game logic, players, events, and world lifecycle.

This approach allowed new game modes to be developed rapidly, often within a single day, due to the clean separation of logic and the reusable systems provided by Xime.

---

## Real-World Usage Stats (Production)

- 40+ virtual servers running concurrently  
- 60+ concurrent players (Tested with 200+ bots aswell)
- 2 CPU cores  
- 12 GB memory  
- 20.0 TPS, <5ms tick times

Each instance behaves like its own "virtual server" while sharing the same JVM, drastically reducing CPU and memory overhead compared to traditional server-per-game setups.

---

## Requirements

- Spigot or Paper 1.8.8 with ViaVersion
- Java 17+
- Slime World Manager-compatible `.slime` worlds
- At least 8 GB RAM (12 GB recommended for 40+ virtual servers)

---

## Getting Started

1. Place `Xime.jar` into your server’s `plugins` folder.
2. Convert your game maps into `.slime` format.
3. Create new game modes by extending the `Serverable` class.
45. Start developing fast and efficiently.

---

## License

This project is licensed under the MIT License. See `LICENSE` for more information.

---

## Final Notes

This plugin served as a cost-effective, high-performance alternative to spinning up multiple Spigot servers or running Docker containers. It acted as a virtualized Minecraft server environment within a single JVM and helped reduce infrastructure costs while keeping development fast and efficient.

If you find it useful, feel free to fork, star, or learn from it.

