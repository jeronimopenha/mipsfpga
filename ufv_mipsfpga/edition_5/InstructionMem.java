package ufv_mipsfpga.edition_5;

import hades.models.PortStdLogicVector;
import hades.simulator.Port;
import hades.symbols.BboxRectangle;
import hades.symbols.BusPortSymbol;
import hades.symbols.ClassLabel;
import hades.symbols.FatLabel;
import hades.symbols.InstanceLabel;
import hades.symbols.PortLabel;
import hades.symbols.Rectangle;
import hades.symbols.Symbol;

public class InstructionMem
        extends hades.models.rtlib.memory.ROM {

    public InstructionMem() {
        super();
    }

    @Override
    protected void constructPorts() {
        port_D = new PortStdLogicVector(this, "Inst", Port.OUT, null, n_bits);
        port_A = new PortStdLogicVector(this, "Addr", Port.IN, null,
                getAddressBusWidth());

        ports = new Port[2];
        ports[0] = port_A;
        ports[1] = port_D;
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

        Rectangle rec = new Rectangle();
        rec.initialize("0 0 6000 6000");
        symbol.addMember(rec);

        InstanceLabel instlabel = new InstanceLabel();
        instlabel.initialize("150 1200 inst_mem");
        symbol.addMember(instlabel);

        ClassLabel classlabel = new ClassLabel();
        classlabel.initialize("150 4800 instruction_mem");
        symbol.addMember(classlabel);

        FatLabel fatlabel0 = new FatLabel();
        fatlabel0.initialize("3000 2500 2 Instruction");
        symbol.addMember(fatlabel0);

        FatLabel fatlabel1 = new FatLabel();
        fatlabel1.initialize("2900 3500 2 Memory");
        symbol.addMember(fatlabel1);

        PortLabel portlabel0 = new PortLabel();
        portlabel0.initialize("5700 3300 3 Inst");
        symbol.addMember(portlabel0);

        PortLabel portlabel1 = new PortLabel();
        portlabel1.initialize("300 600 Address");
        symbol.addMember(portlabel1);

        BusPortSymbol busportsymbol0 = new BusPortSymbol();
        busportsymbol0.initialize("6000 3000 Inst");
        symbol.addMember(busportsymbol0);

        BusPortSymbol busportsymbol1 = new BusPortSymbol();
        busportsymbol1.initialize("0 600 Addr");
        symbol.addMember(busportsymbol1);

    }
}
