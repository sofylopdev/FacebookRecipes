# FacebookRecipes
App made in third (app itself) and fifth (app testing) lessons from 'Professional Android App Development' Course from edX/GalileoX.

## App description

App that shows recipes to the user in a format similar to Tinder, showing one recipe’s image at a time. 
The user can discard the recipe or save it locally. 
The saved recipes can be shared in Facebook.

 
 ## What you need to run this app:
 
1- Install the Facebook app in you device.

2- [Subscribe as a developer on the Facebook page](https://developers.facebook.com/), if you haven't already.

3- Create your [app id](https://developers.facebook.com/quickstarts/?platform=android).


4- Sign up as a developer on [Food2Fork](https://www.food2fork.com/about/api) and get your api key.


5- Put your api keys (Facebook and Food2Fork) in your gradle.properties.

Your gradle.properties should have these values:

```
FACEBOOK_APP_ID = "your_facebook_api_key"
FACEBOOK_PROVIDER = "com.facebook.app.FacebookContentProvider"
FOOD_API_KEY = "your_FoodToFork_api_key"
```


## Project uses:

 - Java
 - Architectural pattern: MVP
 
 - [Facebook SDK](https://developers.facebook.com/docs/android)
 
 - [Retrofit](https://github.com/square/retrofit)
 - [Gson](https://github.com/google/gson) (as a converter in Retrofit)
 - [Glide](https://github.com/bumptech/glide)
 - [Butterknife](https://github.com/JakeWharton/butterknife)
 - [Dagger](https://github.com/google/dagger)
 - [EventBus](https://github.com/greenrobot/EventBus)
 - [DBFlow](https://github.com/Raizlabs/DBFlow)
 
 - [Robolectric](https://github.com/robolectric/robolectric)
 - [Mockito](https://github.com/mockito/mockito)
 
