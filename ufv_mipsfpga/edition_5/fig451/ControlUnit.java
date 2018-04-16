/* 
 * alucontrol.java -  class ufv.edition_5.alucontrol
 *
 * 23.10.2014 - livro, pagina descreve o componente 
 * (C) Nome e email
 */
package ufv_mipsfpga.edition_5.fig451;

import hades.models.PortStdLogic1164;
import hades.models.PortStdLogicVector;
import hades.models.StdLogic1164;
import hades.models.StdLogicVector;
import hades.signals.Signal;
import hades.simulator.Port;
import hades.simulator.SimEvent;
import hades.simulator.SimKernel;
import hades.symbols.BboxRectangle;
import hades.symbols.BusPortSymbol;
import hades.symbols.FatLabel;
import hades.symbols.PortSymbol;
import hades.symbols.Rectangle;
import hades.symbols.Symbol;

public class ControlUnit
        extends hades.models.rtlib.GenericRtlibObject {

    protected PortStdLogicVector port_opcode, port_WB, port_M, port_EX;
    protected StdLogicVector value_opcode, vector_WB, vector_M, vector_EX;
    protected PortStdLogic1164 port_reset;

    public ControlUnit() {
        super();
    }

    public void constructPorts() {
        port_opcode = new PortStdLogicVector(this, "opcode", Port.IN, null, 6);
        port_WB = new PortStdLogicVector(this, "WB", Port.OUT, null, 2);
        /* WB  Reg-write and MemtoReg */
        port_M = new PortStdLogicVector(this, "M", Port.OUT, null, 3);
        /* Mem stage : branch, memread, memwrite */
        port_EX = new PortStdLogicVector(this, "EX", Port.OUT, null, 4);
        /* Ex - Regdst Aluop1 Aluop0 AluSrc */
        port_reset = new PortStdLogic1164(this, "R", Port.IN, null);
        //port_NR  = new PortStdLogic1164(   this, "NR",  Port.IN,  null );

        ports = new Port[5];
        ports[0] = port_opcode;
        ports[1] = port_WB;
        ports[2] = port_M;
        ports[3] = port_EX;
        ports[4] = port_reset;
    }

    /**
     * evaluate(): called by the simulation engine on all events that concern
     * this object. The object is responsible for updating its internal state
     * and for scheduling all pending output events.
     */
    @Override
    public void evaluate(Object arg) {

        SimKernel simulator = parent.getSimulator();
        double time = simulator.getSimTime() + delay;

        Signal signal_opcode, signal_WB, signal_M, signal_EX;

        if ((signal_opcode = port_opcode.getSignal()) == null) {
            vector_WB = vector_UUU.copy();
            vector_M = vector_UUU.copy();
            vector_EX = vector_UUU.copy();
        } else { // control function

            value_opcode = (StdLogicVector) signal_opcode.getValue();

            int code = (int) value_opcode.getValue();

            //SignalStdLogic1164  reset =  (SignalStdLogic1164) port_reset.getSignal();
            StdLogic1164 reset = port_reset.getValueOrU();

            switch (code) {
                /*  
                 WB  Reg-write and MemtoReg 
                 Mem stage : branch, memread, memwrite 
                 Ex - Regdst Aluop1 Aluop0 AluSrc 
                 */
                //Bits sequence
                //RegDst - OP0 - OP1 - ALUSrc - MemWrite - MemRead - Branch - RegWrite - MemToReg
                //  8    -  7  -  6  -   5    -    4     -    3    -   2    -    1     -     0
                //
                //opcode 0 = R
                case 0:
                    if (reset.is_0()) {
                        vector_WB = new StdLogicVector(2, 0);
                        vector_M = new StdLogicVector(3, 0); // branch=0,memread=1, memwrite=0 
                        vector_EX = new StdLogicVector(4, 0); // Regdest=0, ALUop=00, ALUsrc=1
                    } // regwrite =1 , memtoreg = 1
                    else {
                        vector_WB = new StdLogicVector(2, 2);
                        vector_M = new StdLogicVector(3, 0); // branch=0,memread=1, memwrite=0 
                        vector_EX = new StdLogicVector(4, 10); // Regdest=0, ALUop=00, ALUsrc=1
                    } // regwrite =1 , memtoreg = 1
                    break;
                //opcode 35 = LW
                case 35:
                    vector_WB = new StdLogicVector(2, 3); // regwrite =1 , memtoreg = 1
                    vector_M = new StdLogicVector(3, 2); // branch=0,memread=1, memwrite=0 
                    vector_EX = new StdLogicVector(4, 1); // Regdest=0, ALUop=00, ALUsrc=1
                    break;
                //opcode 43 = SW
                case 43:
                    vector_WB = new StdLogicVector(2, 0); // regwrite =1 , memtoreg = 1
                    vector_M = new StdLogicVector(3, 4); // branch=0,memread=1, memwrite=0 
                    vector_EX = new StdLogicVector(4, 1); // Regdest=0, ALUop=00, ALUsrc=1
                    break;
                //opcode 4 = BEQ
                case 4:
                    vector_WB = new StdLogicVector(2, 0); // regwrite =1 , memtoreg = 1
                    vector_M = new StdLogicVector(3, 1); // branch=0,memread=1, memwrite=0 
                    vector_EX = new StdLogicVector(4, 4); // Regdest=0, ALUop=00, ALUsrc=1
                    break;
                //Erro
                default:
                    vector_WB = new StdLogicVector(2, 0); // regwrite =1 , memtoreg = 1
                    vector_M = new StdLogicVector(3, 0); // branch=0,memread=1, memwrite=0 
                    vector_EX = new StdLogicVector(4, 0); // Regdest=0, ALUop=00, ALUsrc=1
                    break;
            }
        }

        if ((signal_WB = port_WB.getSignal()) != null) {
            simulator.scheduleEvent(
                    new SimEvent(signal_WB, time, vector_WB, port_WB));
        }
        if ((signal_M = port_M.getSignal()) != null) {
            simulator.scheduleEvent(
                    new SimEvent(signal_M, time, vector_M, port_M));
        }
        if ((signal_EX = port_EX.getSignal()) != null) {
            simulator.scheduleEvent(
                    new SimEvent(signal_EX, time, vector_EX, port_EX));
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

        FatLabel fatlabel = new FatLabel();
        fatlabel.initialize("1800 2700 2 Control");
        symbol.addMember(fatlabel);

        BusPortSymbol busportsymbol0 = new BusPortSymbol();
        busportsymbol0.initialize("3600 600 WB");
        symbol.addMember(busportsymbol0);

        BusPortSymbol busportsymbol1 = new BusPortSymbol();
        busportsymbol1.initialize("3600 2400 M");
        symbol.addMember(busportsymbol1);

        BusPortSymbol busportsymbol2 = new BusPortSymbol();
        busportsymbol2.initialize("3600 4200 EX");
        symbol.addMember(busportsymbol2);

        BusPortSymbol busportsymbol3 = new BusPortSymbol();
        busportsymbol3.initialize("0 2400 opcode");
        symbol.addMember(busportsymbol3);

        PortSymbol portsymbol = new PortSymbol();
        portsymbol.initialize("600 4800 R");
        symbol.addMember(portsymbol);
    }
}
