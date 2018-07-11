# Popular Movies - Stage 2

## Current app functionality in this stage -

The app will:

1. Present the user with a grid arrangement of movie posters upon launch.
2. Allow user to change sort order via a setting:
	* The sort order can be by most popular or by highest-rated
3. Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
	* original title
	* movie poster image thumbnail
	* A plot synopsis
	* user rating
	* release date
	* genre
	* cast
4. Allow user to view and play trailers ( either in the youtube app or a web browser).
5. Allow user to read reviews of a selected movie.
6. Allow user to mark a movie as a favorite in the details view by tapping a button. A database will store the names, poster paths, and ids of the user's favorite movies.
7. Provide an option to show the user's favorites collection.
8. Provide a share option for user to share the movie trailer.

## Concepts covered -
1. Use of **Retrofit** to get data from API
2. Use of **Glide** to display images from URLs
3. **Recyclerview** and GridLayoutManager
4. Handling of **no network connectivity** cases
5. **Intents** to navigate to new activities, or to perform actions (external intents)
6. **Layout** changes according to device orientation change
7. **Android Vector Drawable** as loading image
8. **Endless scroll** for recyclerview
9. **onSaveInstanceState()** to restore state after orientation change
10. Data persistence using **Room**
11. **Architecture components** - LiveData, ViewModel, Dagger2, Repository

## Video - 
[Watch Demo](https://github.com/arushi26/android-developer-nanodegree/tree/master/proj3-PopularMovies-2/google-udacity-proj3.gif)


## API Key for Movies API -

To get the API key for the variable defined in utils/Constants.java, follow the below given steps.

1. If you don’t already have an account in "The Movie DB" website, you will need to create one in order to request an API Key. Sign up here - https://www.themoviedb.org/account/signup
2. Once you are logged in, navigate to - https://www.themoviedb.org/settings/api
3. Submit your request for the API key. If only using this key for this project, you can state in your request that your usage will be for educational/non-commercial use. You will also need to provide some personal information to complete the request. 
4. Once you submit your request, you should receive your key via email shortly after.

## Overview of major cases checked in App -
Can be checked in - [Cases checked](cases-checked.ods)
