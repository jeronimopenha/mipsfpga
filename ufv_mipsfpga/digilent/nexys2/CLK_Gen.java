
package ufv_mipsfpga.digilent.nexys2;

import hades.models.PortStdLogic1164;
import hades.models.PortStdLogicVector;
import hades.models.StdLogic1164;
import hades.models.StdLogicVector;
import hades.signals.Signal;
import hades.signals.SignalStdLogic1164;
import hades.simulator.Port;
import hades.simulator.SimEvent1164;
import hades.symbols.BboxRectangle;
import hades.symbols.InstanceLabel;
import hades.symbols.Label;
import hades.symbols.Polyline;
import hades.symbols.PortLabel;
import hades.symbols.PortSymbol;
import hades.symbols.Rectangle;
import hades.symbols.Symbol;
import jfig.objects.FigAttribs;
import ufv_mipsfpga.ColorLabel;

public class CLK_Gen extends hades.models.rtlib.GenericRtlibObject {

    protected String string;
    protected Label stringLabel;
    protected Rectangle background;
    protected double t_delay;
    protected int reseted, total, done;

    protected Port port_A, port_EN, port_RES, port_CLK;
    protected StdLogicVector value_A;

    public CLK_Gen() {
        super();
        string = "0_D";
        t_delay = 0.5;
        reseted = 0;
        total = 0;
        done = 0;
    }

    @Override
    public void constructPorts() {
        port_A = new PortStdLogicVector(this, "A", Port.IN, null, 8);
        port_EN = new PortStdLogic1164(this, "EN", Port.IN, null);
        port_RES = new PortStdLogic1164(this, "RES", Port.IN, null);
        port_CLK = new PortStdLogic1164(this, "CLK", Port.OUT, null);

        ports = new Port[4];
        ports[0] = port_A;
        ports[1] = port_EN;
        ports[2] = port_RES;
        ports[3] = port_CLK;
    }

    @Override
    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
        this.symbol.setInstanceLabel("CLK_GEN");
    }

    public void setString(String s) {
        this.string = s;
        stringLabel.setText(s + "_D");
        getSymbol().painter.paint(getSymbol(), 100);
    }

    public String getString() {
        return this.string;
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

        Signal signal_A = port_A.getSignal();
        Signal signal_EN = port_EN.getSignal();
        Signal signal_RES = port_RES.getSignal();

        if (signal_RES == null) {
            return;
        }

        StdLogic1164 reset = (StdLogic1164) signal_RES.getValue();

        int i, tamanho;
        String temp, str;

        int reset_val = (int) reset.getValue();

        if (reset_val == 2) {
            reseted = 1;
            total = 0;

            Signal signalOut = port_CLK.getSignal();
            if (signalOut == null) {
                return;
            }
            double time = simulator.getSimTime() + t_delay;

            StdLogic1164 next_Out = new StdLogic1164(2);

            simulator.scheduleEvent(SimEvent1164.createNewSimEvent(signalOut, time, next_Out, port_CLK));
            str = Integer.toString(total);
            setString(str);
            return;
        }

        if (signal_A == null) {
            return;
        }
        if (signal_EN == null) {
            return;
        }

        StdLogic1164 enable = (StdLogic1164) signal_EN.getValue();
        SignalStdLogic1164 signal_temp;

        int en_val = (int) enable.getValue();

        signal_temp = (SignalStdLogic1164) port_EN.getSignal();

        if (en_val == 3 && reseted == 1 && done == 0) {
            done = 1;

            value_A = (StdLogicVector) signal_A.getValue();

            if (value_A.has_UXZ()) {
                return;
            } else {
                temp = value_A.toString();
            }
            tamanho = temp.length();
            for (i = 0; i < tamanho; i++) {  //busca o índice do primeiro bit na string
                if (temp.substring(i, i + 1).equals(":")) {
                    break;
                }
            }
            i++;

            int vezes = bin_to_int(temp.substring(i, i + 8), 8);
            StdLogic1164 next_Out;
            Signal signalOut;
            double time;

            total += vezes;

            str = Integer.toString(total);

            time = simulator.getSimTime() + t_delay;

            signalOut = port_CLK.getSignal();
            if (signalOut == null) {
                return;
            }

            for (i = 0; i < vezes; i++) {

                next_Out = new StdLogic1164(3);
                simulator.scheduleEvent(SimEvent1164.createNewSimEvent(signalOut, time, next_Out, port_CLK));
                time += t_delay;

                next_Out = new StdLogic1164(2);
                simulator.scheduleEvent(SimEvent1164.createNewSimEvent(signalOut, time, next_Out, port_CLK));
                time += t_delay;

            }
            setString(str);

        } else if (en_val == 2 && reseted == 1 && done == 1) {
            done = 0;
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
        bbr.initialize("0 0 4200 1200");

        InstanceLabel ilabel = new InstanceLabel();
        ilabel.initialize("4400 800 " + getName());

        //símbolos dos ports
        PortSymbol portsymbola = new PortSymbol();
        portsymbola.initialize("2400 1200 A");

        PortSymbol portsymbolclk = new PortSymbol();
        portsymbolclk.initialize("4200 600 CLK");

        PortSymbol portsymbolen = new PortSymbol();
        portsymbolen.initialize("1200 1200 EN");

        PortSymbol portsymbolres = new PortSymbol();
        portsymbolres.initialize("1800 1200 RES");

        //nomes dos ports no desenho
        PortLabel portlabelA = new PortLabel();
        portlabelA.initialize("2600 1600  A");

        PortLabel portlabelclk = new PortLabel();
        portlabelclk.initialize("4400 1000 CLK");

        PortLabel portlabelen = new PortLabel();
        portlabelen.initialize("1000 1600 EN");

        PortLabel portlabelres = new PortLabel();
        portlabelres.initialize("1800 2000 RES");

        stringLabel = new ColorLabel();
        stringLabel.initialize("400 800 " + getString());

        background = new Rectangle();
        background.initialize("0 0 4200 1200");
        jfig.objects.FigAttribs attr = background.getAttributes();
        attr.currentLayer = 50;
        attr.lineColor = null;
        attr.fillColor = java.awt.Color.white;
        attr.fillStyle = FigAttribs.SOLID_FILL;
        background.setAttributes(attr);

        Polyline border = new Polyline();
        border.initialize("5 0 0 4200 0 4200 1200 0 1200 0 0");

        symbol.addMember(ilabel);
        symbol.addMember(portsymbola);
        symbol.addMember(portsymbolclk);
        symbol.addMember(portsymbolen);
        symbol.addMember(portsymbolres);
        symbol.addMember(portlabelA);
        symbol.addMember(portlabelclk);
        symbol.addMember(portlabelen);
        symbol.addMember(portlabelres);
        symbol.addMember(stringLabel);
        symbol.addMember(background);
        symbol.addMember(border);
        symbol.addMember(bbr);
    }
}
