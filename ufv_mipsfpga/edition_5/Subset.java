/* Subset.java -  class hades.models.rtlib.io.Subset
 *
 * 24.08.98 - modified position of subsetLabel
 * 07.07.98 - first try, copied from models.rtlib.arith.Incr
 *
 * (C) F.N.Hendrich, hendrich@informatik.uni-hamburg.de
 */
package ufv_mipsfpga.edition_5;

import hades.symbols.BboxRectangle;
import hades.symbols.BusPortSymbol;
import hades.symbols.ClassLabel;
import hades.symbols.InstanceLabel;
import hades.symbols.Polyline;
import hades.symbols.Symbol;



/**
 * Subset - extract a subset of bits from a SignalStdLogicVector
 */
public class Subset extends hades.models.rtlib.io.Subset {

    public Subset() {
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
        bbr.initialize("-200 300 800 900");
        symbol.addMember(bbr);

        Polyline pol = new Polyline();
        pol.initialize("5 0 400 0 800 600 800 600 400 0 400");
        symbol.addMember(pol);

        ClassLabel classlabel = new ClassLabel();
        classlabel.initialize("0 700 1 S");
        symbol.addMember(classlabel);

        InstanceLabel instlabel = new InstanceLabel();
        instlabel.initialize("0 700 3 subset");
        symbol.addMember(instlabel);

        BusPortSymbol busportsymbol0 = new BusPortSymbol();
        busportsymbol0.initialize("0 600 A");
        symbol.addMember(busportsymbol0);

        BusPortSymbol busportsymbol1 = new BusPortSymbol();
        busportsymbol1.initialize("600 600 Y");
        symbol.addMember(busportsymbol1);
    }

}
