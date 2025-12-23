# Jetpack Compose Presentation Outline

## Slide 1: Title Slide
**Title:** Jetpack Compose Fundamentals
**Subtitle:** Practical Examples and Concepts
**Presenter:** [Your Name]
**Date:** [Date]

---

## Slide 2: Agenda
- Side-effects in Compose
- Modifiers
- Lists
- Text Input
- Images & Icons
- State Hoisting
- Theming

---

## Section 1: Side-effects

### Slide 3: Side-effects Overview
**Title:** Side-effects in Jetpack Compose
**Content:**
- What are side-effects?
- Why do we need them?
- Types of side-effects

**Demo:** Show SideEffectsDemoScreen

---

### Slide 4: DisposableEffect
**Title:** DisposableEffect
**Content:**
- Cleans up resources when composable leaves composition
- Use for: Lifecycle observers, subscriptions, cleanup
- Syntax: `DisposableEffect(key) { onDispose { } }`

**Demo:** Show DisposableEffect example on screen
**Action:** Navigate to SideEffectsDemoScreen, point to DisposableEffect card

---

### Slide 5: produceState
**Title:** produceState
**Content:**
- Converts non-Compose state into Compose state
- Use for: Loading data, async operations
- Automatically manages lifecycle

**Demo:** Show produceState example (loading data after 2 seconds)
**Action:** Point to produceState card showing "Data loaded after 2 seconds!"

---

### Slide 6: derivedStateOf
**Title:** derivedStateOf
**Content:**
- Derives state from other state values
- Only recomposes when dependencies change
- More efficient than recalculating in body

**Demo:** Show derivedStateOf example (doubled value)
**Action:** Click increment button, show how doubled value updates

---

### Slide 7: snapshotFlow
**Title:** snapshotFlow
**Content:**
- Converts Compose state to Flow
- Use with Flow operators
- Bridge between Compose and Flow

**Demo:** Show snapshotFlow explanation
**Action:** Explain the concept with code example

---

## Section 2: Modifiers

### Slide 8: Modifiers Overview
**Title:** Modifiers in Compose
**Content:**
- What are modifiers?
- How do they work?
- Key concepts: Order, Chaining, Composed

**Demo:** Show ModifierDemoScreen

---

### Slide 9: Modifier Order
**Title:** Modifier Order Matters!
**Content:**
- Modifiers are applied in order
- Different order = Different result
- Example: padding → background → border

**Demo:** Show visual example of modifier order
**Action:** Point to the blue box with red border example

---

### Slide 10: Modifier Chaining
**Title:** Modifier Chaining
**Content:**
- Chain multiple modifiers together
- Each modifier transforms the previous one
- Read from left to right

**Demo:** Show chained modifiers example
**Action:** Show button with multiple modifiers

---

### Slide 11: Composed Modifier
**Title:** Composed Modifier
**Content:**
- Reusable modifier combinations
- Create custom modifier functions
- DRY principle

**Demo:** Show composed modifier example
**Action:** Show two boxes using the same composed modifier

---

## Section 3: Lists

### Slide 12: Lists Overview
**Title:** Lists in Compose
**Content:**
- Why Lazy lists?
- Performance benefits
- Types: LazyColumn, LazyRow, LazyVerticalGrid, LazyPagingItems

**Demo:** Show ListDemoScreen

---

### Slide 13: LazyColumn
**Title:** LazyColumn
**Content:**
- Vertical scrolling list
- Only composes visible items
- Efficient for long lists

**Demo:** Show LazyColumn with 50 items
**Action:** Scroll through the list, explain lazy loading

---

### Slide 14: LazyRow
**Title:** LazyRow
**Content:**
- Horizontal scrolling list
- Perfect for horizontal item lists
- Same lazy loading benefits

**Demo:** Show LazyRow example
**Action:** Scroll horizontally through items

---

### Slide 15: LazyVerticalGrid
**Title:** LazyVerticalGrid
**Content:**
- Grid layout for items
- Configurable columns
- Efficient grid rendering

**Demo:** Show LazyVerticalGrid with 2 columns
**Action:** Show grid layout with items

---

### Slide 16: LazyPagingItems
**Title:** LazyPagingItems
**Content:**
- Works with Paging 3 library
- Automatic pagination
- Loading states and error handling

**Demo:** Show LazyPagingItems explanation
**Action:** Explain concept and show code example

---

## Section 4: Text Input

### Slide 17: Text Input Overview
**Title:** Text Input in Compose
**Content:**
- Two main components: TextField and OutlinedTextField
- When to use which?
- Common use cases

