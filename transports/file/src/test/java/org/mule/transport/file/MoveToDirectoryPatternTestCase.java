/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.transport.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

public class MoveToDirectoryPatternTestCase extends FunctionalTestCase
{

    private static final String TEST_FILENAME = "test.txt";

    @Override
    protected void doTearDown() throws Exception
    {
        // clean out the directory tree that's used as basis for this test
        File outputDir = new File(".mule");
        assertTrue(FileUtils.deleteTree(outputDir));

        super.doTearDown();
    }

    @Test
    public void moveToPatternWithDirectory() throws Exception
    {
        MuleClient client = muleContext.getClient();
        writeTestMessageToInputDirectory();
        MuleMessage msg = client.request("vm://file.outbox", 3000);
        assertNotNull(msg);
        assertEquals(TEST_MESSAGE, msg.getPayload());
    }

    private void writeTestMessageToInputDirectory() throws IOException
    {
        File outFile = new File(".mule/files", TEST_FILENAME);
        FileOutputStream out = new FileOutputStream(outFile);
        out.write(TEST_MESSAGE.getBytes());
        out.close();
    }

    @Override
    protected String getConfigResources()
    {
        return "move-to-directory-config.xml";
    }
}
