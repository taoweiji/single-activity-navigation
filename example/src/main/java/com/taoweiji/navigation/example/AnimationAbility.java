package com.taoweiji.navigation.example;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.taoweiji.navigation.Ability;
import com.taoweiji.navigation.BundleBuilder;
import com.taoweiji.navigation.Destination;
import com.taoweiji.navigation.NavController;
import com.taoweiji.navigation.NavOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AnimationAbility extends Ability {
    @NonNull
    @NotNull
    @Override
    protected View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ListView listView = new ListView(getContext());
        MyAdapter adapter = new MyAdapter();
        adapter.urls.add("https://k.zol-img.com.cn/dcbbs/15926/a15925795_s.jpg");
        adapter.urls.add("https://alifei03.cfp.cn/creative/vcg/veer/800/new/VCG41N677399580.jpg");
        adapter.urls.add("https://alifei05.cfp.cn/creative/vcg/veer/800/new/VCG41N857293320.jpg");
        adapter.urls.add("https://k.zol-img.com.cn/dcbbs/15926/a15925795_s.jpg");
        adapter.urls.add("https://k.zol-img.com.cn/dcbbs/15926/a15925795_s.jpg");
        adapter.urls.add("https://k.zol-img.com.cn/dcbbs/15926/a15925795_s.jpg");
        listView.setAdapter(adapter);
        return listView;
    }

    static class MyAdapter extends BaseAdapter {
        List<String> urls = new ArrayList<>();

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public Object getItem(int position) {
            return urls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String url = urls.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, null);
            }
            ImageView imageView = convertView.findViewById(R.id.image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int[] location = new int[2];
                    v.getLocationInWindow(location);
                    Bundle bundle = new BundleBuilder()
                            .put("x", location[0]).put("y", location[1])
                            .put("width", v.getWidth()).put("height", v.getHeight())
                            .put("url", url)
                            .build();
                    NavController.findNavController(v).navigate(Destination.with(new ImageAbility(), bundle));
                }
            });
            Glide.with(parent.getContext()).load(url).into(imageView);
            return convertView;
        }
    }
}
