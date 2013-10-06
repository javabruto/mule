package org.mule.test.integration.transaction;

import org.mule.test.integration.transaction.xa.TransactionalTestSetUp;

/**
 *
 */
public class CompositeTransactionalTestSetUp implements TransactionalTestSetUp
{
    private final TransactionalTestSetUp[] testSetUps;

    public CompositeTransactionalTestSetUp(TransactionalTestSetUp... testSetUps)
    {
        this.testSetUps = testSetUps;
    }

    @Override
    public void initialize() throws Exception
    {
        for (TransactionalTestSetUp setUp : testSetUps)
        {
            setUp.initialize();
        }
    }

    @Override
    public void finalice() throws Exception
    {
        for (TransactionalTestSetUp setUp : testSetUps)
        {
            setUp.finalice();
        }
    }
}
