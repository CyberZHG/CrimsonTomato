package zhaohg.crimson.main;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Vector;

import zhaohg.crimson.R;
import zhaohg.crimson.data.Tomato;
import zhaohg.crimson.data.TomatoAdapter;
import zhaohg.crimson.data.TomatoData;

public class HistoryActivity extends Activity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager layoutManager;
    private RecyclerView recycleTomatoes;

    private boolean refresh = false;
    private int pageNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        this.recycleTomatoes = (RecyclerView) this.findViewById(R.id.recycle_history);
        this.layoutManager = new LinearLayoutManager(this);
        this.recycleTomatoes.setLayoutManager(layoutManager);
        TomatoAdapter adapter = new TomatoAdapter(this);
        this.recycleTomatoes.setAdapter(adapter);
        this.recycleTomatoes.addOnScrollListener(new OnTomatoScrollListener());

        this.swipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipe_refresh);
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


    public void loadNextPage() {
        TomatoData tomatoData = new TomatoData(this);
        Vector<Tomato> tomatoes = tomatoData.getTomatoesOnPage(this.pageNum);
        TomatoAdapter adapter = (TomatoAdapter) recycleTomatoes.getAdapter();
        if (refresh) {
            refresh = false;
            swipeRefreshLayout.setRefreshing(false);
            adapter.clear();
        }
        adapter.append(tomatoes);
        if (tomatoes.size() == TomatoData.PAGE_SIZE) {
            ++pageNum;
        }
    }

    private class OnTomatoScrollListener extends RecyclerView.OnScrollListener {

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
