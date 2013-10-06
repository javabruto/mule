package org.mule.test.integration.transaction.xa;

import org.apache.activemq.broker.BrokerService;
import org.junit.Ignore;

@Ignore
public class JmsBrokerSetUp implements TransactionalTestSetUp
{

    private final int port;
    private BrokerService broker;

    public JmsBrokerSetUp(int port)
    {
        this.port = port;
    }

    @Override
    public void initialize() throws Exception
    {
        broker = new BrokerService();
        broker.setUseJmx(false);
        broker.setPersistent(false);
        broker.addConnector("tcp://localhost:" + port);
        broker.start();
    }

    @Override
    public void finalice() throws Exception
    {
        try
        {
            broker.stop();
        }
        catch (Exception e)
        {
        }
    }
}
