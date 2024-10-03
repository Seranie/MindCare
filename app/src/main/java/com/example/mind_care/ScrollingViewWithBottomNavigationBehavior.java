package com.example.mind_care;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ScrollingViewWithBottomNavigationBehavior extends AppBarLayout.ScrollingViewBehavior {
    private int bottomMargin;


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency) || dependency instanceof BottomNavigationView;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        boolean result = super.onDependentViewChanged(parent, child, dependency);

        if(dependency instanceof BottomNavigationView && dependency.getHeight() != bottomMargin) {
            bottomMargin = dependency.getHeight();
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            layoutParams.bottomMargin = bottomMargin;
            child.setLayoutParams(layoutParams);
            child.requestLayout();
            return true;
        }else { return result; }
    }
}
