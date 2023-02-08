// Generated by view binder compiler. Do not edit!
package acr.browser.lightning.databinding;

import acr.browser.lightning.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final CoordinatorLayout rootView;

  @NonNull
  public final FrameLayout bookmarkDrawer;

  @NonNull
  public final CoordinatorLayout coordinatorLayout;

  @NonNull
  public final DrawerLayout drawerLayout;

  @NonNull
  public final FrameLayout tabDrawer;

  @NonNull
  public final LinearLayout uiLayout;

  private ActivityMainBinding(@NonNull CoordinatorLayout rootView,
      @NonNull FrameLayout bookmarkDrawer, @NonNull CoordinatorLayout coordinatorLayout,
      @NonNull DrawerLayout drawerLayout, @NonNull FrameLayout tabDrawer,
      @NonNull LinearLayout uiLayout) {
    this.rootView = rootView;
    this.bookmarkDrawer = bookmarkDrawer;
    this.coordinatorLayout = coordinatorLayout;
    this.drawerLayout = drawerLayout;
    this.tabDrawer = tabDrawer;
    this.uiLayout = uiLayout;
  }

  @Override
  @NonNull
  public CoordinatorLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bookmark_drawer;
      FrameLayout bookmarkDrawer = ViewBindings.findChildViewById(rootView, id);
      if (bookmarkDrawer == null) {
        break missingId;
      }

      CoordinatorLayout coordinatorLayout = (CoordinatorLayout) rootView;

      id = R.id.drawer_layout;
      DrawerLayout drawerLayout = ViewBindings.findChildViewById(rootView, id);
      if (drawerLayout == null) {
        break missingId;
      }

      id = R.id.tab_drawer;
      FrameLayout tabDrawer = ViewBindings.findChildViewById(rootView, id);
      if (tabDrawer == null) {
        break missingId;
      }

      id = R.id.ui_layout;
      LinearLayout uiLayout = ViewBindings.findChildViewById(rootView, id);
      if (uiLayout == null) {
        break missingId;
      }

      return new ActivityMainBinding((CoordinatorLayout) rootView, bookmarkDrawer,
          coordinatorLayout, drawerLayout, tabDrawer, uiLayout);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}