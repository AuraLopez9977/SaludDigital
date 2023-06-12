package fia.ues.saluddigital.PageLoop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import fia.ues.saluddigital.R;

public class ViewPager extends AppCompatActivity {
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;

    private ViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        viewPager2 = findViewById(R.id.view_pager2);
        iv1 = findViewById(R.id.imgViev1);
        iv2 = findViewById(R.id.imgViev2);
        iv3 = findViewById(R.id.imgViev3);

        List<Integer> images= new ArrayList<>();
        images.add(R.drawable.receta1);
        images.add(R.drawable.receta2);
        images.add(R.drawable.receta3);

        ViewPageAdapter adapter = new ViewPageAdapter(images);
        viewPager2.setAdapter(adapter);


        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                changeColor();
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                changeColor();
            }
        });
    }

    public void changeColor(){
        int currentItem = viewPager2.getCurrentItem();

        switch (currentItem) {
            case 0:
                iv1.setBackgroundColor(getResources().getColor(R.color.teal_200));
                iv2.setBackgroundColor(getResources().getColor(R.color.grey));
                iv3.setBackgroundColor(getResources().getColor(R.color.grey));
                break;

            case 1:
                iv1.setBackgroundColor(getResources().getColor(R.color.grey));
                iv2.setBackgroundColor(getResources().getColor(R.color.teal_200));
                iv3.setBackgroundColor(getResources().getColor(R.color.grey));
                break;
            case 2:
                iv1.setBackgroundColor(getResources().getColor(R.color.grey));
                iv2.setBackgroundColor(getResources().getColor(R.color.grey));
                iv3.setBackgroundColor(getResources().getColor(R.color.teal_200));
                break;
        }

    }
}