package zhaohg.crimson.goal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Vector;

import zhaohg.crimson.R;
import zhaohg.crimson.data.DatabaseUtil;

public class GoalFragment extends Fragment {

    private static final String BUNDLE_KEY_SHOW_TYPE = "show_type";

    public static final int SHOW_ALL = 0;
    public static final int SHOW_UNFINISHED = 1;
    public static final int SHOW_FINISHED = 2;

    private int showType;

    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager layoutManager;
    private RecyclerView recycleGoals;

    private boolean refresh = false;
    private int pageNum = 0;

    public static GoalFragment newInstance(int showType) {
        GoalFragment fragment = new GoalFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_SHOW_TYPE, showType);
        fragment.setArguments(bundle);
        return fragment;
    }

    public GoalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.showType = getArguments().getInt(BUNDLE_KEY_SHOW_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goal, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.recycleGoals = (RecyclerView) this.getView().findViewById(R.id.recycle_goal);
        this.layoutManager = new LinearLayoutManager(this.getActivity());
        this.recycleGoals.setLayoutManager(layoutManager);
        GoalAdapter adapter = new GoalAdapter(this.getActivity());
        this.recycleGoals.setAdapter(adapter);
        this.recycleGoals.addOnScrollListener(new OnGoalScrollListener());

        this.swipeRefreshLayout = (SwipeRefreshLayout) this.getView().findViewById(R.id.swipe_refresh);
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!refresh) {
                    refresh = true;
                    pageNum = 0;
                    loadNextPage();
                }
            }
        });

        loadNextPage();
    }

    private void loadNextPage() {
        GoalData goalData = new GoalData(this.getActivity());
        Vector<Goal> goals;
        switch (this.showType) {
            case SHOW_UNFINISHED:
                goals = goalData.getUnfinishedGoalsOnPage(pageNum);
                break;
            case SHOW_FINISHED:
                goals = goalData.getFinishedGoalsOnPage(pageNum);
                break;
            default:
                goals = goalData.getGoalsOnPage(pageNum);
        }
        GoalAdapter adapter = (GoalAdapter) recycleGoals.getAdapter();
        if (refresh) {
            refresh = false;
            swipeRefreshLayout.setRefreshing(false);
            adapter.clear();
        }
        adapter.append(goals);
        if (goals.size() == DatabaseUtil.PAGE_SIZE) {
            ++pageNum;
        }
    }

    private class OnGoalScrollListener extends RecyclerView.OnScrollListener {

        private int pastVisibleItems;
        private int visibleItemCount;
        private int totalItemCount;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            totalItemCount = layoutManager.getItemCount();
            visibleItemCount = layoutManager.getChildCount();
            pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                loadNextPage();
            }
            if (pastVisibleItems == 0) {
                swipeRefreshLayout.setEnabled(true);
            } else {
                swipeRefreshLayout.setEnabled(false);
            }
        }

    }

}
