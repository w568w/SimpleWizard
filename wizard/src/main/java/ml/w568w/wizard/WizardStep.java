package ml.w568w.wizard;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * @author w568w
 * @date 2017-7-9
 */

public class WizardStep extends Fragment {
    protected Activity mCon;
    protected BaseWizard mBase;
    private boolean mCompleted = true;

    public void notifyComplete(boolean completed) {
        mCompleted = completed;
        mBase.notifyComplete(mCompleted);

    }

    protected final void internalOnShown() {
        mBase.notifyComplete(mCompleted);
        onShown();
    }

    public void onShown() {
    }

    public void setContext(Activity mCon) {
        this.mCon = mCon;
    }

    protected final void setWizard(BaseWizard mBase) {
        this.mBase = mBase;
    }
}
