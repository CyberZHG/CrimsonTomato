package zhaohg.crimson.goal;

import android.content.Context;
import android.graphics.Color;

import zhaohg.crimson.R;

public class Priority {

    public static final int PRIORITY_BLUE = 0;
    public static final int PRIORITY_GREEN = 1;
    public static final int PRIORITY_ORANGE = 2;
    public static final int PRIORITY_RED = 3;
    public static final int PRIORITY_PURPLE = 4;

    public static int getPriorityColor(Context context, int priority) {
        switch (priority) {
            case PRIORITY_BLUE:
                return context.getResources().getColor((R.color.priority_blue));
            case PRIORITY_GREEN:
                return context.getResources().getColor((R.color.priority_green));
            case PRIORITY_ORANGE:
                return context.getResources().getColor((R.color.priority_orange));
            case PRIORITY_RED:
                return context.getResources().getColor((R.color.priority_red));
            case PRIORITY_PURPLE:
                return context.getResources().getColor((R.color.priority_purple));
        }
        return Color.WHITE;
    }
}
