package zhaohg.crimson.tomato;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import zhaohg.crimson.R;
import zhaohg.crimson.data.DatabaseUtil;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

        SwipeableRecyclerViewTouchListener swipeableRecyclerViewTouchListener =
                new SwipeableRecyclerViewTouchListener(recycleTomatoes,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {

                            @Override
                            public boolean canSwipe(int i) {
                                return true;
                            }

                            private void onDismissed(int[] positions) {
                                TomatoAdapter adapter = (TomatoAdapter) recycleTomatoes.getAdapter();
                                for (int position : positions) {
                                    Tomato tomato = adapter.getAt(position);
                                    if (tomato != null) {
                                        TomatoData tomatoData = new TomatoData(getApplicationContext());
                                        tomatoData.deleteTomato(tomato.getId());
                                    }
                                    adapter.removeAt(position);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] positions) {
                                onDismissed(positions);
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] positions) {
                                onDismissed(positions);
                            }
                        });
        recycleTomatoes.addOnItemTouchListener(swipeableRecyclerViewTouchListener);

        loadNextPage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.menu_item_sync_all:
                this.trySyncAll();
                break;
            case R.id.menu_item_clear_history:
                this.tryClearHistory();
                break;
            case R.id.menu_item_export_csv:
                this.exportToCsv();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void trySyncAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_sync_all_title));
        builder.setMessage(getString(R.string.dialog_sync_all_message));
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
                        tomatoData.syncToCalendar();
                        dialog.dismiss();
                    }
                });
        builder.show();
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

    private void exportToCsv() {
        File sdCard = Environment.getExternalStorageDirectory();
        if (sdCard == null) {
            Toast.makeText(this, getString(R.string.toast_export_external_unavailable), Toast.LENGTH_LONG).show();
            return;
        }
        File dir = new File(sdCard.getAbsolutePath() + "/data/zhaohg.crimson/");
        if (!dir.exists() && !dir.mkdirs()) {
            Toast.makeText(this, getString(R.string.toast_export_cannot_create_folder), Toast.LENGTH_LONG).show();
            return;
        }
        String fileName = "export_" + (new Date()).getTime() + ".csv";
        File file = new File(dir, fileName);
        try {
            FileOutputStream output = new FileOutputStream(file);
            TomatoData tomatoData = new TomatoData(this);
            tomatoData.exportToCsv(output);
            output.close();
            Toast.makeText(this, getString(R.string.toast_export_to) + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.toast_export_cannot_write_to_file), Toast.LENGTH_LONG).show();
        }
    }

    void loadNextPage() {
        TomatoData tomatoData = new TomatoData(this);
        Vector<Tomato> tomatoes = tomatoData.getTomatoesOnPage(this.pageNum);
        TomatoAdapter adapter = (TomatoAdapter) recycleTomatoes.getAdapter();
        if (refresh) {
            refresh = false;
            swipeRefreshLayout.setRefreshing(false);
            adapter.clear();
        }
        adapter.append(tomatoes);
        if (tomatoes.size() == DatabaseUtil.PAGE_SIZE) {
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
