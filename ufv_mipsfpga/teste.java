/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ufv_mipsfpga;

import java.io.FileNotFoundException;
import java.util.Vector;

/**
 *
 * @author geraldo
 */
public class teste {

    public static void main(String args[]) throws FileNotFoundException {

        Vector<question> perguntas = new Vector<question>();
        String filePath = "/home/geraldo/Área de Trabalho/formulariojava/perguntas.txt";
        questionReader q = new questionReader();
        q.reader(filePath, perguntas);
        Vector<questionFrame> frames = new Vector<questionFrame>();
        question qt = new question();

        //cria todos frames de pergunta
        for (int i = 0; i < perguntas.size(); i++) {
            qt = perguntas.get(i);
            frames.add(new questionFrame(qt, frames));
            qt = new question();
        }

        for (int i = 0; i < perguntas.size(); i++) {
            perguntas.get(i).print();
        }

        frames.get(0).setVisible(true);

    }

}
