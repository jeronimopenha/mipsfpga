/* StringDisplay.java --  class hades.models.string.StringDisplay
 *
 * a stub class to display Strings in its Symbol
 *
 * 30.03.98 - first version
 *
 * (C) F.N.Hendrich, hendrich@informatik.uni-hamburg.de
 * 
 * 24/05/2016
 * String Debug Component. - UFV MG Brasil
 * Jeronimo Costa Penha - jeronimopenha@gmail.com
 */
package ufv_mipsfpga.debug;

import hades.models.PortStdLogic1164;
import hades.models.PortStdLogicVector;
import hades.models.StdLogic1164;
import hades.models.StdLogicVector;
import hades.signals.Signal;
import hades.simulator.Port;
import hades.simulator.SimEvent1164;
import hades.symbols.BboxRectangle;
import hades.symbols.BusPortSymbol;
import hades.symbols.InstanceLabel;
import hades.symbols.Label;
import hades.symbols.Polyline;
import hades.symbols.PortLabel;
import hades.symbols.PortSymbol;
import hades.symbols.Rectangle;
import hades.symbols.Symbol;
import ufv_mipsfpga.ColorLabel;

/**
 * StringDisplay - a SimObject to display Strings in its Symbol Signals are
 * expected to be StringSignals.
 *
 * @author F.N.Hendrich
 * @version 0.1 25.03.98
 */
public class Hazard_Debug extends hades.models.rtlib.GenericRtlibObject {

    protected Port port_A, port_RES, port_Out, port_CLK, port_CTRL;
    protected String string;

    protected Label stringLabel;
    protected Rectangle background;

    protected StdLogicVector value_A, next_Out;

    protected double t_delay;

    public Hazard_Debug() {
        super();
        string = "NOP";
        t_delay = 10.0E-8;
        next_Out = new StdLogicVector(32);
    }

    @Override
    protected void constructPorts() {
        
        port_A = new PortStdLogicVector(this, "A", Port.IN, null, 32);
        port_RES = new PortStdLogic1164(this, "RES", Port.IN, null);
        port_CLK = new PortStdLogic1164(this, "CLK", Port.IN, null);
        port_CTRL = new PortStdLogic1164(this, "CTRL", Port.IN, null);
        port_Out = new PortStdLogicVector(this, "O", Port.OUT, null, 32);

        ports = new Port[5];
        ports[0] = port_A;
        ports[1] = port_RES;
        ports[2] = port_CLK;
        ports[3] = port_CTRL;
        ports[4] = port_Out;
    }

    public void setString(String s) {
        this.string = s;
        stringLabel.setText(s);
        getSymbol().painter.paint(getSymbol(), 100);
    }

    public String getString() {
        return this.string;
    }

    private int ret_imediate(String im) {
        String temp = "";
        int imediato;

        if (im.substring(0, 1).equals("1")) {
            for (int i = 0; i < 16; i++) {
                if (im.substring(i, i + 1).equals("1")) {
                    temp = temp + "0";
                } else {
                    temp = temp + "1";
                }
            }
            imediato = bin_to_int(temp, 16) + 1;
            imediato *= -1;
        } else {
            imediato = bin_to_int(im, 16);
        }
        return imediato;
    }

    private int bin_to_int(String im, int l) {//converte binario para decimal
        int soma = 0, cont = 0;
        for (int i = l; i > 0; i--) {
            if (im.substring(i - 1, i).equals("1")) {
                soma += (int) Math.pow(2, cont);
            }
            cont++;
        }
        return soma;
    }

