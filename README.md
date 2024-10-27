# AngryBirds

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and a main class extending `Game` that sets the first screen.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
# Angry Birds-Inspired Game Project

This project is an Angry Birds-inspired game built with **LibGDX** in Java. It includes familiar characters like birds and pigs, various obstacles, and interactive levels where players use a slingshot to launch birds to destroy pigs and obstacles.

## Game Features
- **Birds**: Each bird has unique characteristics and images.
- **Pigs**: Different types of pigs as targets.
- **Obstacles**: Variety of obstacles with different orientations and sizes.
- **Slingshot Mechanic**: A basic slingshot functionality to launch birds.
- **Pause, Win, and Lose Screens**: Navigate through different game states with a pause, win, and lose screen.
  
## Screens
- **Level1 Screen**: Main gameplay screen where you play the game.
- **Pause Screen**: Pauses the game.
- **Win Screen**: Displays when you win the level.
- **Lose Screen**: Displays when you lose the level.

## How to Run the Project

### Prerequisites
1. **Java Development Kit (JDK)**: Make sure you have JDK 8 or later installed.
2. **LibGDX**: The project is built with LibGDX, which should be set up as a dependency.
3. **IDE (Optional)**: You can use IntelliJ IDEA, Eclipse, or any other Java IDE for better project management.

