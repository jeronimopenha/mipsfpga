package ufv_mipsfpga;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;

public class questionReader {

    public questionReader() {
    }

    public void reader(String fileName, Vector<question> p) throws FileNotFoundException {// recebe o nome do arquivo e preeenche um array de perguntas

        //-----------------------------------------parte de leitura do arquivo------------------------------------------------------------
        String aux = null;
        Scanner scan;
        question q = new question();
        Vector<String> correctAnswer = new Vector<String>();
        int i = 0; // numero da pergunta

        scan = new Scanner(new FileReader(fileName));
        while (scan.hasNext()) {
            //responsavel por ler a pergunta inicio---------------------------
            scan.nextLine();
            // read question
            aux = scan.nextLine();
            if (aux.contains("<q>")) {
                aux = aux.substring(3, aux.length());//retira a tag <p>
                while (!aux.contains("</q>")) {
                    aux = aux + "\n" + scan.nextLine();
                }
                aux = aux.substring(0, aux.length() - 4);//retira a tag </p>
            }
            q.setQuestion(aux);

            //le a primeira alternativa
            aux = scan.nextLine();
            if (aux.contains("<a1>")) {
                aux = aux.substring(4, aux.length());//retira a tag <p>
                while (!aux.contains("</a1>")) {
                    aux = aux + "\n" + scan.nextLine();
                }
                correctAnswer.add(aux.substring(aux.length() - 6, aux.length() - 5));// vector with correct asnwer-
                aux = aux.substring(0, aux.length() - 7);
            }
            q.setAnswer_1(aux);
            aux = aux.substring(0, aux.length() - 1);

            //le a primeira alternativa
            aux = scan.nextLine();
            if (aux.contains("<a2>")) {
                aux = aux.substring(4, aux.length());//retira a tag <p>
                while (!aux.contains("</a2>")) {
                    aux = aux + "\n" + scan.nextLine();
                }
                correctAnswer.add(aux.substring(aux.length() - 6, aux.length() - 5));
                aux = aux.substring(0, aux.length() - 7);
            }
            q.setAnswer_2(aux);
            //le a primeira alternativa
            aux = scan.nextLine();
            if (aux.contains("<a3>")) {
                aux = aux.substring(4, aux.length());//retira a tag <p>
                while (!aux.contains("</a3>")) {
                    aux = aux + "\n" + scan.nextLine();
                }
                correctAnswer.add(aux.substring(aux.length() - 6, aux.length() - 5));
                aux = aux.substring(0, aux.length() - 7);
            }
            q.setAnswer_3(aux);

            //le a primeira alternativa
            aux = scan.nextLine();
            if (aux.contains("<a4>")) {
                aux = aux.substring(4, aux.length());//retira a tag <p>
                while (!aux.contains("</a4>")) {
                    aux = aux + "\n" + scan.nextLine();
                }
                correctAnswer.add(aux.substring(aux.length() - 6, aux.length() - 5));
                aux = aux.substring(0, aux.length() - 7);
            }
            q.setAnswer_4(aux);

            aux = scan.nextLine();
            if (aux.contains("<a5>")) {
                aux = aux.substring(4, aux.length());//retira a tag <p>
                while (!aux.contains("</a5>")) {
                    aux = aux + "\n" + scan.nextLine();
                }
                correctAnswer.add(aux.substring(aux.length() - 6, aux.length() - 5));
                aux = aux.substring(0, aux.length() - 7);
            }
            q.setAnswer_5(aux);

            q.setCorrectAnswer(correctAnswer);
            q.setQuestionNumber(i++);
            p.add(q);
            q = new question();
            correctAnswer = new Vector<String>();

//fim da parte de leitura do arquivo--------------------------------------------------------------------------------------------------
        }
    }
}
