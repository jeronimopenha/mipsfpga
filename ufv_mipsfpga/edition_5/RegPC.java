package ufv_mipsfpga.edition_5;

import hades.models.ruge.ColoredValueLabel;
import hades.symbols.BboxRectangle;
import hades.symbols.BusPortSymbol;
import hades.symbols.FatLabel;
import hades.symbols.InstanceLabel;
import hades.symbols.PortSymbol;
import hades.symbols.Rectangle;
import hades.symbols.Symbol;

public class RegPC extends hades.models.rtlib.register.RegR {

    public RegPC() {
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

        BboxRectangle bbr = new BboxRectangle();
        bbr.initialize("0 0 1200 2400");
        symbol.addMember(bbr);

        Rectangle rec = new Rectangle();
        rec.initialize("0 0 1200 2400");
        symbol.addMember(rec);

        InstanceLabel instlabel = new InstanceLabel();
        instlabel.initialize("550 550 1 regr");
        symbol.addMember(instlabel);

        BusPortSymbol busportlabel0 = new BusPortSymbol();
        busportlabel0.initialize("1200 1200 Q");
        symbol.addMember(busportlabel0);

        BusPortSymbol busportlabel1 = new BusPortSymbol();
        busportlabel1.initialize("0  1200 D");
        symbol.addMember(busportlabel1);

        PortSymbol portsymbol0 = new PortSymbol();
        portsymbol0.initialize("0 1800 CLK");
        symbol.addMember(portsymbol0);

        PortSymbol portsymbol1 = new PortSymbol();
        portsymbol1.initialize("600 2400 NR");
        symbol.addMember(portsymbol1);

        ColoredValueLabel colorlabel = new ColoredValueLabel();
        colorlabel.initialize("1800 2200 2 42");
        symbol.addMember(colorlabel);

        FatLabel fatlabel0 = new FatLabel();
        fatlabel0.initialize("600 800 2 P");
        symbol.addMember(fatlabel0);

        FatLabel fatlabel1 = new FatLabel();
        fatlabel1.initialize("600 1400 2 C");
        symbol.addMember(fatlabel1);

    }
}
