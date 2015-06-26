package zhaohg.crimson.goal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zhaohg.crimson.R;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolder>  {

    private final List<Goal> goals;

    private final Context context;

    public GoalAdapter(Context context) {
        this.context = context;
        this.goals = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_goal, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Goal goal = goals.get(i);
        viewHolder.setId(goal.getId());
        viewHolder.textViewTitle.setText(goal.getTitle());
        viewHolder.textViewTomatoSpent.setText(context.getText(R.string.goal_item_text_tomato_spent).toString() + goal.getTomatoSpent());
        viewHolder.textViewMinuteSpent.setText(context.getText(R.string.goal_item_text_minute_spent).toString() + goal.getMinuteSpent());
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    private boolean isExisted(Goal newGoal) {
        for (Goal goal : goals) {
            if (goal.getId() == newGoal.getId()) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        goals.clear();
        notifyDataSetChanged();
    }

    public Goal getAt(int position) {
        if (0 <= position && position < goals.size()) {
            return goals.get(position);
        }
        return null;
    }

    public void removeAt(int position) {
        goals.remove(position);
        notifyItemRemoved(position);
    }

    void append(Goal goal) {
        if (!isExisted(goal)) {
            int pos = goals.size();
            goals.add(goal);
            notifyItemInserted(pos);
            notifyItemRangeChanged(pos, 1);
        }
    }

    public void append(List<Goal> goals) {
        for (Goal goal : goals) {
            append(goal);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private int id;

        public final TextView textViewTitle;
        public final TextView textViewTomatoSpent;
        public final TextView textViewMinuteSpent;

        public ViewHolder(View view) {
            super(view);
            textViewTitle = (TextView) view.findViewById(R.id.text_view_title);
            textViewTomatoSpent = (TextView) view.findViewById(R.id.text_view_tomato_spent);
            textViewMinuteSpent = (TextView) view.findViewById(R.id.text_view_minute_spent);
            view.setOnClickListener(new OnPostClickerListener());
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        private class OnPostClickerListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
            }
        }
    }

}
