package nl.tudelft.simulation.dsol.swing.gui.test;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.SingleReplication;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simulators.DevsSimulator;
import nl.tudelft.simulation.dsol.simulators.DevsSimulatorInterface;

/**
 * MM1Application.java.
 * <p>
 * Copyright (c) 2020-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://https://simulation.tudelft.nl/dsol/docs/latest/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class MM1Application
{
    /**
     * M/M/1 queueing application.
     * @throws SimRuntimeException on simulation error
     * @throws RemoteException on remote error
     * @throws NamingException on naming/animation error
     */
    protected MM1Application() throws SimRuntimeException, RemoteException, NamingException
    {
        DevsSimulator<Double> simulator = new DevsSimulator<Double>("MM1SwingApplication.Simulator");
        DSOLModel<Double, DevsSimulatorInterface<Double>> model = new MM1Model(simulator);
        Replication<Double> replication = new SingleReplication<Double>("rep1", 0.0, 0.0, 1000.0);
        simulator.initialize(model, replication);
        simulator.start();
    }

    /**
     * @param args String[]; the arguments (not used, should be empty)
     * @throws SimRuntimeException on simulation error
     * @throws RemoteException on remote error
     * @throws NamingException on naming/animation error
     */
    public static void main(final String[] args) throws SimRuntimeException, RemoteException, NamingException
    {
        new MM1Application();
    }

}
