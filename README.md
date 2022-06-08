# Mobile Application Vee

## Background
We as Indonesian people usually still calculate the expense that comes from our vehicle manually, by gathering all bills that came from gas stations. We usually calculate the total expenses from all bills at the end of the month. Our problem led us to our ideas in this project. To build an integrated mobile app for tracking the expenses from our vehicle. Later on, this app will be our everyday’s partner when using our vehicle.


## TODO
- [x] Design the application using figma
- [x] Implementation the design to Application Layout (XML)
- [x] Importing the required libraries
- [x] Features get nearest gas station
- [x] Features users activity
- [x] Make a connection to REST API
- [x] Implementation of Machine Learning Model
- [x] Localization
- [x] Paging3 Implementation (In progress)
- [ ] Unit Testing & Android Test

## Features
- Get the nearest location of gas stations
- Manage expenses when using the vehicle (currently only fuel expense)
- Predict the forecast of next time expenses based on activity

## Minimum Requirement
- Android Studio Chipmunk | 2021.2.1

## Installation Instructions
Fork and clone the forked repository:
```shell
git clone git://github.com/<your_fork>/Vee-Mobile
```
Navigate into cloned repository:
```shell
cd Vee-Mobile
```
Create file project.properties:
```shell
touch project.properties
```
Fill project.properties with your configuration
```
CLIENT_ID="YOUR CLIENT ID"
API_URL="YOUR API URL"
```
Add string with name **map_key** in app/src/main/res/values
```xml
<string name="map_key" translatable="false">YOUR GOOGLE MAP KEY</string>
```
Add google-services.json to the folder app. You can get google-services.json from Firebase or Google Cloud. 

You can follow this instruction [Using API Keys](https://developers.google.com/maps/documentation/android-sdk/get-api-key?hl=id)
```
cd app && touch google-services.json && nano google-services.json
```
After everything is done, you can build the application using android studio and run this project.

## Suggestion
If you have any suggestion related to this project you can contact us at [C22-PS001[at]bangkit[dot]academy](mailto:c22-ps001@bangkit.academy)

## License
License of this project under MIT. See [LICENSE](LICENSE)

## Credits
We would like to thank the makers of theese cool plugins and images.

### Plugins
- [Koin](https://insert-koin.io)
- [Retrofit2](https://square.github.io/retrofit/)
- [Glide](https://github.com/bumptech/glide)
- [Circle Image View](https://github.com/hdodenhof/CircleImageView)
### Images
- [Empty Notification Animation](https://lottiefiles.com/99955-empty-notifications)
- [No Data Animation](https://assets10.lottiefiles.com/packages/lf20_Dczay3.json)
- [Location Map Animation](https://lottiefiles.com/5733-location-map)
