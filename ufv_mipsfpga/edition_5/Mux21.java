package ufv_mipsfpga.edition_5;

import hades.symbols.BboxRectangle;
import hades.symbols.BusPortSymbol;
import hades.symbols.FatLabel;
import hades.symbols.PortLabel;
import hades.symbols.PortSymbol;
import hades.symbols.Rectangle;
import hades.symbols.Symbol;

public class Mux21
        extends hades.models.rtlib.muxes.Mux21 {

    public Mux21() {
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

        FatLabel fatlabel0 = new FatLabel();
        fatlabel0.initialize("600 800 2 M");
        symbol.addMember(fatlabel0);

        FatLabel fatlabel1 = new FatLabel();
        fatlabel1.initialize("600 1400 2 u");
        symbol.addMember(fatlabel1);

        FatLabel fatlabel2 = new FatLabel();
        fatlabel2.initialize("600 2000 2 x");
        symbol.addMember(fatlabel2);

        BusPortSymbol busportsymbol0 = new BusPortSymbol();
        busportsymbol0.initialize("1200 1200 Y");
        symbol.addMember(busportsymbol0);

        BusPortSymbol busportsymbol1 = new BusPortSymbol();
        busportsymbol1.initialize("0 1800 A1");
        symbol.addMember(busportsymbol1);

        BusPortSymbol busportsymbol2 = new BusPortSymbol();
        busportsymbol2.initialize("0 600 A0");
        symbol.addMember(busportsymbol2);

        PortSymbol portsymbol = new PortSymbol();
        portsymbol.initialize("600 0 S");
        symbol.addMember(portsymbol);

        PortLabel portlabel0 = new PortLabel();
        portlabel0.initialize("150 1950 A1");
        symbol.addMember(portlabel0);

        PortLabel portlabel1 = new PortLabel();
        portlabel1.initialize("150 750 A0");
        symbol.addMember(portlabel1);
    }
}
