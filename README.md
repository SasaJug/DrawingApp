## README ##

### Drawing Application  ###

Android app for on-screen drawing.

* Kotlin
* Clean architecture
* Android Architecture Components
* Dagger2 dependency injection
* AWS (Cognito, DynamoDb and S3 services)


An example of using clean architecture in android app, references:
* [Movie Night](https://github.com/mrsegev/MovieNight)
* [Android Clean Architecture](https://github.com/android10/Android-CleanArchitecture)

Screenshots:

![Screenshot_1](https://i.imgur.com/YSii38qm.png)
![Screenshot_2](https://i.imgur.com/uSe0C6Bm.png)

### AWS  Instructions ###

AWS is used for user management and for syncing data between devices.
In order to use the app it is necessary to add config/amazon.properties 
file to the data module. The amazon.properties file must have following structure:

USER_POOL_ID = String#"YOUR_USER_POOL_ID"

CLIENT_ID = String#"YOUR_CLIENT_ID

CLIENT_SECRET = String#"YOUR_CLIENT_SECRET"

IDENTITY_POOL_REGION = String#"YOUR_IDENTITY_POOL_REGION"

IDENTITY_POOL_ID = String#"YOUR_IDENTITY_POOL_ID"

s3_BUCKET_NAME = String#"YOUR_s3_BUCKET_NAME "

s3_BUCKET_REGION = String#"YOUR_s3_BUCKET_REGION"