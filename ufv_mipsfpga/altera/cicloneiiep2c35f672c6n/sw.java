package ufv_mipsfpga.altera.cicloneiiep2c35f672c6n;

import hades.symbols.BboxRectangle;
import hades.symbols.BusPortSymbol;
import hades.symbols.Circle;
import hades.symbols.ColoredValueLabel;
import hades.symbols.Label;
import hades.symbols.PersistentInstanceLabel;
import hades.symbols.Polyline;
import hades.symbols.Symbol;
import java.util.Enumeration;

public class sw extends hades.models.rtlib.io.IpinVector {

    public sw() {
        super();
    }

    @Override
    public boolean needsDynamicSymbol() {
        return true;
    }

    @Override
    public void constructDynamicSymbol() {
        //symbol.setInstanceLabel("sw0-7");

        symbol = new Symbol();
        symbol.setParent(this);

        BboxRectangle bbr = new BboxRectangle();
        bbr.initialize("-6000 -450 -450 450");
        symbol.addMember(bbr);

        Label label = new Label();
        label.initialize("-6100 150 3 sw0-7");
        symbol.addMember(label);

        BusPortSymbol busportsymbol = new BusPortSymbol();
        busportsymbol.initialize("0 0 Y");
        symbol.addMember(busportsymbol);

        Polyline pol = new Polyline();
        pol.initialize("6 0 0 -450 -450 -6000 -450 -6000 450 -450 450 0 0");
        symbol.addMember(pol);

        ColoredValueLabel colorlabel = new ColoredValueLabel();
        colorlabel.initialize("-1200 300 3 UUUU");
        symbol.addMember(colorlabel);

        Circle circle = new Circle();
        circle.initialize("-600 0 300 300");
        symbol.addMember(circle);
    }
}