**Demo:** Show TextDemoScreen

---

### Slide 18: TextField
**Title:** TextField
**Content:**
- Standard text input with filled background
- Modern Material Design 3 style
- Use for: Forms, inputs with filled style

**Demo:** Show TextField example
**Action:** Type in the TextField, show real-time updates

---

### Slide 19: OutlinedTextField
**Title:** OutlinedTextField
**Content:**
- Text input with outlined border
- More traditional style
- Use for: Forms, inputs with outlined style

**Demo:** Show OutlinedTextField example
**Action:** Type in the OutlinedTextField

---

### Slide 20: Text Input Features
**Title:** Text Input Features
**Content:**
- Password fields (PasswordVisualTransformation)
- Number input (KeyboardType.Number)
- Validation and formatting

**Demo:** Show password and number input examples

---

## Section 5: Images & Icons

### Slide 21: Images & Icons Overview
**Title:** Images & Icons in Compose
**Content:**
- Icon component for vector drawables
- Image/AsyncImage for photos
- Coil library for loading images

**Demo:** Show ImageDemoScreen

---

### Slide 22: Icon Component
**Title:** Icon Component
**Content:**
- Material Icons library
- Vector drawables
- Customizable size and color

**Demo:** Show various icons
**Action:** Point to different icons with different colors

---

### Slide 23: Image with Coil
**Title:** Image Loading with Coil
**Content:**
- Best library for loading images from URLs
- Automatic caching
- Placeholder and error handling

**Demo:** Show AsyncImage loading from URL
**Action:** Show image loading example

---

### Slide 24: Image Shapes
**Title:** Image Shapes
**Content:**
- Circular images
- Rounded corners
- ContentScale options

**Demo:** Show circular and rounded images

---

## Section 6: State Hoisting

### Slide 25: State Hoisting Overview
**Title:** State Hoisting in Compose
**Content:**
- What is state hoisting?
- Why is it important?
- Benefits

**Demo:** Show StateHoistingDemoScreen

---

### Slide 26: State Hoisting Pattern
**Title:** State Hoisting Pattern
**Content:**
- Move state up to common ancestor
- Pass state down as parameters
- Pass callbacks for state changes

**Demo:** Show counter example
**Action:** Show how state is hoisted to parent

---

### Slide 27: Benefits of State Hoisting
**Title:** Benefits
**Content:**
- Reusable components
- Single source of truth
- Easier testing
- Better state management

**Demo:** Show text input example with hoisted state

---

### Slide 28: State Hoisting Example
**Title:** Example: Counter Component
**Content:**
- Stateless child component
- State managed by parent
- Callbacks for state changes

**Demo:** Show counter with increment/decrement

---

## Section 7: Theming

### Slide 29: Theming Overview
**Title:** Theming in Compose
**Content:**
- Material Theme system
- Custom themes
- Components: Color, Typography, Shape, Dimens

**Demo:** Show ThemingDemoScreen

---

### Slide 30: Material Theme Colors
**Title:** Material Theme Colors
**Content:**
- Primary, Secondary, Tertiary
- Error, Surface, Background
- Access via MaterialTheme.colorScheme

**Demo:** Show color palette
**Action:** Point to different colors

---

### Slide 31: Typography
**Title:** Typography System
**Content:**
- Predefined text styles
- Display, Headline, Title, Body, Label
- Consistent typography across app

**Demo:** Show different typography styles
**Action:** Show various text styles

---

### Slide 32: Shapes
**Title:** Shape System
**Content:**
- Small, Medium, Large shapes
- Custom shapes
- Rounded corners, circles

**Demo:** Show different shapes
**Action:** Point to boxes with different shapes

---

### Slide 33: Custom Theme
**Title:** Custom Theme
**Content:**
- Custom Color Schemes
- Custom Typography
- Custom Shapes
- Theme Factories

**Demo:** Show custom theme explanation

---

### Slide 34: Using MaterialTheme
**Title:** Using MaterialTheme
**Content:**
- Access theme values
- MaterialTheme.colorScheme
- MaterialTheme.typography
- MaterialTheme.shapes

**Demo:** Show code example

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

---

## Slide 36: Q&A
**Title:** Questions?
**Content:**
- Thank you!
- Questions and discussion

---

## Slide 37: Resources
**Title:** Additional Resources
**Content:**
- Jetpack Compose Documentation
- Material Design 3
- Compose Samples
- Code examples in this project

