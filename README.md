Showcase
<img src="https://img.shields.io/badge/Language-Kotlin%20Multiplatform-blue"/> <img src="https://img.shields.io/badge/UI-Compose%20Multiplatform-blue"/> <img src="https://img.shields.io/badge/Platform-Android-green"/> <img src="https://img.shields.io/badge/Platform-iOS-black"/> <img src="https://img.shields.io/badge/Platform-Desktop-red"/>
===================================================================================
A collection of cool multiplatform components that are either hidden inside one of my projects or didn't manage to make it to one, packed into a Material You and Light/Dark mode supporting app.

## Date Pickers <a href="https://github.com/AhmedMourad0/showcase/tree/master/common/src/commonMain/kotlin/dev/ahmedmourad/showcase/common/screens/datepickers"><img src="https://img.shields.io/badge/commonMain-dev.ahmedmourad.showcase.common.screens.datepickers-white" /> </a>
<img src="https://img.shields.io/badge/Platform-Android-green"/> <img src="https://img.shields.io/badge/Platform-iOS-black"/> <img src="https://img.shields.io/badge/Platform-Desktop-red"/>

Showcases the following custom Compose Multiplatform components: Date Picker, Days of Month Picker, and Days of Year Picker.

### Single Date Picker
Uses a custom compose layout `ConsiderateBox` with custom Parent Data Modifiers `considerWidth`, `considerHeight`, and `showIf`.

<a href="https://github.com/AhmedMourad0/showcase/blob/master/common/src/commonMain/kotlin/dev/ahmedmourad/showcase/common/compose/layouts/ConsiderateBox.kt"><img src="https://img.shields.io/badge/commonMain-dev.ahmedmourad.showcase.common.compose.layouts.ConsiderateBox-white" /></a>
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
