/* ForwardUnit.java -  class ufv.ForwardUnit
 *
 *
 * @ Christopher Gull - UFV
 */
package ufv_mipsfpga.edition_5.fig460;

import hades.models.PortStdLogic1164;
import hades.models.PortStdLogicVector;
import hades.models.StdLogic1164;
import hades.models.StdLogicVector;
import hades.signals.Signal;
import hades.simulator.Port;
import hades.simulator.SimEvent;
import hades.symbols.BboxRectangle;
import hades.symbols.BusPortSymbol;
import hades.symbols.ClassLabel;
import hades.symbols.FatLabel;
import hades.symbols.InstanceLabel;
import hades.symbols.Polyline;
import hades.symbols.PortLabel;
import hades.symbols.Symbol;
import hades.utils.StringTokenizer;

/**
 * ForwardUnit detects Register usages between pipeline stages. In the event of
 * problem, either/both forward Muxes before ALU receive data from previous EX
 * result or from Memory/EX result from two CCs back.
 *
 */
public class ForwardUnit
        extends hades.models.rtlib.GenericRtlibObject {

    protected PortStdLogicVector port_IDEXARS1, port_IDEXART1, port_EXMEMRD1, port_MEMWBRD1, port_fA, port_fB;
    protected PortStdLogic1164 port_EXMEMRegWrite, port_MEMWBRegWrite;
    protected StdLogicVector IDEXARS1, IDEXART1, EXMEMRD1, MEMWBRD1, fA, fB;
    protected StdLogic1164 EXMEMRegWrite, MEMWBRegWrite;

    public ForwardUnit() {
        super();
    }

    @Override
    public void constructPorts() {
        port_IDEXARS1 = new PortStdLogicVector(this, "IDEX_ARS1", Port.IN, null, 5); //port
        port_IDEXART1 = new PortStdLogicVector(this, "IDEX_ART1", Port.IN, null, 5);
        port_EXMEMRD1 = new PortStdLogicVector(this, "EXMEM_RD1", Port.IN, null, 5);
        port_MEMWBRD1 = new PortStdLogicVector(this, "MEMWB_RD1", Port.IN, null, 5);
        port_EXMEMRegWrite = new PortStdLogic1164(this, "EXMEM_RW", Port.IN, null);
        port_MEMWBRegWrite = new PortStdLogic1164(this, "MEMWB_RW", Port.IN, null);
        port_fA = new PortStdLogicVector(this, "fwdA", Port.OUT, null, 2);
        port_fB = new PortStdLogicVector(this, "fwdB", Port.OUT, null, 2);

        ports = new Port[8];
        ports[0] = port_IDEXARS1;
        ports[1] = port_IDEXART1;
        ports[2] = port_EXMEMRD1;
        ports[3] = port_MEMWBRD1;
        ports[4] = port_EXMEMRegWrite;
        ports[5] = port_MEMWBRegWrite;
        ports[6] = port_fA;
        ports[7] = port_fB;
    }

    /**
     * evaluate(): called by the simulation engine on all events that concern
     * this object. The object is responsible for updating its internal state
     * and for scheduling all pending output events.
     */
    @Override
    public void evaluate(Object arg) {

        Signal signal_IDEXARS1 = null, signal_IDEXART1 = null, signal_EXMEMRD1 = null, signal_MEMWBRD1 = null;
        Signal signal_EXMEMRegWrite = null, signal_MEMWBRegWrite = null, signal_fA = null, signal_fB = null;
        boolean isX = false;

        if ((signal_IDEXARS1 = port_IDEXARS1.getSignal()) == null) {
            isX = true;
        } else if ((signal_IDEXART1 = port_IDEXART1.getSignal()) == null) {
            isX = true;
        } else if ((signal_EXMEMRD1 = port_EXMEMRD1.getSignal()) == null) {
            isX = true;
        } else if ((signal_MEMWBRD1 = port_MEMWBRD1.getSignal()) == null) {
            isX = true;
        } else if ((signal_EXMEMRegWrite = port_EXMEMRegWrite.getSignal()) == null) {
            isX = true;
        } else if ((signal_MEMWBRegWrite = port_MEMWBRegWrite.getSignal()) == null) {
            isX = true;
        }
        if (isX) {
            vector = vector_UUU.copy();
        } else {
            IDEXARS1 = (StdLogicVector) signal_IDEXARS1.getValue();
            IDEXART1 = (StdLogicVector) signal_IDEXART1.getValue();
            EXMEMRD1 = (StdLogicVector) signal_EXMEMRD1.getValue();
            MEMWBRD1 = (StdLogicVector) signal_MEMWBRD1.getValue();
            EXMEMRegWrite = (StdLogic1164) signal_EXMEMRegWrite.getValue();
            MEMWBRegWrite = (StdLogic1164) signal_MEMWBRegWrite.getValue();

            forwardA(EXMEMRegWrite, MEMWBRegWrite, EXMEMRD1, MEMWBRD1, IDEXARS1);
            forwardB(EXMEMRegWrite, MEMWBRegWrite, EXMEMRD1, MEMWBRD1, IDEXART1);
        }

        double time = simulator.getSimTime() + delay;

        if ((signal_fA = port_fA.getSignal()) == null) {
            return;
        }
        simulator.scheduleEvent(new SimEvent(signal_fA, time, fA, port_fA));
        if ((signal_fB = port_fB.getSignal()) == null) {
            return;
        }
        simulator.scheduleEvent(new SimEvent(signal_fB, time, fB, port_fB));

    }

    public void forwardA(StdLogic1164 exmem_RegWrite, StdLogic1164 memwb_RegWrite, StdLogicVector exmem_RD1, StdLogicVector memwb_RD1, StdLogicVector idex_ARS1) {
        if ((exmem_RegWrite.is_1()) && ((!exmem_RD1.is_000()) && (exmem_RD1.equals(idex_ARS1)))) {
            fA = new StdLogicVector(2, 2);

        } //else if ((memwb_RegWrite.is_1()) && (!memwb_RD1.is_000()) && !(exmem_RegWrite.is_1() && (!exmem_RD1.is_000()) && !exmem_RD1.equals(idex_ARS1)) && memwb_RD1.equals(idex_ARS1))
        else if (((memwb_RegWrite.is_1()) && (!memwb_RD1.is_000()) && (exmem_RegWrite.is_1() && (!exmem_RD1.is_000())) && (!exmem_RD1.equals(idex_ARS1)) && (memwb_RD1.equals(idex_ARS1))) || ((memwb_RegWrite.is_1()) && (!memwb_RD1.is_000()) && !(exmem_RegWrite.is_1() && (!exmem_RD1.is_000()) && !exmem_RD1.equals(idex_ARS1)) && memwb_RD1.equals(idex_ARS1))) {
            fA = new StdLogicVector(2, 1);
        } else {
            fA = new StdLogicVector(2, 0);
        }
    }

    public void forwardB(StdLogic1164 exmem_RegWrite, StdLogic1164 memwb_RegWrite, StdLogicVector exmem_RD1, StdLogicVector memwb_RD1, StdLogicVector idex_ART1) {
        if ((exmem_RegWrite.is_1()) && ((!exmem_RD1.is_000()) && (exmem_RD1.equals(idex_ART1)))) {
            fB = new StdLogicVector(2, 2);
        } //else if ((memwb_RegWrite.is_1()) && (!memwb_RD1.is_000()) && !(exmem_RegWrite.is_1() && (!exmem_RD1.is_000()) && !exmem_RD1.equals(idex_ART1)) && memwb_RD1.equals(idex_ART1))
        else if (((memwb_RegWrite.is_1()) && (!memwb_RD1.is_000()) && (exmem_RegWrite.is_1() && (!exmem_RD1.is_000())) && (!exmem_RD1.equals(idex_ART1)) && (memwb_RD1.equals(idex_ART1))) || ((memwb_RegWrite.is_1()) && (!memwb_RD1.is_000()) && !(exmem_RegWrite.is_1() && (!exmem_RD1.is_000()) && !exmem_RD1.equals(idex_ART1)) && memwb_RD1.equals(idex_ART1))) {
            fB = new StdLogicVector(2, 1);
        } else {
            fB = new StdLogicVector(2, 0);
        }
    }

    @Override
    public void write(java.io.PrintWriter ps) {
        ps.print(" " + delay);
    }

    @Override
    public boolean initialize(String s) {

        StringTokenizer st = new StringTokenizer(s);
        int n_tokens = st.countTokens();
        try {

            constructPorts();
            constructStandardValues();

            setDelay(delay);
        } catch (Exception e) {
            message("-E- " + toString() + ".initialize(): " + e + " " + s);
        }
        return true;
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
        bbr.initialize("0 0 3000 2400");
        symbol.addMember(bbr);

        Polyline polyline = new Polyline();
        polyline.initialize("5 0 0 3000 0 3000 2400 0 2400 0 0");
        symbol.addMember(polyline);

        ClassLabel classlabel = new ClassLabel();
        classlabel.initialize("700 -60 1 FwdUnit");
        symbol.addMember(classlabel);

        InstanceLabel instancelabel = new InstanceLabel();
        instancelabel.initialize("4850 550 1 FwdUnit");
        symbol.addMember(instancelabel);

        FatLabel fatlabel0 = new FatLabel();
        fatlabel0.initialize("1550 1000 2 FWD");
        symbol.addMember(fatlabel0);

        FatLabel fatlabel1 = new FatLabel();
        fatlabel1.initialize("1510 1800 2 Unit");
        symbol.addMember(fatlabel1);

        BusPortSymbol busportsymbol0 = new BusPortSymbol();
        busportsymbol0.initialize("1200 0 fwdA");
        symbol.addMember(busportsymbol0);

        BusPortSymbol busportsymbol1 = new BusPortSymbol();
        busportsymbol1.initialize("1800 0 fwdB");
        symbol.addMember(busportsymbol1);

        BusPortSymbol busportsymbol2 = new BusPortSymbol();
        busportsymbol2.initialize("0 600 IDEX_ARS1");
        symbol.addMember(busportsymbol2);

        BusPortSymbol busportsymbol3 = new BusPortSymbol();
        busportsymbol3.initialize("0 1800 IDEX_ART1");
        symbol.addMember(busportsymbol3);

        BusPortSymbol busportsymbol4 = new BusPortSymbol();
        busportsymbol4.initialize("3000 600 EXMEM_RD1");
        symbol.addMember(busportsymbol4);

        BusPortSymbol busportsymbol5 = new BusPortSymbol();
        busportsymbol5.initialize("3000 1800 MEMWB_RD1");
        symbol.addMember(busportsymbol5);

        BusPortSymbol busportsymbol6 = new BusPortSymbol();
        busportsymbol6.initialize("1800 2400 EXMEM_RW");
        symbol.addMember(busportsymbol6);

        BusPortSymbol busportsymbol7 = new BusPortSymbol();
        busportsymbol7.initialize("2400 2400 MEMWB_RW");
        symbol.addMember(busportsymbol7);

        PortLabel portlabel0 = new PortLabel();
        portlabel0.initialize("1000 400 2 fwdA");
        symbol.addMember(portlabel0);

        PortLabel portlabel1 = new PortLabel();
        portlabel1.initialize("2000 400 2 fwdB");
        symbol.addMember(portlabel1);

        PortLabel portlabel2 = new PortLabel();
        portlabel2.initialize("0  1000 2 IDEX_ARS1");
        symbol.addMember(portlabel2);

        PortLabel portlabel3 = new PortLabel();
        portlabel3.initialize("0 1600 2 IDEX_ART1");
        symbol.addMember(portlabel3);

        PortLabel portlabel4 = new PortLabel();
        portlabel4.initialize("3000 1000 2 EXMEM_RD1");
        symbol.addMember(portlabel4);

        PortLabel portlabel5 = new PortLabel();
        portlabel5.initialize("3000 1600 2 MEMWB_RD1");
        symbol.addMember(portlabel5);

        PortLabel portlabel6 = new PortLabel();
        portlabel6.initialize("1000 2250 2 EXMEM_RW");
        symbol.addMember(portlabel6);

        PortLabel portlabel7 = new PortLabel();
        portlabel7.initialize("3200 2250 2 MEMWB_RW");
        symbol.addMember(portlabel7);
    }
}
