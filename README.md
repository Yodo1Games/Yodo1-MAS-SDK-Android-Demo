# Yodo1-MAS-SDK-Android-Demo

## Overview

MAS is Yodo1's in-app monetization solution.

Please check out our [documentation](https://developers.yodo1.com/article-categories/android/) to get started on integrating.

## Demo App

To get started with the demo app, follow the instructions below:

1. Open the project in Android Studio.
2. Verify that the dependency implementation `'com.yodo1.mas:full:+'` or `com.yodo1.mas:google:+` is included in your `build.gradle` (Module: app)
3. Update `applicationId` value in `build.gradle` file with your application id.

	```ruby
	defaultConfig {
        applicationId "Your Application Id"
        ...
    }
	```
	
4. Update the `com.google.android.gms.ads.APPLICATION_ID` value in `AndroidManifest.xml` file with the `AdMob App Id` from your MAS account.

	```xml
	<meta-data
	    android:name="com.google.android.gms.ads.APPLICATION_ID"
	    android:value="Your AdMob App Id" />
	```
	
5. Update the `AppId` value in `MainActivity.java` file with the `AppId` from your MAS account.

	```java
	Yodo1Mas.getInstance().init(this, "Your App Id", new Yodo1Mas.InitListener() {
	    @Override
	    public void onMasInitSuccessful() {
	        
	    }
	
	    @Override
	    public void onMasInitFailed(@NonNull Yodo1MasError error) {
	        
	    }
	});
	```

## Support
For feature requests, improvements, questions or any other integration issues using MAS Mediation by Yodo1, please contact us via our support page: https://developers.yodo1.com/contact-us/.
