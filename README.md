# 3D Labyrinth Ball Game - JavaFX Implementation

This repository contains the JavaFX implementation of a 3D labyrinth ball game, developed as part of a comparative study of 3D game development tools.

## About the Project

This is one of three implementations of the same 3D game, created to compare different game development technologies. The game is inspired by classic wooden labyrinth puzzles where players tilt a platform to guide a ball through obstacles to reach the goal.

This implementation serves as a reference point for students learning computer graphics, demonstrating 3D programming concepts using Java and JavaFX.

## Game Features

- **Platform Control**: Rotate the labyrinth using keyboard controls (limited to ±30 degrees)
- **Physics Simulation**: Custom ball physics responding to platform tilt
- **Interactive Camera**: 
  - Perspective camera with mouse controls (right-click and drag)
  - Orthographic top-down camera
  - Camera switching with 'C' key
  - Zoom with mouse wheel
- **Game Elements**:
  - Wooden textured platform with obstacles
  - Multiple holes (traps and goal)
  - Goal hole with golden rim
  - Custom collision detection
- **Visual Features**:
  - Multi-layered 3D scene structure
  - Textured materials
  - Lighting system
  - Win condition with fade-out text

## Technology Stack

- **Framework**: JavaFX
- **Language**: Java
- **3D Graphics**: JavaFX 3D API
- **Build Tool**: Gradle
- **IDE**: IntelliJ IDEA
- **Platform Support**: 8 platforms (Windows, Linux, macOS, embedded devices, web, Android, iOS, TV)

## Requirements

- Java 11 or higher
- JavaFX SDK
- Gradle (for building)

## How to Run

### Using Gradle
```bash
git clone <repository-url>
cd javafx-labyrinth-game
./gradlew run
```

### Building Executable
```bash
./gradlew jpackage
```

This creates a native installer (.msi on Windows) and standalone application.

## Controls

- **Arrow Keys / WASD**: Tilt the platform
- **Right Mouse Button + Drag**: Rotate perspective camera
- **Mouse Wheel**: Zoom in/out
- **C Key**: Switch between perspective and orthographic cameras

## Architecture

### Class Structure
- `Main`: Application entry point and scene setup
- `Level`: Main game logic coordinator and scene management
- `Arena`: Platform rotation and state management
- `Ball`: Ball physics and movement simulation
- `Obstacle`: Static obstacle collision detection
- `Hole`: Hole collision detection and win condition
- `Skybox`: Custom skybox implementation

### Helper Classes
- `StageController`: Singleton for scene management
- `KeyboardController`: Singleton for smooth keyboard input handling
- `Timer`: Animation timer management
- `Utility`: Common utility functions

### 3D Scene Hierarchy
The implementation uses a complex scene structure to handle skybox rendering:

```
Main 2D Scene
├── Background 3D Subscene (Skybox)
│   ├── Skybox (TriangleMesh)
│   ├── Perspective Camera A
│   ├── Parallel Camera A
│   └── Ambient Light
├── Foreground 3D Subscene (Game)
│   ├── Arena
│   │   ├── Platform
│   │   ├── Obstacles
│   │   ├── Holes
│   │   └── Ball
│   ├── Perspective Camera B
│   ├── Parallel Camera B
│   └── Point Light
└── Win Text (2D)
```

## Technical Challenges & Solutions

### Skybox Implementation
JavaFX doesn't support shaders, so the skybox is implemented using:
- Dual 3D subscenes (background and foreground)
- Custom `TriangleMesh` for skybox geometry
- Camera synchronization to prevent parallax effects

### Smooth Input Handling
- Custom `KeyboardController` using `HashMap` to track pressed keys
- Avoids frame rate dependent input issues
- Enables smooth platform rotation

### Z-Buffer Issues
- Objects slightly elevated above platform to prevent z-fighting
- Careful depth management in multi-scene setup

## Performance Metrics

According to the comparative study:
- **Development Time**: 31 engineer-hours
- **Lines of Code**: 812
- **Executable Size**: 84.3 MB
- **Memory Usage**: 143.8 MB
- **Project Size**: 3.05 MB (smallest)

## Limitations

- No shader support (no advanced lighting effects)
- No built-in shadows
- Complex skybox implementation due to API limitations
- Middle-level abstraction requires matrix math knowledge

## Related Repositories

This implementation is part of a larger comparative study. See also:
- [Bevy Implementation](../bevy_dissertation)
- [Unity Implementation](../unity_dissertation)
- [Main Repository with Thesis](../3d-game-development-comparison)

## Academic Context

This implementation was developed as part of a bachelor's thesis at the University of Belgrade - Faculty of Electrical Engineering, comparing three different approaches to 3D game development.

**Thesis**: "Comparison of Tools for 3D Video Game Development in JavaFX, Bevy, and Unity 3D Technologies"  
**Author**: Uroš Filipović  
**Mentor**: dr Igor Tartalja, v.prof.  
**Year**: 2022

This implementation specifically serves as educational material for computer graphics courses, demonstrating fundamental 3D programming concepts.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Survey Results

In user testing with 21 participants, this JavaFX implementation received:
- **Graphics Quality**: 5.95/10
- **Responsiveness**: 6.48/10  
- **Overall Rating**: 6.14/10

While scoring lower in visual appeal, this implementation provides valuable educational insights into 3D programming fundamentals and the challenges of working with limited graphics APIs.
