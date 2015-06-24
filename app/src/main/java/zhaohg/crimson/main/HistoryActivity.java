package zhaohg.crimson.main;

import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Vector;

import zhaohg.crimson.R;
import zhaohg.crimson.data.Setting;
import zhaohg.crimson.data.Tomato;
import zhaohg.crimson.data.TomatoAdapter;
import zhaohg.crimson.data.TomatoData;

public class HistoryActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager layoutManager;
    private RecyclerView recycleTomatoes;

    private boolean refresh = false;
    private int pageNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.menu_item_clear_history:
                this.tryClearHistory();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void tryClearHistory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_clear_history_title));
        builder.setMessage(getString(R.string.dialog_clear_history_message));
        builder.setNegativeButton(getString(R.string.action_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton(getString(R.string.action_confirm),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TomatoData tomatoData = new TomatoData(getApplicationContext());
                        tomatoData.clearTomato();
                        refresh = true;
                        pageNum = 0;
                        loadNextPage();
                        dialog.dismiss();
                    }
                });
        builder.show();
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
