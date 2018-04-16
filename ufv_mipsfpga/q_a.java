package ufv_mipsfpga;

import hades.manager.DesignManager;
import hades.symbols.BboxRectangle;
import hades.symbols.ClassLabel;
import hades.symbols.InstanceLabel;
import hades.symbols.Rectangle;
import hades.symbols.Symbol;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.util.Vector;

/**
 * Description of the Class
 *
 * @author Ricardo Ferreira / Geraldo Fontes
 * @created 2015
 */
public class q_a extends hades.models.rtlib.GenericRtlibObject {
    
    protected String filename;
    
    public q_a() {
        super();
        filename = "null";
    }
    
    public void selectFile() {
        DesignManager DM = DesignManager.getDesignManager();
        String name = DM.selectFileOrURLName(
                "Select the question and answer file:",
                filename,
                new String[]{".txt"},
                java.awt.FileDialog.LOAD);
        
        if (name == null) { // Cancel'ed
            return;
        } else {
            filename = name;
        }
    }
    
    @Override
    public void mousePressed(MouseEvent me) {
        // inserir sua classe
        try {
            
            Vector<question> perguntas = new Vector<question>();
            questionReader q = new questionReader();
            q.reader(filename, perguntas);
            Vector<questionFrame> frames = new Vector<questionFrame>();
            question qt = new question();
            
            for (int i = 0; i < perguntas.size(); i++) {
                qt = perguntas.get(i);
                frames.add(new questionFrame(qt, frames));
                qt = new question();
            }
            
            frames.get(0).setVisible(true);
            
        } catch (FileNotFoundException ex) {
        }
    }
    
    @Override
    public void configure() {
        selectFile();
    }
    
    @Override
    public boolean needsDynamicSymbol() {
        return true;
    }
    
    @Override
    public void constructDynamicSymbol() {
        symbol = new Symbol();
        symbol.setParent(this);
        
        InstanceLabel instlabel = new InstanceLabel();
        instlabel.initialize("1750 1150 3 i0");
        symbol.addMember(instlabel);
        
        BboxRectangle bbr = new BboxRectangle();
        bbr.initialize("0 0 3600 3600");
        symbol.addMember(bbr);
        
        ClassLabel classlabel = new ClassLabel();
        classlabel.initialize("1800 1900 2  Questions");
        symbol.addMember(classlabel);
        
        Rectangle rec = new Rectangle();
        rec.initialize("0 0 3600 3600");
        symbol.addMember(rec);
    }
}
