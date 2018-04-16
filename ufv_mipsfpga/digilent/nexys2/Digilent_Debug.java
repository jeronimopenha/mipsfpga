package ufv_mipsfpga.digilent.nexys2;

import hades.models.PortStdLogic1164;
import hades.models.PortStdLogicVector;
import hades.models.StdLogic1164;
import hades.models.StdLogicVector;
import hades.signals.Signal;
import hades.signals.SignalStdLogic1164;
import hades.simulator.Port;
import hades.simulator.SimEvent;
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

public class Digilent_Debug extends hades.models.rtlib.GenericRtlibObject {

    protected Port port_KEY0, port_KEY1, port_KEY2, port_KEY3;  //keys de seleção
    protected Port port_VIN;                                    //switches de valor
    protected Port port_CLKP;
    protected Port port_PC;
    protected Port port_RA, port_RD;
    protected Port port_MA, port_MD;

    protected String string;

    protected Label stringLabel;
    protected Rectangle background;

    protected double t_delay;

    public Digilent_Debug() {
        super();
        string = "00000000_H";
        t_delay = 10.0E-8;
    }

    @Override
    protected void constructPorts() {

        //keys de seleção
        port_KEY0 = new PortStdLogic1164(this, "KEY0", Port.IN, null);
        port_KEY1 = new PortStdLogic1164(this, "KEY1", Port.IN, null);
        port_KEY2 = new PortStdLogic1164(this, "KEY2", Port.IN, null);
        port_KEY3 = new PortStdLogic1164(this, "KEY3", Port.IN, null);
        //switches de valor
        port_VIN = new PortStdLogicVector(this, "VIN", Port.IN, null, 8);
        //entradas de acesso
        port_CLKP = new PortStdLogicVector(this, "CLKP", Port.OUT, null, 8);
        port_PC = new PortStdLogicVector(this, "PC", Port.IN, null, 32);
        port_RA = new PortStdLogicVector(this, "RA", Port.OUT, null, 5);
        port_RD = new PortStdLogicVector(this, "RD", Port.IN, null, 32);
        port_MA = new PortStdLogicVector(this, "MA", Port.OUT, null, 12);
        port_MD = new PortStdLogicVector(this, "MD", Port.IN, null, 32);

        ports = new Port[11];
        ports[0] = port_KEY0;
        ports[1] = port_KEY1;
        ports[2] = port_KEY2;
        ports[3] = port_KEY3;
        ports[4] = port_VIN;
        ports[5] = port_CLKP;
        ports[6] = port_PC;
        ports[7] = port_RA;
        ports[8] = port_RD;
        ports[9] = port_MA;
        ports[10] = port_MD;
    }

