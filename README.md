Showcase
<img src="https://img.shields.io/badge/Language-Kotlin%20Multiplatform-blue"/> <img src="https://img.shields.io/badge/UI-Compose%20Multiplatform-blue"/> <img src="https://img.shields.io/badge/Platform-Android-green"/> <img src="https://img.shields.io/badge/Platform-iOS-black"/> <img src="https://img.shields.io/badge/Platform-Desktop-red"/>
===================================================================================
A collection of cool multiplatform components that are either hidden inside one of my projects or didn't manage to make it to one, packed into a Material You and Light/Dark mode supporting app.

> [!WARNING]
> The app might spontaneously crash on some Android versions, this is due to a [current bug](https://issuetracker.google.com/issues/308501489) with Compose that is yet to be fixed.

## Canvas <a href="https://github.com/AhmedMourad0/showcase/tree/master/common/src/commonMain/kotlin/dev/ahmedmourad/showcase/common/screens/canvas"><img src="https://img.shields.io/badge/commonMain-dev.ahmedmourad.showcase.common.screens.canvas-white" /> </a> <img src="https://img.shields.io/badge/Platform-Android-green"/> <img src="https://img.shields.io/badge/Platform-iOS-black"/> <img src="https://img.shields.io/badge/Platform-Desktop-red"/>

- Showcases the Canvas component created with Compose Multiplatform that allows users to add, remove, and manipulate various shapes and images in the canvas space, with the ability to undo/redo their actions.
- Leverages the Compose Snapshot system to greatly simplify the code needed for the undo/redo functionality.
- Separates dragging from rotations and zooming for better user experience.
- Includes various interchangeable implementations of the focus manager used to determine which item has focus and when it will lose it.
- Channels rotation and zooming applied to the canvas itself to the last focused item, which facilitates manipulating tiny items.
- Allows bringing items to front by clicking on them.
- Allows clicking through large items to focus items hidden beneath them.

<table>
  <tr>
     <td align="center">Canvas #1</td>
     <td align="center">Canvas #2</td>
  </tr>
  <tr>
    <td><img src="/screenshots/canvas1.jpg" width=270></td>
    <td><img src="/screenshots/canvas2.jpg" width=270></td>
  </tr>
</table>

## Date Pickers <a href="https://github.com/AhmedMourad0/showcase/tree/master/common/src/commonMain/kotlin/dev/ahmedmourad/showcase/common/screens/datepickers"><img src="https://img.shields.io/badge/commonMain-dev.ahmedmourad.showcase.common.screens.datepickers-white" /> </a> <img src="https://img.shields.io/badge/Platform-Android-green"/> <img src="https://img.shields.io/badge/Platform-iOS-black"/> <img src="https://img.shields.io/badge/Platform-Desktop-red"/>

- Showcases the following custom Compose Multiplatform components: Date Picker, Days of Month Picker, and Days of Year Picker.
- Uses a custom compose layout [`ConsiderateBox`](https://github.com/AhmedMourad0/showcase/blob/master/common/src/commonMain/kotlin/dev/ahmedmourad/showcase/common/compose/layouts/ConsiderateBox.kt) with custom Parent Data Modifiers `considerWidth`, `considerHeight`, and `showIf`.

### Single Date Picker

<table>
  <tr>
     <td align="center">Days View</td>
     <td align="center">Months View</td>
     <td align="center">Years View</td>
  </tr>
  <tr>
    <td><img src="/screenshots/datepicker_daysmode.jpg" width=270></td>
    <td><img src="/screenshots/datepicker_monthsmode.jpg" width=270></td>
    <td><img src="/screenshots/datepicker_yearsmode.jpg" width=270></td>
  </tr>
</table>

### Days of Year Picker
<table>
  <tr>
     <td align="center">Days View</td>
     <td align="center">Months View</td>
     <td align="center">Fallback</td>
  </tr>
  <tr>
    <td><img src="/screenshots/daysofyearpicker_daysmode.jpg" width=270></td>
    <td><img src="/screenshots/daysofyearpicker_monthsmode.jpg" width=270></td>
    <td><img src="/screenshots/daysofyearpicker_fallback.jpg" width=270></td>
  </tr>
</table>

### Days of Month Picker
<table>
  <tr>
     <td align="center">Days View</td>
     <td align="center">Fallback</td>
  </tr>
  <tr>
    <td><img src="/screenshots/daysofmonthpicker.jpg" width=270></td>
    <td><img src="/screenshots/daysofmonthpicker_fallback.jpg" width=270></td>
  </tr>
</table>

## Bungee Gum Bars <a href="https://github.com/AhmedMourad0/showcase/tree/master/common/src/commonMain/kotlin/dev/ahmedmourad/showcase/common/screens/bungeegumbars"><img src="https://img.shields.io/badge/commonMain-dev.ahmedmourad.showcase.common.screens.bungeegumbars-white" /> </a> <img src="https://img.shields.io/badge/Platform-Android-green"/> <img src="https://img.shields.io/badge/Platform-iOS-black"/> <img src="https://img.shields.io/badge/Platform-Desktop-red"/>

- Showcases a custom Compose Multiplatform range bar with elastic properties that makes it fun to use.
- Fully customizable for various levels of elasticity.
- Possesses the properties of both rubber and gum.

<table>
  <tr>
     <td align="center">Bungee Gum #1</td>
     <td align="center">Bungee Gum #2</td>
  </tr>
  <tr>
    <td><img src="/screenshots/bungeegum1.jpg" width=270></td>
    <td><img src="/screenshots/bungeegum2.jpg" width=270></td>
  </tr>
</table>

## Theme Selector <a href="https://github.com/AhmedMourad0/showcase/tree/master/common/src/commonMain/kotlin/dev/ahmedmourad/showcase/common/screens/themeselector"><img src="https://img.shields.io/badge/commonMain-dev.ahmedmourad.showcase.common.screens.themeselector-white" /> </a> <img src="https://img.shields.io/badge/Platform-Android-green"/> <img src="https://img.shields.io/badge/Platform-iOS-black"/> <img src="https://img.shields.io/badge/Platform-Desktop-red"/>

- Showcases a multiplatform, extensible, and responsive compose theme selector.
- Provides the option for Material You theme on all devices that support it.
- Fully supports dark/light mode for all provided themes.
- App theme updates instantaneously upon selection, including system bar colors.

### Dark Blue Theme

<table>
  <tr>
     <td align="center">Dark Mode #1</td>
     <td align="center">Dark Mode #2</td>
     <td align="center">Light Mode #1</td>
     <td align="center">Light Mode #2</td>
  </tr>
  <tr>
    <td><img src="/screenshots/darkbluedark1.jpg" width=270></td>
    <td><img src="/screenshots/darkbluedark2.jpg" width=270></td>
    <td><img src="/screenshots/darkbluelight1.jpg" width=270></td>
    <td><img src="/screenshots/darkbluelight2.jpg" width=270></td>
  </tr>
</table>

### Brown Orange Theme

<table>
  <tr>
     <td align="center">Dark Mode #1</td>
     <td align="center">Dark Mode #2</td>
     <td align="center">Light Mode #1</td>
     <td align="center">Light Mode #2</td>
  </tr>
  <tr>
    <td><img src="/screenshots/brownorangedark1.jpg" width=270></td>
    <td><img src="/screenshots/brownorangedark2.jpg" width=270></td>
    <td><img src="/screenshots/brownorangelight1.jpg" width=270></td>
    <td><img src="/screenshots/brownorangelight2.jpg" width=270></td>
  </tr>
</table>

## Permissions Requester <a href="https://github.com/AhmedMourad0/showcase/tree/master/common/src/androidMain/kotlin/dev/ahmedmourad/showcase/common/screens/permissions"><img src="https://img.shields.io/badge/androidMain-dev.ahmedmourad.showcase.common.screens.permissions-white" /> </a> <img src="https://img.shields.io/badge/Platform-Android-green"/>

- Showcases a custom customizable Compose permissions requester.
- Supports requesting a single or multiple permissions at once.
- By default, once the requester enters composition, it checks the permission status:
  - If the permission was granted, nothing happens.
  - If the permission was never requested, the permissions is requested immediately.
  - If the permission was denied once, the rationale is shown with an option to request the permission again.
  - if the permission was denied permanently, a rationale is shown with the option to go to settings to grant it.
- Updated the state of each permission instantly when the user returns to the app from settings.
- Allows observing the state of each permission individually.
- Can be enabled/disabled by a flag.
- Supports custom strategies for each permission, useful for special permissions such as [SYSTEM_ALERT_WINDOW](https://developer.android.com/reference/android/Manifest.permission#SYSTEM_ALERT_WINDOW)
- Supports showing the rationale before requesting the permission, useful for sensitive permissions such as [READ_PHONE_STATE](https://developer.android.com/reference/android/Manifest.permission#READ_PHONE_STATE)
- Supports displaying custom rationale, can be a dialog, a sheet, or even embedded into other composables.
- Supports permission-walling content, where the provided content will not be shown unless the permission is granted.
- Provides an imperative callback that is invoked when all permissions are granted.

<table>
  <tr>
     <td align="center">Permissions Not Granted</td>
     <td align="center">Permissions Granted</td>
  </tr>
  <tr>
    <td><img src="/screenshots/permissions1.jpg" width=270></td>
    <td><img src="/screenshots/permissions2.jpg" width=270></td>
  </tr>
</table>
