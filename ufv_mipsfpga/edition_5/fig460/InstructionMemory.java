/* ROM.java - hades.models.rtl.ROM
 *
 * 29.08.98 - check isConnected() before resize()ing, new Symbol
 * 27.08.98 - allow resize()ing of the memory array
 * 26.08.98 - first version
 *
 * (C) F.N.Hendrich, hendrich@informatik.uni-hamburg.de
 */
package ufv_mipsfpga.edition_5.fig460;

import hades.models.PortStdLogicVector;
import hades.models.StdLogicVector;
import hades.models.rtlib.memory.GenericMemory;
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


/**
 * ROM - a generic ROM with n words by m bits.
 * <p>
 * This class models a standard LSI ROM without clocking or enable.
 * <p>
 *
 * @author F.N.Hendrich
 * @version 0.4 23.02.98
 */
public class InstructionMemory
        extends GenericMemory {

    protected PortStdLogicVector port_Address, port_Instruction;

    public final static double t_access = 30.0E-9; // read access time
    public final static double t_tristate = 5.0E-9; // outputs -> Z
    public final static double t_undefined = 6.0E-9; // outputs -> X

    /**
     * InstructionMemory constructor
     */
    public InstructionMemory() {
        super();
    }

    @Override
    protected void constructPorts() {
        port_Address = new PortStdLogicVector(this, "Addr", Port.IN, null, 12);
        port_Instruction = new PortStdLogicVector(this, "Inst", Port.OUT, null, 32);

        ports = new Port[2];
        ports[0] = port_Address;
        ports[1] = port_Instruction;
    }

    @Override
    public boolean canChangeSize() {
        return !isConnected();
    }

    @Override
    public void setSize(int n_words) {
        resize(n_words, getBitsPerWord());
    }

    @Override
    public void setBitsPerWord(int n_bits) {
        resize(getSize(), n_bits);
    }

    @Override
    public boolean resize(int n_words, int n_bits) {
        this.n_words = n_words;
        this.n_bits = n_bits;

        this.data = new long[n_words];
        initializeWithZeroes();

        constructStandardValues();
        constructPorts();
        return true;
    }

    /**
     * elaborate(): On elaboration, the inputs of the ROM will be undefined.
     * Correspondingly, the outputs are, too.
     */
    @Override
    public void elaborate(Object arg) {

        simulator = parent.getSimulator();

        if (simulator != null && port_Instruction.getSignal() != null) {
            simulator.scheduleEvent(
                    new SimEvent(port_Instruction.getSignal(), 0.0, vector_UUU.copy(), port_Instruction));
        }
    }

    /**
     * evaluate(): Return the data at address after t_access
     */
    @Override
    public void evaluate(Object arg) {

        Signal signal_D = port_Instruction.getSignal();
        if (signal_D == null) {
            return;
        }

        double time = simulator.getSimTime() + t_access;

        StdLogicVector vector_A = port_Address.getVectorOrUUU();
        StdLogicVector vector_D = null;
        int addr;
        long data;

        if (vector_A.has_UXZ()) {
            vector_D = vector_UUU.copy();
            addr = UNDEFINED;
            data = UNDEFINED;
        } else {
            addr = (int) vector_A.getValue();
            data = getDataAt(addr);
            vector_D = new StdLogicVector(32, data);
            notifyReadListeners(addr, data);
        }

        simulator.scheduleEvent(
                new SimEvent(signal_D, time, vector_D, port_Instruction));
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
        bbr.initialize("0 0 6000 6000");
        symbol.addMember(bbr);

        Polyline polyline1 = new Polyline();
        polyline1.initialize("5 0 0 0 6000 6000 6000 6000 0 0 0");
        symbol.addMember(polyline1);

        ClassLabel classlabel = new ClassLabel();
        classlabel.initialize("5150 4800 3 InstructionMemory");
        symbol.addMember(classlabel);

        InstanceLabel instancelabel = new InstanceLabel();
        instancelabel.initialize("150 1200 3 InstructionMemory");
        symbol.addMember(instancelabel);

        FatLabel fatlabel0 = new FatLabel();
        fatlabel0.initialize("3000 2500 2 Instruction");
        symbol.addMember(fatlabel0);

        FatLabel fatlabel1 = new FatLabel();
        fatlabel1.initialize("2900 3500 2 Memory");
        symbol.addMember(fatlabel1);

        BusPortSymbol busportsymbol0 = new BusPortSymbol();
        busportsymbol0.initialize("0 3000 Addr");
        symbol.addMember(busportsymbol0);

        BusPortSymbol busportsymbol1 = new BusPortSymbol();
        busportsymbol1.initialize("6000 3600 Inst");
        symbol.addMember(busportsymbol1);

        PortLabel portlabel0 = new PortLabel();
        portlabel0.initialize("300 600 1 Address");
        symbol.addMember(portlabel0);

        PortLabel portlabel1 = new PortLabel();
        portlabel1.initialize("5700 3300 2 Inst");
        symbol.addMember(portlabel1);
    }
    
}