    //OVERRIDE
    @Override
    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
        this.symbol.setInstanceLabel("ALTERA_CLONE_II");
    }

    public void setString(String s) {
        this.string = s;
        stringLabel.setText(s);
        getSymbol().painter.paint(getSymbol(), 100);
    }

    public String getString() {
        return this.string;
    }

    /*
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
     */
    private String bin_to_hex(String im, int l) {//converte binario para decimal
        int soma = 0, cont = 0;
        for (int i = l; i > 0; i--) {
            if (im.substring(i - 1, i).equals("1")) {
                soma += (int) Math.pow(2, cont);
            }
            cont++;
        }
        switch (soma) {
            case 10:
                return "A";
            case 11:
                return "B";
            case 12:
                return "C";
            case 13:
                return "D";
            case 14:
                return "E";
            case 15:
                return "F";
            default:
                return Integer.toString(soma);
        }

    }

    @Override
    public void evaluate(Object arg) {

        Signal signal_KEY0 = port_KEY0.getSignal();
        Signal signal_KEY1 = port_KEY1.getSignal();
        Signal signal_KEY2 = port_KEY2.getSignal();
        Signal signal_KEY3 = port_KEY3.getSignal();
        Signal signal_VIN = port_VIN.getSignal();
        Signal signal_RD = port_RD.getSignal();
        Signal signal_PC = port_PC.getSignal();
        Signal signal_MD = port_MD.getSignal();

        if (signal_KEY0 == null) {
            return;
        }
        if (signal_KEY1 == null) {
            return;
        }
        if (signal_KEY2 == null) {
            return;
        }
        if (signal_KEY3 == null) {
            return;
        }
        if (signal_VIN == null) {
            return;
        }
        if (signal_RD == null) {
            return;
        }
        if (signal_MD == null) {
            return;
        }

        StdLogic1164 KEY0 = (StdLogic1164) signal_KEY0.getValue();
        StdLogic1164 KEY1 = (StdLogic1164) signal_KEY1.getValue();
        StdLogic1164 KEY2 = (StdLogic1164) signal_KEY2.getValue();
        StdLogic1164 KEY3 = (StdLogic1164) signal_KEY3.getValue();

        StdLogicVector value_VIN = (StdLogicVector) signal_VIN.getValue();
        StdLogicVector value_RD = (StdLogicVector) signal_RD.getValue();
        StdLogicVector value_MD = (StdLogicVector) signal_MD.getValue();

        int key0_val = (int) KEY0.getValue();
        int key1_val = (int) KEY1.getValue();
        int key2_val = (int) KEY2.getValue();
        int key3_val = (int) KEY3.getValue();

        int i, tamanho;
        String temp, str;

        //controle de solicitações e envio de dados para os dispositivos do processador
        //sempre em borda de subida
        Signal signalOut;
        double time;

        SignalStdLogic1164 signal_temp;

        //para key0 // Programação do clock
        signalOut = port_CLKP.getSignal();
        if (signalOut == null) {
            return;
        }
        time = simulator.getSimTime() + t_delay;
        simulator.scheduleEvent(SimEvent.createNewSimEvent(signalOut, time, value_VIN, port_CLKP));

        //para key2 // solicitação de dados para o banco de registradores
        signal_temp = (SignalStdLogic1164) port_KEY2.getSignal();
        if (signal_temp.hasRisingEdge()) {
            signalOut = port_RA.getSignal();
            if (signalOut == null) {
                return;
            }
            time = simulator.getSimTime() + t_delay;
            simulator.scheduleEvent(SimEvent.createNewSimEvent(signalOut, time, value_VIN, port_RA));

        }

        //para key3 // solicitação de dados para a memória
        signal_temp = (SignalStdLogic1164) port_KEY3.getSignal();
        if (signal_temp.hasRisingEdge()) {
            signalOut = port_MA.getSignal();
            if (signalOut == null) {
                return;
            }
            time = simulator.getSimTime() + t_delay;
            simulator.scheduleEvent(SimEvent.createNewSimEvent(signalOut, time, value_VIN, port_MA));
        }
    //****    fim do controle de solicitações

        //controle de exibição do display
        if (key1_val == 3 && key0_val == 2) { // exibição de pc
            if (signal_PC == null) {
                return;
            }

            StdLogicVector value_PC = (StdLogicVector) signal_PC.getValue();

            //encontrar o inicio do binario
            if (value_PC.has_UXZ()) {
                temp = ":00000000000000000000000000000000";
            } else {
                temp = value_PC.toString();
            }

            tamanho = temp.length();
            for (i = 0; i < tamanho; i++) {  //busca o índice do primeiro bit na string
                if (temp.substring(i, i + 1).equals(":")) {
                    break;
                }
            }
            i++;
            str = bin_to_hex(temp.substring(i, i + 4), 4) + bin_to_hex(temp.substring(i + 4, i + 8), 4) + bin_to_hex(temp.substring(i + 8, i + 12), 4)
                    + bin_to_hex(temp.substring(i + 12, i + 16), 4) + bin_to_hex(temp.substring(i + 16, i + 20), 4) + bin_to_hex(temp.substring(i + 20, i + 24), 4)
                    + bin_to_hex(temp.substring(i + 24, i + 28), 4) + bin_to_hex(temp.substring(i + 28, i + 32), 4) + "_H";
        } else if (key2_val == 3) { // exibição do dado dsolicitado ao banco de registradores
            //encontrar o inicio do binario
            if (value_RD.has_UXZ()) {
                temp = ":00000000000000000000000000000000";
            } else {
                temp = value_RD.toString();
            }

            tamanho = temp.length();
            for (i = 0; i < tamanho; i++) {  //busca o índice do primeiro bit na string
                if (temp.substring(i, i + 1).equals(":")) {
                    break;
                }
            }
            i++;
            str = bin_to_hex(temp.substring(i, i + 4), 4) + bin_to_hex(temp.substring(i + 4, i + 8), 4) + bin_to_hex(temp.substring(i + 8, i + 12), 4)
                    + bin_to_hex(temp.substring(i + 12, i + 16), 4) + bin_to_hex(temp.substring(i + 16, i + 20), 4) + bin_to_hex(temp.substring(i + 20, i + 24), 4)
                    + bin_to_hex(temp.substring(i + 24, i + 28), 4) + bin_to_hex(temp.substring(i + 28, i + 32), 4) + "_H";

        } else if (key3_val == 3) { // exibição do dado dsolicitado ao módulo de memória RAM
            //encontrar o inicio do binario
            if (value_MD.has_UXZ()) {
                temp = ":00000000000000000000000000000000";
            } else {
                temp = value_MD.toString();
            }

            tamanho = temp.length();
            for (i = 0; i < tamanho; i++) {  //busca o índice do primeiro bit na string
                if (temp.substring(i, i + 1).equals(":")) {
                    break;
                }
            }
            i++;
            str = bin_to_hex(temp.substring(i, i + 4), 4) + bin_to_hex(temp.substring(i + 4, i + 8), 4) + bin_to_hex(temp.substring(i + 8, i + 12), 4)
                    + bin_to_hex(temp.substring(i + 12, i + 16), 4) + bin_to_hex(temp.substring(i + 16, i + 20), 4) + bin_to_hex(temp.substring(i + 20, i + 24), 4)
                    + bin_to_hex(temp.substring(i + 24, i + 28), 4) + bin_to_hex(temp.substring(i + 28, i + 32), 4) + "_H";
        } else { // caso nenhum seja selecionado ou durante a configuração do clock exibe o valor dos switches
            //encontrar o inicio do binario
            if (value_VIN.has_UXZ()) {
                temp = ":00000000";
            } else {
                temp = value_VIN.toString();
            }

            tamanho = temp.length();
            for (i = 0; i < tamanho; i++) {  //busca o índice do primeiro bit na string
                if (temp.substring(i, i + 1).equals(":")) {
                    break;
                }
            }
            i++;
            str = "000000" + bin_to_hex(temp.substring(i, i + 4), 4) + bin_to_hex(temp.substring(i + 4, i + 8), 4) + "_H";
        }
    //*****    fim do controle de exibição do display

        //atualização do display
        setString(str);
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
        PortSymbol portsymbolkey0 = new PortSymbol();
        portsymbolkey0.initialize("600 1200 KEY0");

        PortSymbol portsymbolkey1 = new PortSymbol();
        portsymbolkey1.initialize("1200 1200 KEY1");

        PortSymbol portsymbolkey2 = new PortSymbol();
        portsymbolkey2.initialize("1800 1200 KEY2");

        PortSymbol portsymbolkey3 = new PortSymbol();
        portsymbolkey3.initialize("2400 1200 KEY3");

        PortSymbol portsymbolvin = new PortSymbol();
        portsymbolvin.initialize("0 600 VIN");

        PortSymbol portsymbolclkp = new PortSymbol();
        portsymbolclkp.initialize("600 0 CLKP");

        PortSymbol portsymbolpc = new PortSymbol();
        portsymbolpc.initialize("1200 0 PC");

        PortSymbol portsymbolra = new PortSymbol();
        portsymbolra.initialize("1800 0 RA");

        PortSymbol portsymbolrd = new PortSymbol();
        portsymbolrd.initialize("2400 0 RD");

        PortSymbol portsymbolma = new PortSymbol();
        portsymbolma.initialize("3000 0 MA");

        PortSymbol portsymbolmd = new PortSymbol();
        portsymbolmd.initialize("3600 0 MD");

        //nomes dos ports no desenho
        PortLabel portlabelkey0 = new PortLabel();
        portlabelkey0.initialize("200 2400  KEY0");

        PortLabel portlabelkey1 = new PortLabel();
        portlabelkey1.initialize("800 1800 KEY1");

        PortLabel portlabelkey2 = new PortLabel();
        portlabelkey2.initialize("1400 2400 KEY2");

        PortLabel portlabelkey3 = new PortLabel();
        portlabelkey3.initialize("2000 1800 KEY3");

        PortLabel portlabelvin = new PortLabel();
        portlabelvin.initialize("-600 500 VIN");

        PortLabel portlabelclkp = new PortLabel();
        portlabelclkp.initialize("200 -600 CLKP");

        PortLabel portlabelpc = new PortLabel();
        portlabelpc.initialize("1000 -1200 PC");

        PortLabel portlabelra = new PortLabel();
        portlabelra.initialize("1600 -600 RA");

        PortLabel portlabelrd = new PortLabel();
        portlabelrd.initialize("2200 -1200 RD");

        PortLabel portlabelma = new PortLabel();
        portlabelma.initialize("2800 -600 MA");

        PortLabel portlabelmd = new PortLabel();
        portlabelmd.initialize("3200 -1200 MD");

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
        symbol.addMember(portsymbolkey0);
        symbol.addMember(portsymbolkey1);
        symbol.addMember(portsymbolkey2);
        symbol.addMember(portsymbolkey3);
        symbol.addMember(portsymbolvin);
        symbol.addMember(portsymbolclkp);
        symbol.addMember(portsymbolpc);
        symbol.addMember(portsymbolra);
        symbol.addMember(portsymbolrd);
        symbol.addMember(portsymbolma);
        symbol.addMember(portsymbolmd);
        symbol.addMember(portlabelkey0);
        symbol.addMember(portlabelkey1);
        symbol.addMember(portlabelkey2);
        symbol.addMember(portlabelkey3);
        symbol.addMember(portlabelvin);
        symbol.addMember(portlabelclkp);
        symbol.addMember(portlabelpc);
        symbol.addMember(portlabelra);
        symbol.addMember(portlabelrd);
        symbol.addMember(portlabelma);
        symbol.addMember(portlabelmd);
        symbol.addMember(stringLabel);
        symbol.addMember(background);
        symbol.addMember(border);
        symbol.addMember(bbr);
    }
}
