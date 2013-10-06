package org.mule.test.integration.transaction.xa;

import org.mule.api.MuleContext;
import org.mule.api.client.MuleClient;
import org.mule.api.context.MuleContextAware;

import org.junit.Ignore;

@Ignore
public class QueueInboundMessageGenerator implements TransactionScenarios.InboundMessagesGenerator, MuleContextAware
{

    private MuleClient muleClient;

    @Override
    public Integer generateInboundMessages() throws Exception
    {
        while (muleClient.request("inboundDispatcher", 100) != null)
        {
            ;
        }
        for (int i = 0; i < NUMBER_OF_MESSAGES; i++)
        {
            muleClient.dispatch("inboundDispatcher", "test" + i, null);
        }
        return NUMBER_OF_MESSAGES;
    }

    @Override
    public void setMuleContext(MuleContext context)
    {
        this.muleClient = context.getClient();
    }
}
