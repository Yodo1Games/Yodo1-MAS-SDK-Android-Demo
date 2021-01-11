# Yodo1-MAS-SDK-Android-Demo
直接使用`AndroidStudio`打开
- 修改`app`级别`build.gradle`文件的`applicationId`为自己游戏的`applicationId`
```ruby
defaultConfig {
        applicationId "Your Application Id"
        ...
    }
```

- 修改`AndroidManifest.xml`文件的`com.google.android.gms.ads.APPLICATION_ID`为自己游戏的`AdMob App Id`
```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="Your AdMob App Id" />
```

- 修改`MainActivity.java`中`Yodo1Mas`初始化的`AppId`为自己游戏的`AppId`
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