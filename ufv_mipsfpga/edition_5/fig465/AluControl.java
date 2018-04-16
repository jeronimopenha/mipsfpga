/* 
 * AluControl.java - class ufv.AluControl
 * 
 * 23.10.2014 - MK.Computer.Organization.and.Design.4th.Edition.Oct.2011, Page 316;
 *  
 * (C) Racyus Delano - racyusdelanoo@gmail.com
 */
package ufv_mipsfpga.edition_5.fig465;

import hades.models.PortStdLogicVector;
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

public class AluControl extends hades.models.rtlib.GenericRtlibObject {

    protected PortStdLogicVector port_Funct, port_ALUOp, port_Out;
    protected StdLogicVector value_Funct, value_ALUOp;

    public AluControl() {
        super();
    }

    @Override
    public void constructPorts() {
        port_Funct = new PortStdLogicVector(this, "Funct", Port.IN, null, 6);
        port_ALUOp = new PortStdLogicVector(this, "ALUOp", Port.IN, null, 2);
        port_Out = new PortStdLogicVector(this, "OutALUcontrol", Port.OUT, null, 4);

        ports = new Port[3];
        ports[0] = port_Funct;
        ports[1] = port_ALUOp;
        ports[2] = port_Out;

        vectorOutputPort = port_Out;
    }

    /**
     * evaluate(): called by the simulation engine on all events that concern
     * this object. The object is responsible for updating its internal state
     * and for scheduling all pending output events.
     */
    @Override
    public void evaluate(Object arg) {

        Signal signal_Funct, signal_ALUOp, signal_Out;

        if ((signal_Funct = port_Funct.getSignal()) == null) {
            vector = vector_UUU.copy();
        } else if ((signal_ALUOp = port_ALUOp.getSignal()) == null) {
            vector = vector_UUU.copy();
        } else {

            value_Funct = (StdLogicVector) signal_Funct.getValue();
            value_ALUOp = (StdLogicVector) signal_ALUOp.getValue();

            int funct = (int) value_Funct.getValue();
            int aluOp = (int) value_ALUOp.getValue();

            operacao(funct, aluOp);
        }

        double time = simulator.getSimTime() + delay;

        if ((signal_Out = port_Out.getSignal()) != null) {
            simulator.scheduleEvent(
                    new SimEvent(signal_Out, time, vector, port_Out));
        }
    }

    public void operacao(int funct, int aluOp) {

        //OBS: aluOp and Funct based in page 317 the book
        switch (aluOp) {
            //(Instructions Type I - Lw e Sw) 
            //ADD is an operation that will be sent to ALU
            case 0: {

                vector = new StdLogicVector(4, 2);
            }
            break;

            //(Instruction Tipo I - Beq) 
            //SUB is an operation that will be sent to ALU
            case 1:

                vector = new StdLogicVector(4, 6);
                break;

            //(Instruction Type R) 
            //Operations on Type R		
            case 2: {
                switch (funct) {
                    //Operation ADD
                    case 32:
                        vector = new StdLogicVector(4, 2);
                        break;

                    //Operation SUB		 
                    case 34:
                        vector = new StdLogicVector(4, 6);
                        break;

                    //Operation AND			
                    case 36:
                        vector = new StdLogicVector(4, 0);
                        break;

                    //Operation OR		 
                    case 37:
                        vector = new StdLogicVector(4, 1);
                        break;

                    //Operation SLT		 
                    case 42:
                        vector = new StdLogicVector(4, 7);
                        break;
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
        bbr.initialize("0 0 3600 2400");
        symbol.addMember(bbr);
        
        Polyline polyline = new Polyline();
        polyline.initialize("5 0 0 3600 0 3600 2400 0 2400 0 0");
        symbol.addMember(polyline);
        
        ClassLabel classlabel = new ClassLabel();
        classlabel.initialize("700 -60 1 AluControl ");
        symbol.addMember(classlabel);
        
        InstanceLabel instancelabel = new InstanceLabel();
        instancelabel.initialize("4850 550 1 aluControl ");
        symbol.addMember(instancelabel);
        
        FatLabel fatlabel0 = new FatLabel();
        fatlabel0.initialize("1900 1000 2 ALU ");
        symbol.addMember(fatlabel0);
        
        FatLabel fatlabel1 = new FatLabel();
        fatlabel1.initialize("1860 1800 2 Control");
        symbol.addMember(fatlabel1);
        
        BusPortSymbol busportsymbol0 = new BusPortSymbol();
        busportsymbol0.initialize("3600 600 OutALUcontrol");
        symbol.addMember(busportsymbol0);
        
        BusPortSymbol busportsymbol1 = new BusPortSymbol();
        busportsymbol1.initialize("0    600 Funct");
        symbol.addMember(busportsymbol1);
        
        BusPortSymbol busportsymbol2 = new BusPortSymbol();
        busportsymbol2.initialize("1800 2400 ALUOp");
        symbol.addMember(busportsymbol2);
        
        PortLabel portlabel0 = new PortLabel();
        portlabel0.initialize("600  700 2 Funct");
        symbol.addMember(portlabel0);
        
        PortLabel portlabel1 = new PortLabel();
        portlabel1.initialize("1900  2260 2 AluOp");
        symbol.addMember(portlabel1);
       
        PortLabel portlabel2 = new PortLabel();
        portlabel2.initialize("3140 700 2 Out");
        symbol.addMember(portlabel2);
    }
}
