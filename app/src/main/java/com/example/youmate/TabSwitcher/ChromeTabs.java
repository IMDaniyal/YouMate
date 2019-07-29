package com.example.youmate.TabSwitcher;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.youmate.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

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

import static de.mrapp.android.util.DisplayUtil.getDisplayWidth;

public class ChromeTabs extends AppCompatActivity implements TabSwitcherListener
{




    /**
     * The state of tabs, which display list items in a list view.
     */




    String firsturl ="";
    List<String> urls = new ArrayList();
    int currentindex=0;
    int indexgoing=0;
    int indexcoming=0;


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

    private class State extends AbstractState
            implements AbstractDataBinder.Listener<ArrayAdapter<String>, Tab, ListView, Void>,
            TabPreviewListener {

        /**
         * The adapter, which contains the list items of the tab.
         */
        private ArrayAdapter<String> adapter;

        /**
         * Creates a new state of a tab, which displays list items in a list view.
         *
         * @param tab
         *         The tab, the state should correspond to, as an instance of the class {@link Tab}.
         *         The tab may not be null
         */
        State(@NonNull final Tab tab) {
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

            if (adapter == null) {
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

                outState.putStringArray(String.format(ADAPTER_STATE_EXTRA, getTab().getTitle()),
                        array);
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
        public boolean onLoadTabPreview(@NonNull final TabSwitcher tabSwitcher,
                                        @NonNull final Tab tab)
        {
            return !getTab().equals(tab) || adapter != null;
        }

    }

    /**
     * The decorator, which is used to inflate and visualize the tabs of the activity's tab
     * switcher.
     */
    private class Decorator extends StatefulTabSwitcherDecorator<State> {

        @Nullable
        @Override
        protected State onCreateState(@NonNull final Context context,
                                      @NonNull final TabSwitcher tabSwitcher,
                                      @NonNull final View view, @NonNull final Tab tab,
                                      final int index, final int viewType,
                                      @Nullable final Bundle savedInstanceState)
        {



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
                //view = inflater.inflate(R.layout.tab_text_view, parent, false);
                view = inflater.inflate(R.layout.tab_edit_text, parent, false);
            }else
                {
              // startActivity(new Intent(getApplicationContext(), TabEditActivity.class));
                 view = inflater.inflate(R.layout.tab_edit_text, parent, false);

            } /*else {
               // view = inflater.inflate(R.layout.tab_list_view, parent, false);
         //   }*/

            Toolbar toolbar = view.findViewById(R.id.toolbar);
            toolbar.inflateMenu(R.menu.tab);
            toolbar.setOnMenuItemClickListener(createToolbarMenuListener());
            Menu menu = toolbar.getMenu();
            TabSwitcher.setupWithMenu(tabSwitcher, menu, createTabSwitcherButtonListener());
            return view;
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
            web.getSettings().setJavaScriptEnabled(true);
            web.getSettings().setLoadWithOverviewMode(true);
            web.getSettings().setUseWideViewPort(true);

            web.setWebViewClient(new WebViewClient()
            {



                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
             //       urltext.setText(url);
                    view.loadUrl(url);
                    return true;
                }
                @Override
                public void onPageFinished(WebView view, final String url) {
            //      urltext.setText(url);
                }
            });


if(currentindex%2==0)
{
    if(web !=null)
    {
      if(flag)
        {
            flag=false;
            web.loadUrl(urls.get(currentindex));
        }
    }
}

            BottomNavigationView bottomNavigationView;
            if (viewType == 1) {
                final EditText edu = findViewById(R.id.edu);

                if(web !=null)
                {
                    if(flag)
                    {
                        flag=false;
                            web.loadUrl(urls.get(currentindex));
                    }
                }
/*
                ImageButton imgbtn=findViewById(R.id.imgboo);
                imgbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick( View v ) {
                        startActivity(new Intent(getApplicationContext(), BookMarkPage.class));
                    }
                });
                */
               /*
                TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction( TextView v, int actionId, KeyEvent event ) {

                        switch (actionId)
                        {
                            case EditorInfo
                                    .IME_ACTION_SEARCH:
                                String ip = edu.getText().toString().trim();
                                Intent iweb = new Intent(getApplicationContext(), WebActivity.class);
                                iweb.putExtra("IP", ip);
                                startActivity(iweb);
                                break;
                        }

                        return false;
                    }
                };

                edu.setOnEditorActionListener(editorActionListener);
*/
                if (savedInstanceState == null)
                {


                }
                /*
                bottomNavigationView=findViewById(R.id.nav1);
                bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
                    @Override
                    public void onNavigationItemReselected( @NonNull MenuItem menuItem ) {
                        switch (menuItem.getItemId()){
                            case R.id.item1:
                                startActivity(new Intent(getApplicationContext(),Main2Activity.class));
                        finish();
                                break;
                            case R.id.item2:
                                Intent i=new Intent(getApplicationContext(), MainTry.class);
                                startActivity(i);
                                finish();
                                break;
                            case R.id.item3:
                           //     startActivity(new Intent(getApplicationContext(), ChromeTabs.class));
                           //     finish();
                                Toast.makeText(getApplicationContext(), "Already in Tab", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.item4:
                                startActivity(new Intent(getApplicationContext(),Download.class));
                                finish();
                                break;
                            case R.id.item5:
                                startActivity(new Intent(getApplicationContext(),AccountActivity.class));
                                finish();
                                break;
                        }

                    }
                });
*/

             //   edu.requestFocus();
            }
            else{
                final EditText edu = findViewById(R.id.edu);
             /*   ImageButton imgbtn=findViewById(R.id.imgboo);
                imgbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick( View v ) {
                        startActivity(new Intent(getApplicationContext(), BookMarkPage.class));
                    }
                });*/

             /*
                TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction( TextView v, int actionId, KeyEvent event ) {

                        switch (actionId) {
                            case EditorInfo
                                    .IME_ACTION_SEARCH:
                                String ip = edu.getText().toString().trim();
                                Intent iweb = new Intent(getApplicationContext(), WebActivity.class);
                                iweb.putExtra("IP", ip);
                                startActivity(iweb);
                                break;
                        }

                        return false;
                    }
                };
                edu.setOnEditorActionListener(editorActionListener);
*/

          /*
                BottomNavigationView bottomNavigationView1;
                bottomNavigationView1=findViewById(R.id.nav1);
                bottomNavigationView1.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
                    @Override
                    public void onNavigationItemReselected( @NonNull MenuItem menuItem ) {
                        switch (menuItem.getItemId()){
                            case R.id.item1:
                                startActivity(new Intent(getApplicationContext(),Main2Activity.class));
                        finish();
                                break;
                            case R.id.item2:
                                Intent i=new Intent(getApplicationContext(),MainTry.class);
        startActivity(i);
        finish();
                                break;
                            case R.id.item3:
                                Toast.makeText(getApplicationContext(), "Already in Tab", Toast.LENGTH_SHORT).show();

                                //     startActivity(new Intent(getApplicationContext(), ChromeTabs.class));
                           //     finish();
                                break;
                            case R.id.item4:
                                startActivity(new Intent(getApplicationContext(),Download.class));
                                finish();
                                break;
                            case R.id.item5:
                                startActivity(new Intent(getApplicationContext(),AccountActivity.class));
                                finish();
                                break;
                        }

                    }
                });
*/
            }
            /*else if (viewType == 0 && state != null) {
//                ListView listView = findViewById(android.R.id.list);
//                state.loadItems(listView);
            }*/

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

    /**
     * A data binder, which is used to asynchronously load the list items, which are displayed by a
     * tab.
     */
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
     * The name of the extra, which is used to store the view type of a tab within a bundle.
     */
    private static final String VIEW_TYPE_EXTRA = ChromeTabs.class.getName() + "::ViewType";

    /**
     * The name of the extra, which is used to store the state of a list adapter within a bundle.
     */
    private static final String ADAPTER_STATE_EXTRA = State.class.getName() + "::%s::AdapterState";

    /**
     * The number of tabs, which are contained by the example app's tab switcher.
     */
    private static final int TAB_COUNT = 1;

    /**
     * The activity's tab switcher.
     */
    private TabSwitcher tabSwitcher;

    /**
     * The decorator of the activity's tab switcher.
     */
    private Decorator decorator;

    /**
     * The activity's snackbar.
     */
    private Snackbar snackbar;

    /**
     * The data binder, which is used to load the list items of tabs.
     */
    private DataBinder dataBinder;

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


        return new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.remove_tab_menu_item:
                        Tab selectedTab = tabSwitcher.getSelectedTab();


                        if (selectedTab != null) {
                            tabSwitcher.removeTab(selectedTab);
                        }

                        return true;

                    case R.id.add_tab_menu_item:
                        int index = tabSwitcher.getCount();
                        Tab tab = createTab(index);

                        if (tabSwitcher.isSwitcherShown()) {
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
    private AddTabButtonListener createAddTabButtonListener() {
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
                urls.remove(indexcoming);
                urls.add(indexcoming,url);
                indexcoming = indexgoing;

            }

        }

        if(currentindex>=urls.size())
        {
            urls.add(currentindex,"https://www.google.com");
        }



        Log.i("ss","ss");

/*
        if(urls!=null&&urls.size() >0)
        {
            if(web!=null)
            {
                urls.remove(selectedTabIndex);
                urls.add(selectedTabIndex,web.getOriginalUrl());
            }

        }
        */

          Log.i("tab","switched");
    }

    @Override
    public final void onTabAdded(@NonNull final TabSwitcher tabSwitcher, final int index,
                                 @NonNull final Tab tab, @NonNull final Animation animation) {
        inflateMenu();
        TabSwitcher.setupWithMenu(tabSwitcher, createTabSwitcherButtonListener());
    }

    @Override
    public final void onTabRemoved(@NonNull final TabSwitcher tabSwitcher, final int index,
                                   @NonNull final Tab tab, @NonNull final Animation animation) {
        CharSequence text = getString(R.string.removed_tab_snackbar, tab.getTitle());
        showUndoSnackbar(text, index, tab);
        inflateMenu();
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

    int loadedfromstate=0;
    Bundle webdata;
    TextView urltext;
    Handler handler;
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
          if(firsturl ==null)
          {
              urls.add(0,"https://www.google.com");
          }
          else
          {
              urls.add(0,firsturl);
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

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            /* my set of codes for repeated work */
            if(web !=null)
            {
                if(urltext !=null)
                {
                  if(!urltext.getText().toString().equals(web.getUrl()))
                  {
                    urltext.setText(web.getUrl());
                  }

                }
            }
            handler.postDelayed(this, 1000); // reschedule the handler
        }
    };// new handler

}
