package com.example.youmate.TabSwitcher;

import android.Manifest;
import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.youmate.AccountActivity;
import com.example.youmate.Download;
import com.example.youmate.Main2Activity;
import com.example.youmate.MainTry;
import com.example.youmate.PrefManager;
import com.example.youmate.R;
import com.example.youmate.VideoPlayerActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.File;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.mrapp.android.tabswitcher.AbstractState;
import de.mrapp.android.tabswitcher.AddTabButtonListener;
import de.mrapp.android.tabswitcher.Animation;
import de.mrapp.android.tabswitcher.Layout;
import de.mrapp.android.tabswitcher.PeekAnimation;
import de.mrapp.android.tabswitcher.PullDownGesture;
import de.mrapp.android.tabswitcher.RevealAnimation;
import de.mrapp.android.tabswitcher.StatefulTabSwitcherDecorator;
import de.mrapp.android.tabswitcher.SwipeGesture;
import de.mrapp.android.tabswitcher.Tab;
import de.mrapp.android.tabswitcher.TabPreviewListener;
import de.mrapp.android.tabswitcher.TabSwitcher;
import de.mrapp.android.tabswitcher.TabSwitcherListener;
import de.mrapp.android.util.ThemeUtil;
import de.mrapp.android.util.multithreading.AbstractDataBinder;
import de.mrapp.util.Condition;

import static android.view.View.LAYER_TYPE_HARDWARE;
import static android.view.View.LAYER_TYPE_SOFTWARE;
import static de.mrapp.android.util.DisplayUtil.getDisplayWidth;

public class ChromeTabs extends AppCompatActivity implements TabSwitcherListener
{

  String firsturl ="";
  List<String> urls = new ArrayList();
  int currentindex=0;
  int indexgoing=0;
  int indexcoming=0;
  private static final String VIEW_TYPE_EXTRA = ChromeTabs.class.getName() + "::ViewType";
  private static final String ADAPTER_STATE_EXTRA = State.class.getName() + "::%s::AdapterState";
  private static final int TAB_COUNT = 1;
  private TabSwitcher tabSwitcher;
  private Decorator decorator;
  private Snackbar snackbar;
  private DataBinder dataBinder;
  int loadedfromstate=0;
  Bundle webdata;
  EditText urltext;
  Handler handler;
  private int isInternetConnected=1;
  private static final String ARG_POSITION = "position";
  private static final int PERMISSION_CALLBACK_CONSTANT = 101;
  private static final int REQUEST_PERMISSION_SETTING = 102;
  private SharedPreferences permissionStatus;
  private boolean sentToSettings = false;
  private PrefManager pref;
  public static ArrayList<String> downloadlist=new ArrayList<String>();
  public static ArrayList<String> profilepublicList=new ArrayList<>();
  static int fbcheck=0;
  boolean addfromhome=false;
  static  int goingtohome= 0;



  void exit()
  {
    super.onBackPressed();
  }

