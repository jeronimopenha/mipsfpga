/* 
 * alucontrol.java -  class ufv.alucontrol
 *
 * 23.10.2014 - livro, pagina descreve o componente 
 * (C) Nome e email
 */
package ufv_mipsfpga.edition_5.fig465;

import hades.models.PortStdLogic1164;
import hades.models.PortStdLogicVector;
import hades.models.StdLogic1164;
import hades.models.StdLogicVector;
import hades.signals.Signal;
import hades.simulator.Port;
import hades.simulator.SimEvent;
import hades.symbols.BusPortSymbol;
import hades.symbols.Circle;
import hades.symbols.FatLabel;
import hades.symbols.PortSymbol;
import hades.symbols.Symbol;

public class Comp
        extends hades.models.rtlib.GenericRtlibObject {

    protected PortStdLogicVector port_rs, port_rt;
    protected PortStdLogic1164 port_equal;

    public Comp() {
        super();
    }

    @Override
    public void constructPorts() {
        port_rs = new PortStdLogicVector(this, "rs", Port.IN, null, 32);
        port_rt = new PortStdLogicVector(this, "rt", Port.IN, null, 32);
        port_equal = new PortStdLogic1164(this, "equal", Port.IN, null);

        ports = new Port[3];
        ports[0] = port_rs;
        ports[1] = port_rt;
        ports[2] = port_equal;
    }

    /**
     * evaluate(): called by the simulation engine on all events that concern
     * this object. The object is responsible for updating its internal state
     * and for scheduling all pending output events.
     */
    @Override
    public void evaluate(Object arg) {

        StdLogicVector vector_rs = port_rs.getVectorOrUUU();
        StdLogicVector vector_rt = port_rt.getVectorOrUUU();
        StdLogic1164 value_equal = null;

        //comportamento
        if (vector_rs.has_UXZ()) {
            message("-W- " + toString()
                    + "rs undefined: data loss would occur! Ignoring...");
        } else if (vector_rt.has_UXZ()) {
            message("-W- " + toString()
                    + "rt undefined: data loss would occur! Ignoring...");
        } else {
            long rs = vector_rs.getValue();
            long rt = vector_rt.getValue();

            if (rs == rt) {
                value_equal = new StdLogic1164(3);
            } else {
                value_equal = new StdLogic1164(2);
            }
        }

        double time = simulator.getSimTime();

        Signal signal_equal = port_equal.getSignal();

        if (signal_equal != null) {
            simulator.scheduleEvent(
                    new SimEvent(signal_equal, time, value_equal, port_equal));
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

        FatLabel fatlabel = new FatLabel();
        fatlabel.initialize("600 950 2 =");
        symbol.addMember(fatlabel);
        
        BusPortSymbol busportsymbol0 = new BusPortSymbol();
        busportsymbol0.initialize("600 -600 rs");
        symbol.addMember(busportsymbol0);
        
        BusPortSymbol busportsymbol1 = new BusPortSymbol();
        busportsymbol1.initialize("600 1800 rt");
        symbol.addMember(busportsymbol1);
        
        PortSymbol portsymbol = new PortSymbol();
        portsymbol.initialize("1200 600 equal");
        symbol.addMember(portsymbol);
        
        Circle circle = new Circle();
        circle.initialize("600 600 600 1200");
        symbol.addMember(circle);
    }
}
