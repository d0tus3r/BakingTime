package net.digitalswarm.bakingtime.widgets;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class WidgetUpdateService extends IntentService {

    //vars
    public static final String ACTION_UPDATE_WIDGETS = "net.digitalswarm.bakingtime.widgets.WidgetUpdateService.update_app_widget";
    public static final String ACTION_UPDATE_LIST_VIEW = "net.digitalswarm.bakingtime.widgets.WidgetUpdateService.update_app_widget_list";

    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final String action = intent.getAction();
        if (ACTION_UPDATE_WIDGETS.equals(action)) {
            handleActionUpdateAppWidgets();
        } else if(ACTION_UPDATE_LIST_VIEW.equals(action)) {
            handleActionUpdateListView();
        }
    }

    private void handleActionUpdateListView() {

    }
}
