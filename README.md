# tank-software-design

## Layers

| Layer | Classes |
| --- | --- |
| Domain (entities) | `Level`, `Tank`, `Bullet`, `Tree`, `Movement`, `Health`, `Direction`, `GameObject`, `Moving`, `Movable`, `Obstacle`, `Shooter`, `Combatant`, `HealthHolder`, `Field`, `BulletRegistry`, `TankState` with its states, `TankStates`, `HealthStates` |
| Application (use cases) | `Command`, `MoveCommand`, `ShootCommand`, `ToggleCommand`, `CommandProvider`, `InputController`, `AiCommandProvider`, `TankBehaviour`, `RandomTankBehaviour`, `BoundedLevelSource`, `RandomLevelSource` |
| Infrastructure (adapters) | `GameDesktopLauncher`, `GdxKeyboard`, `LevelRenderer`, `LevelGraphics`, `GameObjectGraphics`, `TextureGraphicsFactory`, `HealthBarGraphics`, `HealthBarDecorator`, `FileLevelSource`, `TileMovement`, `GdxGameUtils` |

## Ports and adapters

| Port | Direction | Adapters |
| --- | --- | --- |
| `Command`, `CommandProvider` | driving | `GameDesktopLauncher` |
| `Keyboard` | driven | `GdxKeyboard` |
| `LevelSource` | driven | `FileLevelSource`, `RandomLevelSource`, `BoundedLevelSource` |
| `LevelListener` | driven | `LevelGraphics`, `AiCommandProvider` |
| `Switch` | driven | `LevelGraphics::toggleHealthBars` |
| `Renderer`, `Renderable`, `GraphicsFactory`, `RenderableDecorator` | driven | `LevelRenderer`, `GameObjectGraphics`, `TextureGraphicsFactory`, `HealthBarGraphics`, `HealthBarDecorator` |

## Ports which are missing

- coordinates of our own: the entities speak `com.badlogic.gdx.math.GridPoint2`
- a drawing port which does not speak libGDX: `Renderable.render(Batch, TileMovement)`, `Disposable`
- a clock port: `GameDesktopLauncher` takes the time from `Gdx.graphics.getDeltaTime()` and runs the loop itself
- a port for reading the layout: `FileLevelSource` both reads the resource and parses it
- a port for the end of the game: nobody is told when the player's tank is destroyed
- packages are named after technology, not after the layers
