<!-- markdownlint-disable -->
<p align="center">
<img src="./readmeResources/PixelMKMenuLogo.png" alt="Logo" width=100%>
</p>

<!-- markdownlint-enable -->
[![Javadoc](https://javadoc.io/badge2/io.github.apolo49.pixelmk/pixelmkmenu-1.18.2/javadoc.svg?style=for-the-badge)](https://javadoc.io/doc/io.github.apolo49.pixelmk/pixelmkmenu-1.18.2)
![Forge Modloader](https://img.shields.io/badge/mod%20loader-forge-1976d2?style=for-the-badge)
![Client Only](https://img.shields.io/badge/environment-client-1976d2?style=for-the-badge)
[![Patreon](https://img.shields.io/endpoint.svg?url=https%3A%2F%2Fshieldsio-patreon.vercel.app%2Fapi%3Fusername%3Dapolo49%26type%3Dpatrons&style=for-the-badge)](https://shieldsio-patreon.vercel.app/api/?username=apolo49&type=patrons)
[![Available for Minecraft 1.18](https://img.shields.io/badge/Available%20for-MC%201.18-c70039?style=for-the-badge)](https://www.curseforge.com/minecraft/mc-mods/pixel-mk-menu)
[![GitHub Sponsors](https://img.shields.io/github/sponsors/apolo49?color=red&logo=github&style=for-the-badge)](https://github.com/sponsors/apolo49)
[![Lines of code](https://img.shields.io/tokei/lines/github/apolo49/Pixel-MK-Menu?logo=github&style=for-the-badge)](https://github.com/apolo49/Pixel-MK-Menu)
<!-- markdownlint-disable MD002 -->

## Executive Summary

Pixel MK Menu is a client side GUI mod for minecraft. It changes the main menu,
buttons, the pause menu and other miscellaneous GUIs.

## Table of Contents

- [Executive Summary](#executive-summary)
- [Table of Contents](#table-of-contents)
- [Introduction](#introduction)
- [Changes](#changes)
  - [Main Menu](#main-menu)
    - [Default Menu](#default-menu)
    - [With Create and Quark](#with-create-and-quark)
  - [Pause Menu](#pause-menu)
- [Adding Buttons](#adding-buttons)
- [Credits](#credits)
- [License](#license)

## Introduction

The Minecraft main menu and other menus aren't that modern, sleek or beautiful.
We can see due to the popularity of menu editor mods and GUI redesign mods and
other mod GUIs that the menus leave a lot to be desired. This desire to change the
menu and the soon-to-be-released Pixel MK 4 modpack leaves a hole to be filled
that was filled with Pixel MK 1 and 2's Voxel Menu mod that was brought over from
the Voxel Modpack.

This mod serves as a port/successor to that mod, redesigning the menu and,
bringing in modern mod support as well as support for Forge.

## Changes

Pixel MK Menu currently only changes the Main Menu, Create's main menu
(if Create is loaded) and the Pause Menu.

### Main Menu

The Main menu has up to 10 buttons, when both Create and Quark are
enabled/installed alongside Pixel MK Menu, normally this count is only 8.

#### Default Menu

![Image of main menu with Pixel MK Menu](/readmeResources/MainMenu.png "Main Menu")

As you can see the main menu looks much more sleek and polished. This modernity
directly reflects the modpack as a whole and is translated into the modpack using
resourcepacks and others. For anyone who doesn't like the menu it can be removed
without consequence.

One of the new buttons on the menu is a mute button to mute menu music entirely,
the menu music can also be muted using the config to restore the original menu music.

It is also possible to add a modpack definition in the top-left corner to bring
a little extra touch to the menu using a JSON file, this will eventually also be
extended into an entire button and dynamic screen.

#### With Create and Quark

![Image of main menu with Pixel MK Menu installed alongside Quark and Create](/readmeResources/MainMenuQuarkCreate.png "Main Menu with Create and Quark")

The Buttons added with Quark and Create are built into the mod but any other buttons
will have to be added via determination from other mod authors unless requested
and approved through the issue tracker.

The way to add this is documented [here](#adding-buttons)

### Pause Menu

![Pause Menu](/readmeResources/PauseMenu.png "Pause Menu")

The pause menu has been redesigned to take up a smaller space of the screen and
no longer dims the background meaning that it is easier to see you surroundings
when online.
The pause menu also has animations associated with it.

## Adding Buttons

To add a button it is as simple as catching an event (`AddModButtonsEvent` event)
on the Forge Event Bus.

The file for this event can be found at [AddModButtonsEvent.java](/src/main/java/io/github/pixelmk/pixelmkmenu/event/AddModButtonsEvent.java).

The code used would be something along the lines of

```java
import com.pixelmk.pixelmkmenu.event.AddModButtonsEvent;
import net.minecraft.client.gui.components.Button;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

// ...

public void init() {
    // ...
    MinecraftForge.EVENT_BUS.addListener(this::addButton);
    // ...
}

@SubscribeEvent
public void addButton(AddModButtonsEvent event) {
    event.createButton("examplemod.usingStringButton",
        $ -> {LOGGER.info("This button was created using a string")});
    event.createButton(new TranslatableComponent("examplemod.usingComponentButton"),
        $ -> {LOGGER.info("This button was created using a component")});
    event.addButton(new Button("examplemod.usingMCButton"),
        $ -> {LOGGER.info("This button was created using a Minecraft Button")});
}
```

## Credits

- Programming - Joe Targett of the Pixel MK team

- Some code lifted and edited from
[Pack Menu](https://github.com/Shadows-of-Fire/PackMenu) and [Somnia Awoken](https://github.com/Su5eD/Somnia-Awoken).

- Music by eXtaticus, Kuraishin, ph00tbag, and FelixMoog.

- Art by Joe Targett

- Idea by xIGBClutchIx, Mumfrey and the rest of the [VoxelModPack Team](http://voxelmodpack.com/team.html).

## License

MIT License

Copyright (c) 2022 Joe Targett

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
