package ml.w568w.wizard;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * Created by w568w on 2017-7-9.
 */

public abstract class BaseWizard extends Fragment {
    ViewPager pager;
    Settings settings;
    Button prev;
    Button next;
    protected Activity mCon;

    public class Settings {
        final String prev;
        final String next;
        final String finish;
        final WizardStep[] fragments;

        public Settings(String prev, String next, String finish, WizardStep[] fragments) {
            this.prev = prev;
            this.next = next;
            this.finish = finish;
            this.fragments = fragments;
        }
    }

    protected abstract Settings getSettings();

    int position = 0;

    public void onWizardComplete() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCon = getActivity();
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.wizard_layout, null);
        pager = linearLayout.findViewById(R.id.view_pager);
        prev = linearLayout.findViewById(R.id.button_prev);
        next = linearLayout.findViewById(R.id.button_next);
        settings = getSettings();
        for (WizardStep ws : settings.fragments) {
            ws.setContext(mCon);
            ws.setWizard(this);
        }
        prev.setText(settings.prev);
        if (settings.fragments.length == 1) {
            next.setText(settings.finish);
        } else {
            next.setText(settings.next);
        }
        pager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager()));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BaseWizard.this.position = position;
                prev.setEnabled(position > 0);
                if (position == settings.fragments.length - 1) {
                    next.setText(settings.finish);
                } else {
                    next.setText(settings.next);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollToPrev();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollToNext();
            }
        });
        return linearLayout;
    }

    public void scrollToNext() {
        if (position < settings.fragments.length - 1) {
            pager.arrowScroll(View.FOCUS_FORWARD);
            position++;
            settings.fragments[position].internalOnShown();
        } else {
            onWizardComplete();
        }
    }

    public void notifyComplete(boolean completed) {
        next.setEnabled(completed);
    }

    public void scrollToPrev() {
        if (position > 0) {
            pager.arrowScroll(View.FOCUS_BACKWARD);
            position--;
            settings.fragments[position].internalOnShown();
        }
    }

    public void scrollTo(int page) {
        int orgPosition = page - position;
        if (page >= settings.fragments.length) {
            return;
        }
        if (orgPosition >= 0) {
            for (int i = 0; i < orgPosition; i++) {
                pager.arrowScroll(View.FOCUS_FORWARD);
            }
        } else {
            orgPosition = -orgPosition;
            for (int i = 0; i < orgPosition; i++) {
                pager.arrowScroll(View.FOCUS_BACKWARD);
            }
        }
        position = page;
        settings.fragments[position].internalOnShown();
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return settings.fragments[position];
        }

        @Override
        public int getCount() {
            return settings.fragments.length;
        }
    }

}
