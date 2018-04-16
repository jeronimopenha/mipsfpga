/* RegBank.java - hades.models.rtl.RegBank
 * 
 * standard two-read one-write port register bank, with write-enable
 * and clock input for writing.
 *
 * 23-10-2014 - MK.Computer.Organization.and.Design.4th.Edition.Oct.2011, P.360
 *
 * (C) T.T. Almeida, thales.almeida@ufv.edition_5.br
 */
package ufv_mipsfpga.edition_5;


import hades.models.PortStdLogic1164;
import hades.models.PortStdLogicVector;
import hades.models.StdLogic1164;
import hades.models.StdLogicVector;
import hades.signals.SignalStdLogic1164;
import hades.simulator.Port;
import hades.symbols.BboxRectangle;
import hades.symbols.BusPortSymbol;
import hades.symbols.ClassLabel;
import hades.symbols.FatLabel;
import hades.symbols.InstanceLabel;
import hades.symbols.Polyline;
import hades.symbols.PortLabel;
import hades.symbols.PortSymbol;
import hades.symbols.Symbol;

/**
 * RegBank - a generic register band with two-read ports (X,Y) and one-write (Z)
 * port with n words by m bits. It has a global active-low write-enable and an
 * edge-sensitive clock for writing.
 * <p>
 * Note that the model currently does not check for timing violations during
 * write cycles.
 * <p>
 */
