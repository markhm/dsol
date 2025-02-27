package nl.tudelft.simulation.dsol.simulators;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;

/**
 * The DEVSSimulatorTestmodel specifies the model.
 * <p>
 * Copyright (c) 2002-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://https://simulation.tudelft.nl/dsol/docs/latest/license.html" target="_blank">
 * https://https://simulation.tudelft.nl/dsol/docs/latest/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>,
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class DEVSTestModel extends TestModel
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * constructs a new DEVSTestModel.
     * @param simulator the simulator
     */
    public DEVSTestModel(final DevsSimulatorInterface<Double> simulator)
    {
        super(simulator);
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel()
    {
        DevsSimulatorInterface<Double> devsSimulator = (DevsSimulatorInterface<Double>) getSimulator();
        super.constructModel();
        for (int i = 0; i < 100; i++)
        {
            try
            {
                devsSimulator.scheduleEvent(
                        new SimEvent<Double>(Math.random() * 100, this, "run", null));
            }
            catch (Exception exception)
            {
                this.simulator.getLogger().always().warn(exception, "constructModel");
            }
        }
    }

    /**
     * the method which is scheduled.
     */
    public void run()
    {
        // Testing method
    }
}