  @Override
  public void onBackPressed()
  {
    if(web.canGoBack())
    {
      web.goBack();
    }
    else
    {
      final AlertDialog alertDialog = new AlertDialog.Builder(ChromeTabs.this).create();
      alertDialog.setTitle("Alert Dialog");
      alertDialog.setMessage("are you sure to exit tabs ?");
      //  alertDialog.setIcon(R.drawable.welcome);
      alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "yes", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which)
        {
          exit();
        }
      });
      alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          alertDialog.dismiss();
        }
      });
      alertDialog.show();
    }

  }

  /**
   * Creates a listener, which allows to apply the window insets to the tab switcher's padding.
   *
   * @return The listener, which has been created, as an instance of the type {@link
   * OnApplyWindowInsetsListener}. The listener may not be nullFG
   */
  @NonNull
  private OnApplyWindowInsetsListener createWindowInsetsListener() {
    return new OnApplyWindowInsetsListener() {

      @Override
      public WindowInsetsCompat onApplyWindowInsets(final View v,
          final WindowInsetsCompat insets) {
        int left = insets.getSystemWindowInsetLeft();
        int top = insets.getSystemWindowInsetTop();
        int right = insets.getSystemWindowInsetRight();
        int bottom = insets.getSystemWindowInsetBottom();
        tabSwitcher.setPadding(left, top, right, bottom);
        float touchableAreaTop = top;

        if (tabSwitcher.getLayout() == Layout.TABLET) {
          touchableAreaTop += getResources()
              .getDimensionPixelSize(R.dimen.tablet_tab_container_height);
        }

        RectF touchableArea = new RectF(left, touchableAreaTop,
            getDisplayWidth(ChromeTabs.this) - right, touchableAreaTop +
            ThemeUtil.getDimensionPixelSize(ChromeTabs.this, R.attr.actionBarSize));
        tabSwitcher.addDragGesture(
            new SwipeGesture.Builder().setTouchableArea(touchableArea).create());
        tabSwitcher.addDragGesture(
            new PullDownGesture.Builder().setTouchableArea(touchableArea).create());
        return insets;
      }

    };
  }

  /**
   * Creates and returns a listener, which allows to add a tab to the activity's tab switcher,
   * when a button is clicked.
   *
   * @return The listener, which has been created, as an instance of the type {@link
   * View.OnClickListener}. The listener may not be null
   */
  @NonNull
  private View.OnClickListener createAddTabListener() {
    return new View.OnClickListener() {

      @Override
      public void onClick(final View view) {
        int index = tabSwitcher.getCount();
        Animation animation = createRevealAnimation();
        tabSwitcher.addTab(createTab(index), 0, animation);
      }

    };
  }

  /**
   * Creates and returns a listener, which allows to observe, when an item of the tab switcher's
   * toolbar has been clicked.
   *
   * @return The listener, which has been created, as an instance of the type {@link
   * Toolbar.OnMenuItemClickListener}. The listener may not be null
   */
  @NonNull
  private Toolbar.OnMenuItemClickListener createToolbarMenuListener() {


    return new Toolbar.OnMenuItemClickListener()
    {

      @Override
      public boolean onMenuItemClick(final MenuItem item) {
        switch (item.getItemId()) {
          case R.id.remove_tab_menu_item:
            Tab selectedTab = tabSwitcher.getSelectedTab();


            if (selectedTab != null)
            {
              tabSwitcher.removeTab(selectedTab);
            }

            return true;

          case R.id.add_tab_menu_item:
            int index = tabSwitcher.getCount();
            Tab tab = createTab(index);

            if (tabSwitcher.isSwitcherShown())
            {
              tabSwitcher.addTab(tab, 0, createRevealAnimation());
            } else {
              tabSwitcher.addTab(tab, 0, createPeekAnimation());
            }

            return true;
          case R.id.clear_tabs_menu_item:
            tabSwitcher.clear();
            return true;

          case R.id.Add_bookmark:
            if(web !=null)
            {
              urlcopy = web.getUrl();
            }
            final SharedPreferences sharedPreferences=getSharedPreferences("MyPref",MODE_PRIVATE);
            String a=sharedPreferences.getString("Url","");
            if(a!="") {
              a = a + "," + urlcopy;
            }
            else
            {
              a=urlcopy;
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Url",a);
            editor.commit();
            Toast.makeText(ChromeTabs.this, "BookMarked", Toast.LENGTH_SHORT).show();
            return true;
//                    case R.id.settings_menu_item:
//                        Intent intent = new Intent(ChromeTabs.this, SettingsActivity.class);
//                        startActivity(intent);
          default:
            return false;
        }
      }

    };
  }

  /**
   * Inflates the tab switcher's menu, depending on whether it is empty, or not.
   */
  private void inflateMenu()
  {
    tabSwitcher
        .inflateToolbarMenu(tabSwitcher.getCount() > 0 ? R.menu.tab_switcher : R.menu.tab,
            createToolbarMenuListener());
  }

  /**
   * Creates and returns a listener, which allows to toggle the visibility of the tab switcher,
   * when a button is clicked.
   *
   * @return The listener, which has been created, as an instance of the type {@link
   * View.OnClickListener}. The listener may not be null
   */
  @NonNull
  private View.OnClickListener createTabSwitcherButtonListener()
  {
    return new View.OnClickListener() {

      @Override
      public void onClick(final View view) {
        tabSwitcher.toggleSwitcherVisibility();
      }

    };
  }

  /**
   * Creates and returns a listener, which allows to add a new tab to the tab switcher, when the
   * corresponding button is clicked.
   *
   * @return The listener, which has been created, as an instance of the type {@link
   * AddTabButtonListener}. The listener may not be null
   */
  @NonNull
  private AddTabButtonListener createAddTabButtonListener()
  {
    return new AddTabButtonListener() {

      @Override
      public void onAddTab(@NonNull final TabSwitcher tabSwitcher) {
        int index = tabSwitcher.getCount();
        Tab tab = createTab(index);
        tabSwitcher.addTab(tab, 0);
      }

    };
  }

  /**
   * Creates and returns a listener, which allows to undo the removal of tabs from the tab
   * switcher, when the button of the activity's snackbar is clicked.
   *
   * @param snackbar
   *         The activity's snackbar as an instance of the class {@link Snackbar}. The snackbar
   *         may not be null
   * @param index
   *         The index of the first tab, which has been removed, as an {@link Integer} value
   * @param tabs
   *         An array, which contains the tabs, which have been removed, as an array of the type
   *         {@link Tab}. The array may not be null
   * @return The listener, which has been created, as an instance of the type {@link
   * View.OnClickListener}. The listener may not be null
   */
  @NonNull
  private View.OnClickListener createUndoSnackbarListener(@NonNull final Snackbar snackbar,
      final int index,
      @NonNull final Tab... tabs) {
    return new View.OnClickListener() {

      @Override
      public void onClick(final View view) {
        snackbar.setAction(null, null);

        if (tabSwitcher.isSwitcherShown()) {
          tabSwitcher.addAllTabs(tabs, index);
        } else if (tabs.length == 1) {
          tabSwitcher.addTab(tabs[0], 0, createPeekAnimation());
        }

      }

    };
  }

  /**
   * Creates and returns a callback, which allows to observe, when a snackbar, which allows to
   * undo the removal of tabs, has been dismissed.
   *
   * @param tabs
   *         An array, which contains the tabs, which have been removed, as an array of the type
   *         {@link Tab}. The tab may not be null
   * @return The callback, which has been created, as an instance of the type class {@link
   * BaseTransientBottomBar.BaseCallback}. The callback may not be null
   */
  @NonNull
  private BaseTransientBottomBar.BaseCallback<Snackbar> createUndoSnackbarCallback(
      final Tab... tabs) {
    return new BaseTransientBottomBar.BaseCallback<Snackbar>() {

      @Override
      public void onDismissed(final Snackbar snackbar, final int event)
      {
        if (event != DISMISS_EVENT_ACTION)
        {
          for (Tab tab : tabs)
          {
            tabSwitcher.clearSavedState(tab);
            decorator.clearState(tab);
          }
        }
      }
    };
  }

  /**
   * Shows a snackbar, which allows to undo the removal of tabs from the activity's tab switcher.
   *
   * @param text
   *         The text of the snackbar as an instance of the type {@link CharSequence}. The text
   *         may not be null
   * @param index
   *         The index of the first tab, which has been removed, as an {@link Integer} value
   * @param tabs
   *         An array, which contains the tabs, which have been removed, as an array of the type
   *         {@link Tab}. The array may not be null
   */
  private void showUndoSnackbar(@NonNull final CharSequence text, final int index,
      @NonNull final Tab... tabs) {
    snackbar = Snackbar.make(tabSwitcher, text, Snackbar.LENGTH_LONG).setActionTextColor(
        ContextCompat.getColor(this, R.color.snackbar_action_text_color));
    snackbar.setAction(R.string.undo, createUndoSnackbarListener(snackbar, index, tabs));
    snackbar.addCallback(createUndoSnackbarCallback(tabs));
    snackbar.show();
  }

  /**
   * Creates a reveal animation, which can be used to add a tab to the activity's tab switcher.
   *
   * @return The reveal animation, which has been created, as an instance of the class {@link
   * Animation}. The animation may not be null
   */
  @NonNull
  private Animation createRevealAnimation() {
    float x = 0;
    float y = 0;
    View view = getNavigationMenuItem();

    if (view != null) {
      int[] location = new int[2];
      view.getLocationInWindow(location);
      x = location[0] + (view.getWidth() / 2f);
      y = location[1] + (view.getHeight() / 2f);
    }

    return new RevealAnimation.Builder().setX(x).setY(y).create();
  }

  /**
   * Creates a peek animation, which can be used to add a tab to the activity's tab switcher.
   *
   * @return The peek animation, which has been created, as an instance of the class {@link
   * Animation}. The animation may not be null
   */
  @NonNull
  private Animation createPeekAnimation() {
    return new PeekAnimation.Builder().setX(tabSwitcher.getWidth() / 2f).create();
  }

  /**
   * Returns the menu item, which shows the navigation icon of the tab switcher's toolbar.
   *
   * @return The menu item, which shows the navigation icon of the tab switcher's toolbar, as an
   * instance of the class {@link View} or null, if no navigation icon is shown
   */
  @Nullable
  private View getNavigationMenuItem() {
    Toolbar[] toolbars = tabSwitcher.getToolbars();

    if (toolbars != null) {
      Toolbar toolbar = toolbars.length > 1 ? toolbars[1] : toolbars[0];
      int size = toolbar.getChildCount();

      for (int i = 0; i < size; i++) {
        View child = toolbar.getChildAt(i);

        if (child instanceof ImageButton) {
          return child;
        }
      }
    }

    return null;
  }

  /**
   * Creates and returns a tab.
   *
   * @param index
   *         The index, the tab should be added at, as an {@link Integer} value
   * @return The tab, which has been created, as an instance of the class {@link Tab}. The tab may
   * not be null
   */
  @NonNull
  private Tab createTab(final int index)
  {
    CharSequence title = getString(R.string.tab_title, index + 1);
    Tab tab = new Tab(title);
    Bundle parameters = new Bundle();
    parameters.putInt(VIEW_TYPE_EXTRA, index % 3);
    tab.setParameters(parameters);
    return tab;
  }

  @Override
  public final void onSwitcherShown(@NonNull final TabSwitcher tabSwitcher)
  {

    web.saveState(webdata);
    loadedfromstate=1;
    url = web.getUrl();
    indexcoming = currentindex;
    Log.i("sss","dd");
  }

  @Override
  public final void onSwitcherHidden(@NonNull final TabSwitcher tabSwitcher) {


    if (snackbar != null)
    {
      snackbar.dismiss();
    }

  }

  @Override
  public final void onSelectionChanged(@NonNull final TabSwitcher tabSwitcher, final int selectedTabIndex, @Nullable final Tab selectedTab)
  {

    if(selectedTab != null)
    {
      indexgoing = Integer.parseInt(selectedTab.getTitle().toString().substring(4));
    }

    indexgoing -=1;
    currentindex= indexgoing;
    flag=true;
    Log.i("ss","ss");

    if(urls!=null&&urls.size() >0)
    {
      if(web!=null)
      {
        if(urls.size()>0)
        {
          if(indexcoming !=-1)
          {

            if(url!=null &&url.length()>0)
            {
              if(indexcoming<urls.size())
              {
                urls.remove(indexcoming);
                urls.add(indexcoming,url);
                indexcoming = indexgoing;
              }

            }

          }
        }
      }

    }

    if(currentindex>=urls.size())
    {

      if(!addfromhome)
      {
        try{
          urls.add(currentindex,"https://www.google.com");
        }
        catch (Exception e)
        {
          urls.add(0,"https://www.google.com");
        }

      }
      else
      {
          addfromhome=false;
      }



    }
    Log.i("tab","switched");
  }

  @Override
  public final void onTabAdded(@NonNull final TabSwitcher tabSwitcher, final int index,
      @NonNull final Tab tab, @NonNull final Animation animation)
  {
    inflateMenu();
    TabSwitcher.setupWithMenu(tabSwitcher, createTabSwitcherButtonListener());
  }

  @Override
  public final void onTabRemoved(@NonNull final TabSwitcher tabSwitcher, final int index,
      @NonNull final Tab tab, @NonNull final Animation animation) {
    CharSequence text = getString(R.string.removed_tab_snackbar, tab.getTitle());
    showUndoSnackbar(text, index, tab);
    int removed = Integer.parseInt(tab.getTitle().toString().substring(4));
    removed -=1;
    inflateMenu();
    try
    {
      urls.remove(removed);
    }
    catch (Exception e)
    {
        urls.clear();
        currentindex=0;
        indexcoming=0;

    }

    if(currentindex>=urls.size())
    {
      currentindex--;
    }
    TabSwitcher.setupWithMenu(tabSwitcher, createTabSwitcherButtonListener());
  }

  @Override
  public final void onAllTabsRemoved(@NonNull final TabSwitcher tabSwitcher,
      @NonNull final Tab[] tabs,
      @NonNull final Animation animation) {
    CharSequence text = getString(R.string.cleared_tabs_snackbar);
    showUndoSnackbar(text, 0, tabs);
    inflateMenu();
    TabSwitcher.setupWithMenu(tabSwitcher, createTabSwitcherButtonListener());
  }

  @Override
  public final void setTheme(final int resid) {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    String themeKey = getString(R.string.theme_preference_key);
    String themeDefaultValue = getString(R.string.theme_preference_default_value);
    int theme = Integer.valueOf(sharedPreferences.getString(themeKey, themeDefaultValue));

    if (theme != 0) {
      super.setTheme(R.style.AppTheme_Translucent_Dark);
    } else {
      super.setTheme(R.style.AppTheme_Translucent_Light);
    }
  }

  WebView web;
  boolean flag = true;
  String url="";
  String urlcopy="";


  @Override
  protected void onResume() {
    super.onResume();
    if(webdata!=null)
    {


    }

  }

  @Override
  protected void onPause()
  {
    super.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
    urls.remove(currentindex);
    urls.add(currentindex,web.getUrl());
    flag=true;
  }




  public  void setpermissions()
  {
    fbcheck=1;
    permissionStatus = getSharedPreferences("permissionStatus",this.MODE_PRIVATE);
    if(isInternetConnected==0)
    {
    //  Snackbar.make(,"Please Connect to Internet", Snackbar.LENGTH_LONG).show();
    }
    if(ActivityCompat.checkSelfPermission(this, permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
      if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission.WRITE_EXTERNAL_STORAGE)){
        //Show Information about why you need the permission
        Builder builder = new Builder(ChromeTabs.this);
        builder.setTitle("Need Storage Permission");
        builder.setMessage("This app needs phone permission.");
        builder.setPositiveButton("Grant", new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            if (VERSION.SDK_INT >= VERSION_CODES.M)
            {
              requestPermissions(new String[]{permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE},PERMISSION_CALLBACK_CONSTANT);
            }
          }
        });
        builder.setNegativeButton("Cancel", new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });
        builder.show();
      } else if (permissionStatus.getBoolean(permission.WRITE_EXTERNAL_STORAGE,false)) {
        //Previously Permission Request was cancelled with 'Dont Ask Again',
        // Redirect to Settings after showing Information about why you need the permission
        Builder builder = new Builder(this);
        builder.setTitle("Need Storage Permission");
        builder.setMessage("This app needs storage permission.");
        builder.setPositiveButton("Grant", new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            sentToSettings = true;
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getCallingActivity().getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
            Toast.makeText(getApplicationContext(), "Go to Permissions to Grant Phone", Toast.LENGTH_LONG).show();
          }
        });
        builder.setNegativeButton("Cancel", new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });
        builder.show();
      }
      else
        {
        //just request the permission
        // Check if we're running on Android 5.0 or higher
          if (VERSION.SDK_INT >= VERSION_CODES.M)
          {
            requestPermissions(new String[]{permission.WRITE_EXTERNAL_STORAGE},PERMISSION_CALLBACK_CONSTANT);
          }
      }
      Editor editor = permissionStatus.edit();
      editor.putBoolean(permission.WRITE_EXTERNAL_STORAGE,true);
      editor.commit();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if(requestCode == PERMISSION_CALLBACK_CONSTANT){
      //check if all permissions are granted
      boolean allgranted = false;
      for(int i=0;i<grantResults.length;i++){
        if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
          allgranted = true;
        } else {
          allgranted = false;
          break;
        }
      }
      if(allgranted)
      {
        //  Toast.makeText(getActivity(),"Permissions Granted",Toast.LENGTH_LONG).show();
        web.loadUrl("https://m.facebook.com/");
      } else if(ActivityCompat.shouldShowRequestPermissionRationale(ChromeTabs.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
        AlertDialog.Builder builder = new AlertDialog.Builder(ChromeTabs.this);
        builder.setTitle("Need Storage Permission");
        builder.setMessage("This app needs phone permission.");
        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            if (VERSION.SDK_INT >= VERSION_CODES.M)
            {
              requestPermissions(new String[]{permission.WRITE_EXTERNAL_STORAGE},PERMISSION_CALLBACK_CONSTANT);
            }
          }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });
        builder.show();
      } else {
        Toast.makeText(ChromeTabs.this,"Unable to get Permission", Toast.LENGTH_LONG).show();
      }
    }
  }
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if(requestCode==131)
    {

      if(data!=null && data.getStringExtra("newurl")!=null)
      {
        int index = tabSwitcher.getCount();
        // indexcoming=index-1;
        indexcoming= goingtohome;
        Tab tab = createTab(index);
        addfromhome=true;
        url=web.getUrl();
        tabSwitcher.addTab(tab, index, createRevealAnimation());
        String ur = data.getStringExtra("newurl");
        ur.replace("www.","");
        if(ur.contains("http://")||ur.contains("https://"))
        {

        }else
        {
          ur= "http://"+ur;
        }

        if(ur.contains(" ")|| !ur.contains("."))
        {
          ur= "https://www.google.com/search?q="+ur;
        }
        urls.add(index,ur);
        currentindex = index;


      }



}
    if (requestCode == REQUEST_PERMISSION_SETTING)
        {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        //Got Permission
        Toast.makeText(this,"Permissions Granted", Toast.LENGTH_LONG).show();
        }
        }
        }

private boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager
    = (ConnectivityManager)ChromeTabs.this.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

public ArrayList<String> getList() {
    return downloadlist;
    }


  @Override
  protected final void onCreate(final Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chrome_tabs);
    dataBinder = new DataBinder(this);
    decorator = new Decorator();
    tabSwitcher = findViewById(R.id.tab_switcher);
    tabSwitcher.clearSavedStatesWhenRemovingTabs(false);
    Intent old = getIntent();
    firsturl = old.getStringExtra("IP");
    pref = new PrefManager(ChromeTabs.this);
    if(firsturl ==null)
    {
      if(urls.size()==0)
      {
        if(!addfromhome)
        {
          urls.add(0,"https://www.google.com");
        }

      }
    }
    else
    {
      String ur = firsturl;
      ur.replace("www.","");
      if(ur.contains("http://")||ur.contains("https://"))
      {

      }else
      {
        ur= "http://"+ur;
      }

      if(ur.contains(" ")|| !ur.contains("."))
      {
        ur= "https://www.google.com/search?q="+ur;
      }
       urls.add(0,ur);
    if(firsturl.contains("facebook.com"))
    {
      setpermissions();
    }
    }
    webdata=new Bundle();
    ViewCompat.setOnApplyWindowInsetsListener(tabSwitcher, createWindowInsetsListener());
    tabSwitcher.setDecorator(decorator);
    tabSwitcher.addListener(this);
    tabSwitcher.showToolbars(true);
    for (int i = 0; i < TAB_COUNT; i++)
    {
      tabSwitcher.addTab(createTab(i));
    }

    tabSwitcher.showAddTabButton(createAddTabButtonListener());
    tabSwitcher.setToolbarNavigationIcon(R.drawable.ic_plus_24dp, createAddTabListener());
    TabSwitcher.setupWithMenu(tabSwitcher, createTabSwitcherButtonListener());
    inflateMenu();

    handler = new Handler();
    handler.postDelayed(runnable, 1000);

  }

  private Runnable runnable = new Runnable()
  {
    @Override
    public void run()
    {

      if(web !=null)
      {
        if(urltext !=null)
        {
          if(urltext.hasFocus())
          {
           urltext.setOnEditorActionListener(new OnEditorActionListener() {
             @Override
             public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               boolean handled = false;
               if (actionId == EditorInfo.IME_ACTION_DONE)
               {
                 String ur = urltext.getText().toString();
                 ur.replace("www.","");
                 if(ur.contains("http://")||ur.contains("https://"))
                 {

                 }else
                 {
                   ur= "http://"+ur;
                 }

                 if(ur.contains(" ")|| !ur.contains("."))
                 {
                   ur= "https://www.google.com/search?q="+ur;
                 }

                web.loadUrl(ur);
                urls.remove(currentindex);
                urls.add(currentindex,ur);
                 urltext.setFocusableInTouchMode(false);
                 urltext.setFocusable(false);
                 urltext.setFocusableInTouchMode(true);
                 urltext.setFocusable(true);
                 handled = true;
               }
               return handled;
             }
           });
          }

          else if(!urltext.getText().toString().equals(web.getUrl()))
          {
            urltext.setText(web.getUrl());
          }

        }
      }
      handler.postDelayed(this, 1000);
    }
  };


  private class State extends AbstractState
      implements AbstractDataBinder.Listener<ArrayAdapter<String>, Tab, ListView, Void>,
      TabPreviewListener {


    private ArrayAdapter<String> adapter;

    /**
     * Creates a new state of a tab, which displays list items in a list view.
     *
     * @param tab
     *         The tab, the state should correspond to, as an instance of the class {@link Tab}.
     *         The tab may not be null
     */
    State(@NonNull final Tab tab)
    {
      super(tab);
    }

    /**
     * Loads the list items of the tab.
     *
     * @param listView
     *         The list view, which should be used to display the list items, as an instance of
     *         the class {@link ListView}. The list view may not be null
     */
    public void loadItems(@NonNull final ListView listView)
    {
      Condition.INSTANCE.ensureNotNull(listView, "The list view may not be null");

      if (adapter == null)
      {
        dataBinder.addListener(this);
        dataBinder.load(getTab(), listView);
      } else if (listView.getAdapter() != adapter) {
        listView.setAdapter(adapter);
      }
    }

    @Override
    public boolean onLoadData(
        @NonNull final AbstractDataBinder<ArrayAdapter<String>, Tab, ListView, Void> dataBinder,
        @NonNull final Tab key, @NonNull final Void... params) {
      return true;
    }

    @Override
    public void onCanceled(
        @NonNull final AbstractDataBinder<ArrayAdapter<String>, Tab, ListView, Void> dataBinder) {

    }

    @Override
    public void onFinished(
        @NonNull final AbstractDataBinder<ArrayAdapter<String>, Tab, ListView, Void> dataBinder,
        @NonNull final Tab key, @Nullable final ArrayAdapter<String> data,
        @NonNull final ListView view, @NonNull final Void... params) {
      if (getTab().equals(key)) {
        view.setAdapter(data);
        adapter = data;
        dataBinder.removeListener(this);
      }
    }



    @Override
    public final void saveInstanceState(@NonNull final Bundle outState)
    {
      if (adapter != null && !adapter.isEmpty()) {
        String[] array = new String[adapter.getCount()];
        for (int i = 0; i < array.length; i++) {
          array[i] = adapter.getItem(i);
        }
        outState.putStringArray(String.format(ADAPTER_STATE_EXTRA, getTab().getTitle()),array);
      }
    }

    @Override
    public void restoreInstanceState(@Nullable final Bundle savedInstanceState)
    {
      if (savedInstanceState != null) {
        String key = String.format(ADAPTER_STATE_EXTRA, getTab().getTitle());
        String[] items = savedInstanceState.getStringArray(key);

        if (items != null && items.length > 0)
        {
          adapter = new ArrayAdapter<>(ChromeTabs.this,
              android.R.layout.simple_list_item_1, items);
        }
      }
    }

    @Override
    public boolean onLoadTabPreview(@NonNull final TabSwitcher tabSwitcher,@NonNull final Tab tab)
    {
      return !getTab().equals(tab) || adapter != null;
    }

  }

  private static class DataBinder
      extends AbstractDataBinder<ArrayAdapter<String>, Tab, ListView, Void> {

    /**
     * Creates a new data binder, which is used to asynchronously load the list items, which are
     * displayed by a tab.
     *
     * @param context
     *         The context, which should be used by the data binder, as an instance of the class
     *         {@link Context}. The context may not be null
     */
    public DataBinder(@NonNull final Context context) {
      super(context.getApplicationContext());
    }

    @Nullable
    @Override
    protected ArrayAdapter<String> doInBackground(@NonNull final Tab key,
        @NonNull final Void... params) {
      String[] array = new String[10];

      for (int i = 0; i < array.length; i++) {
        array[i] = String.format(Locale.getDefault(), "%s, item %d", key.getTitle(), i + 1);
      }

      try {
        // Simulate a long loading time...
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        // There's nothing we can do...
      }

      return new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, array);
    }

    @Override
    protected void onPostExecute(@NonNull final ListView view,
        @Nullable final ArrayAdapter<String> data, final long duration,
        @NonNull final Void... params) {
      if (data != null) {
        view.setAdapter(data);
      }
    }

  }

  /**
   * The decorator, which is used to inflate and visualize the tabs of the activity's tab
   * switcher.
   */
  private class Decorator extends StatefulTabSwitcherDecorator<State>
  {

    @Nullable
    @Override
    protected State onCreateState(@NonNull final Context context, @NonNull final TabSwitcher tabSwitcher, @NonNull final View view, @NonNull final Tab tab, final int index, final int viewType, @Nullable final Bundle savedInstanceState)
    {

      /*
      if (viewType == 2)
      {
        State state = new State(tab);
        tabSwitcher.addTabPreviewListener(state);

        if (savedInstanceState != null)
        {
          state.restoreInstanceState(savedInstanceState);
        }

        return state;
      }
      */

      return null;
    }

    @Override
    protected void onClearState(@NonNull final State state) {
      tabSwitcher.removeTabPreviewListener(state);
    }

    @Override
    protected void onSaveInstanceState(@NonNull final View view, @NonNull final Tab tab, final int index, final int viewType, @Nullable final State state, @NonNull final Bundle outState)
    {

      if (state != null)
      {
        state.saveInstanceState(outState);
      }
    }

    @NonNull
    @Override
    public View onInflateView(@NonNull final LayoutInflater inflater,
        @Nullable final ViewGroup parent, final int viewType)
    {
      View view = null;

      if (viewType == 0)
      {

        view = inflater.inflate(R.layout.tab_edit_text, parent, false);
      }else
      {
        view = inflater.inflate(R.layout.tab_edit_text, parent, false);

      }

      Toolbar toolbar = view.findViewById(R.id.toolbar);
      toolbar.inflateMenu(R.menu.tab);
      toolbar.setOnMenuItemClickListener(createToolbarMenuListener());
      Menu menu = toolbar.getMenu();
      TabSwitcher.setupWithMenu(tabSwitcher, menu, createTabSwitcherButtonListener());
      return view;
    }

    @JavascriptInterface
    public void getData(final String pathvideo)
    {
      Log.d("scroled","jo");

      final AlertDialog alertDialog = new AlertDialog.Builder(ChromeTabs.this).create();
      alertDialog.setTitle("Save Video?");
      alertDialog.setMessage("Do you Really want to Save Video ?");
      //  alertDialog.setIcon(R.drawable.welcome);

      alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "yes", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which)
        {
          String finalurl ;
          finalurl=pathvideo;
          finalurl=finalurl.replaceAll("%3A",":");
          finalurl=finalurl.replaceAll("%2F","/");
          finalurl=finalurl.replaceAll("%3F","?");
          finalurl=finalurl.replaceAll("%3D","=");
          finalurl=finalurl.replaceAll("%26","&");
          downloadvideo(finalurl);
          updataLevel();
        }
      });
      alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Watch", new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          if(isNetworkAvailable())
          {
            String finalurl;
            finalurl = pathvideo;
            finalurl = finalurl.replaceAll("%3A", ":");
            finalurl = finalurl.replaceAll("%2F", "/");
            finalurl = finalurl.replaceAll("%3F", "?");
            finalurl = finalurl.replaceAll("%3D", "=");
            finalurl = finalurl.replaceAll("%26", "&");
            Intent intent = new Intent(ChromeTabs.this, VideoPlayerActivity.class);
            intent.putExtra("videofilename", finalurl);
            startActivity(intent);
          }
          else
          {
            Toast.makeText(ChromeTabs.this,"Please Connect to Internet", Toast.LENGTH_LONG).show();
          }
        }
      });

      alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Copy Url", new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
          String finalurl ;
          finalurl=pathvideo;
          finalurl=finalurl.replaceAll("%3A",":");
          finalurl=finalurl.replaceAll("%2F","/");
          finalurl=finalurl.replaceAll("%3F","?");
          finalurl=finalurl.replaceAll("%3D","=");
          finalurl=finalurl.replaceAll("%26","&");
          ClipboardManager clipboard = (ClipboardManager)ChromeTabs.this.getSystemService(Context.CLIPBOARD_SERVICE);
          ClipData clip = ClipData.newPlainText("mainurlcopy",finalurl);
          clipboard.setPrimaryClip(clip);
          Toast.makeText(ChromeTabs.this,"Url Copied", Toast.LENGTH_SHORT).show();
        }
      });
      alertDialog.show();


    }
    public void updataLevel () {
      if(FirebaseAuth.getInstance().getCurrentUser() != null) {
        final String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference noteRef = db.collection("UserInfo").document(userEmail);
        noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
          @Override
          public void onSuccess(DocumentSnapshot documentSnapshot) {
            Double valueLevel = documentSnapshot.getDouble("level");
            Double valuePoint = documentSnapshot.getDouble("point");
            try {
              //checking for level
              if (valuePoint == 50 || valuePoint == 100 || valuePoint == 150 || valuePoint == 200) {

                noteRef.update("email", userEmail,
                    "level", valueLevel + 1, "point", valuePoint + 5).addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess( Void aVoid ) {
                    Toast.makeText(ChromeTabs.this, "Congratulations for next level", Toast.LENGTH_SHORT).show();

                  }
                }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure( @NonNull Exception e ) {
                    Toast.makeText(ChromeTabs.this, "Points not updated", Toast.LENGTH_SHORT).show();
                  }
                });

              } else {
                noteRef.update("email", userEmail,
                    "point", valuePoint + 5).addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess( Void aVoid ) {
                    Toast.makeText(ChromeTabs.this, "Points updated", Toast.LENGTH_SHORT).show();
                  }
                }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure( @NonNull Exception e ) {
                    Toast.makeText(ChromeTabs.this, "Points not updated", Toast.LENGTH_SHORT).show();
                  }
                });
              }

            }
            catch (Exception e)
            {
              Toast.makeText(ChromeTabs.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
      else{
        Toast.makeText(ChromeTabs.this.getApplicationContext(), "Login to get points.", Toast.LENGTH_SHORT).show();
      }
    }
    public void downloadvideo(String pathvideo)
    {
      if(pathvideo.contains(".mp4"))
      {
        File directory = new File(Environment.getExternalStorageDirectory()+ File.separator+"Facebook Videos");
        directory.mkdirs();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pathvideo));
        int Number=pref.getFileName();
        request.allowScanningByMediaScanner();
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        File root = new File(Environment.getExternalStorageDirectory() + File.separator+"Facebook Videos");
        Uri path = Uri.withAppendedPath(Uri.fromFile(root), "Video-"+Number+".mp4");
        request.setDestinationUri(path);
        DownloadManager dm = (DownloadManager)ChromeTabs.this.getSystemService(ChromeTabs.DOWNLOAD_SERVICE);
        if(downloadlist.contains(pathvideo))
        {
          Toast.makeText(ChromeTabs.this.getApplicationContext(),"The Video is Already Downloading", Toast.LENGTH_LONG).show();
        }
        else
        {
          downloadlist.add(pathvideo);
          dm.enqueue(request);
          Toast.makeText(ChromeTabs.this.getApplicationContext(),"Downloading Video-"+Number+".mp4", Toast.LENGTH_LONG).show();
          Number++;
          pref.setFileName(Number);
        }

      }
    }


    @Override
    public void onShowTab(@NonNull final Context context,
        @NonNull final TabSwitcher tabSwitcher, @NonNull final View view,
        @NonNull final Tab tab, final int index, final int viewType,
        @Nullable final State state,
        @Nullable final Bundle savedInstanceState)
    {

      TextView textView = findViewById(android.R.id.title);
      textView.setText(tab.getTitle());
      Toolbar toolbar = findViewById(R.id.toolbar);
      toolbar.setVisibility(tabSwitcher.isSwitcherShown() ? View.GONE : View.VISIBLE);
      web = view.findViewById(R.id.loadweb);
      urltext = view.findViewById(R.id.urltext);
      web.getSettings().setLoadWithOverviewMode(true);
      web.getSettings().setUseWideViewPort(true);

      if(fbcheck==0)
      {
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient()
        {

          @Override
          public void onPageStarted(WebView view, String url, Bitmap favicon)
          {
            super.onPageStarted(view, url, favicon);
          }

          @Override
          public boolean shouldOverrideUrlLoading(WebView view, String url)
          {

            view.loadUrl(url);
            return true;
          }
          @Override
          public void onPageFinished(WebView view, final String url)
          {

          }
        });

      }
      else
      {

        web.getSettings().setSupportZoom(true);       //Zoom Control on web (You don't need this
        web.getSettings().setBuiltInZoomControls(true);
        web.addJavascriptInterface(this, "mJava");
        web.getSettings().setJavaScriptEnabled(true);

        web.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 19)
        {
          web.setLayerType(LAYER_TYPE_HARDWARE, null);
        }
        else

        {
          web.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        web.setWebViewClient(new WebViewClient()
        {
          public void onPageFinished(WebView view, String url)
          {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
              @Override
              public void run()
              {

                web.loadUrl("javascript:"+
                    "var e=0;\n" +
                    "window.onscroll=function()\n" +
                    "{\n" +
                    "\tvar ij=document.querySelectorAll(\"video\");\n" +
                    "\t\tfor(var f=0;f<ij.length;f++)\n" +
                    "\t\t{\n" +
                    "\t\t\tif((ij[f].parentNode.querySelectorAll(\"img\")).length==0)\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\tvar nextimageWidth=ij[f].nextSibling.style.width;\n" +
                    "\t\t\t\tvar nextImageHeight=ij[f].nextSibling.style.height;\n" +
                    "\t\t\t\tvar Nxtimgwd=parseInt(nextimageWidth, 10);\n" +
                    "\t\t\t\tvar Nxtimghght=parseInt(nextImageHeight, 10); \n" +
                    "\t\t\t\tvar DOM_img = document.createElement(\"img\");\n" +
                    "\t\t\t\t\tDOM_img.height=\"68\";\n" +
                    "\t\t\t\t\tDOM_img.width=\"68\";\n" +
                    "\t\t\t\t\tDOM_img.style.top=(Nxtimghght/2-20)+\"px\";\n" +
                    "\t\t\t\t\tDOM_img.style.left=(Nxtimgwd/2-20)+\"px\";\n" +
                    "\t\t\t\t\tDOM_img.style.position=\"absolute\";\n" +
                    "\t\t\t\t\tDOM_img.src = \"https://image.ibb.co/kobwsk/one.png\"; \n" +
                    "\t\t\t\t\tij[f].parentNode.appendChild(DOM_img);\n" +
                    "\t\t\t}\t\t\n" +
                    "\t\t\tij[f].remove();\n" +
                    "\t\t} \n" +
                    "\t\t\te++;\n" +
                    "};"+
                    "var a = document.querySelectorAll(\"a[href *= 'video_redirect']\");\n" +
                    "for (var i = 0; i < a.length; i++) {\n" +
                    "    var mainUrl = a[i].getAttribute(\"href\");\n" +
                    "  a[i].removeAttribute(\"href\");\n"+
                    "\tmainUrl=mainUrl.split(\"/video_redirect/?src=\")[1];\n" +
                    "\tmainUrl=mainUrl.split(\"&source\")[0];\n" +
                    "    var threeparent = a[i].parentNode.parentNode.parentNode;\n" +
                    "    threeparent.setAttribute(\"src\", mainUrl);\n" +
                    "    threeparent.onclick = function() {\n" +
                    "        var mainUrl1 = this.getAttribute(\"src\");\n" +
                    "         mJava.getData(mainUrl1);\n" +
                    "    };\n" +
                    "}"+
                    "var k = document.querySelectorAll(\"div[data-store]\");\n" +
                    "for (var j = 0; j < k.length; j++) {\n" +
                    "    var h = k[j].getAttribute(\"data-store\");\n" +
                    "    var g = JSON.parse(h);\nvar jp=k[j].getAttribute(\"data-sigil\");\n"+
                    "    if (g.type === \"video\") {\n" +
                    "if(jp==\"inlineVideo\")" +
                    "{" +
                    "   k[j].removeAttribute(\"data-sigil\");" +
                    "}\n" +
                    "        var url = g.src;\n" +
                    "        k[j].setAttribute(\"src\", g.src);\n" +
                    "        k[j].onclick = function() {\n" +
                    "            var mainUrl = this.getAttribute(\"src\");\n" +
                    "               mJava.getData(mainUrl);\n" +
                    "        };\n" +
                    "    }\n" +
                    "\n" +
                    "}");
              }
            }, 3000);
          }

          public void onLoadResource(WebView view, String url)
          {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
              @Override
              public void run() {
                web.loadUrl("javascript:"+
                    "var e=document.querySelectorAll(\"span\"); " +
                    "if(e[0]!=undefined)" +
                    "{"+
                    "var fbforandroid=e[0].innerText;" +
                    "if(fbforandroid.indexOf(\"Facebook\")!=-1)" +
                    "{ " +
                    "var h =e[0].parentNode.parentNode.parentNode.style.display=\"none\";" +
                    "} " +
                    "}" +
                    "var installfb=document.querySelectorAll(\"a\");\n" +
                    "for (var hardwares = 0; hardwares < installfb.length; hardwares++) \n" +
                    "{\n" +
                    "\tif(installfb[hardwares].text.indexOf(\"Install\")!=-1)\n" +
                    "\t{\n" +
                    "\t\tvar soft=installfb[hardwares].parentNode.style.display=\"none\";\n" +
                    "\n" +
                    "\t}\n" +
                    "}\n");
                web.loadUrl("javascript:"+
                    "var e=0;\n" +
                    "window.onscroll=function()\n" +
                    "{\n" +
                    "\tvar ij=document.querySelectorAll(\"video\");\n" +
                    "\t\tfor(var f=0;f<ij.length;f++)\n" +
                    "\t\t{\n" +
                    "\t\t\tif((ij[f].parentNode.querySelectorAll(\"img\")).length==0)\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\tvar nextimageWidth=ij[f].nextSibling.style.width;\n" +
                    "\t\t\t\tvar nextImageHeight=ij[f].nextSibling.style.height;\n" +
                    "\t\t\t\tvar Nxtimgwd=parseInt(nextimageWidth, 10);\n" +
                    "\t\t\t\tvar Nxtimghght=parseInt(nextImageHeight, 10); \n" +
                    "\t\t\t\tvar DOM_img = document.createElement(\"img\");\n" +
                    "\t\t\t\t\tDOM_img.height=\"68\";\n" +
                    "\t\t\t\t\tDOM_img.width=\"68\";\n" +
                    "\t\t\t\t\tDOM_img.style.top=(Nxtimghght/2-20)+\"px\";\n" +
                    "\t\t\t\t\tDOM_img.style.left=(Nxtimgwd/2-20)+\"px\";\n" +
                    "\t\t\t\t\tDOM_img.style.position=\"absolute\";\n" +
                    "\t\t\t\t\tDOM_img.src = \"https://image.ibb.co/kobwsk/one.png\"; \n" +
                    "\t\t\t\t\tij[f].parentNode.appendChild(DOM_img);\n" +
                    "\t\t\t}\t\t\n" +
                    "\t\t\tij[f].remove();\n" +
                    "\t\t} \n" +
                    "\t\t\te++;\n" +
                    "};"+
                    "var a = document.querySelectorAll(\"a[href *= 'video_redirect']\");\n" +
                    "for (var i = 0; i < a.length; i++) {\n" +
                    "    var mainUrl = a[i].getAttribute(\"href\");\n" +
                    "  a[i].removeAttribute(\"href\");\n"+
                    "\tmainUrl=mainUrl.split(\"/video_redirect/?src=\")[1];\n" +
                    "\tmainUrl=mainUrl.split(\"&source\")[0];\n"+
                    "    var threeparent = a[i].parentNode.parentNode.parentNode;\n" +
                    "    threeparent.setAttribute(\"src\", mainUrl);\n" +
                    "    threeparent.onclick = function() {\n" +
                    "        var mainUrl1 = this.getAttribute(\"src\");\n" +
                    "         mJava.getData(mainUrl1);\n" +
                    "    };\n" +
                    "}"+
                    "var k = document.querySelectorAll(\"div[data-store]\");\n" +
                    "for (var j = 0; j < k.length; j++) {\n" +
                    "    var h = k[j].getAttribute(\"data-store\");\n" +
                    "    var g = JSON.parse(h);var jp=k[j].getAttribute(\"data-sigil\");\n"+
                    "    if (g.type === \"video\") {\n" +
                    "if(jp==\"inlineVideo\")" +
                    "{" +
                    "   k[j].removeAttribute(\"data-sigil\");" +
                    "}\n" +
                    "        var url = g.src;\n" +
                    "        k[j].setAttribute(\"src\", g.src);\n" +
                    "        k[j].onclick = function() {\n" +
                    "            var mainUrl = this.getAttribute(\"src\");\n" +
                    "               mJava.getData(mainUrl);\n" +
                    "        };\n" +
                    "    }\n" +
                    "\n" +
                    "}");
              }
            }, 3000);
          }
        });
      }


        if(web !=null)
        {
          if(flag)
          {
            flag=false;
            web.loadUrl(urls.get(currentindex));
          }
        }

      BottomNavigationView bottomNavigationView;
      if (viewType == 1)
      {
        final EditText edu = findViewById(R.id.edu);

        /*
        if(web !=null)
        {
          if(flag)
          {
            flag=false;
            web.loadUrl(urls.get(currentindex));
          }
        }
        */

        if (savedInstanceState == null)
        {


        }

        bottomNavigationView=findViewById(R.id.nav1);
        bottomNavigationView.setSelectedItemId(R.id.item3);

        bottomNavigationView.setOnNavigationItemSelectedListener(
            new OnNavigationItemSelectedListener()
            {
              @Override
              public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
              {
                boolean flag = true;
                Intent chrome;
                switch (menuItem.getItemId()){
                  case R.id.item1:
                    chrome=  new Intent(getApplicationContext(),Main2Activity.class);
                    chrome.putExtra("chorme",1);
                    goingtohome=currentindex;
                    startActivityForResult(chrome,131);
                    break;
                  case R.id.item2:
                    chrome=  new Intent(getApplicationContext(),MainTry.class);
                    chrome.putExtra("chorme",1);
                    startActivity(chrome);
                    // finish();
                    break;
                  case R.id.item3:
                    //     startActivity(new Intent(getApplicationContext(), ChromeTabs.class));
                    //     finish();
                    Toast.makeText(getApplicationContext(), "Already in Tab", Toast.LENGTH_SHORT).show();
                    break;
                  case R.id.item4:
                    chrome=  new Intent(getApplicationContext(),Download.class);
                    chrome.putExtra("chorme",1);
                    startActivity(chrome);
                    break;
                  case R.id.item5:
                    chrome=  new Intent(getApplicationContext(),AccountActivity.class);
                    chrome.putExtra("chorme",1);
                    startActivity(chrome);
                    break;
                }
                return flag;
              }
            });

        /*

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
          @Override
          public void onNavigationItemReselected( @NonNull MenuItem menuItem )
          {
            Intent chrome;
            switch (menuItem.getItemId()){
              case R.id.item1:
                chrome=  new Intent(getApplicationContext(),Main2Activity.class);
                chrome.putExtra("chorme",1);
                goingtohome=currentindex;
                startActivityForResult(chrome,131);
                break;
              case R.id.item2:
                chrome=  new Intent(getApplicationContext(),MainTry.class);
                chrome.putExtra("chorme",1);
                startActivity(chrome);
                // finish();
                break;
              case R.id.item3:
                //     startActivity(new Intent(getApplicationContext(), ChromeTabs.class));
                //     finish();
                Toast.makeText(getApplicationContext(), "Already in Tab", Toast.LENGTH_SHORT).show();
                break;
              case R.id.item4:
                chrome=  new Intent(getApplicationContext(),Download.class);
                chrome.putExtra("chorme",1);
                startActivity(chrome);
                break;
              case R.id.item5:
                chrome=  new Intent(getApplicationContext(),AccountActivity.class);
                chrome.putExtra("chorme",1);
                startActivity(chrome);
                break;
            }

          }
        });
        */

      }
      else{
        final EditText edu = findViewById(R.id.edu);
        BottomNavigationView bottomNavigationView1;
        bottomNavigationView1=findViewById(R.id.nav1);
        bottomNavigationView1.setSelectedItemId(R.id.item3);

        bottomNavigationView1.setOnNavigationItemSelectedListener(
            new OnNavigationItemSelectedListener()
            {
              @Override
              public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
              {
                boolean flag = true;
                Intent chrome;
                switch (menuItem.getItemId()){
                  case R.id.item1:
                    chrome=  new Intent(getApplicationContext(),Main2Activity.class);
                    goingtohome = currentindex;
                    chrome.putExtra("chorme",1);
                    startActivityForResult(chrome,131);

                    break;
                  case R.id.item2:
                    chrome=  new Intent(getApplicationContext(),MainTry.class);
                    chrome.putExtra("chorme",1);
                    startActivity(chrome);
                    // finish();
                    break;
                  case R.id.item3:
                    //     startActivity(new Intent(getApplicationContext(), ChromeTabs.class));
                    //     finish();
                    Toast.makeText(getApplicationContext(), "Already in Tab", Toast.LENGTH_SHORT).show();
                    break;
                  case R.id.item4:
                    chrome=  new Intent(getApplicationContext(),Download.class);
                    chrome.putExtra("chorme",1);
                    startActivity(chrome);
                    break;
                  case R.id.item5:
                    chrome=  new Intent(getApplicationContext(),AccountActivity.class);
                    chrome.putExtra("chorme",1);
                    startActivity(chrome);
                    break;
                }
                return flag;
              }
            });

        /*
        bottomNavigationView1.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
          @Override
          public void onNavigationItemReselected( @NonNull MenuItem menuItem )
          {
            Intent chrome;
            switch (menuItem.getItemId()){
              case R.id.item1:
                chrome=  new Intent(getApplicationContext(),Main2Activity.class);
                goingtohome = currentindex;
                chrome.putExtra("chorme",1);
                startActivityForResult(chrome,131);

                break;
              case R.id.item2:
                chrome=  new Intent(getApplicationContext(),MainTry.class);
                chrome.putExtra("chorme",1);
                startActivity(chrome);
                // finish();
                break;
              case R.id.item3:
                //     startActivity(new Intent(getApplicationContext(), ChromeTabs.class));
                //     finish();
                Toast.makeText(getApplicationContext(), "Already in Tab", Toast.LENGTH_SHORT).show();
                break;
              case R.id.item4:
                chrome=  new Intent(getApplicationContext(),Download.class);
                chrome.putExtra("chorme",1);
                startActivity(chrome);
                break;
              case R.id.item5:
                chrome=  new Intent(getApplicationContext(),AccountActivity.class);
                chrome.putExtra("chorme",1);
                startActivity(chrome);
                break;
            }

          }
        });

        */


      }

      urltext.setText(web.getUrl());
    }

    @Override
    public int getViewTypeCount() {
      return 2;
    }

    @Override
    public int getViewType(@NonNull final Tab tab, final int index) {
      Bundle parameters = tab.getParameters();
      return parameters != null ? parameters.getInt(VIEW_TYPE_EXTRA) : 0;
    }

  }
}