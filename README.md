#AwOiSoAk: Visor

Visor is a photo viewer that displays pictures from the last post entries
of the [AwOiSoAk](http://www.awoisoak.com) travel blog.

It's a simple app to demonstrate the use of a [MVP](https://github.com/googlesamples/android-architecture) pattern using concepts of the [Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html).
It retrieves the different needed information via the [WordPress Rest API](https://developer.wordpress.org/rest-api/).
The app filters out portrait and panoramic images for a better UIX {@see MediaFromPostDeserializer}.
Within the galleries, a first low resolution is downloaded to be displayed temporarily until the large resolution one is ready for responsiveness purposes 
A floating button into the galleries activity opens a Webview with the corresponding blog entry.
An ORMLite DB is used to keep a cache of the posts and images except high-res ones to avoid increasing too much the app size.

Implementation details:

- Applies Dependency injection with [Dagger2](https://google.github.io/dagger/)
- [Otto](http://square.github.io/otto/) is used to decouple Interactors from Presenters
- Server is attacked with [Retrofit2](http://square.github.io/retrofit/)
- [ButterKnife](http://jakewharton.github.io/butterknife/) is used to find the corresponding views
- [Glide](https://github.com/bumptech/glide) is used for image processing
- [Subsampling Scale Image View](https://github.com/davemorrissey/subsampling-scale-image-view) is used to interact with the high resolution pictures
- Two flavors were added: mock & prod (@see app/build.gradle) in order to be able to build an app which aims to a Mock Server (@see MockWPManager)



![alt tag](http://awoisoak.com/public/android/app1.jpg)
![alt tag](http://awoisoak.com/public/android/app2.jpg)
![alt tag](http://awoisoak.com/public/android/app3.jpg)