package com.dongseo.vegechecker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilteringMaterialActivity extends AppCompatActivity {
    RecyclerView mRecyclerView, aRecyclerView;
    MaterialRecyclerAdapter mRecyclerAdapter, aRecyclerAdapter;
    ArrayList<MaterialItem> mMaterialItems, aMaterialItems;
    MaterialItem items;
    String rawItem, material, allergy;
    String[] materials, allergys;
    String[] meatList = {"돼지", "소고기", "비프", "어묵", "맛살", "수육", "새우", "굴", "게", "홍합", "오징어", "전복", "고등어", "쇠", "닭", "참치", "육수", "계란"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_result);

        mRecyclerView = (RecyclerView) findViewById(R.id.materialList);
        aRecyclerView = (RecyclerView) findViewById(R.id.allegyList);

        mRecyclerAdapter = new MaterialRecyclerAdapter();
        aRecyclerAdapter = new MaterialRecyclerAdapter();

        mRecyclerView.setAdapter(mRecyclerAdapter);
        aRecyclerView.setAdapter(aRecyclerAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        aRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMaterialItems = new ArrayList<>();
        aMaterialItems = new ArrayList<>();
        setMaterials();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("result", "success!");
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setMaterials(){
        items = (MaterialItem) getIntent().getSerializableExtra("items");
        setFoodName(items.productName);
        rawItem = items.material;
        rawItem = rawItem.replace("원재료 : ", "");
        String[] arr = rawItem.split("알러지 : ");
        material = arr[0];
        allergy = arr[1];
        materials = splitMaterial(material);
        allergys = splitMaterial(allergy);
        //materials = material.split(",");
        //allergys = allergy.split(",");

        for(int i=0;i<materials.length;i++){
            mMaterialItems.add(new MaterialItem(materials[i], items.productName, checkMeat(materials[i])));
        }
        for(int i=0;i<allergys.length;i++){
            aMaterialItems.add(new MaterialItem(allergys[i], items.productName, false));
        }

        mRecyclerAdapter.setMaterialItems(mMaterialItems);
        aRecyclerAdapter.setMaterialItems(aMaterialItems);
    }

    private String[] splitMaterial(String items){
        Pattern p = Pattern.compile("([ㄱ-ㅎ|ㅏ-ㅣ|가-힣]+\\/)?[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]+(\\([ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*,?[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*\\))?");
        Matcher m = p.matcher(items);
        ArrayList<String> matched = new ArrayList<String>();
        while(m.find()){
            matched.add(m.group());
        }
        return matched.toArray(new String[0]);
    }
    private boolean checkMeat(String item){
        for (int checkI = 0; checkI < meatList.length; checkI++){
            if (item.contains(meatList[checkI])){
                Log.d("FilteringMaterialActivity", item + " " + meatList[checkI]);
                return true;
            }
        }
        return false;
    }

    private void setFoodName(String foodName){
        TextView FoodName = (TextView) findViewById(R.id.foodName);
        FoodName.setText(foodName);
    }
}
