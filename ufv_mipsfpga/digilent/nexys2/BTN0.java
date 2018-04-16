package ufv_mipsfpga.digilent.nexys2;

import hades.symbols.BboxRectangle;
import hades.symbols.ColoredCircle;
import hades.symbols.Label;
import hades.symbols.Polyline;
import hades.symbols.PortSymbol;
import hades.symbols.Symbol;

public class BTN0 extends hades.models.io.Ipin {

    public BTN0() {
        super();
    }

    @Override
    public boolean needsDynamicSymbol() {
        return true;
    }

    @Override
    public void constructDynamicSymbol() {
        symbol = new Symbol();
        symbol.setParent(this);

        symbol = new Symbol();
        symbol.setParent(this);

        BboxRectangle bbr = new BboxRectangle();
        bbr.initialize("-1200 -600 0 600");
        symbol.addMember(bbr);

        Label label = new Label();
        label.initialize("-1300 150 3 BTN0");
        symbol.addMember(label);

        PortSymbol portsymol = new PortSymbol();
        portsymol.initialize("0 0 Y");
        symbol.addMember(portsymol);

        Polyline pol = new Polyline();
        pol.initialize("6 0 0 -450 -450 -1200 -450 -1200 450 -450 450 0 0");
        symbol.addMember(pol);

        ColoredCircle colorcircle = new ColoredCircle();
        colorcircle.initialize("-750 0 300 300");
        symbol.addMember(colorcircle);

        symbol.setInstanceLabel("key0");
        initDisplay();
    }
}
