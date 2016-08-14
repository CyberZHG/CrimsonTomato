package zhaohg.crimson.goal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Vector;

import zhaohg.crimson.R;

public class PriorityGroupView extends LinearLayout {

    private int priority;
    private Vector<PriorityItemView> itemViews = new Vector<>();
    private OnPriorityChangedListener onPriorityChangedListener;

    public PriorityGroupView(Context context) {
        super(context);
        this.initItems();
    }

    public PriorityGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initItems();
    }

    private class OnItemClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            PriorityItemView itemView = (PriorityItemView) view;
            if (itemView.getPriority() != priority) {
                for (PriorityItemView item : itemViews) {
                    item.setSelected(false);
                }
                itemView.setSelected(true);
                priority = itemView.getPriority();
                if (onPriorityChangedListener != null) {
                    onPriorityChangedListener.onPriorityChanged(priority);
                }
            }
        }
    }

    private void initItems() {
        priority = Priority.PRIORITY_BLUE;
        inflate(getContext(), R.layout.priority_group, this);

        PriorityItemView itemBlue = (PriorityItemView) findViewById(R.id.priority_item_blue);
        itemBlue.setPriority(Priority.PRIORITY_BLUE);
        itemBlue.setOnClickListener(new OnItemClickListener());
        itemViews.add(itemBlue);

        PriorityItemView itemGreen = (PriorityItemView) findViewById(R.id.priority_item_green);
        itemGreen.setPriority(Priority.PRIORITY_GREEN);
        itemGreen.setOnClickListener(new OnItemClickListener());
        itemViews.add(itemGreen);

        PriorityItemView itemOrange = (PriorityItemView) findViewById(R.id.priority_item_orange);
        itemOrange.setPriority(Priority.PRIORITY_ORANGE);
        itemOrange.setOnClickListener(new OnItemClickListener());
        itemViews.add(itemOrange);

        PriorityItemView itemRed = (PriorityItemView) findViewById(R.id.priority_item_red);
        itemRed.setPriority(Priority.PRIORITY_RED);
        itemRed.setOnClickListener(new OnItemClickListener());
        itemViews.add(itemRed);

        PriorityItemView itemPurple = (PriorityItemView) findViewById(R.id.priority_item_purple);
        itemPurple.setPriority(Priority.PRIORITY_PURPLE);
        itemPurple.setOnClickListener(new OnItemClickListener());
        itemViews.add(itemPurple);
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        if (this.priority != priority) {
            this.priority = priority;
            for (PriorityItemView item : itemViews) {
                item.setSelected(item.getPriority() == priority);
            }
        }
    }

    public void setOnPriorityChangedListener(OnPriorityChangedListener onPriorityChangedListener) {
        this.onPriorityChangedListener = onPriorityChangedListener;
    }

    public interface OnPriorityChangedListener {
        void onPriorityChanged(int priority);
    }

}