public class RegBank
        extends hades.models.rtlib.memory.RegBank {

    protected PortStdLogicVector port_ADebug, port_DDebug;

    /**
     * RegBank constructor
     */
    public RegBank() {
        super();
    }

    protected void constructPorts() {
        int na = getAddressBusWidth();
        port_AX = new PortStdLogicVector(this, "ReadRegister1", Port.IN, null, na);
        port_AY = new PortStdLogicVector(this, "ReadRegister2", Port.IN, null, na);
        port_AZ = new PortStdLogicVector(this, "WriteRegister", Port.IN, null, na);

        port_ADebug = new PortStdLogicVector(this, "ReadDebug", Port.IN, null, na);

        port_DX = new PortStdLogicVector(this, "ReadData1", Port.OUT, null, n_bits);
        port_DY = new PortStdLogicVector(this, "ReadData2", Port.OUT, null, n_bits);
        port_DDebug = new PortStdLogicVector(this, "DataDebug", Port.OUT, null, n_bits);
        port_DZ = new PortStdLogicVector(this, "WriteData", Port.IN, null, n_bits);

        port_nWE = new PortStdLogic1164(this, "RegWrite", Port.IN, null);
        port_CLK = new PortStdLogic1164(this, "CLK", Port.IN, null);

        ports = new Port[10];
        ports[0] = port_AX;
        ports[1] = port_AY;
        ports[2] = port_AZ;
        ports[3] = port_DX;
        ports[4] = port_DY;
        ports[5] = port_DZ;
        ports[6] = port_nWE;
        ports[7] = port_CLK;
        ports[8] = port_ADebug;
        ports[9] = port_DDebug;
    }

    /**
     * evaluate(): If either port_CLK or port_nWE are undefined, the RegBank
     * data is invalidated, and the DO1 value is undefined.
     * <p>
     * Warning: The RegBank model currently does not check for timing violations
     * or address glitches during write cycles. Instead, the corresponding
     * memory locations are written.
     */
    @Override
    public void evaluate(Object arg) {

        double time = simulator.getSimTime() + t_access;

        StdLogicVector vector_AX = port_AX.getVectorOrUUU();
        StdLogicVector vector_AY = port_AY.getVectorOrUUU();
        StdLogicVector vector_AZ = port_AZ.getVectorOrUUU();
        StdLogicVector vector_ADebug = port_ADebug.getVectorOrUUU();

        StdLogicVector vector_DX = null;
        StdLogicVector vector_DY = null;
        StdLogicVector vector_DDebug = null;
        StdLogicVector vector_DZ = port_DZ.getVectorOrUUU();

        StdLogic1164 value_nWE = port_nWE.getValueOrU();
        StdLogic1164 value_CLK = port_CLK.getValueOrU();

        if (!value_nWE.is_01()) {
            message("-W- " + toString()
                    + "nWE undefined: data loss would occur! Ignoring...");
        } else if (!value_CLK.is_01()) {
            message("-W- " + toString()
                    + "CLK undefined: data loss would occur! Ignoring...");
        } else if (vector_AZ.has_UXZ()) {
            message("-W- " + toString()
                    + "AZ address undefined: data loss would occur! Ignoring...");
        } else {
            SignalStdLogic1164 clk = (SignalStdLogic1164) port_CLK.getSignal();
            if (value_nWE.is_1() && clk != null && clk.hasFallingEdge()) {
                int addr_z = (int) vector_AZ.getValue();
                long old_z = getDataAt(addr_z);
                long data_z = vector_DZ.getValue();

                setDataAt(addr_z, data_z);
                notifyWriteListeners(addr_z, old_z, data_z);
            }
        }

        //
        // read two values: DX = regbank[AX], DY = regbank[AY]
        //
        if (vector_AX.has_UXZ()) {
            vector_DX = vector_UUU.copy();
        } else {
            int addr_x = (int) vector_AX.getValue();
            long data_x = getDataAt(addr_x);
            vector_DX = new StdLogicVector(n_bits, data_x);
            notifyReadListeners(addr_x, data_x);

            schedule(port_DX, vector_DX, time + t_access);
        }

        if (vector_AY.has_UXZ()) {
            vector_DY = vector_UUU.copy();
        } else {
            int addr_y = (int) vector_AY.getValue();
            long data_y = getDataAt(addr_y);
            vector_DY = new StdLogicVector(n_bits, data_y);
            notifyReadListeners(addr_y, data_y);

            schedule(port_DY, vector_DY, time + t_access);
        }

        if (vector_ADebug.has_UXZ()) {
            vector_DDebug = vector_UUU.copy();
        } else {
            int addr_y = (int) vector_ADebug.getValue();
            long data_y = getDataAt(addr_y);
            vector_DDebug = new StdLogicVector(n_bits, data_y);
            notifyReadListeners(addr_y, data_y);

            schedule(port_DDebug, vector_DDebug, time + t_access);
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
        bbr.initialize("0 0 6000 6000");
        symbol.addMember(bbr);

        ClassLabel classlabel = new ClassLabel();
        classlabel.initialize("2000 4800 3 RegBank");
        symbol.addMember(classlabel);

        InstanceLabel instlabel = new InstanceLabel();
        instlabel.initialize("2000 1550 3 regbank");
        symbol.addMember(instlabel);

        FatLabel fatlabel = new FatLabel();
        fatlabel.initialize("3600 3300 2 Registers");
        symbol.addMember(fatlabel);

        Polyline pol0 = new Polyline();
        pol0.initialize("3 2100 0 2400 600 2700 0 ");
        symbol.addMember(pol0);

        Polyline pol1 = new Polyline();
        pol1.initialize("4 3300 0 3300 300 3900 300 3900 0");
        symbol.addMember(pol1);

        Polyline pol2 = new Polyline();
        pol2.initialize("5 0 6000  0 0 6000 0 6000 6000 0 6000");
        symbol.addMember(pol2);

        PortSymbol portsymbol0 = new PortSymbol();
        portsymbol0.initialize("2400 0 CLK");
        symbol.addMember(portsymbol0);

        PortSymbol portsymbol1 = new PortSymbol();
        portsymbol1.initialize("3600 0 RegWrite");
        symbol.addMember(portsymbol1);

        BusPortSymbol busportsymbol0 = new BusPortSymbol();
        busportsymbol0.initialize("0 1200 ReadRegister1");
        symbol.addMember(busportsymbol0);

        BusPortSymbol busportsymbol1 = new BusPortSymbol();
        busportsymbol1.initialize("0 2400 ReadRegister2");
        symbol.addMember(busportsymbol1);

        BusPortSymbol busportsymbol2 = new BusPortSymbol();
        busportsymbol2.initialize("0 3600 WriteRegister");
        symbol.addMember(busportsymbol2);

        BusPortSymbol busportsymbol3 = new BusPortSymbol();
        busportsymbol3.initialize("0 4800 WriteData");
        symbol.addMember(busportsymbol3);

        BusPortSymbol busportsymbol4 = new BusPortSymbol();
        busportsymbol4.initialize("1200 6000 ReadDebug");
        symbol.addMember(busportsymbol4);

        BusPortSymbol busportsymbol5 = new BusPortSymbol();
        busportsymbol5.initialize("1800 6000 DataDebug");
        symbol.addMember(busportsymbol5);

        BusPortSymbol busportsymbol6 = new BusPortSymbol();
        busportsymbol6.initialize("6000 1200 ReadData1");
        symbol.addMember(busportsymbol6);

        BusPortSymbol busportsymbol7 = new BusPortSymbol();
        busportsymbol7.initialize("6000 3000 ReadData2");
        symbol.addMember(busportsymbol7);

        PortLabel portlabel0 = new PortLabel();
        portlabel0.initialize("2400 0 1 CLK");
        symbol.addMember(portlabel0);

        PortLabel portlabel1 = new PortLabel();
        portlabel1.initialize("3600 0 1 RegWrite");
        symbol.addMember(portlabel1);

        PortLabel portlabel2 = new PortLabel();
        portlabel2.initialize("250 1200 1 Read");
        symbol.addMember(portlabel2);

        PortLabel portlabel3 = new PortLabel();
        portlabel3.initialize("250 1500 1 register1");
        symbol.addMember(portlabel3);

        PortLabel portlabel4 = new PortLabel();
        portlabel4.initialize("250 2400 1 Read");
        symbol.addMember(portlabel4);

        PortLabel portlabel5 = new PortLabel();
        portlabel5.initialize("250 2700 1 register2");
        symbol.addMember(portlabel5);

        PortLabel portlabel6 = new PortLabel();
        portlabel6.initialize("250 3600 1 Write");
        symbol.addMember(portlabel6);

        PortLabel portlabel7 = new PortLabel();
        portlabel7.initialize("250 3900 1 register");
        symbol.addMember(portlabel7);

        PortLabel portlabel8 = new PortLabel();
        portlabel8.initialize("250 4800 1 Write");
        symbol.addMember(portlabel8);

        PortLabel portlabel9 = new PortLabel();
        portlabel9.initialize("250 5100 1 data");
        symbol.addMember(portlabel9);

        PortLabel portlabel10 = new PortLabel();
        portlabel10.initialize("5200 1200 2 Read");
        symbol.addMember(portlabel10);

        PortLabel portlabel11 = new PortLabel();
        portlabel11.initialize(" 5200 1500 2 data1");
        symbol.addMember(portlabel11);

        PortLabel portlabel12 = new PortLabel();
        portlabel12.initialize("5200 3000 2 Read");
        symbol.addMember(portlabel12);

        PortLabel portlabel13 = new PortLabel();
        portlabel13.initialize("5200 3300 2 data2");
        symbol.addMember(portlabel13);

        PortLabel portlabel14 = new PortLabel();
        portlabel14.initialize("1200 5700 2 RA");
        symbol.addMember(portlabel14);

        PortLabel portlabel15 = new PortLabel();
        portlabel15.initialize("1800 5700 2 RD");
        symbol.addMember(portlabel15);

    }

}
