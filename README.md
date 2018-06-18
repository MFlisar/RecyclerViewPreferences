### RecyclerViewPreferences [![Release](https://jitpack.io/v/MFlisar/RecyclerViewPreferences.svg)](https://jitpack.io/#MFlisar/RecyclerViewPreferences)
A full recycler view based and NO XML preferences replacement with a lot of extra features

![Demo GIF](https://github.com/MFlisar/RecyclerViewPreferences/blob/master/files/demo.gif?raw=true)

![Demo MP4 (1.92 MB)](https://github.com/MFlisar/RecyclerViewPreferences/blob/master/files/demo.mp4?raw=true)

### Features
* No xml preferences
* Supports numbers, strings, boolean, color settings and can be extended to support any other type
* Uses `DialogFragments` and therefore support rotation and pause/resume in any state
* View based, can be placed in any layout at any place and at any size
* `PreferenceFragment` that can be placed in layouts anywhere, which handles all the library specific events
* Supports ANY storage like `SharedPreference` or any other, default setting implementations for global settings that are stored in `SharedPreferences` are integrated
* Offers a framework to overwrite the global settings on a per item base (i.e. define a global folder style and save it in the `SharedPreferences`, overwrite those settings per folder and save those settings in your database)

### Gradle (via JitPack.io)

1) Add jitpack to your project's build.gradle:
```
repositories {
	maven { url "https://jitpack.io" }
}
```

2) Add the compile statement to your module's build.gradle:
```
dependencies {
	compile 'com.github.MFlisar:RecyclerViewPreferences:0.6'
}
```

### Example

TODO

For a full example, check out the demo app

### Improvements
* Layouts look already very good, but can be improved. If you have good ideas, contribute or contact me
* Additional setting types can be added as well
