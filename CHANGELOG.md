<!--publish=false-->
### 2.2.1:
- Update <ins>Fresh Animations</ins> (&#x200A;<sub><a title="Fresh Animations&#10;on Modrinth" href="https://modrinth.com/resourcepack/fresh-animations/"><img width=20 src="https://img.shields.io/badge/-%20-%23032a?style=flat&logo=modrinth"></a></sub><!--SEPARATOR_V--><img width=5 height=8 src="https://raw.githubusercontent.com/pnk2u/resources/main/ModProjects/shared/pres/icon/separator_v.svg"><sub><a title="Fresh Animations&#10;on Curseforge" href="https://www.curseforge.com/minecraft/texture-packs/fresh-animations/"><img width=20 src="https://img.shields.io/badge/--%23302a?style=flat&logo=curseforge"></a></sub>&#x200A;) compatibility pack to `1.10.4`
- `1.20.1`:
  - Fixes crash due to incompatibility with Java 17
  - Fixes _Fresh Animations_ compatibility pack appearing incompatible 
    > ("Made for a newer version of Minecraft")
  - Fixes one of Goats' default feedable items (_Short Grass_) not being feedable
  - Fixes Goats not following the player when holding an item that can be used to feed a goat
    > **_Note_**:  
    This feature is restricted to Items in the `#hungrycows:goat_feedable` tag due to limitations specific to the Goat behavior logic in <ins>1.20.1</ins>.  
  This tag used to be empty by default to let the config take precedence, but due to the issues described above, it now includes the default feedable items: _Wheat_ and _Short Grass_.  
    You can still feed _Goats_ to restore _Milkability_ even with items set by the config, they simply won't follow you when holding them unless they are in the tag.

<br></br>
<sub>License update to [CC-BY-NC-SA 4.0](https://creativecommons.org/licenses/by-nc-sa/4.0/) (previously [MIT](https://opensource.org/licenses/MIT))</sub>