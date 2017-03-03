[![Codacy Badge](https://api.codacy.com/project/badge/Grade/fb93a49eba1941d695cab79d4359a9b0)](https://www.codacy.com/app/gurupadmamadapur/Fad-Flicks?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Protino/Fad-Flicks&amp;utm_campaign=Badge_Grade)

FAD FLICKS
=
* [About](#about)
* [Screenshots](#screenshots)
* [What did I learn](#what-did-i-learn)
* [Build Configuration](#build-configuration)
* [Libraries](#libraries)
* [Todo](#todo)
* [License](#license)

### About
This application is part of the [Udacity Android Developer Nanodegree]. It provides list of popular and top rated movies
which are fetched from [TMDB - The Movie Database][TMDB].

### Screenshots

<p align="center">
<img src="https://drive.google.com/uc?id=0B7HoD_UwfapHRTM0aGFUOV83NWc" width="960" height="540" alt="Cover">
</p>

### What did I learn?

### Android stuff
* AsyncTasks
* Fragments - There usefulness in master-detail flow.
* Network calls in both ways - Using Retrofit and `HttpURLConnection`.
* SharedPreferences.
* GridView and RecyclerView -  After all not that scary.
* CustomAdapters.
* Using Open-source libraries - [check them here](#libraries).
* Coordinator layout and about scroll flags.
* Material Design

### Non-Android stuff
* Git. Seriously, I feel more comfortable with it now. There is still a lot to learn though as I develop individually.
* Debugging. Instead of creating hundreds of log messages better use the built-in android studio debugger.
* Markdown.
* UI Mockups and detailed plan before starting to code. Gathering all necessary information needed to develop a functionality beforehand helps to avoid bugs and saves time.

### Build Configuration
Add the following lines in `gradle.properties` file :

    TMDB_API_KEY = "[KEY]"
    YOUTUBE_API_KEY = "[KEY]"

### Libraries
I'm very thankful to the developers and contributors  of these libraries. They've saved lot of time and effort.
* [Picasso]
* [OkHttp]
* [Gson]
* [Retrofit]
* [Butterknife]
* [Icepick]
* [FloatingActionButton]
* [LeakCanary]
* [SimpleRatingBar]

### Todo


* Add more categories like upcoming movies, In theaters, etc
* Use event bus to communicate instead of cohesive callbacks
* Implement content providers or use greenDao or other library to store data in a persistent database.
* Add functionality to search for movies, actors etc. or search movies through cast names.
* Fetch data from multiple sources (not just TMDB).

### License
    Copyright 2016 Gurupad Mamadapur

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

  [TMDB]:https://www.themoviedb.org/?language=en
  [Udacity Android Developer Nanodegree]:https://www.udacity.com/degrees/android-developer-nanodegree-by-google--nd801
  [Picasso]:http://square.github.io/picasso/
  [OkHttp]:http://square.github.io/okhttp/
  [Gson]:https://github.com/google/gson
  [Retrofit]:https://github.com/square/retrofit
  [Butterknife]:http://jakewharton.github.io/butterknife/
  [Icepick]:https://github.com/frankiesardo/icepick
  [FloatingActionButton]:https://github.com/Clans/FloatingActionButton
  [LeakCanary]:https://github.com/square/leakcanary
  [SimpleRatingBar]:https://github.com/FlyingPumba/SimpleRatingBar