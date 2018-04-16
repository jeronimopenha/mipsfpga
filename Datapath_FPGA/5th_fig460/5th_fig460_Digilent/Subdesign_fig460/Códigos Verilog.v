module comparador (ValueRS, ValueRT, res);
  input [31:0] ValueRS, ValueRT;
  output res;
  
  assign res = (ValueRS==ValueRT)?1'b1:1'b0;
endmodule 

module hazard(stall, IDEXMemRead, IDEXrt, Rt, Rs);
  input IDEXMemRead;
  input [4:0] IDEXrt, Rt, Rs;
  output stall;

  assign stall = (IDEXMemRead && ((IDEXrt==Rt) || (IDEXrt==Rs)));
endmodule 

module UnidadeControle(Opcode, RSRTEqual, Controls);
  parameter LW = 6'b100011, SW = 6'b101011, BEQ = 6'b000100, R = 6'b0;
  input [5:0] Opcode;                     
  input RSRTEqual;
  output [9:0] Controls;
  
  //Fazendo codificação do opcode e gerando os sinais de controle
  //IF.Flush,	Branch,	RegDst,	Aluop1,	Aluop0,	AluSrc,	MemWrite,	MemRead,	MemToReg,	RegWrite
  assign Controls = (Opcode==LW)? 10'b0000010111: ((Opcode==SW)? 10'b0000011000: ((Opcode==R)? 10'b0011000001: ((Opcode==BEQ)? ((RSRTEqual)? 10'b1100100000: 10'b0100100000): 10'b0000000000)));
  
endmodule

module UnidadeControle2(opcode, controls, RSRTEqual);
  input [5:0] opcode;
  output [9:0] controls;
  reg [9:0] controls; 
  input RSRTEqual;

  //Fazendo codificação do opcode e gerando os sinais de controle
  //IF.Flush,	Branch,	RegDst,	Aluop1,	Aluop0,	AluSrc,	MemWrite,	MemRead,	MemToReg,	RegWrite
  always @(opcode, RSRTEqual) begin
    case (opcode)
      6'b100011: controls = 10'b0000010111; //LW - 35
  	  6'b101011: controls = 10'b0000011000; //SW - 43
  	  6'b000000: controls = 10'b0011000001; //R
	  6'b000100: controls = (RSRTEqual)? 10'b1100100000: 10'b0100100000; //BEQ - 4
  	  default: $display ($time, "Erro: Unidade de Controle");
    endcase        
  end  
endmodule
