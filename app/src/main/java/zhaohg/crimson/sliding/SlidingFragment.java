package zhaohg.crimson.sliding;

import android.support.v4.app.Fragment;

public class SlidingFragment extends Fragment {

    private String title;

    public SlidingFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

}