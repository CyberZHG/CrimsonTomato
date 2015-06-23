package zhaohg.crimson.data;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.Date;

public class TestSetting extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testSetAndGet() {
        Context context = this.getInstrumentation().getTargetContext();
        Setting setting = Setting.getInstance();
        setting.init(context);
        setting.setPeriod(30);
        assertEquals(30, setting.getPeriod());
        setting.setPeriod(25);
        assertEquals(25, setting.getPeriod());
        setting.setVibrate(false);
        assertFalse(setting.isVibrate());
        setting.setVibrate(true);
        assertTrue(setting.isVibrate());
        setting.setLastBegin(new Date());
        assertNotNull(setting.getLastBegin());
        setting.setLastBegin(null);
        assertNull(setting.getLastBegin());
    }

}
