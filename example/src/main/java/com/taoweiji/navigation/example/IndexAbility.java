package com.taoweiji.navigation.example;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.AbilityBuilder;
import com.taoweiji.navigation.BundleBuilder;
import com.taoweiji.navigation.NavController;
import com.taoweiji.navigation.ViewUtils;
import com.taoweiji.navigation.example.dialog.DialogAbility2;
import com.taoweiji.navigation.example.event.EventFirstAbility;
import com.taoweiji.navigation.example.mvvm.MvvmAbility;
import com.taoweiji.navigation.example.result.TestResultAbility;
import com.taoweiji.navigation.example.result.TestResultActivity;
import com.taoweiji.navigation.example.result.TestResultFragment;

import java.util.ArrayList;
import java.util.List;

import static android.view.MenuItem.SHOW_AS_ACTION_ALWAYS;


public class IndexAbility extends Ability {

    private ListView list;
    private ListAdapter adapter;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        createDefaultToolbar();
        getToolbar().getMenu().add("源码").setShowAsAction(SHOW_AS_ACTION_ALWAYS);
        getToolbar().setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/taoweiji/single-activity-navigation")));
                return true;
            }
        });
        setTitle("单 Activity 框架");
        setStatusBarColor(getResources().getColor(R.color.purple_500));
        list = new ListView(getContext());
        adapter = new ListAdapter();
        NavController nav = findNavController();
        adapter.add("AbilityBuilder 跳转", () -> nav.navigate(new AbilityBuilder() {
            @Override
            public View builder(Context context, Bundle arguments) {
                createDefaultToolbar();

                setTitle("AbilityBuilder 跳转");
                TextView hello = new TextView(context);
                hello.setGravity(Gravity.CENTER);
                hello.setText("Hello World");
                return hello;
            }
        }));
        adapter.add("直接跳转 Ability", () -> nav.navigate(new UserAbility(), new BundleBuilder().put("id", 0).build()));
        adapter.add("直接跳转 Fragment", () -> nav.navigate(new SimpleFragment()));
        adapter.add("路由跳转", () -> nav.navigate("user"));
        adapter.add("URI 跳转", () -> nav.navigate(Uri.parse("scheme://host/hello?msg=Hello")));
        adapter.add("解析 URI 成路由跳转", () -> nav.navigate(Uri.parse("scheme://host/weather")));

        adapter.add("获取 Ability 返回值", () -> {
            nav.navigate(new TestResultAbility()).registerForResult(result -> {
                Toast.makeText(getContext(), result.getString("msg"), Toast.LENGTH_SHORT).show();
            });
        });
        adapter.add("获取 Fragment 返回值", () -> {
            nav.navigate(new TestResultFragment()).registerForResult(result -> {
                Toast.makeText(getContext(), result.getString("msg"), Toast.LENGTH_SHORT).show();
            });
        });
        adapter.add("获取 Activity 返回值", () -> {
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getData() != null) {
                    Toast.makeText(getContext(), result.getData().getExtras().getString("msg"), Toast.LENGTH_SHORT).show();
                }
            }).launch(new Intent(getContext(), TestResultActivity.class));
        });

        adapter.add("跳转页面，且关闭当前页面", () -> nav.navigate(new PopAndPushAbility()));
        adapter.add("跳转页面，且关闭所有页面", () -> nav.navigate(new ReLaunchAbility()));
        adapter.add("跳转页面，且有条件关闭页面", () -> nav.navigate(new PushAndRemoveUntilAbility()));


        adapter.add("页面预创建 prepareCreate", () -> {
            Ability ability = new PrepareCreateAbility();
            long start = System.currentTimeMillis();
            ability.prepareCreate(getContext());
            long duration = System.currentTimeMillis() - start;
            nav.navigate(ability, new BundleBuilder().put("duration", duration).build());
        });
        adapter.add("DialogAbility", () -> nav.navigate(new DialogAbility2()));
        adapter.add("BottomSheetDialogAbility", () -> nav.navigate(new DialogAbility2()));

        adapter.add("Lifecycle、LiveData 实现 MVVM", () -> nav.navigate(new MvvmAbility()));
        adapter.add("在 ViewPager 使用 AbilityPageAdapter", () -> nav.navigate(new ViewPagerAbility()));
        adapter.add("自定义转场动画", () -> nav.navigate(new AnimationAbility()));
        adapter.add("设置背景、状态栏颜色等", () -> nav.navigate(new UiAbility()));
        adapter.add("发送页面消息通知", () -> nav.navigate(new EventFirstAbility()));
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> adapter.tasks.get(position).run());
        return list;
    }

    static class ListAdapter extends BaseAdapter {
        List<String> items = new ArrayList<>();
        List<Runnable> tasks = new ArrayList<>();

        void add(String title, Runnable runnable) {
            items.add(title);
            tasks.add(runnable);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String item = items.get(position);
            TextView textView = (TextView) convertView;
            if (convertView == null) {
                textView = new TextView(parent.getContext());
                int dp = ViewUtils.dp2px(parent.getContext(), 7);
                textView.setPadding(dp, dp, dp, dp);
                textView.setTextSize(16);
            }
            textView.setText(item);
            return textView;
        }
    }
}
