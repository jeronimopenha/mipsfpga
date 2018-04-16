/* RegBank.java - hades.models.rtl.RegBank
 * 
 * standard two-read one-write port register bank, with write-enable
 * and clock input for writing.
 *
 * 23-10-2014 - MK.Computer.Organization.and.Design.4th.Edition.Oct.2011, P.360
 *
 * (C) T.T. Almeida, thales.almeida@ufv.edition_5.br
 */
package ufv_mipsfpga.edition_5.fig421;

import hades.models.StdLogic1164;
import hades.models.StdLogicVector;
import hades.signals.SignalStdLogic1164;

public class RegBank extends ufv_mipsfpga.edition_5.RegBank {

    /**
     * RegBank constructor
     */
    public RegBank() {
        super();
    }

    @Override
    public void evaluate(Object arg) {

        double time = simulator.getSimTime() + t_access;

        StdLogicVector vector_AX = port_AX.getVectorOrUUU();
        StdLogicVector vector_AY = port_AY.getVectorOrUUU();
        StdLogicVector vector_AZ = port_AZ.getVectorOrUUU();
        StdLogicVector vector_ADebug = port_ADebug.getVectorOrUUU();

        StdLogicVector vector_DX = null;
        StdLogicVector vector_DY = null;
        StdLogicVector vector_DDebug = null;
        StdLogicVector vector_DZ = port_DZ.getVectorOrUUU();

        StdLogic1164 value_nWE = port_nWE.getValueOrU();
        StdLogic1164 value_CLK = port_CLK.getValueOrU();

        if (!value_nWE.is_01()) {
            message("-W- " + toString()
                    + "nWE undefined: data loss would occur! Ignoring...");
        } else if (!value_CLK.is_01()) {
            message("-W- " + toString()
                    + "CLK undefined: data loss would occur! Ignoring...");
        } else if (vector_AZ.has_UXZ()) {
            message("-W- " + toString()
                    + "AZ address undefined: data loss would occur! Ignoring...");
        } else {
            SignalStdLogic1164 clk = (SignalStdLogic1164) port_CLK.getSignal();
            if (value_nWE.is_1() && clk != null && clk.hasRisingEdge()) {
                int addr_z = (int) vector_AZ.getValue();
                long old_z = getDataAt(addr_z);
                long data_z = vector_DZ.getValue();

                setDataAt(addr_z, data_z);
                notifyWriteListeners(addr_z, old_z, data_z);
            }
        }

        //
        // read two values: DX = regbank[AX], DY = regbank[AY]
        //
        if (vector_AX.has_UXZ()) {
            vector_DX = vector_UUU.copy();
        } else {
            int addr_x = (int) vector_AX.getValue();
            long data_x = getDataAt(addr_x);
            vector_DX = new StdLogicVector(n_bits, data_x);
            notifyReadListeners(addr_x, data_x);

            schedule(port_DX, vector_DX, time + t_access);
        }

        if (vector_AY.has_UXZ()) {
            vector_DY = vector_UUU.copy();
        } else {
            int addr_y = (int) vector_AY.getValue();
            long data_y = getDataAt(addr_y);
            vector_DY = new StdLogicVector(n_bits, data_y);
            notifyReadListeners(addr_y, data_y);

            schedule(port_DY, vector_DY, time + t_access);
        }

        if (vector_ADebug.has_UXZ()) {
            vector_DDebug = vector_UUU.copy();
        } else {
            int addr_y = (int) vector_ADebug.getValue();
            long data_y = getDataAt(addr_y);
            vector_DDebug = new StdLogicVector(n_bits, data_y);
            notifyReadListeners(addr_y, data_y);

            schedule(port_DDebug, vector_DDebug, time + t_access);
        }
    }
}
