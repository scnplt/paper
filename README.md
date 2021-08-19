[![Android CI](https://github.com/scnplt/paper/actions/workflows/android.yml/badge.svg?branch=main)](https://github.com/scnplt/paper/actions/workflows/android.yml)
[![Scan with Detekt](https://github.com/scnplt/paper/actions/workflows/detekt-analysis.yml/badge.svg?branch=main)](https://github.com/scnplt/paper/actions/workflows/detekt-analysis.yml)

# Paper
Simple note-taking application for Android users. 


## Screenshots
<img src="screenshots/login.png" height="400" alt="Login screen"/> <img src="screenshots/home.png" height="400" alt="Home screen"/>
<img src="screenshots/note.png" height="400" alt="Note screen"/> <img src="screenshots/addnote.png" height="400" alt="Add note screen"/>

<img src="screenshots/night/login.png" height="400" alt="Login screen"/> <img src="screenshots/night/home.png" height="400" alt="Home screen"/>
<img src="screenshots/night/note.png" height="400" alt="Note screen"/> <img src="screenshots/night/addnote.png" height="400" alt="Add note screen"/>

### Delete note
You can delete notes by swiping left on the home screen.

<img src="screenshots/delete.gif" height="400" alt="Delete note"/>


## Using
You need to create a [Firebase](https://firebase.google.com/) project and put the `google-services.json` file under the [app](app) folder.


## Theme
- Font: [Mali](https://fonts.google.com/specimen/Mali)
- Colors:
    | Grey                                                                      | Orange                                                                    |
    |---------------------------------------------------------------------------|---------------------------------------------------------------------------|
    | ![#FAFAFA](https://via.placeholder.com/15/FAFAFA/000000?text=+) `#FAFAFA` | ![#FFB74D](https://via.placeholder.com/15/FFB74D/000000?text=+) `#FFB74D` |
    | ![#EEEEEE](https://via.placeholder.com/15/EEEEEE/000000?text=+) `#EEEEEE` | ![#FFA726](https://via.placeholder.com/15/FFA726/000000?text=+) `#FFA726` |
    | ![#E0E0E0](https://via.placeholder.com/15/E0E0E0/000000?text=+) `#E0E0E0` | ![#FF9800](https://via.placeholder.com/15/FF9800/000000?text=+) `#FF9800` |
    | ![#616161](https://via.placeholder.com/15/616161/000000?text=+) `#616161` |                                                                           |
    | ![#424242](https://via.placeholder.com/15/424242/000000?text=+) `#424242` |                                                                           |
    | Red                                                                       | Black                                                                     |
    | ![#EF9A9A](https://via.placeholder.com/15/EF9A9A/000000?text=+) `#EF9A9A` | ![#262626](https://via.placeholder.com/15/262626/000000?text=+) `#262626` |
    | ![#EF5350](https://via.placeholder.com/15/EF5350/000000?text=+) `#EF5350` | ![#000000](https://via.placeholder.com/15/000000/000000?text=+) `#000000` |
    | White                                                                     |
    | ![#FFFFFF](https://via.placeholder.com/15/FFFFFF/000000?text=+) `#FFFFFF` |

## Used Technologies and Libraries
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [Firebase](https://firebase.google.com/)
- [Navigation](https://developer.android.com/guide/navigation)
- [Parcelize](https://developer.android.com/kotlin/parcelize)
- [Data Binding](https://developer.android.com/topic/libraries/data-binding)
- [Livedata](https://developer.android.com/topic/libraries/architecture/livedata)
- [Lottie](https://airbnb.io/lottie/#/)
- [Coroutines](https://developer.android.com/kotlin/coroutines)
- [Flow](https://developer.android.com/kotlin/flow)
- [Truth](https://truth.dev/)
- [LeakCanary](https://square.github.io/leakcanary/)


## Credits
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
