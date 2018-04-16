package ufv_mipsfpga.edition_5;

import hades.symbols.BboxRectangle;
import hades.symbols.BusPortSymbol;
import hades.symbols.ClassLabel;
import hades.symbols.FatLabel;
import hades.symbols.InstanceLabel;
import hades.symbols.Polyline;
import hades.symbols.PortLabel;
import hades.symbols.Symbol;

public class Add
        extends hades.models.rtlib.arith.Add {

    public Add() {
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
        bbr.initialize("0 0 2400 4800 ");
        symbol.addMember(bbr);

        Polyline pol = new Polyline();
        pol.initialize("8 0 0 0 2100 600 2400 0 2700 0 4800 2400 3600 2400 1200 0 0");
        symbol.addMember(pol);

        ClassLabel classLabel = new ClassLabel();
        classLabel.initialize("1600 4700 1 Add");
        symbol.addMember(classLabel);

        InstanceLabel instancelabel = new InstanceLabel();
        instancelabel.initialize("400 0 1 add");
        symbol.addMember(instancelabel);

        FatLabel fatlabel = new FatLabel();
        fatlabel.initialize("1450 2800 2 Add ");
        symbol.addMember(fatlabel);

        BusPortSymbol busportsymbol0 = new BusPortSymbol();
        busportsymbol0.initialize("2400 2400 SUM");
        symbol.addMember(busportsymbol0);

        BusPortSymbol busportsymbol1 = new BusPortSymbol();
        busportsymbol1.initialize("0    1200 A");
        symbol.addMember(busportsymbol1);

        BusPortSymbol busportsymbol2 = new BusPortSymbol();
        busportsymbol2.initialize("0    3600 B");
        symbol.addMember(busportsymbol2);

        PortLabel portlabel0 = new PortLabel();
        portlabel0.initialize("2500 2300 SUM");
        symbol.addMember(portlabel0);

        PortLabel portlabel1 = new PortLabel();
        portlabel1.initialize("400  1300 A");
        symbol.addMember(portlabel1);

        PortLabel portlabel2 = new PortLabel();
        portlabel2.initialize("400  3700 B");
        symbol.addMember(portlabel2);
    }
}
