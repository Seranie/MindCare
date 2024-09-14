package com.example.mind_care.home;

import androidx.viewpager2.widget.ViewPager2;

import com.example.mind_care.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewpagerNavigationMediator {
    private BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager;

    public ViewpagerNavigationMediator(BottomNavigationView bottomNavigationView, ViewPager2 viewPager) {
        this.bottomNavigationView = bottomNavigationView;
        this.viewPager = viewPager;
    }

    private void attachBottomNavigation(){
        //Clicking on bottom nav changes viewpager page
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.reminders_tab) {
                viewPager.setCurrentItem(0);
                return true;
            } else if (itemId == R.id.contacts_tab) {
                viewPager.setCurrentItem(1);
                return true;
            } else if (itemId == R.id.fences_tab) {
                viewPager.setCurrentItem(2);
                return true;
            }
            else if (itemId == R.id.chat_buddy_tab) {
                viewPager.setCurrentItem(3);
                return true;
            }
            return false;
        });
    }

    private void attachViewPager(){
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position >= 2){
                    //skip middle menu set for fab
                    bottomNavigationView.getMenu().getItem(position + 1).setChecked(true);
                }
                else {
                    bottomNavigationView.getMenu().getItem(position).setChecked(true);
                }
            }
        });
    }

    public void attach(){
        attachBottomNavigation();
        attachViewPager();
    }
}
