/* 
 * 24/05/2016
 * String Debug Component. - UFV MG Brasil
 * Jeronimo Costa Penha - jeronimopenha@gmail.com
 */
package ufv_mipsfpga.debug;

import hades.simulator.*;
import hades.signals.Signal;
import hades.models.*;
import hades.symbols.*;
import jfig.objects.FigAttribs;

public class IF_Debug extends hades.models.rtlib.GenericRtlibObject {

    protected Port port_A;

    protected String string;
    protected Rectangle background;
    protected StdLogicVector value_A;

    public IF_Debug() {
        super();
    }

    @Override
    protected void constructPorts() {
        port_A = new PortStdLogicVector(this, "A", Port.IN, null, 32);

        ports = new Port[1];
        ports[0] = port_A;

        string = "NOP";
    }

    @Override
    public String getToolTip(java.awt.Point position, long millis) {
        return getName() + "\n"
                + "\nInstruction = " + string + "\n\n"
                + getClass().getName();
    }

    public void setString(String s) {
        this.string = s;
        //stringLabel.setText( s );
        //getSymbol().painter.paint( getSymbol(), 100 );
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

        if (signal == null) {
            return;
        }

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
        bbr.initialize("0 -1200 3600 1200");

        InstanceLabel ilabel = new InstanceLabel();
        ilabel.initialize("3600 0 " + getName());

        BusPortSymbol busportsymbol = new BusPortSymbol();
        busportsymbol.initialize("1800 -1200 A");

        PortLabel portlabel = new PortLabel();
        portlabel.initialize("1800 -800 A");

        background = new Rectangle();
        background.initialize("0 -1200 3400 1200");
        jfig.objects.FigAttribs attr = background.getAttributes();
        attr.currentLayer = 50;
        attr.lineColor = null;
        attr.fillColor = java.awt.Color.white;
        attr.fillStyle = FigAttribs.SOLID_FILL;
        background.setAttributes(attr);

        Polyline border = new Polyline();
        border.initialize("7 0 -1200 1800 -400 3600 -1200 0 -1200 0 1200 3600 1200 3600 -1200");

        Polyline selo = new Polyline();
        selo.initialize("5 200 -400 1000 -400 1000 400 200 400 200 -400");

        Polyline linha1 = new Polyline();
        linha1.initialize("2 2400 -200 3400 -200");

        Polyline linha2 = new Polyline();
        linha2.initialize("2 2400 0 3400 0");

        Polyline linha3 = new Polyline();
        linha3.initialize("2 2400 200 3400 200");

        Polyline linha4 = new Polyline();
        linha4.initialize("2 2400 400 3400 400 ");

        symbol.addMember(ilabel);
        symbol.addMember(busportsymbol);
        symbol.addMember(portlabel);
        symbol.addMember(background);
        symbol.addMember(border);
        symbol.addMember(selo);
        symbol.addMember(linha1);
        symbol.addMember(linha2);
        symbol.addMember(linha3);
        symbol.addMember(linha4);
        symbol.addMember(bbr);
    }
}
