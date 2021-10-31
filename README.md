# Android Single Activity Application Framework

 [![Maven Central](https://img.shields.io/maven-central/v/io.github.taoweiji.single-activity-navigation/navigation)](https://search.maven.org/search?q=io.github.taoweiji.single-activity-navigation)


Ability 具备 Fragment 的碎片能力，具备和 Fragment 相似的生命周期，可以当做 View 去使用。也具备 Activity 丰富的功能，可以作为页面代替 Activity。

- 支持配置 Deep Link，支持外部 url 跳转；
- 支持 Ability、View 和 Fragment 作为页面；
- Ability 支持预渲染，可以有效提高复杂页面跳转性能；
- 支持页面栈嵌套使用，实现 Instagram 的复杂页面结构；
- 支持配合 ViewModel、Lifecycle、LiveData 实现 MVVM；
- 内置多种转场动画，支持自定义转场动画；
- 支持页面之间跳转传递参数，支持申请权限，跳转 Activity 获取返回值；

### Ability 解决 Android 存在的问题
- Activity 性能差，页面启动耗时过程。
- Activity 受到 Manifest 控制，无法在 Hook 增加页面。
- Fragment 生命周期不受开发者控制，难以实现理想的生命周期。
- Fragment、Activity 动画支持较差。

#### demo二维码

[示例APP下载](https://github.com/taoweiji/single-activity-navigation/releases/download/0.1.0/example-release.apk)


![nav](https://user-images.githubusercontent.com/3044176/132284737-1a9ec82a-bcea-40f2-aff4-7382d9f1ccad.png)


https://user-images.githubusercontent.com/3044176/124356698-f5d48100-dc49-11eb-8dda-8712564841fc.mp4









### 使用教程

##### 引入依赖

```groovy
implementation 'io.github.taoweiji.single-activity-navigation:navigation:+'
```


### 通过 AbilityBuilder 跳转页面

跳转一个新页面就是如此简单

```java
NavController nav = new NavController.Builder().create(this, frameLayout);
nav.navigate(new AbilityBuilder() {
    @Override
    public View builder(Context context, Bundle arguments) {
        TextView hello = new TextView(context);
        hello.setBackgroundColor(Color.WHITE);
        hello.setText(arguments.getString("msg"));
        hello.setOnClickListener(v -> {
            // 返回
            NavController.findNavController(v).pop();
        });
        return hello;
    }
}, new BundleBuilder().put("msg", "Hello World").build());
```



### 定义 HelloAbility

Ability 是框架的页面，具备和 Fragment 几乎一样的生命周期，可以按照 Fragment 的理解去使用 Ability，如果开发者本身就已经有其它的业务架构，无法继承 Ability，建议通过 AbilityBuilder 去实现跳转。

```java
public class HelloAbility extends Ability {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(getContext());
        textView.setBackgroundColor(Color.WHITE);
        return textView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView info = (TextView) view;
        info.setText(getArguments().getString("msg"));
    }
}
```



#### 直接跳转页面

```java
NavController nav = new NavController.Builder()
  .create(this, R.id.container);
// 跳转页面
nav.navigate(new HelloAbility());
```



#### 路由跳转页面

```java
NavController nav = new NavController.Builder()
  .registerRoute("hello", context -> new HelloAbility())
  .create(this, R.id.container);
// 跳转页面
nav.navigate("hello", new BundleBuilder().put("msg", "Hello World").build());
```



#### 通过自定义路由构造器跳转页面

Uri 的参数会自动解析成 Bundle 对象，在 Ability 内通过 getArguments() 获取。

```java
NavController nav = new NavController.Builder()
  .onGenerateRoute(destination -> {
    if (destination.uri != null && destination.uri.getPath().equals("/hello")) {
      return new UserAbility();
    }
    return null;
  }).create(this, R.id.container);
// 通过uri跳转
nav.navigate(Uri.parse("myScheme://host/hello?msg=Hello"));
```



### 通过自定义路由器解析成普通路由

navigate 优先会先检查 `onGenerateRoute`再检查 `routes`映射，所以可以在 onGenerateRoute 把 Uri 解析成普通的 name。

```java
NavController nav = new NavController.Builder()
  .registerRoute("hello", context -> new HelloAbility())
  .onGenerateRoute(destination -> {
    if (destination.uri != null && destination.getScheme().equals("myScheme")) {
      String path = destination.uri.getPath();
      destination.name = path.substring(1);
    }
    if (destination.uri != null && destination.getScheme().contains("http")) {
      // 框架已经把 uri 保存到 arguments，可以在 Ability 通过 getArguments().getParcelable(Destination.URI_KEY) 获取
      destination.arguments.putString("url",destination.uri.toString())
      return new WebAbility();
    }
    return null;
  }).create(this, R.id.container);
// 通过uri跳转
nav.navigate(Uri.parse("myScheme://host/hello?msg=Hello"));
```



### 跳转到 Fragment

框架是完全支持 Fragment 跳转，但是推荐使用 Ability，Ability 更加像一个 Activity，提供了更加强大的功能用于构建页面，比如setStatusBarColor、finish 等，而且还支持预创建 preCreateView 方法。Fragment 的支持也是通过 FragmentAbility 封装实现的，实际上还是包了一层 Ability。可以通过 `NavController.findAbility(fragment)` 获取 Ability 对象，使用其丰富的功能。

```java
NavController nav = new NavController.Builder().create(this, R.id.container);
// 方式一
nav.navigate(new UserFragment());
// 方式二
nav.navigate(new FragmentAbility(new UserFragment()));
// 通过 Fragment 获取 Ability 对象
NavController.findAbility(fragment).setStatusBarColor()
```



### 获取页面返回值

```java
public class HelloAbility extends AbilityContainer {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(getContext());
        textView.setBackgroundColor(Color.WHITE);
        return textView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      	// 返回数据
      	setResult(new BundleBuilder().put("msg", "Hello World").build());
    }
}
nav.navigate(new HelloAbility()).registerForResult(result -> {
    Log.e("返回数据", result.getString("msg"));
  });
```



### 在 Ability 跳转 Activity 获取返回值

在 Ability 是无法使用 startActivityForResult 方法，但是 Ability 实现了 ActivityResultCaller，提供更加简单获取 Activity 返回值的 registerForActivityResult 方法。

```java
registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
    String url = result.getData().getStringExtra("url");
}).launch(new Intent(getContext(), QrcodeActivity.class));
```



### 在 Ability 使用 ViewModel、Lifecycle、LiveData 实现 MVVM

定义好 ViewModel

```java
public class WeatherViewModel extends ViewModel {
    public MutableLiveData<Integer> weatherData = new MutableLiveData<>();

    public void updateWeather() {
        weatherData.postValue(new Random().nextInt(32));
    }
}
```

在 Ability 使用 LiveData 和 Fragment 是无差异的，不需要担心出现泄漏问题，框架已经管理好 Lifecycle。

```java
// WeatherAbility
@Override
public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    weatherViewModel.weatherData.observe(this, new Observer<Integer>() {
        @Override
        public void onChanged(Integer temperature) {
            info.setText("当前温度：" + temperature);
        }
    });
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            weatherViewModel.updateWeather();
        }
    });
}
```



### 在 ViewPager 使用 Ability



Single-top

single instance

Single 

