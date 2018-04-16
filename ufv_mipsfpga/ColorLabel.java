/* ColoredValueLabel.java --  hades.symbols.ColoredValueLabel
 *
 * 06.12.98 - use attribs.getClone() in copy(), faster setColor()
 * 22.06.98 - moved from hades.models.ruge to hades.symbols
 * 03.04.98 - first version, copied from InstanceLabel
 *
 * (C) 1997-1998 by F.N.Hendrich, hendrich@informatik.uni-hamburg.de
 */
package ufv_mipsfpga;

import java.awt.Color;
import java.awt.Point;
import jfig.objects.FigAttribs;
import jfig.objects.FigObject;


/**
 * ColorLabel: a colored and animated label on a SimObject symbol
 */
public class ColorLabel
        extends hades.symbols.Label {

    private Color color = new Color(64, 164, 164);

    /**
     * construct an empty ColorLabel.
     */
    public ColorLabel() {
        super(); // initialize the text
    }

    @Override
    protected void build_attribs() {
        FigAttribs attribs = getAttributes().getClone();
        attribs.lineColor = attribs.fillColor = color;
        attribs.fillStyle = attribs.NO_FILL;
        attribs.currentLayer = 10;
        attribs.fontSize = 17;
        attribs.fig_font = FigAttribs.FONT_HELVETICA;
        setAttributes(attribs);
    }

    @Override
    public void setColor(Color _color) {
        color = _color;
        attribs.lineColor = attribs.fillColor = color;
    }

    @Override
    public FigObject copy() {
        ColorLabel carbon = new ColorLabel();
        carbon.setAttributes(this.getAttributes().getClone());
        carbon.setText(this.getText());
        Point pp[] = this.getPoints();
        carbon.move(pp[0].x, pp[0].y);
        return carbon;
    }

    /**
     * toString() - the usual info method
     */
    @Override
    public String toString() {
        return "ColorLabel[" + super.toString() + "]";
    }
}

/* end ColorLabel.java */
