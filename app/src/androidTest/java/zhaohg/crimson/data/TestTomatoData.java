package zhaohg.crimson.data;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class TestTomatoData extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testCreateAndDrop() {
        Context context = this.getInstrumentation().getTargetContext();
        TomatoData tomatoData = new TomatoData(context);
        tomatoData.initDatabase();
        tomatoData.dropDatabase();
    }

    public void testInsertAndSelect() {
        Context context = this.getInstrumentation().getTargetContext();
        TomatoData tomatoData = new TomatoData(context);
        tomatoData.initDatabase();
        tomatoData.clearTomato();
        Tomato tomato = new Tomato();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        tomato.setBegin(date);
        tomato.setEnd(date);
        tomato.setNote("Note");
        tomato.setLocation("Location");
        tomatoData.addTomato(tomato);
        Vector<Tomato> tomatoes = tomatoData.getAllTomatoes();
        assertEquals(1, tomatoes.size());
        tomato = tomatoes.get(0);
        assertEquals(format.format(date), format.format(tomato.getBegin()));
        assertEquals(format.format(date), format.format(tomato.getEnd()));
        assertEquals("Note", tomato.getNote());
        assertEquals("Location", tomato.getLocation());
        assertFalse(tomato.isUploaded());
    }

}