    @Override
    public void evaluate(Object arg) {
        int posicao = 0, i, tamanho;
        String temp, str = "";

        Signal signal = port_A.getSignal();
        Signal signal_CLK = port_CLK.getSignal();
        Signal signal_CTRL = port_CTRL.getSignal();
        Signal signal_RES = port_RES.getSignal();

        if (signal_CLK == null) {
            return;
        }
        if (signal_CTRL == null) {
            return;
        }
        if (signal_RES == null) {
            return;
        }
        if (signal == null) {
            return;
        }

        StdLogic1164 clock = (StdLogic1164) signal_CLK.getValue();
        StdLogic1164 reset = (StdLogic1164) signal_RES.getValue();
        StdLogic1164 control = (StdLogic1164) signal_CTRL.getValue();

        int clk_val = (int) clock.getValue();
        int reset_val = (int) reset.getValue();
        int control_val = (int) control.getValue();

        value_A = (StdLogicVector) signal.getValue();

        temp = value_A.toString();
        tamanho = temp.length();

        for (i = 0; i < tamanho; i++) {  //busca o índice do primeiro bit na string
            if (temp.substring(i, i + 1).equals(":")) {
                posicao = i + 1;
                break;
            }
        }

        int opcode = bin_to_int(temp.substring(i + 1, i + 7), 6);//retorna o valor de opcode inteiro

        switch (opcode) {
            case 35: // LW
                str = "LW " + "R" + bin_to_int(temp.substring(i + 12, i + 17), 5) + ", " + ret_imediate(temp.substring(i + 17, i + 33)) + "(R" + bin_to_int(temp.substring(i + 7, i + 12), 5) + ")";
                break;
            case 43: // SW
                str = "SW " + "R" + bin_to_int(temp.substring(i + 12, i + 17), 5) + ", " + ret_imediate(temp.substring(i + 17, i + 33)) + "(R" + bin_to_int(temp.substring(i + 7, i + 12), 5) + ")";
                break;
            case 4: //BEQ
                str = "BEQ " + "R" + bin_to_int(temp.substring(i + 12, i + 17), 5) + ", " + ret_imediate(temp.substring(i + 17, i + 33)) + "(R" + bin_to_int(temp.substring(i + 7, i + 12), 5) + ")";
                break;
            case 0: // R Type

                int func = bin_to_int(temp.substring(i + 27, i + 33), 6);

                switch (func) {
                    case 32://add
                        str = "ADD " + "R" + bin_to_int(temp.substring(i + 17, i + 22), 5) + ", R" + bin_to_int(temp.substring(i + 7, i + 12), 5) + ", R" + bin_to_int(temp.substring(i + 12, i + 17), 5);
                        break;
                    case 34://sub
                        str = "SUB " + "R" + bin_to_int(temp.substring(i + 17, i + 22), 5) + ", R" + bin_to_int(temp.substring(i + 7, i + 12), 5) + ", R" + bin_to_int(temp.substring(i + 12, i + 17), 5);
                        break;
                    case 36://and
                        str = "AND " + "R" + bin_to_int(temp.substring(i + 17, i + 22), 5) + ", R" + bin_to_int(temp.substring(i + 7, i + 12), 5) + ", R" + bin_to_int(temp.substring(i + 12, i + 17), 5);
                        break;
                    case 37://or
                        str = "OR " + "R" + bin_to_int(temp.substring(i + 17, i + 22), 5) + ", R" + bin_to_int(temp.substring(i + 7, i + 12), 5) + ", R" + bin_to_int(temp.substring(i + 12, i + 17), 5);
                        break;
                    case 42://slt
                        str = "SLT " + "R" + bin_to_int(temp.substring(i + 17, i + 22), 5) + ", R" + bin_to_int(temp.substring(i + 7, i + 12), 5) + ", R" + bin_to_int(temp.substring(i + 12, i + 17), 5);
                        break;
                    default:
                        str = "NOP";
                }
                break;
            default:
                str = "NOP";
        }
        setString(str);

        if (reset_val == 2) {
            str = "NOP";
            next_Out.setValue(0);
            setString(str);
        } else {

            if (clk_val == 2) {
                if (control_val == 3) {
                    next_Out.setValue(0);
                } else {
                    next_Out.setValue(value_A.getValue());
                }
            } else if (clk_val == 3) {
                Signal signalOut = port_Out.getSignal();
                if (signalOut == null) {
                    return;
                }

                double time = simulator.getSimTime() + t_delay;

                simulator.scheduleEvent(SimEvent1164.createNewSimEvent(signalOut, time, next_Out, port_Out));
            }
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
        bbr.initialize("0 -600 4000 600");

        InstanceLabel ilabel = new InstanceLabel();
        ilabel.initialize("4100 200 " + getName());

        BusPortSymbol busportsymbol0 = new BusPortSymbol();
        busportsymbol0.initialize("-600 0 A");

        BusPortSymbol busportsymbol1 = new BusPortSymbol();
        busportsymbol1.initialize("5400 0 O");

        PortSymbol portsymbolclk = new PortSymbol();
        portsymbolclk.initialize("0 600 CLK");

        PortSymbol portsymbolctrl = new PortSymbol();
        portsymbolctrl.initialize("0 -600 CTRL");

        PortSymbol portsymbolres = new PortSymbol();
        portsymbolres.initialize("600 600 RES");

        PortLabel portlabel = new PortLabel();
        portlabel.initialize("-400 0 A");

        PortLabel portlabelO = new PortLabel();
        portlabelO.initialize("5000 0 O");

        PortLabel portlabelclk = new PortLabel();
        portlabelclk.initialize("0 400 CLK");

        PortLabel portlabelctrl = new PortLabel();
        portlabelctrl.initialize("0 400 CTRL");

        PortLabel portlabelres = new PortLabel();
        portlabelres.initialize("600 400 RES");

        stringLabel = new ColorLabel();
        stringLabel.initialize("0 200 " + getString());

        background = new Rectangle();
        background.initialize("0 -600 4000 600");
        jfig.objects.FigAttribs attr = background.getAttributes();
        attr.currentLayer = 50;
        attr.lineColor = null;
        attr.fillColor = java.awt.Color.white;
        attr.fillStyle = attr.SOLID_FILL;
        background.setAttributes(attr);

        Polyline border = new Polyline();
        border.initialize("7 0 -600 -600 -600 -600 600 4800 600 5400 0 4800 -600 0 -600");

        //symbol.addMember( classlabel );
        symbol.addMember(ilabel);
        symbol.addMember(busportsymbol0);
        symbol.addMember(busportsymbol1);
        symbol.addMember(portsymbolclk);
        symbol.addMember(portsymbolres);
        symbol.addMember(portsymbolctrl);
        symbol.addMember(portlabel);
        symbol.addMember(portlabelO);
        symbol.addMember(portlabelclk);
        symbol.addMember(portlabelctrl);
        symbol.addMember(portlabelres);
        symbol.addMember(stringLabel);
        symbol.addMember(background);
        symbol.addMember(border);
        symbol.addMember(bbr);
    }
}
