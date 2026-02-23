<!--publish=true-->
## 2.2.0:
- `1.21.5`, `1.21.6(-11)`: Update to <ins>1.21.5</ins>~<ins>1.21.11</ins>
#### <ins>Added</ins>:
- Implement _Mooshrooms_ and _Cows_ eating plant blocks (e.g. _Short Grass_) like _Sheep_.  
  This is controlled by _Block Tags_, that by default contain the following items:
  - `#hungrycows:edible_for_cows`:
    - _Wheat_
    - _Short Grass_
    > `1.21.5+` or when _Vanilla Backport_'s installed:
    >    - `#hungrycows:edible_for_cold_cows`:  
<sup>`#hungrycows:edible_for_cows`, _Fern_</sup>
    >    - `#hungrycows:edible_for_temperate_cows`:  
<sup>`#hungrycows:edible_for_cows`, _Fern_</sup>
    >    - `#hungrycows:edible_for_warm_cows`:  
<sup>`#hungrycows:edible_for_cows`, _Short Dry Grass_, _Tall Dry Grass_</sup>
  - `#hungrycows:edible_for_mooshrooms`:
    - _Short Grass_
    - _Fern_
  - `#minecraft:small_flowers`   
    > (For _Brown Mooshrooms_,  
    their _Stew_ becomes _Suspicious_ based on the flower eaten.)
  - `#hungrycows:edible_for_sheep`:
    - _Short Grass_
    - _Fern_
    - `#minecraft:edible_for_sheep` (`1.21.5+`):  
      <sup>_Short Grass_, _Short Dry Grass_, _Tall Dry Grass_, _Fern_</sup>
- Add eating logic for _Mooshrooms_ to not eat _Mycelium_ blocks that have a _Mushroom_ on top of them
  > This is to prevent _Mooshrooms_ from removing all _Mushrooms_ in their habitat as eating _Mycelium_ blocks breaks any _Mushrooms_ on top.
- Add new <ins>Jade</ins> (&#x200A;<sub><a title="Jade&#10;on Modrinth" href="https://modrinth.com/mod/jade/"><img width=20 src="https://img.shields.io/badge/-%20-%23032a?style=flat&logo=modrinth"></a></sub><!--SEPARATOR_V--><img width=5 height=8 src="https://raw.githubusercontent.com/pnk2u/resources/main/ModProjects/shared/pres/icon/separator_v.svg"><sub><a title="Jade&#10;on Curseforge" href="https://www.curseforge.com/minecraft/mc-mods/jade/"><img width=20 src="https://img.shields.io/badge/--%23302a?style=flat&logo=curseforge"></a></sub>&#x200A;) tooltips:
  - _Blocks_: Showing if they are edible for _Cows_, _Mooshrooms_ and/or _Sheep_
  - _Brown Mooshrooms_: Showing the flower they have consumed (relevant for _Suspicious Stew_ effects)

- Implement eating sound and subtitles for _Cows_ and _Sheep_ eating _Grass Blocks_
- Add compatibility with <ins>Vanilla Backport</ins> (&#x200A;<sub><a title="Vanilla Backport&#10;on Modrinth" href="https://modrinth.com/mod/vanilla-backport/"><img width=20 src="https://img.shields.io/badge/-%20-%23032a?style=flat&logo=modrinth"></a></sub><!--SEPARATOR_V--><img width=5 height=8 src="https://raw.githubusercontent.com/pnk2u/resources/main/ModProjects/shared/pres/icon/separator_v.svg"><sub><a title="Vanilla Backport&#10;on Curseforge" href="https://www.curseforge.com/minecraft/mc-mods/vanilla-backport/"><img width=20 src="https://img.shields.io/badge/--%23302a?style=flat&logo=curseforge"></a></sub>&#x200A;)'s _Cow_ variants
#### <ins>Fixed</ins>:
- Update <ins>Fresh Animations</ins> (&#x200A;<sub><a title="Fresh Animations&#10;on Modrinth" href="https://modrinth.com/resourcepack/fresh-animations/"><img width=20 src="https://img.shields.io/badge/-%20-%23032a?style=flat&logo=modrinth"></a></sub><!--SEPARATOR_V--><img width=5 height=8 src="https://raw.githubusercontent.com/pnk2u/resources/main/ModProjects/shared/pres/icon/separator_v.svg"><sub><a title="Fresh Animations&#10;on Curseforge" href="https://www.curseforge.com/minecraft/texture-packs/fresh-animations/"><img width=20 src="https://img.shields.io/badge/--%23302a?style=flat&logo=curseforge"></a></sub>&#x200A;) compatibility resourcepack "_Hungry & Fresh Cows_" to latest version `1.10.3` (previously `1.9.2`) fixing many visual bugs found using the incompatible versions
  - _Cows_/_Mooshrooms_ eating Blocks now use <ins>FA</ins>'s own eating animation instead of the modified Vanilla eating animation (originally from _Sheep_)
  > **_Note_**:   
  > From now on, the compatibility resourcepack will have the version of the Fresh Animations version it has been made compatible with in its name and description, like so:
  > ![screenshot of the resourcepack's name and description in the resourcepack selection screen](https://uwu.catgirl.host/i/5ugmm.png)  
  > That does not necessarily mean that the resourcepack will not work with newer or older versions but if you're encountering any issues, use the Fresh Animations version specified until I update the compatibility resourcepack.  
  > I would also appreciate it a lot if you could report any issues you encounter with newer versions of <ins>Fresh Animations</ins> so I can update the compatibility resourcepack as soon as possible.  
  > You can report issues either on [GitHub](https://www.github.com/pnk2u/hungrycows/issues), in the [Discord](https://discord.lieonlion.dev) or via [E-Mail](mailto:contact@pnku.de).
- Fix "`Show Neither`" option (to disable _Milkability_ texture) forcing the Vanilla _Cow_/_Mooshroom_ texture instead of letting resourcepacks/mods overwrite Vanilla textures as intended
- Fix incorrect subtitles:
  - _Cows_ and _Sheep_ being fed to restore _Milkability_ and health
  - _Mooshrooms_ being milked with a _Bucket_