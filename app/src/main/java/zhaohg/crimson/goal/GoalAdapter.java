package zhaohg.crimson.goal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
        viewHolder.setGoal(goal);
        viewHolder.checkBoxTitle.setText(goal.getTitle());
        viewHolder.checkBoxTitle.setChecked(goal.isFinished());
        viewHolder.textViewTomatoSpent.setText(context.getString(R.string.goal_item_text_tomato_spent) + " " + goal.getTomatoSpent());
        viewHolder.textViewTimeSpent.setText(context.getString(R.string.goal_item_text_time_spent) + " " + goal.getFormatedMinuteSpent(context));
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
        notifyDataSetChanged();
    }

    void append(Goal goal) {
        if (!isExisted(goal)) {
            int pos = goals.size();
            goals.add(goal);
            notifyItemInserted(pos);
            notifyItemRangeChanged(pos, 1);
            notifyDataSetChanged();
        }
    }

    public void append(List<Goal> goals) {
        for (Goal goal : goals) {
            append(goal);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Goal goal;

        public final CheckBox checkBoxTitle;
        public final TextView textViewTomatoSpent;
        public final TextView textViewTimeSpent;

        public ViewHolder(View view) {
            super(view);
            checkBoxTitle = (CheckBox) view.findViewById(R.id.text_view_title);
            checkBoxTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    GoalData goalData = new GoalData(context);
                    goalData.updateFinished(goal, isChecked);
                }
            });
            textViewTomatoSpent = (TextView) view.findViewById(R.id.text_view_tomato_spent);
            textViewTimeSpent = (TextView) view.findViewById(R.id.text_view_time_spent);
            view.setOnClickListener(new OnPostClickerListener());
        }

        public Goal getGoal() {
            return goal;
        }

        public void setGoal(Goal goal) {
            this.goal = goal;
        }

        private class OnPostClickerListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(GoalActivity.KEY_GOAL_ID, goal.getId());
                context.startActivity(intent);
            }
        }
    }

}
