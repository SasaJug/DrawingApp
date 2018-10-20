## README##

### Drawing Application  ###

* Android app for on-screen drawing

* Kotlin
* Clean architecture
* Android Architecture Components (ViewModel and LiveData)
* Dagger2 dependency injection

* Screenshots:

![Screenshot_1](https://i.imgur.com/YSii38qm.png)
![Screenshot_2](https://i.imgur.com/uSe0C6Bm.png)

### AWS Configuration ###
In order to use the app it is needed to:
 1. create AWS user and identity pools
 2. create app/config/aws.properties
 3. add aws credentials to it in following format: 
 
 USER_POOL_ID = String#"YOUR_COGNITO_USER_POOL_ID"
 
 CLIENT_ID = String#"YOUR_COGNITO_CLIENT_ID"
 
 CLIENT_SECRET = String#"YOUR_COGNITO_CLIENT_SECRET"
 
 IDENTITY_POOL_REGION = String#"YOUR_COGNITO_IDENTITY_POOL_REGION"
 
 IDENTITY_POOL_ID = String#"YOUR_COGNITO_IDENTITY_POOL_ID"