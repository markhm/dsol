package nl.tudelft.simulation.examples.dsol.timesharedcomputer;

import java.rmi.RemoteException;

import org.djutils.event.Event;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.flow.Station;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simulators.DevsSimulator;
import nl.tudelft.simulation.dsol.statistics.SimCounter;
import nl.tudelft.simulation.dsol.statistics.SimPersistent;
import nl.tudelft.simulation.dsol.swing.charts.boxAndWhisker.BoxAndWhiskerChart;
import nl.tudelft.simulation.dsol.swing.charts.histogram.Histogram;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;
import nl.tudelft.simulation.jstats.distributions.DistExponential;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Computer example as published in Simulation Modeling and Analysis by A.M. Law &amp; W.D. Kelton section 1.4 and 2.4..
 * <p>
 * Copyright (c) 2003-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://https://simulation.tudelft.nl/dsol/docs/latest/license.html" target="_blank">
 * https://https://simulation.tudelft.nl/dsol/docs/latest/license.html</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm">Peter Jacobs </a>
 */
public class Computer extends AbstractDSOLModel<Double, DevsSimulator<Double>>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the number of jobs. */
    public static final long NUMBER_OF_JOBS = 1000;

    /** the number of terminals. */
    public static final long NUMBER_OF_TERMINALS = 80;

    /**
     * constructs a new Computer.
     * @param simulator DEVSSimulator&lt;Double&gt;; the simulator
     */
    public Computer(final DevsSimulator<Double> simulator)
    {
        super(simulator);
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel() throws SimRuntimeException
    {
        StreamInterface stream = this.simulator.getModel().getStream("default");

        CPU cpu = new CPU(this.simulator);
        DistContinuous thinkDelay = new DistExponential(stream, 25);
        DistContinuous processDelay = new DistExponential(stream, 0.8);

        try
        {
            // First the statistics
            SimPersistent<Double> persistent = new SimPersistent<>("service time", this);
            ExitCounter exitCounter = new ExitCounter("counter", this);

            // Now the charts
            Histogram histogram = new Histogram(this.simulator, "service time", new double[] {0, 200}, 200);
            histogram.add("serviceTime", persistent, SimPersistent.TIMED_OBSERVATION_ADDED_EVENT);

            BoxAndWhiskerChart boxAndWhisker = new BoxAndWhiskerChart(this.simulator, "serviceTime");
            boxAndWhisker.add(persistent);

            // Now we start the action
            for (int i = 0; i < NUMBER_OF_TERMINALS; i++)
            {
                Terminal terminal = new Terminal(this.simulator, cpu, thinkDelay, processDelay);
                terminal.addListener(exitCounter, Station.RELEASE_EVENT);
                terminal.addListener(persistent, Terminal.SERVICE_TIME);
            }
        }
        catch (RemoteException exception)
        {
            throw new SimRuntimeException(exception);
        }
    }

    /**
     * A counter which stops after a predifined number of jobs.
     */
    public static class ExitCounter extends SimCounter<Double>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * constructs a new ExitCounter.
         * @param description String; the description of the counter
         * @param model DSOLModel&lt;Double, SimulatorInterface&lt;Double&gt;&gt;; the model to register the OutputStatistics
         * @throws RemoteException on network failure
         */
        public ExitCounter(final String description, final DSOLModel<Double, DevsSimulator<Double>> model)
                throws RemoteException
        {
            super(description, model);
        }

        /** {@inheritDoc} */
        @Override
        public void notify(final Event event)
        {
            super.notify(event);
            if (getCount() >= NUMBER_OF_JOBS)
            {
                try
                {
                    if (getSimulator().isStartingOrRunning())
                    {
                        getSimulator().stop();
                    }
                }
                catch (SimRuntimeException exception)
                {
                    getSimulator().getLogger().always().error(exception);
                }
            }
        }
    }
}
