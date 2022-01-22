# NYTimesMostViewedArticles
This app displays details about NY Times most viewed articles from the past few days. Users can select articles they are interested in and view more details. In order to view the full article the user can click the "Read More" hyperlink in the DetailsScreen.

## Setup
- Run `git clone https://github.com/lukekivi/NYTimesMostViewedArticles.git` or just download zip from git.
- Install onto your device
- Run.

## New York Times Most Viewed Articles API
The time period options offered by the [API][1] are:
| Interval | Used |
| -------- | ---- |
| 1 day    | No   |
| 7 days   | Yes  |
| 30 days  | No   |
\
\
The queried article data includes media. This app is only concerned with exposing images and there are three sizes:
|         Name        | Width | Height | Used |
| ------------------- | ----- | ------ | ---- |
| Standard Thumbnail  |  74   |   75   | No   |
| mediumThreeByTwo210 |  210  |   140  | No   |
| mediumThreeByTwo440 |  440  |   293  | Yes  |

## Dependencies
|      Use Case        |            Dependency              | 
| -------------------- | ---------------------------------- | 
|        UI            |         Android Compose            | 
|    Navigation        |  Accompanist Navigation Animation  | 
|    HTTP Client       |             Retrofit               |
|  Json Conversion     |              Moshi                 |
|   Image Loading      |              Coil                  |
| Dependency Injection |              Hilt                  |
| Asynchronous Ops     |           Coroutines               |

## Future Changes
- Right now if network connection is unavailable the error message from retrofit is displayed. In the future I would like to more gracefully handle loss of internet by:
    - adding Room persistence for a local backup of data acquired from the API and
    - monitoring network connection in app in order to alert users of a loss of connection.
- Add sort functionality to section row on top of the main screen.
- Add a period selector.
- If the API supplied more articles allowing the user to sort based on clicking the article detail bubbles would be cool. With 15 articles it would be mostly useless.

## Links
- [Git](https://github.com/lukekivi/NYTimesMostViewedArticles)
- [Figma project](https://www.figma.com/file/Wuke3S3snr9L3hcRVRQID0/NYTimesMostViewedArticles?node-id=0%3A1)
- [NY Times API][1]

[1]: (https://developer.nytimes.com/docs/most-popular-product/1/routes/viewed/%7Bperiod%7D.json/get)