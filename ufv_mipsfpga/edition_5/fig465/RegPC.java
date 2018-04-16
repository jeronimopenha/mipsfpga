/* RegR.java --  class hades.models.rtlib.register.RegR
 *
 * an edge-triggered n-bit D-type register with reset (active low)
 *
 * 28.07.99 - added vectorOutputPort for setValue()
 * 10.09.98 - use scheduleAfter() etc.
 * 03.09.98 - first version, copied from RegE
 *
 * (C) F.N.Hendrich, hendrich@informatik.uni-hamburg.de
 */
package ufv_mipsfpga.edition_5.fig465;

import hades.models.PortStdLogic1164;
import hades.models.PortStdLogicVector;
import hades.models.StdLogic1164;
import hades.models.StdLogicVector;
import hades.models.ruge.ColoredValueLabel;
import hades.simulator.Assignable;
import hades.simulator.Port;
import hades.symbols.BboxRectangle;
import hades.symbols.BusPortSymbol;
import hades.symbols.FatLabel;
import hades.symbols.InstanceLabel;
import hades.symbols.PortSymbol;
import hades.symbols.Rectangle;
import hades.symbols.Symbol;

/**
 * RegR - an edge-triggered n-bit d-type register with active-low reset. Signals
 * are expected to be of type SignalStdLogicVector for D and Q, and
 * SignalStdLogic1164 for CLK and NR.
 * <p>
 * The RegR model will not check for setup-violations.
 * <p>
 * @author F.N.Hendrich
 * @version 0.3 03.09.98
 */
public class RegPC extends hades.models.rtlib.register.RegRE implements Assignable {

    public RegPC() {
        super();
    }

    @Override
    public void constructPorts() {
        port_D = new PortStdLogicVector(this, "D", Port.IN, null, n_bits);
        port_Q = new PortStdLogicVector(this, "Q", Port.OUT, null, n_bits);
        port_CLK = new PortStdLogic1164(this, "CLK", Port.IN, null);
        port_NR = new PortStdLogic1164(this, "NR", Port.IN, null);
        port_ENA = new PortStdLogic1164(this, "ENA", Port.IN, null);

        ports = new Port[5];
        ports[0] = port_CLK;
        ports[1] = port_NR;
        ports[2] = port_ENA;
        ports[3] = port_D;
        ports[4] = port_Q;

        vectorOutputPort = port_Q;
    }

    @Override
    public void evaluate(Object arg) {

        StdLogic1164 value_CLK = port_CLK.getValueOrU();
        StdLogic1164 value_ENA = port_ENA.getValueOrU();
        StdLogic1164 value_NR = port_NR.getValueOrU();
        StdLogicVector value_D = port_D.getVectorOrUUU();

        if (value_NR.is_0()) {                                     // reset active
            vector = vector_000.copy();
        } else if (!value_NR.is_1()) {                            // reset undefined
            vector = vector_XXX.copy();
        } else if (!value_ENA.is_01()) {                         // enable undefined
            vector = vector_XXX.copy();
        } else if (!value_CLK.is_01()) {                          // clock undefined
            vector = vector_XXX.copy();
        } else if (port_CLK.hasEvent() && value_CLK.is_1()) {   // check rising edge
            if (value_ENA.is_1()) {
                if (value_D.has_UXZ()) {
                    vector = vector_XXX.copy();
                } else {
                    vector = value_D.copy();
                }
            }
        }
        if (enableAnimationFlag) {
            wakeupAfter(delay);
        }
        scheduleAfter(delay, port_Q, vector);
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
        portsymbol0.initialize("0 1800 CLK");
        symbol.addMember(portsymbol0);

        PortSymbol portsymbol1 = new PortSymbol();
        portsymbol1.initialize("600 2400 NR");
        symbol.addMember(portsymbol1);

        PortSymbol portsymbol2 = new PortSymbol();
        portsymbol2.initialize("600 0 ENA");
        symbol.addMember(portsymbol2);

        BusPortSymbol busportsymbol0 = new BusPortSymbol();
        busportsymbol0.initialize("1200 1200 Q");
        symbol.addMember(busportsymbol0);

        BusPortSymbol busportsymbol1 = new BusPortSymbol();
        busportsymbol1.initialize("0  1200 D");
        symbol.addMember(busportsymbol1);

        ColoredValueLabel coloredvaluelabel0 = new ColoredValueLabel();
        coloredvaluelabel0.initialize("1800 2200 2 42 ");
        symbol.addMember(coloredvaluelabel0);
        
        FatLabel fatlabel0 = new FatLabel();
        fatlabel0.initialize("600 800 2 P");
        symbol.addMember(fatlabel0);
        
        FatLabel fatlabel1 = new FatLabel();
        fatlabel1.initialize("600 1400 2 C");
        symbol.addMember(fatlabel1);
    }
}
