# World wide News listing

#### This sample was created to showcase my skills in the Android Framework.

##### To be able to compile this project, you need to add the following in your global gradle
properties file:

##### NY_API_KEY

For this client app I used https://developer.nytimes.com/docs/articlesearch-product/1/overview to fetch the latest articles list

Used Glide for normal image loading

Used Navigation component to provide the navigation in the app

Kotlin-coroutines were used for blocking operations(fetching articles list from the server,..)

Added some nice animation for the search view in the actionbar

Used LiveData and ViewModels as well

Koin was used to glue the different components together in the application

Included some few number of unit tests as well.


### Architecture:

I am using the MVVM architecture and some state machine concept on top of it.
Every screen has a view, a model, and a ViewModel. The ViewModel contains a state that represents
the properties of the View. This state will be emitted using LiveData to the observer(view).

The ViewModel state is represented using a simple kotlin data class with different fields.

I also use sealed classes to model some repetitive behaviors. Like, when fetching data in an
asynchronous fashion, the usual states are Loading, Failed(with the failure), or Success(with the
actual data).

Repository is the single source of truth that is used to fetch data(either from the network or
from the local storage)

Here is a gif demonstrating this super mini app:

I created a small animation for the search bar expansion and collapse.

Search behavior and animation:

![Search animation Gif](/readmeassets/ny_articles_search_expand_collapse.gif)


NY articles listing and details

![App Gif demo](/readmeassets/ny_infinite_scrolling.gif)
