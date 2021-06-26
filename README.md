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

#### 通过路由跳转页面

```java
NavController nav = new NavController.Builder()
  .registerRoute("helloAbility", context -> new HelloAbility())
  .create(this, R.id.container);
// 跳转页面
nav.navigate("helloAbility", new BundleBuilder().put("msg", "Hello World").build());
```

#### 通过自定义路由构造器跳转页面

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

