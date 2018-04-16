/* 
 * Alu.java - class ufv.edition_5.Alu
 * 
 * 24.10.2014 - MK.Computer.Organization.and.Design.4th.Edition.Oct.2011, Page 316;
 *  
 * (C) Racyus Delano - racyusdelanoo@gmail.com
 */
package ufv_mipsfpga.edition_5.fig451;

import hades.models.Const1164;
import hades.models.PortStdLogic1164;
import hades.models.PortStdLogicVector;
import hades.models.StdLogic1164;
import hades.models.StdLogicVector;
import hades.signals.Signal;
import hades.simulator.Port;
import hades.simulator.SimEvent;
import hades.simulator.SimEvent1164;
import hades.symbols.BboxRectangle;
import hades.symbols.BusPortSymbol;
import hades.symbols.ClassLabel;
import hades.symbols.FatLabel;
import hades.symbols.InstanceLabel;
import hades.symbols.Polyline;
import hades.symbols.PortLabel;
import hades.symbols.PortSymbol;
import hades.symbols.Symbol;

public class Alu extends hades.models.rtlib.GenericRtlibObject {

    protected PortStdLogicVector port_A, port_B, port_opCode, port_OutAluResult;
    protected PortStdLogic1164 port_zero;

    protected StdLogicVector value_A, value_B, value_opCode;
    protected StdLogic1164 output_0, output_1, value;

    public Alu() {
        super();
    }

    @Override
    public void constructPorts() {
        port_A = new PortStdLogicVector(this, "A", Port.IN, null, n_bits);
        port_B = new PortStdLogicVector(this, "B", Port.IN, null, n_bits);
        port_opCode = new PortStdLogicVector(this, "port_opCode", Port.IN, null, 4);
        port_zero = new PortStdLogic1164(this, "port_zero", Port.OUT, null);
        port_OutAluResult = new PortStdLogicVector(this, "port_OutAluResult", Port.OUT, null, n_bits);

        ports = new Port[5];
        ports[0] = port_A;
        ports[1] = port_B;
        ports[2] = port_opCode;
        ports[3] = port_zero;
        ports[4] = port_OutAluResult;

        output_0 = new StdLogic1164(StdLogic1164._0);
        output_1 = new StdLogic1164(StdLogic1164._1);

        vectorOutputPort = port_OutAluResult;
    }

    /**
     * evaluate(): called by the simulation engine on all events that concern
     * this object. The object is responsible for updating its internal state
     * and for scheduling all pending output events.
     */
    @Override
    public void evaluate(Object arg) {

        Signal signal_A, signal_B, signal_opCode, signal_OutAluResult, signal_zero;

        if ((signal_A = port_A.getSignal()) == null) {
            vector = vector_UUU.copy();
        } else if ((signal_B = port_B.getSignal()) == null) {
            vector = vector_UUU.copy();
        } else if ((signal_opCode = port_opCode.getSignal()) == null) {
            vector = vector_UUU.copy();
        } else {

            value_A = (StdLogicVector) signal_A.getValue();
            value_B = (StdLogicVector) signal_B.getValue();
            value_opCode = (StdLogicVector) signal_opCode.getValue();

            int opcode = (int) value_opCode.getValue();

            operacao(value_A, value_B, opcode);

            if (((int) vector.getValue()) == 0) {
                value = Const1164.__1;
            } else {
                value = Const1164.__0;
            }
        }

        double time = simulator.getSimTime() + delay;

        if ((signal_OutAluResult = port_OutAluResult.getSignal()) != null) {
            simulator.scheduleEvent(
                    new SimEvent(signal_OutAluResult, time, vector, port_OutAluResult));
        }

        if ((signal_zero = port_zero.getSignal()) != null) {
            simulator.scheduleEvent(
                    SimEvent1164.createNewSimEvent(signal_zero, time, value, port_zero));
        }

    }

    public void operacao(StdLogicVector A, StdLogicVector B, int opcode) {

        //OBS: opcode based in page 317 the book
        vector = new StdLogicVector(n_bits, 0);
        switch (opcode) {
            //(Operation AND - Instruction AND)
            case 0: {
                vector = A.and_bitwise(B);

            }
            break;

            //(Operation OR - Instruction OR)
            case 1: {
                vector = A.or_bitwise(B);

            }
            break;

            //(Operation ADD - Instructions Type R and Lw/Sw)
            case 2: {
                vector = A.add(B);
            }
            break;

            //(Operation Sub - Instructions Beq and SUB)
            case 6: {
                vector = A.sub(B);

                int result = (int) vector.getValue();

            }
            break;

            //(Operation A Lower B - Instruction SLT)
            case 7: {
                if (A.getSignedValue() < B.getSignedValue()) {
                    vector = new StdLogicVector(n_bits, 1);
                } else {
                    vector = new StdLogicVector(n_bits, 0);
                }
            }
            break;
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
        bbr.initialize("0 0 2400 4800 ");
        symbol.addMember(bbr);

        Polyline polyline = new Polyline();
        polyline.initialize("8 0 0 0 2100 600 2400 0 2700 0 4800 2400 3600 2400 1200 0 0");
        symbol.addMember(polyline);

        ClassLabel classlabel = new ClassLabel();
        classlabel.initialize("1600 4700 1 Alu");
        symbol.addMember(classlabel);

        InstanceLabel instlabel = new InstanceLabel();
        instlabel.initialize("1200 0 1 alu");
        symbol.addMember(instlabel);

        FatLabel fatlabel = new FatLabel();
        fatlabel.initialize("1400 2800 2 Alu ");
        symbol.addMember(fatlabel);

        PortSymbol portsymbol = new PortSymbol();
        portsymbol.initialize("2400 1800 port_zero");
        symbol.addMember(portsymbol);

        BusPortSymbol busportsymbol0 = new BusPortSymbol();
        busportsymbol0.initialize("2400 3000 port_OutAluResult");
        symbol.addMember(busportsymbol0);

        BusPortSymbol busportsymbol1 = new BusPortSymbol();
        busportsymbol1.initialize("1200 4200 port_opCode");
        symbol.addMember(busportsymbol1);

        BusPortSymbol busportsymbol2 = new BusPortSymbol();
        busportsymbol2.initialize("0    1160 A");
        symbol.addMember(busportsymbol2);

        BusPortSymbol busportsymbol3 = new BusPortSymbol();
        busportsymbol3.initialize("0    3600 B");
        symbol.addMember(busportsymbol3);

        PortLabel portlabel0 = new PortLabel();
        portlabel0.initialize("400  1300 2 A");
        symbol.addMember(portlabel0);

        PortLabel portlabel1 = new PortLabel();
        portlabel1.initialize("400  3700 2 B");
        symbol.addMember(portlabel1);

        PortLabel portlabel2 = new PortLabel();
        portlabel2.initialize("1800 1900 2 Zero");
        symbol.addMember(portlabel2);

        PortLabel portlabel3 = new PortLabel();
        portlabel3.initialize("1800 3200 2 ALU");
        symbol.addMember(portlabel3);

        PortLabel portlabel4 = new PortLabel();
        portlabel4.initialize("1800 3500 2 result");
        symbol.addMember(portlabel4);

        PortLabel portlabel5 = new PortLabel();
        portlabel5.initialize("1000 4000 2 opCode");
        symbol.addMember(portlabel5);

    }
}
