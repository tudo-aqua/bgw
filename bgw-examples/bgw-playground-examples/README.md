# BGW development playground

This module is the visual regression playground for BGW development. Unlike the documentation
examples, every entry deliberately exposes live mutations, removal/reinsertion, and reset behavior.
Each component gets its own scene, so a broken renderer is easy to isolate.

Run it from PowerShell:

```powershell
.\gradlew.bat :bgw-examples:bgw-playground:run
```

When adding a renderable BGW component, add a `ComponentStory` in `ComponentStories.kt`. A story
must construct the component in a useful non-empty state. Add component-specific actions for state
that is not already covered by the shared move, resize, rotate, fade, visual, remove/add, and reset
controls.
