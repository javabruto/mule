package org.mule.test.integration.transaction.xa;

import static org.junit.Assert.assertThat;

import org.mule.tck.probe.PollingProber;
import org.mule.tck.probe.Probe;

import org.hamcrest.core.Is;
import org.junit.Ignore;

@Ignore
public class TransactionScenarios
{

    private final InboundMessagesGenerator inboundMessagesGenerator;
    private final OutboundMessagesCounter outboundMessagesVerifier;
    private int verificationTimeout;

    public TransactionScenarios(InboundMessagesGenerator inboundMessagesGenerator, OutboundMessagesCounter outboundMessagesVerifier)
    {
        this.inboundMessagesGenerator = inboundMessagesGenerator;
        this.outboundMessagesVerifier = outboundMessagesVerifier;
    }

    public void testNoFailureDuringFlowExecution()
    {
        try
        {
            FailureGeneratorMessageProcessor.noFailure();
            testFlow(false);
        }
        finally
        {
            outboundMessagesVerifier.close();
        }
    }


    public void testIntermittentFailureDuringFlowExecution()
    {
        try
        {
            FailureGeneratorMessageProcessor.generateIntermitentFailure();
            testFlow(false);
        }
        finally
        {
            outboundMessagesVerifier.close();
        }
    }

    public void testAlwaysFailureDuringFlowException()
    {
        try
        {
            FailureGeneratorMessageProcessor.allFailure();
            testFlow(true);
        }
        finally
        {
            outboundMessagesVerifier.close();
        }
    }

    private void testFlow(final boolean noMessageExpected)
    {

        try
        {
            final Integer numberOfMessagesCreated = inboundMessagesGenerator.generateInboundMessages();
            verificationTimeout = 10000;
            if (noMessageExpected)
            {
                Thread.sleep(verificationTimeout);
                assertThat(outboundMessagesVerifier.numberOfMessagesThatArrived(), Is.is(0));
            }
            else
            {
                new PollingProber(10000,100).check(new Probe()
                {
                    @Override
                    public boolean isSatisfied()
                    {
                        try
                        {
                            return outboundMessagesVerifier.numberOfMessagesThatArrived() == numberOfMessagesCreated;
                        }
                        catch (Exception e)
                        {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public String describeFailure()
                    {
                        return "Not all the messages arrived";
                    }
                });
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public TransactionScenarios setVerificationTimeout(int verificationTimeout)
    {
        this.verificationTimeout = verificationTimeout;
        return this;
    }


    public interface InboundMessagesGenerator
    {
        int NUMBER_OF_MESSAGES = 6;
        /**
         * Send messages to transactional inbound endpoint
         */
        Integer generateInboundMessages() throws Exception;
    }


    public interface OutboundMessagesCounter
    {
        int NUMBER_OF_MESSAGES = InboundMessagesGenerator.NUMBER_OF_MESSAGES;
        /**
         *  Returns he number of messages received in the outbound endpoint
         */
        int numberOfMessagesThatArrived() throws Exception;
        void close();
    }

}
