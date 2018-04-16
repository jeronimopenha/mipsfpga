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
import hades.symbols.BboxRectangle;
import hades.symbols.BusPortSymbol;
import hades.symbols.FatLabel;
import hades.symbols.PortSymbol;
import hades.symbols.Rectangle;
import hades.symbols.Symbol;

public class Hazard
        extends hades.models.rtlib.GenericRtlibObject {

    protected PortStdLogicVector port_rs, port_rt, port_IDEXrt;
    protected PortStdLogic1164 port_IDEXmemread, port_PCwrite, port_IFIDwrite, port_stall;

    public Hazard() {
        super();
    }

    @Override
    public void constructPorts() {
        port_rs = new PortStdLogicVector(this, "rs", Port.IN, null, 5);
        port_rt = new PortStdLogicVector(this, "rt", Port.IN, null, 5);
        port_IDEXrt = new PortStdLogicVector(this, "IDEXrt", Port.IN, null, 5);
        port_IDEXmemread = new PortStdLogic1164(this, "IDEXmemread", Port.IN, null);
        port_PCwrite = new PortStdLogic1164(this, "PCwrite", Port.OUT, null);
        port_IFIDwrite = new PortStdLogic1164(this, "IFIDwrite", Port.OUT, null);
        port_stall = new PortStdLogic1164(this, "stall", Port.OUT, null);

        ports = new Port[7];
        ports[0] = port_rs;
        ports[1] = port_rt;
        ports[2] = port_IDEXrt;
        ports[3] = port_IDEXmemread;
        ports[4] = port_PCwrite;
        ports[5] = port_IFIDwrite;
        ports[6] = port_stall;
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
        StdLogicVector vector_IDEXrt = port_IDEXrt.getVectorOrUUU();
        StdLogic1164 value_IDEXmemread = port_IDEXmemread.getValueOrU();
        StdLogic1164 value_PCwrite = new StdLogic1164(3);
        StdLogic1164 value_IFIDwrite = new StdLogic1164(3);
        StdLogic1164 value_stall = new StdLogic1164(2);

        boolean isX = false;

        //comportamento
        if (vector_rs.has_UXZ()) {
            isX = true;
        } else if (vector_rt.has_UXZ()) {
            isX = true;
        } else if (vector_IDEXrt.has_UXZ()) {
            isX = true;
        } else if (!value_IDEXmemread.is_01()) {
            isX = true;
        }

        if (!isX) {
            long rs = vector_rs.getValue();
            long rt = vector_rt.getValue();
            long IDEXrt = vector_IDEXrt.getValue();
            int IDEXmemread = (int) value_IDEXmemread.getValue(); //o valor de IDEXmemread vem como 2 ou 3

            if (IDEXmemread == 3 && (rs == IDEXrt || rt == IDEXrt)) {
                value_PCwrite = new StdLogic1164(2);
                value_IFIDwrite = new StdLogic1164(2);
                value_stall = new StdLogic1164(3);
            } else {
                value_PCwrite = new StdLogic1164(3);
                value_IFIDwrite = new StdLogic1164(3);
                value_stall = new StdLogic1164(2);
            }
        }

        double time = simulator.getSimTime() + delay;

        Signal signal_stall = port_stall.getSignal();
        Signal signal_PCwrite = port_PCwrite.getSignal();
        Signal signal_IFIDwrite = port_IFIDwrite.getSignal();

        if (signal_stall != null) {
            simulator.scheduleEvent(
                    new SimEvent(signal_stall, time, value_stall, port_stall));
        }
        if (signal_PCwrite != null) {
            simulator.scheduleEvent(
                    new SimEvent(signal_PCwrite, time, value_PCwrite, port_PCwrite));
        }
        if (signal_IFIDwrite != null) {
            simulator.scheduleEvent(
                    new SimEvent(signal_IFIDwrite, time, value_IFIDwrite, port_IFIDwrite));
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

        Rectangle rec = new Rectangle();
        rec.initialize("0 0 3600 4800");
        symbol.addMember(rec);

        BboxRectangle bbr = new BboxRectangle();
        bbr.initialize("0 0 3600 4800");
        symbol.addMember(bbr);

        FatLabel fatlabel0 = new FatLabel();
        fatlabel0.initialize("1800 1700 2 Hazard");
        symbol.addMember(fatlabel0);

        FatLabel fatlabel1 = new FatLabel();
        fatlabel1.initialize("1800 2700 2 detection");
        symbol.addMember(fatlabel1);

        FatLabel fatlabel2 = new FatLabel();
        fatlabel2.initialize("1800 3700 2 unit");
        symbol.addMember(fatlabel2);

        PortSymbol portsymbol0 = new PortSymbol();
        portsymbol0.initialize("3600 2400 IDEXmemread");
        symbol.addMember(portsymbol0);

        PortSymbol portsymbol1 = new PortSymbol();
        portsymbol1.initialize("3600 3000 stall");
        symbol.addMember(portsymbol1);

        PortSymbol portsymbol2 = new PortSymbol();
        portsymbol2.initialize("0 600 PCwrite");
        symbol.addMember(portsymbol2);

        PortSymbol portsymbol3 = new PortSymbol();
        portsymbol3.initialize("0 1200 IFIDwrite");
        symbol.addMember(portsymbol3);

        BusPortSymbol busportsymbol0 = new BusPortSymbol();
        busportsymbol0.initialize("600 4800 IDEXrt");
        symbol.addMember(busportsymbol0);

        BusPortSymbol busportsymbol1 = new BusPortSymbol();
        busportsymbol1.initialize("0 2400 rs");
        symbol.addMember(busportsymbol1);

        BusPortSymbol busportsymbol2 = new BusPortSymbol();
        busportsymbol2.initialize("0 3600 rt");
        symbol.addMember(busportsymbol2);
    }
}
