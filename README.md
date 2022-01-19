# NYTimesMostViewedArticles
This app displays details about NY Times most viewed articles from the past few days. Users can select articles they are interested in and view more details. In order to view the full article the user can click the "Read More" hyperlink in the DetailsScreen.

## Setup
- Download `git clone https://github.com/lukekivi/NYTimesMostViewedArticles.git` or just download zip from git.
- Install onto your device
- Run.

## API
The time period options offered by the API are:
- 1 day
- 7 days
- 30 days

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

## Links
- [Git](https://github.com/lukekivi/NYTimesMostViewedArticles)
- [Figma project](https://www.figma.com/file/Wuke3S3snr9L3hcRVRQID0/NYTimesMostViewedArticles?node-id=0%3A1)
- [NY Times API](https://developer.nytimes.com/docs/most-popular-product/1/routes/viewed/%7Bperiod%7D.json/get)


 