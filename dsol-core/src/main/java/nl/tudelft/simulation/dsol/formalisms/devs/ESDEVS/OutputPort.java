package nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.logger.Cat;

/**
 * OutputPort class. The output port transfers the event (message) to the next receiver. In case there is no next receiver (e.g.
 * in case of the model being the highest coupled model in the simulation, the event is currently not transferred.
 * <p>
 * Copyright (c) 2009-2023 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://https://simulation.tudelft.nl/dsol/docs/latest/license.html" target="_blank">
 * https://https://simulation.tudelft.nl/dsol/docs/latest/license.html</a>.
 * </p>
 * @author <a href="http://tudelft.nl/mseck">Mamadou Seck</a><br>
 * @author <a href="http://tudelft.nl/averbraeck">Alexander Verbraeck</a><br>
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @param <TYPE> The type of messages the port produces.
 */
public class OutputPort<T extends Number & Comparable<T>, TYPE> implements OutputPortInterface<T, TYPE>
{
    /** The model to which the port links. */
    private AbstractDEVSModel<T> model;

    /**
     * Constructor for the output port where the model is a coupled model.
     * @param coupledModel CoupledModel&lt;T&gt;; the coupled model.
     */
    public OutputPort(final CoupledModel<T> coupledModel)
    {
        this.model = coupledModel;
    }

    /**
     * Constructor for the output port where the model is an atomic model.
     * @param atomicModel AtomicModel&lt;T&gt;; the atomic model.
     */
    public OutputPort(final AtomicModel<T> atomicModel)
    {
        this.model = atomicModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final TYPE value)
    {
        if (this.model.parentModel != null)
        {
            try
            {
                this.model.getSimulator().getLogger().filter(Cat.DSOL).debug("send: TIME IS {}",
                        this.model.getSimulator().getSimulatorTime());
                this.model.parentModel.transfer(this, value);
            }
            catch (RemoteException | SimRuntimeException e)
            {
                this.model.getSimulator().getLogger().always().error(e);
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractDEVSModel<T> getModel()
    {
        return this.model;
    }
}
