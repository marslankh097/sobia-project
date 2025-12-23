# Jetpack Compose Presentation - PowerPoint Template

This document can be used to create a PowerPoint presentation. You can:
1. Copy content into PowerPoint slides
2. Use a tool like Pandoc to convert to PPTX
3. Import into Google Slides

---

## Slide Design Recommendations

- **Color Scheme:** Use Material Design colors (Primary: #6200EE, Secondary: #03DAC6)
- **Font:** Roboto or similar sans-serif
- **Layout:** Clean, minimal, with plenty of white space
- **Images:** Use screenshots from the demo screens

---

## Slide 1: Title Slide

**Background:** Gradient or solid color
**Layout:** Centered

```
┌─────────────────────────────────────┐
│                                     │
│     Jetpack Compose                 │
│     Fundamentals                    │
│                                     │
│     Practical Examples              │
│     and Concepts                    │
│                                     │
│     [Your Name]                    │
│     [Date]                          │
│                                     │
└─────────────────────────────────────┘
```

---

## Slide 2: Agenda

**Title:** What We'll Cover Today

**Content:**
- Side-effects in Compose
- Modifiers
- Lists
- Text Input
- Images & Icons
- State Hoisting 
- Theming

**Design:** Bullet points with icons

---

## Section 1: Side-effects

### Slide 3: Side-effects Overview

**Title:** Side-effects in Jetpack Compose

**Content:**
- What are side-effects?
- Why do we need them?
- Types of side-effects:
  - DisposableEffect
  - produceState
  - derivedStateOf
  - snapshotFlow

**Visual:** Icon or diagram showing side-effects concept

---

### Slide 4: DisposableEffect

**Title:** DisposableEffect

**Content:**
- Cleans up resources when composable leaves composition
- Use for: Lifecycle observers, subscriptions, cleanup
- Syntax: `DisposableEffect(key) { onDispose { } }`

**Code Example:**
```kotlin
DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            count++
        }
    }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose {
        lifecycleOwner.lifecycle.removeObserver(observer)
    }
}
```

**Visual:** Screenshot of DisposableEffect demo screen

---

### Slide 5: produceState

**Title:** produceState

**Content:**
- Converts non-Compose state into Compose state
- Use for: Loading data, async operations
- Automatically manages lifecycle

**Code Example:**
```kotlin
val result by produceState(initialValue = "Loading...") {
    delay(2000)
    value = "Data loaded!"
}
```

**Visual:** Screenshot showing loading state transition

---

### Slide 6: derivedStateOf

**Title:** derivedStateOf

**Content:**
- Derives state from other state values
- Only recomposes when dependencies change
- More efficient than recalculating in body

**Code Example:**
```kotlin
val doubledValue by remember {
    derivedStateOf { inputValue * 2 }
}
```

**Visual:** Screenshot of counter with doubled value

---

### Slide 7: snapshotFlow

**Title:** snapshotFlow

**Content:**
- Converts Compose state to Flow
- Use with Flow operators
- Bridge between Compose and Flow

**Code Example:**
```kotlin
val flow = snapshotFlow { state }
flow.collect { value ->
    // Handle value
}
```

**Visual:** Diagram showing state → Flow conversion

---

## Section 2: Modifiers

### Slide 8: Modifiers Overview

**Title:** Modifiers in Compose

**Content:**
- What are modifiers?
- How do they work?
- Key concepts:
  - Order matters
  - Chaining
  - Composed modifiers

**Visual:** Diagram showing modifier application

---

### Slide 9: Modifier Order

**Title:** Modifier Order Matters!

**Content:**
- Modifiers are applied in order
- Different order = Different result
- Example: padding → background → border

**Visual:** 
- Side-by-side comparison
- Box 1: padding → background → border
- Box 2: background → padding → border
- Show the difference

**Screenshot:** From ModifierDemoScreen

---

### Slide 10: Modifier Chaining

**Title:** Modifier Chaining

**Content:**
- Chain multiple modifiers together
- Each modifier transforms the previous one
- Read from left to right

**Code Example:**
```kotlin
Modifier
    .fillMaxWidth()
    .height(56.dp)
    .padding(8.dp)
    .clickable { }
```

**Visual:** Screenshot of button with chained modifiers

---

### Slide 11: Composed Modifier

**Title:** Composed Modifier

**Content:**
- Reusable modifier combinations
- Create custom modifier functions
- DRY principle

**Code Example:**
```kotlin
fun cardModifier() = Modifier
    .fillMaxWidth()
    .background(Color.Gray, RoundedCornerShape(12.dp))
    .padding(8.dp)
```

**Visual:** Screenshot showing two boxes with same modifier

---

## Section 3: Lists

### Slide 12: Lists Overview

**Title:** Lists in Compose

**Content:**
- Why Lazy lists?
- Performance benefits
- Types:
  - LazyColumn
  - LazyRow
  - LazyVerticalGrid
  - LazyPagingItems

**Visual:** Icons for each list type

---

### Slide 13: LazyColumn

**Title:** LazyColumn

**Content:**
- Vertical scrolling list
- Only composes visible items
- Efficient for long lists

**Code Example:**
```kotlin
LazyColumn {
    items(items) { item ->
        ItemCard(item = item)
    }
}
```

**Visual:** Screenshot of LazyColumn demo

---

### Slide 14: LazyRow

**Title:** LazyRow

**Content:**
- Horizontal scrolling list
- Perfect for horizontal item lists
- Same lazy loading benefits

**Visual:** Screenshot of LazyRow demo

---

### Slide 15: LazyVerticalGrid

**Title:** LazyVerticalGrid

**Content:**
- Grid layout for items
- Configurable columns
- Efficient grid rendering

**Code Example:**
```kotlin
LazyVerticalGrid(
    columns = GridCells.Fixed(2)
) {
    items(items) { item ->
        GridItem(item = item)
    }
}
```

**Visual:** Screenshot of grid layout

---

### Slide 16: LazyPagingItems

**Title:** LazyPagingItems

**Content:**
- Works with Paging 3 library
- Automatic pagination
- Loading states and error handling

**Visual:** Diagram showing pagination flow

---

## Section 4: Text Input

### Slide 17: Text Input Overview

**Title:** Text Input in Compose

**Content:**
- Two main components:
  - TextField
  - OutlinedTextField
- When to use which?
- Common use cases

**Visual:** Side-by-side comparison

---

### Slide 18: TextField

**Title:** TextField

**Content:**
- Standard text input with filled background
- Modern Material Design 3 style
- Use for: Forms, inputs with filled style

**Visual:** Screenshot of TextField

---

### Slide 19: OutlinedTextField

**Title:** OutlinedTextField

**Content:**
- Text input with outlined border
- More traditional style
- Use for: Forms, inputs with outlined style

**Visual:** Screenshot of OutlinedTextField

---

### Slide 20: Text Input Features

**Title:** Text Input Features

**Content:**
- Password fields (PasswordVisualTransformation)
- Number input (KeyboardType.Number)
- Validation and formatting

**Visual:** Screenshots of password and number inputs

---

## Section 5: Images & Icons

### Slide 21: Images & Icons Overview

**Title:** Images & Icons in Compose

**Content:**
- Icon component for vector drawables
- Image/AsyncImage for photos
- Coil library for loading images

**Visual:** Icons and images side-by-side

---

### Slide 22: Icon Component

**Title:** Icon Component

**Content:**
- Material Icons library
- Vector drawables
- Customizable size and color

**Visual:** Grid of different icons

---

### Slide 23: Image with Coil

**Title:** Image Loading with Coil

**Content:**
- Best library for loading images from URLs
- Automatic caching
- Placeholder and error handling

**Code Example:**
```kotlin
AsyncImage(
    model = "https://example.com/image.jpg",
    contentDescription = "Image"
)
```

**Visual:** Screenshot of AsyncImage

---

### Slide 24: Image Shapes

**Title:** Image Shapes

**Content:**
- Circular images
- Rounded corners
- ContentScale options

**Visual:** Examples of different image shapes

---

## Section 6: State Hoisting

### Slide 25: State Hoisting Overview

**Title:** State Hoisting in Compose

**Content:**
- What is state hoisting?
- Why is it important?
- Benefits

**Visual:** Diagram showing state flow

---

### Slide 26: State Hoisting Pattern

**Title:** State Hoisting Pattern

**Content:**
- Move state up to common ancestor
- Pass state down as parameters
- Pass callbacks for state changes

**Visual:** Diagram showing parent-child relationship

---

### Slide 27: Benefits of State Hoisting

**Title:** Benefits

**Content:**
- Reusable components
- Single source of truth
- Easier testing
- Better state management

**Visual:** Comparison: with vs without hoisting

---

### Slide 28: State Hoisting Example

**Title:** Example: Counter Component

**Content:**
- Stateless child component
- State managed by parent
- Callbacks for state changes

**Code Example:**
```kotlin
@Composable
fun CounterDisplay(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
)
```

**Visual:** Screenshot of counter demo

---

## Section 7: Theming

### Slide 29: Theming Overview

**Title:** Theming in Compose

**Content:**
- Material Theme system
- Custom themes
- Components:
  - Color
  - Typography
  - Shape
  - Dimens

**Visual:** Theme system diagram

---

### Slide 30: Material Theme Colors

**Title:** Material Theme Colors

**Content:**
- Primary, Secondary, Tertiary
- Error, Surface, Background
- Access via MaterialTheme.colorScheme

**Visual:** Color palette

---

### Slide 31: Typography

**Title:** Typography System

**Content:**
- Predefined text styles
- Display, Headline, Title, Body, Label
- Consistent typography across app

**Visual:** Typography scale

---

### Slide 32: Shapes

**Title:** Shape System

**Content:**
- Small, Medium, Large shapes
- Custom shapes
- Rounded corners, circles

**Visual:** Examples of different shapes

---

### Slide 33: Custom Theme

**Title:** Custom Theme

**Content:**
- Custom Color Schemes
- Custom Typography
- Custom Shapes
- Theme Factories

**Visual:** Custom theme example

---

### Slide 34: Using MaterialTheme

**Title:** Using MaterialTheme

**Content:**
- Access theme values
- MaterialTheme.colorScheme
- MaterialTheme.typography
- MaterialTheme.shapes

**Code Example:**
```kotlin
Text(
    text = "Hello",
    color = MaterialTheme.colorScheme.primary,
    style = MaterialTheme.typography.headlineLarge
)
```

---

## Slide 35: Summary

**Title:** Key Takeaways

**Content:**
- Side-effects for lifecycle and async operations
- Modifiers: order matters, chain them, compose reusable ones
- Lazy lists for performance
- TextField vs OutlinedTextField
- Icons for UI, Images for content
- State hoisting for reusable components
- Material Theme for consistent design

**Design:** Bullet points with icons

---

## Slide 36: Q&A

**Title:** Questions?

**Content:**
- Thank you!
- Questions and discussion

**Design:** Simple, clean slide

---

## Slide 37: Resources

**Title:** Additional Resources

**Content:**
- Jetpack Compose Documentation
- Material Design 3
- Compose Samples
- Code examples in this project

**Design:** Links or QR codes

---

## PowerPoint Creation Tips

1. **Use Consistent Design**
   - Same color scheme throughout
   - Same font family
   - Consistent spacing

2. **Add Screenshots**
   - Take screenshots of each demo screen
   - Add them to relevant slides
   - Annotate important parts

3. **Code Formatting**
   - Use monospace font for code
   - Add syntax highlighting if possible
   - Keep code examples short and focused

4. **Animations (Optional)**
   - Use subtle transitions
   - Don't overdo it
   - Focus on content, not effects

5. **Speaker Notes**
   - Add notes for each slide
   - Reference the guidance document
   - Include timing information

---

## Converting to PowerPoint

### Option 1: Manual Creation
1. Open PowerPoint
2. Create new presentation
3. Copy content from this document
4. Add screenshots from demo screens
5. Apply consistent design

### Option 2: Using Pandoc
```bash
pandoc PowerPoint_Template.md -o Presentation.pptx
```

### Option 3: Google Slides
1. Import this markdown
2. Convert to slides
3. Add screenshots
4. Customize design

---

Good luck with your presentation! 🎯

