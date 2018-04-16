 /* ShiftLeft.java - class ufv.edition_5
 * 
 * 24-10-2014 - MK.Computer.Organization.and.Design.4th.Edition.Oct.2011, P.xxx
 *
 * (C) T.T. Almeida, thales.almeida@ufv.edition_5.br
 */
package ufv_mipsfpga.edition_5;

import hades.models.PortStdLogicVector;
import hades.models.StdLogicVector;
import hades.signals.Signal;
import hades.simulator.Port;
import hades.simulator.SimEvent;
import hades.symbols.BboxRectangle;
import hades.symbols.BusPortSymbol;
import hades.symbols.ClassLabel;
import hades.symbols.InstanceLabel;
import hades.symbols.Label;
import hades.symbols.Polyline;
import hades.symbols.PortLabel;
import hades.symbols.Symbol;

public class ShiftLeft extends hades.models.rtlib.GenericRtlibObject {

    protected PortStdLogicVector port_A, port_outSL;
    protected StdLogicVector value_A;

    public ShiftLeft() {
        super();
    }

    @Override
    public void constructPorts() {
        port_A = new PortStdLogicVector(this, "A", Port.IN, null, 32);
        port_outSL = new PortStdLogicVector(this, "OutSL", Port.OUT, null, 32);

        ports = new Port[2];
        ports[0] = port_A;
        ports[1] = port_outSL;

        vectorOutputPort = port_outSL;
    }

    /**
     * evaluate(): called by the simulation engine on all events that concern
     * this object. The object is responsible for updating its internal state
     * and for scheduling all pending output events.
     */
    @Override
    public void evaluate(Object arg) {

        Signal signal_A, signal_outSL;

        if ((signal_A = port_A.getSignal()) == null) {
            vector = vector_UUU.copy();
        } else {

            value_A = (StdLogicVector) signal_A.getValue();

            int result = (int) value_A.getValue() * 4;

            vector = new StdLogicVector(32, result);
        }

        double time = simulator.getSimTime() + delay;

        if ((signal_outSL = port_outSL.getSignal()) != null) {
            simulator.scheduleEvent(
                    new SimEvent(signal_outSL, time, vector, port_outSL));
        }
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

        Polyline pol = new Polyline();
        pol.initialize("5 0 0 1200 0 1200 2400 0 2400 0 0");
        symbol.addMember(pol);

        ClassLabel classlabel = new ClassLabel();
        classlabel.initialize("0 1800 1 ShiftLeft2");
        symbol.addMember(classlabel);

        InstanceLabel instlabel = new InstanceLabel();
        instlabel.initialize("0 300 1 shiftleft2");
        symbol.addMember(instlabel);

        Label label0 = new Label();
        label0.initialize("300 1100 Shift");
        symbol.addMember(label0);

        Label label1 = new Label();
        label1.initialize("300 1400 left2");
        symbol.addMember(label1);

        BusPortSymbol busportsymbol0 = new BusPortSymbol();
        busportsymbol0.initialize("1200 1200 OutSL");
        symbol.addMember(busportsymbol0);

        BusPortSymbol busportsymbol1 = new BusPortSymbol();
        busportsymbol1.initialize("600 2400 A");
        symbol.addMember(busportsymbol1);

        PortLabel portlabel0 = new PortLabel();
        portlabel0.initialize("600 2200 2 A");
        symbol.addMember(portlabel0);

        PortLabel portlabel1 = new PortLabel();
        portlabel1.initialize("600 600 2 OutSL");
        symbol.addMember(portlabel1);

    }

}
