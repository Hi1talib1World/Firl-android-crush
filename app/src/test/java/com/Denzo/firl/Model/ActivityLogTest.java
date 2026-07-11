package com.Denzo.firl.Model;

import org.junit.Test;
import static org.junit.Assert.*;

public class ActivityLogTest {

    @Test
    public void testActivityLogCreation() {
        long before = System.currentTimeMillis();
        ActivityLog log = new ActivityLog("TestOp", ActivityLog.Status.SUCCESS, "Detail");
        long after = System.currentTimeMillis();

        assertEquals("TestOp", log.getOperationName());
        assertEquals(ActivityLog.Status.SUCCESS, log.getStatus());
        assertEquals("Detail", log.getDetails());
        assertTrue(log.getTimestamp() >= before && log.getTimestamp() <= after);
    }
}
