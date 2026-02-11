<a name="2.1.5"></a>
### 2.1.5:
- `1.20.1`, `1.21(.1)`: Fix animation bug for modded _Cows_ without explicit compatibility (e.g. _Friends and Foes' Mooblooms_)

  
---
<a name="2.1.4"></a>
### 2.1.4:
- `1.21(.1)`, `1.21.4`: 
  - <ins>Bovines and Buttercups</ins> (&#x200A;<sub><a title="Bovines and Buttercups&#10;on Modrinth" href="https://modrinth.com/mod/bovines-and-buttercups/"><img width=20 src="https://img.shields.io/badge/-%20-%23032a?style=flat&logo=modrinth"></a></sub><!--SEPARATOR_V--><img width=5 height=8 src="https://raw.githubusercontent.com/pnk2u/resources/main/ModProjects/shared/pres/icon/separator_v.svg"><sub><a title="Bovines and Buttercups&#10;on Curseforge" href="https://www.curseforge.com/minecraft/mc-mods/bovines-and-buttercups/"><img width=20 src="https://img.shields.io/badge/--%23302a?style=flat&logo=curseforge"></a></sub>&#x200A;) (Mooblooms) Compatibility:
    - Update to `2.2.0+` (incl. new textures and added/removed _Moobloom_ types)
    - Fix the _Sombercup Moobloom_ using the extended animated skulk texture even when not milkable.
- `1.21(.1)`, `1.21.4`: Fix "edible" _Milk_ not being able to fill the crafting recipe upon clicking a crafting recipe like _Cake_
- Fix game crash when starting first a new instance or just without an options file
- `1.20.1`, `1.21(.1)`: Fix _Cow_ head being two pixels/voxels too low (was flat with the body, should be two above)
- Fix <ins>Jade</ins> (&#x200A;<sub><a title="Jade&#10;on Modrinth" href="https://modrinth.com/mod/jade/"><img width=20 src="https://img.shields.io/badge/-%20-%23032a?style=flat&logo=modrinth"></a></sub><!--SEPARATOR_V--><img width=5 height=8 src="https://raw.githubusercontent.com/pnk2u/resources/main/ModProjects/shared/pres/icon/separator_v.svg"><sub><a title="Jade&#10;on Curseforge" href="https://www.curseforge.com/minecraft/mc-mods/jade/"><img width=20 src="https://img.shields.io/badge/--%23302a?style=flat&logo=curseforge"></a></sub>&#x200A;) tooltips (feeding items) not rendering correctly

  
---
<a name="2.1.2"></a>
### 2.1.2:
- `1.21.4`: Fixes crash on startup (`2.1.1.a`)

  
---
<a name="2.1.1.a"></a>
#### 2.1.1.a:
- Potentially fix crash on start-up

  
---
<a name="2.1.1"></a>
### 2.1.1:
- Fix crash when running in a server environment


  
---
<a name="2.1.0"></a>
## 2.1.0:
- Add compatibility for:
  ### • [Fresh Animations](https://modrinth.com/resourcepack/fresh-animations):
  - Cows/Mooshrooms now eat only when they actually attempt to eat and no longer as an idle animation,  
    the same as Sheep
  - The milkable model and its texture are now no longer broken and can be used in conjunction with Fresh Animations
    - To achieve this, there now is a _built-in compatibility resource pack_ "**Hungry** & **Fresh** Cows" which gets added by default if Fresh Animations was one of the selected Resource Packs on game start-up
    - If you select Fresh Animations only after having started up the game, remember to add the "**Hungry** & **Fresh** Cows" pack on top as can be seen here:
      ![](https://uwu.catgirl.host/i/f2x4y.png)
  ### • [Bovines and Buttercups (Mooblooms)](https://modrinth.com/mod/bovines-and-buttercups/)<sup>1.21+</sup>:
  - Mooblooms have all Cow features applied to them
    - That includes custom Milkable textures for each Moobloom having darker and more saturated udder colors if they can be milked ![](https://uwu.catgirl.host/i/aeerg.png) <br> <sup>Left: Two Milkable Mooblooms, Right: A not yet Milkable Moobloom</sup>
  - Nectar Bowls received from Mooblooms now stack up to 16 (only applies to newly milked Nectar Bowls, does not apply retroactively)

  ![](https://uwu.catgirl.host/i/0gun7.png)
- If animals affected by this mod are fed to regain Milk/Wool/Health they now display appropriate particles, same goes for them being milked or eating Grass/Mycelium
- Milkable Cows/Mooshrooms now have a slightly more noticeable udder color in the custom texture
- Added Brazilian Portuguese translation (Thanks to @demorogabrtz)
- Bug fixes:
  - Fix Sheep being able to be fed any Item
  - Fix baby Cows/Mooshrooms having their whole head disappear in the ground while eating
  - Fix "fake" extra Bucket appearing after drinking the last Milk Bucket of a stack, actual Bucket count wasn't affected however, more of a visual bug

  
---
<a name="2.0.3"></a>
### 2.0.3:
- `1.21(.1)`: Fix incompatibility with mods affecting milk drinking behavior

  
---
<a name="2.0.2"></a>
### 2.0.2:
- Remove development leftovers that broke the Lime Wool texture and Mip Map levels

  
---
<a name="2.0.1"></a>
### 2.0.1:
- Fixes crash on startup and other _Mooshroom_ related issues
- Add <ins>owo-lib</ins> (&#x200A;<sub><a title="owo-lib&#10;on Modrinth" href="https://modrinth.com/mod/owo-lib/"><img width=20 src="https://img.shields.io/badge/-%20-%23032a?style=flat&logo=modrinth"></a></sub><!--SEPARATOR_V--><img width=5 height=8 src="https://raw.githubusercontent.com/pnk2u/resources/main/ModProjects/shared/pres/icon/separator_v.svg"><sub><a title="owo-lib&#10;on Curseforge" href="https://www.curseforge.com/minecraft/mc-mods/owo-lib/"><img width=20 src="https://img.shields.io/badge/--%23302a?style=flat&logo=curseforge"></a></sub>&#x200A;) as Fabric required dependency
  
---
<a name="2.0.0"></a>
# 2.0.0:

## **Mooshrooms**
- Now share all _Cow_ features but eat **Mycelium** instead of grass.
- Can be milked for either _Milk_ or **Mushroom**/**Suspicious Stew**, one at a time.

## **Feedability**
- **Cows/Mooshrooms** can now be fed with **Wheat** (default)¹ to regain milkability, once every **5 minutes** (default)¹.
- **Goats** can now be milked for edible milk but don’t eat grass naturally - only when fed.
- **Sheep** can now be fed to regrow their wool, using the same 5-minute cooldown (default)¹.
- **Jade** integration: Displays cooldown & if a held item is feedable.
  *![](https://uwu.catgirl.host/i/aag5a.gif)*
- **¹Configurability:**
  - Feedable items for each animal can be configured via settings or tags (e.g. `#hungrycows:cow_feedable`).
  - Feeding cooldown is adjustable.

## **Milkability**
- **Mooshrooms & Goats** can now, too, be milked for edible milk.
- Milkable Cows/Mooshrooms now have distinct models/textures:
  - Slightly increased size and subtly darker/pinkish udder color when milkable.
    ![](https://uwu.catgirl.host/i/4dy9k.png)
- **Milk**:
  - **Three milk types** (Cow, Mooshroom, Goat) with separate nutrition & saturation values (configurable).
  - **Milk Buckets** now stack up to **16** (like empty buckets).
    ![](https://uwu.catgirl.host/i/sdhzc.png)

## **Healing**
- **Cows, Mooshrooms, Goats, and Sheep** can now be healed:
  - Feeding them from their feedable item list restores **1 heart (2 HP)** (like Horses with Wheat).
  - Eating Grass/Mycelium heals **½ heart (1 HP)** (configurable).

## **Automation/Redstone**
- **Dispenser milking**:
  - **Cows, Mooshrooms, and Goats** can now be milked via dispensers with empty buckets.
  - **Mooshrooms** can also be milked with empty **Bowls**.
    ![](https://uwu.catgirl.host/i/ylswg.png)

### **Note for 1.20.1 version:**
- **Still requires Serilum’s <ins>Nutritious Milk</ins> (&#x200A;<sub><a title="Nutritious Milk&#10;on Modrinth" href="https://modrinth.com/mod/nutritious-milk/"><img width=20 src="https://img.shields.io/badge/-%20-%23032a?style=flat&logo=modrinth"></a></sub><!--SEPARATOR_V--><img width=5 height=8 src="https://raw.githubusercontent.com/pnk2u/resources/main/ModProjects/shared/pres/icon/separator_v.svg"><sub><a title="Nutritious Milk&#10;on Curseforge" href="https://www.curseforge.com/minecraft/mc-mods/nutritious-milk/"><img width=20 src="https://img.shields.io/badge/--%23302a?style=flat&logo=curseforge"></a></sub>&#x200A;)**
  - All other new features are fully available.
- Adding feedable items to their list requires a world reload to affect animal following behavior, but feeding still works instantly.

### **Config Changes:**
- Switch to <ins>owo-lib</ins> (&#x200A;<sub><a title="owo-lib&#10;on Modrinth" href="https://modrinth.com/mod/owo-lib/"><img width=20 src="https://img.shields.io/badge/-%20-%23032a?style=flat&logo=modrinth"></a></sub><!--SEPARATOR_V--><img width=5 height=8 src="https://raw.githubusercontent.com/pnk2u/resources/main/ModProjects/shared/pres/icon/separator_v.svg"><sub><a title="owo-lib&#10;on Curseforge" href="https://www.curseforge.com/minecraft/mc-mods/owo-lib/"><img width=20 src="https://img.shields.io/badge/--%23302a?style=flat&logo=curseforge"></a></sub>&#x200A;) (now required) for easier configuration
  ![](https://uwu.catgirl.host/i/7z9c9.webp)
- Implement automatic transfer of settings from older versions and adaptation into the new configuration file

  
---
<a name="1.4.3"></a>
### 1.4.3:
- `1.21.3`, `1.21.4`: Fix _Mooshrooms_ crashing the game

  (**Note**: The next update `1.5.0` will actually implement full _**Hungry** Mooshrooms_ functionality meaning _Mooshrooms_ will need to eat _Mycelium_ to replenish their "stew-giving" abilities.)

  
---
<a name="1.4.2"></a>
### 1.4.2:
- `1.21.4`: Update to <ins>1.21.4</ins>
- `1.21.2`, `1.21.3`: Update to <ins>1.21.2</ins>, <ins>1.21.3</ins>
- Add German localization

  
---
<a name="1.4.1"></a>
### 1.4.1:
- Fix incompatibility with <ins>Dynamic Crosshair</ins> (&#x200A;<sub><a title="Dynamic Crosshair&#10;on Modrinth" href="https://modrinth.com/mod/dynamiccrosshair/"><img width=20 src="https://img.shields.io/badge/-%20-%23032a?style=flat&logo=modrinth"></a></sub><!--SEPARATOR_V--><img width=5 height=8 src="https://raw.githubusercontent.com/pnk2u/resources/main/ModProjects/shared/pres/icon/separator_v.svg"><sub><a title="Dynamic Crosshair&#10;on Curseforge" href="https://www.curseforge.com/minecraft/mc-mods/dynamic-crosshair/"><img width=20 src="https://img.shields.io/badge/--%23302a?style=flat&logo=curseforge"></a></sub>&#x200A;)

  
---
<a name="1.4.0"></a>
## 1.4.0:
- Fix incompatibility with <ins>[Let's Do] Meadow</ins> (&#x200A;<sub><a title="[Let's Do] Meadow&#10;on Modrinth" href="https://modrinth.com/mod/lets-do-meadow/"><img width=20 src="https://img.shields.io/badge/-%20-%23032a?style=flat&logo=modrinth"></a></sub><!--SEPARATOR_V--><img width=5 height=8 src="https://raw.githubusercontent.com/pnk2u/resources/main/ModProjects/shared/pres/icon/separator_v.svg"><sub><a title="[Let's Do] Meadow&#10;on Curseforge" href="https://www.curseforge.com/minecraft/mc-mods/lets-do-meadow/"><img width=20 src="https://img.shields.io/badge/--%23302a?style=flat&logo=curseforge"></a></sub>&#x200A;)

  
---
<a name="1.3.0"></a>
## 1.3.0:
- Fix config tooltip not showing
- Add support for **<ins>1.20.1</ins>-<ins>1.20.4</ins>**
  >#####       **_Note:_**
  >In these versions (<ins>1.20.1</ins>, <ins>1.20.4</ins>), milk has no inherent hunger/saturation value, as that feature is based on <ins>1.20.5+</ins> functionality.  
  > As such, I recommend downloading Serilum's <ins>Nutritious Milk</ins> (&#x200A;<sub><a title="Nutritious Milk&#10;on Modrinth" href="https://modrinth.com/mod/nutritious-milk/"><img width=20 src="https://img.shields.io/badge/-%20-%23032a?style=flat&logo=modrinth"></a></sub><!--SEPARATOR_V--><img width=5 height=8 src="https://raw.githubusercontent.com/pnk2u/resources/main/ModProjects/shared/pres/icon/separator_v.svg"><sub><a title="Nutritious Milk&#10;on Curseforge" href="https://www.curseforge.com/minecraft/mc-mods/nutritious-milk/"><img width=20 src="https://img.shields.io/badge/--%23302a?style=flat&logo=curseforge"></a></sub>&#x200A;)-Mod on top of this one to enjoy all the intended features.
- `Internal`: Change mappings from Yarn to Mojmaps

---
<a name="1.2.0"></a>
## 1.2.0:
- `1.21`: Update to <ins>1.21</ins>
- Implement eating animations for _Cows_ (Thanks for the help, <ins>Qu</ins> (&#x200A;<sub><a title="Qu&#10;on Modrinth" href="https://modrinth.com/user/Quplet"><img width=20 src="https://img.shields.io/badge/-%20-%23032a?style=flat&logo=modrinth"></a></sub>&#x200A;)! ♡)

  
---
<a name="1.1.0"></a>
## 1.1.0:
- Add configurability through <ins>Mod Menu</ins> (&#x200A;<sub><a title="Mod Menu&#10;on Modrinth" href="https://modrinth.com/mod/modmenu/"><img width=20 src="https://img.shields.io/badge/-%20-%23032a?style=flat&logo=modrinth"></a></sub><!--SEPARATOR_V--><img width=5 height=8 src="https://raw.githubusercontent.com/pnk2u/resources/main/ModProjects/shared/pres/icon/separator_v.svg"><sub><a title="Mod Menu&#10;on Curseforge" href="https://www.curseforge.com/minecraft/mc-mods/modmenu/"><img width=20 src="https://img.shields.io/badge/--%23302a?style=flat&logo=curseforge"></a></sub>&#x200A;) integration
- Implement creating and saving settings to an editable `hungrycows.json` file located in the config folder

  
---
<a name="1.0.1"></a>
### 1.0.1:
- Fix a game-breaking bug that ocurred in `1.0.0`

  
---
<a name="1.0.0"></a>
# 1.0.0:
- Initial Release

  
---
