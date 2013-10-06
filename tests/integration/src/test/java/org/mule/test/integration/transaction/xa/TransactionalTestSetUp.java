package org.mule.test.integration.transaction.xa;

import org.junit.Ignore;

@Ignore
public interface TransactionalTestSetUp
{

    /**
     * Creates resources required by the test
     * @throws Exception
     */
    void initialize() throws Exception;

    /**
     * Destroy resources created for the test
     * @throws Exception
     */
    void finalice() throws Exception;
}
