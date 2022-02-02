[![Android CI](https://github.com/scnplt/paper/actions/workflows/android.yml/badge.svg?branch=main)](https://github.com/scnplt/paper/actions/workflows/android.yml)
[![Scan with Detekt](https://github.com/scnplt/paper/actions/workflows/detekt-analysis.yml/badge.svg?branch=main)](https://github.com/scnplt/paper/actions/workflows/detekt-analysis.yml)

# Paper

Simple note-taking application for Android users. 

## Screenshots

<img src="screenshots/login.png" width="200" alt="Login screen"/> <img src="screenshots/home.png" width="200" alt="Home screen"/>
<img src="screenshots/note.png" width="200" alt="Note screen"/> <img src="screenshots/addnote.png" width="200" alt="Add note screen"/>

<img src="screenshots/night/login.png" width="200" alt="Login screen"/> <img src="screenshots/night/home.png" width="200" alt="Home screen"/>
<img src="screenshots/night/note.png" width="200" alt="Note screen"/> <img src="screenshots/night/addnote.png" width="200" alt="Add note screen"/>

## Using

- Create a [Firebase](https://firebase.google.com/) project and put the `google-services.json` file under the [app](app) folder.
- Go to https://console.firebase.google.com/project/{YOUR_PROJECT_ID}/firestore/indexes and create a composite index.
  - Collection ID: `notes`
  - Fields: `userUid` Ascending - `updateDate` Descending
  - Query scope: `Collection`

## Theme
- Font
  - [Mali](https://fonts.google.com/specimen/Mali)
  - [Roboto](https://fonts.google.com/specimen/Roboto)
- Colors: Default [Material Design 3](https://m3.material.io/) colors

## Used Technologies and Libraries

- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [Firebase](https://firebase.google.com/)
- [Navigation](https://developer.android.com/guide/navigation)
- [Parcelize](https://developer.android.com/kotlin/parcelize)
- [Data Binding](https://developer.android.com/topic/libraries/data-binding)
- [Flow](https://developer.android.com/kotlin/flow)
- [Lottie](https://airbnb.io/lottie/#/)
- [Coroutines](https://developer.android.com/kotlin/coroutines)
- [Truth](https://truth.dev/)
- [LeakCanary](https://square.github.io/leakcanary/)

## Thanks

- [Freepik](https://www.flaticon.com/authors/freepik) - App icon
- [Rasmus Foxman](https://lottiefiles.com/user/71324) - Splash screen animation

## License

```
MIT License

Copyright (c) 2021 Sertan Canpolat

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
