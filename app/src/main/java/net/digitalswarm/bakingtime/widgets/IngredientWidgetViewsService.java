package net.digitalswarm.bakingtime.widgets;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class IngredientWidgetViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientWidgetViewsFactory(this);
    }
}
