# single-activity-navigation

- 支持 View 和 Fragment 的跳转
- 支持获取返回值，registerForActivityResult
- 支持Lifecycle
- 支持页面之间的通信



### 通过 AbilityBuilder 跳转页面

跳转一个新页面就是如此简单

```java
NavController nav = new NavController.Builder().create(this, R.id.container);
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
  .registerRoute("helloAbility", context -> new HelloAbility())
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



### 自定义动画

框架默认的动画是左右的动画，开发者也根据自己的需求开发动画



### 在 Ability 跳转 Activity 获取返回值



### 在 Ability 使用 ViewModel、Lifecycle、LiveData 实现 MVVM





### Ability

为什么不直接使用 Fragment，而要定义新的 Ability？首先框架是支持 Fragment 跳转的，但是也是封装了一层 AbilityFragmentContainer 实现的。Ability 相比 Fragment，还增加预创建能力，我们可以根据业务的需求提高页面的跳转性能，同时也提供了更加简单的方法把数据返回到上一个页面。

- 支持页面预创建
- 返回上一个页面数据更加简单











