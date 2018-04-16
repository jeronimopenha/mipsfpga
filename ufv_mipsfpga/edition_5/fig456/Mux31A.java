/* Mux41.java -  class hades.models.rtlib.muxes.Mux41
 *
 * 29.10.99 - renoved unused wakeup call
 * 05.07.98 - first try, copied from hades.models.rtlib.muxes.Mux21
 *
 * (C) F.N.Hendrich, hendrich@informatik.uni-hamburg.de
 *  @Christopher Gull (edited as 3:1 mux)
 */
package ufv_mipsfpga.edition_5.fig456;

import hades.models.PortStdLogicVector;
import hades.models.StdLogicVector;
import hades.signals.Signal;
import hades.simulator.Port;
import hades.simulator.SimEvent;
import hades.symbols.BboxRectangle;
import hades.symbols.BusPortSymbol;
import hades.symbols.FatLabel;
import hades.symbols.InstanceLabel;
import hades.symbols.PortLabel;
import hades.symbols.PortSymbol;
import hades.symbols.Rectangle;
import hades.symbols.Symbol;

/**
 * Mux31 - a simple three-input n-bit multiplexer. Signals are expected to be
 * SignalStdLogicVector objects.
 *
 */
public class Mux31A
        extends hades.models.rtlib.GenericRtlibObject {

    protected PortStdLogicVector port_A2, port_A1, port_A0, port_Y, port_S1;

    public Mux31A() {
        super();
    }

    @Override
    public void constructPorts() {
        port_A2 = new PortStdLogicVector(this, "AluD", Port.IN, null, 32);
        port_A1 = new PortStdLogicVector(this, "MemD", Port.IN, null, 32);
        port_A0 = new PortStdLogicVector(this, "RegD", Port.IN, null, 32);
        port_S1 = new PortStdLogicVector(this, "fwdA", Port.IN, null, 2);
        port_Y = new PortStdLogicVector(this, "Y", Port.OUT, null, 32);

        ports = new Port[5];
        ports[0] = port_A0;
        ports[1] = port_A1;
        ports[2] = port_A2;
        ports[3] = port_S1;
        ports[4] = port_Y;

        vectorOutputPort = port_Y;
    }

    /**
     * evaluate(): called by the simulation engine on all events that concern
     * this object. The object is responsible for updating its internal state
     * and for scheduling all pending output events.
     */
    @Override
    public void evaluate(Object arg) {
        Signal signal_A0 = null, signal_A1 = null;
        Signal signal_A2 = null;
        Signal signal_S1 = null, signal_Y = null;
        boolean isX = false;

        if ((signal_A0 = port_A0.getSignal()) == null) {
            isX = true;
        } else if ((signal_A1 = port_A1.getSignal()) == null) {
            isX = true;
        } else if ((signal_A2 = port_A2.getSignal()) == null) {
            isX = true;
        } else if ((signal_S1 = port_S1.getSignal()) == null) {
            isX = true;
        }
        if (isX) {
            vector = vector_UUU.copy();
        } else {

            StdLogicVector a0 = (StdLogicVector) signal_A0.getValue();
            StdLogicVector a1 = (StdLogicVector) signal_A1.getValue();
            StdLogicVector a2 = (StdLogicVector) signal_A2.getValue();
            StdLogicVector s1 = (StdLogicVector) signal_S1.getValue();

            if (s1.equals(new StdLogicVector(2, 2))) {
                vector = a2.copy();
            } else if (s1.equals(new StdLogicVector(2, 1))) {
                vector = a1.copy();
            } else if (s1.equals(new StdLogicVector(2, 0))) {
                vector = a0.copy();
            } else {
                vector = vector_UUU.copy();
            }
        }

        double time = simulator.getSimTime() + delay;

        //simulator.scheduleWakeup( this, time, this );
        if ((signal_Y = port_Y.getSignal()) == null) {
            return; // no output
        }
        simulator.scheduleEvent(new SimEvent(signal_Y, time, vector, port_Y));
    }

    @Override
    public boolean needsDynamicSymbol() {
        return true;
    }

    @Override
    public void constructDynamicSymbol() {
        symbol = new Symbol();
        symbol.setParent(this);

        BboxRectangle bbr = new BboxRectangle();
        bbr.initialize("0 0 1200 2400");
         symbol.addMember(bbr);

        Rectangle rec = new Rectangle();
        rec.initialize("0 0 1200 2400");
        symbol.addMember(rec);

        InstanceLabel instlabel0 = new InstanceLabel();
        instlabel0.initialize("550 550 1 regr");
        symbol.addMember(instlabel0);

        PortSymbol portsymbol0 = new PortSymbol();
        portsymbol0.initialize("600  2400 fwdA");
        symbol.addMember(portsymbol0);

        BusPortSymbol busportsymbol0 = new BusPortSymbol();
        busportsymbol0.initialize("1200 1200 Y");
        symbol.addMember(busportsymbol0);

        BusPortSymbol busportsymbol1 = new BusPortSymbol();
        busportsymbol1.initialize("0    1800 AluD");
        symbol.addMember(busportsymbol1);

        BusPortSymbol busportsymbol2 = new BusPortSymbol();
        busportsymbol2.initialize("0    1200 MemD");
        symbol.addMember(busportsymbol2);

        BusPortSymbol busportsymbol3 = new BusPortSymbol();
        busportsymbol3.initialize("0 600 RegD");
        symbol.addMember(busportsymbol3);

        FatLabel fatlabel0 = new FatLabel();
        fatlabel0.initialize("600 800 2 M");
        symbol.addMember(fatlabel0);

        FatLabel fatlabel1 = new FatLabel();
        fatlabel1.initialize("600 1400 2 u");
        symbol.addMember(fatlabel1);

        FatLabel fatlabel2 = new FatLabel();
        fatlabel2.initialize("600 2000 2 x");
        symbol.addMember(fatlabel2);

        PortLabel portlabel0 = new PortLabel();
        portlabel0.initialize("1000 1200 2 Y");
        symbol.addMember(portlabel0);

        PortLabel portlabel1 = new PortLabel();
        portlabel1.initialize("450  1800 2 AluD");
        symbol.addMember(portlabel1);

        PortLabel portlabel2 = new PortLabel();
        portlabel2.initialize("450  1200 2 MemD");
        symbol.addMember(portlabel2);

        PortLabel portlabel3 = new PortLabel();
        portlabel3.initialize("450  600 2 RegD");
        symbol.addMember(portlabel3);

        PortLabel portlabel4 = new PortLabel();
        portlabel4.initialize("600  2200 1 fwdA");
        symbol.addMember(portlabel4);
    }
}
