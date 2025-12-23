# Jetpack Compose Presentation Materials

This folder contains all materials for your Jetpack Compose presentation.

## Files

1. **Presentation_Outline.md** - Complete slide-by-slide outline for your PowerPoint presentation
2. **Presentation_Guidance.md** - Detailed guidance on what to say and when to demonstrate
3. **PowerPoint_Template.md** - Template for creating your PowerPoint slides
4. **README.md** - This file

## Quick Start

### 1. Create PowerPoint Presentation

- Open `PowerPoint_Template.md` or `Presentation_Outline.md`
- Copy content into PowerPoint slides
- Add screenshots from the demo screens
- Apply consistent design (Material Design colors recommended)

### 2. Prepare Demo Screens

- Build and run the project
- Navigate to `PresentationMainScreen` to see all demo topics
- Test each demo screen before presentation
- Take screenshots for your slides

### 3. Practice

- Review `Presentation_Guidance.md` for what to say on each slide
- Practice navigating between slides and demo screens
- Time yourself (aim for 60-75 minutes total)

## Accessing Demo Screens

### Option 1: Add Navigation Button

Add a button in your app (e.g., in MainHomeScreen or OnBoardingScreen) to navigate to the presentation:

```kotlin
Button(onClick = { 
    appNavigation.navigateToPresentationMainScreen() 
}) {
    Text("View Presentation Demos")
}
```

### Option 2: Direct Navigation

You can navigate directly to any demo screen:

```kotlin
// In your navigation code
appNavigation.navigateToSideEffectsDemo()
appNavigation.navigateToModifierDemo()
// etc.
```

### Option 3: Deep Link (Advanced)

You can set up deep links to launch specific screens directly.

## Demo Screens Available

1. **SideEffectsDemo** - DisposableEffect, produceState, derivedStateOf, snapshotFlow
2. **ModifierDemo** - Modifier Order, Chaining, Composed modifier
3. **ListDemo** - LazyColumn, LazyRow, LazyVerticalGrid, LazyPagingItems
4. **TextDemo** - TextField, OutlinedTextField
5. **ImageDemo** - Icon, Image with Coil
6. **StateHoistingDemo** - State hoisting pattern
7. **ThemingDemo** - Material Theme (Color, Typography, Shape), Custom Theme

## Presentation Structure

1. **Introduction** (5 min)
   - Title slide
   - Agenda

2. **Side-effects** (10-12 min)
   - 4 concepts with demos

3. **Modifiers** (8-10 min)
   - 3 concepts with demos

4. **Lists** (10-12 min)
   - 4 list types with demos

5. **Text Input** (6-8 min)
   - 2 components with demos

6. **Images & Icons** (6-8 min)
   - 2 concepts with demos

7. **State Hoisting** (8-10 min)
   - Pattern explanation with demos

8. **Theming** (10-12 min)
   - Material Theme components with demos

9. **Conclusion** (5 min)
   - Summary
   - Q&A

**Total Time:** ~60-75 minutes

## Tips

1. **Screenshots**: Take screenshots of each demo screen for your slides
2. **Interactivity**: Don't just show static screens - interact with the demos
3. **Code Examples**: Show code snippets when explaining concepts
4. **Questions**: Encourage questions throughout, not just at the end
5. **Practice**: Run through the entire presentation at least once before the actual presentation

## Troubleshooting

### Demo screens not showing?
- Make sure you've built the project
- Check that navigation routes are set up correctly
- Verify all imports are correct

### Images not loading?
- Check internet connection (Coil needs network for URL images)
- You can use local images as fallback

### Navigation issues?
- Verify Screens enum includes all presentation screens
- Check AppNavigation.kt has all routes
- Ensure AppNavigationActions has navigation functions

## After Presentation

1. Share the project code with your team
2. Share the presentation slides
3. Share this guidance document
4. Follow up on any questions

Good luck! 🚀

