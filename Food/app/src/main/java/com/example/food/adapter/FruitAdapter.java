package com.example.food.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.food.R;
import com.example.food.bean.Fruit;
import com.example.food.util.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {
    private List<Fruit> list =new ArrayList<>();
    private Context mActivity;
    private ItemListener mItemListener;
    private LinearLayout llEmpty;
    private RecyclerView rvfruitList;
    public void setItemListener(ItemListener itemListener){
        this.mItemListener = itemListener;
    }
    public FruitAdapter(LinearLayout llEmpty, RecyclerView rvfruitList){
        this.llEmpty = llEmpty;
        this.rvfruitList =rvfruitList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view= LayoutInflater.from(mActivity).inflate(R.layout.item_rv_fruit_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Fruit fruit = list.get(i);
        if (fruit != null) {
            viewHolder.title.setText(fruit.getTitle());
            viewHolder.author_name.setText(String.format("￥ %s",fruit.getIssuer()));
            viewHolder.date.setText(fruit.getDate());
            Glide.with(mActivity)
                    .asBitmap()
                    .load(fruit.getImg())
                    .error(R.drawable.ic_error)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(viewHolder.img);
            Boolean isAdmin = (Boolean) SPUtils.get(mActivity,SPUtils.IS_ADMIN,false);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener!=null){
                        mItemListener.ItemClick(fruit);
                    }
                }
            });
            if (isAdmin){
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                        dialog.setMessage("确认要删除该美食吗");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(fruit);
                                fruit.delete();
                                notifyDataSetChanged();
                                Toast.makeText(mActivity,"删除成功", Toast.LENGTH_LONG).show();
                                if (list!=null && list.size() > 0){
                                    rvfruitList.setVisibility(View.VISIBLE);
                                    llEmpty.setVisibility(View.GONE);
                                }else {
                                    rvfruitList.setVisibility(View.GONE);
                                    llEmpty.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        return false;
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void addItem(List<Fruit> listAdd) {
        //如果是加载第一页，需要先清空数据列表
        this.list.clear();
        if (listAdd!=null){
            //添加数据
            this.list.addAll(listAdd);
        }
        //通知RecyclerView进行改变--整体
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView author_name;
        private TextView date;
        private ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author_name = itemView.findViewById(R.id.author_name);
            date = itemView.findViewById(R.id.date);
            img = itemView.findViewById(R.id.img);
        }
    }

    public interface ItemListener{
        void ItemClick(Fruit fruit);
    }
    // 将JSON数据解析成Fruit列表
    public List<Fruit> parseJsonToList(String jsonData) {
        List<Fruit> fruitList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int typeId = jsonObject.getInt("typeId");
                String title = jsonObject.getString("title");
                String img = jsonObject.getString("img");
                String content = jsonObject.getString("content");
                String issuer = jsonObject.getString("issuer");
                String date = jsonObject.getString("date");

                Fruit fruit = new Fruit(typeId, title, img, content, issuer, date);
                fruitList.add(fruit);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fruitList;
    }
}
