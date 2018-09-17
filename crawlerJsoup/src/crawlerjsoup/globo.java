/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlerjsoup;

import static crawlerjsoup.crawlerjsoup.getPrint;
import static crawlerjsoup.crawlerjsoup.getVerifica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javax.swing.JOptionPane;

/**
 *
 * @author Samsung
 */
public class globo 
{
     public void busca() throws IOException {
        String[] sargs = {"http://www.globo.com", "http://www.uol.com.br", "https://g1.globo.com/economia/noticia/2018/08/09/petrobras-recebe-mais-de-r-1-bi-recuperado-pela-operacao-lava-jato.ghtml", "https://oglobo.globo.com/brasil/ibope-sem-lula-bolsonaro-aparece-com-20-marina-com-12-ciro-com-9-22995661"};
//        Validate.isTrue(sargs.length == 1, "http://www.globo.com");
//        Validate.isTrue(args.length == 1, "globo.com");
        String url = sargs[0];
        getPrint("Fetching %s...", url);///Greek question mark;

        Document doc = Jsoup.connect(url).get();
        Elements titles = doc.select("a[class]");

        List<String> valorProcurado = new ArrayList<String>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("entradas.txt"), Charset.forName("ISO-8859-1")));
        String line;
        while ((line = reader.readLine()) != null) {
            valorProcurado.add(line.toLowerCase());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-hh-mm");
        System.out.println(sdf.format(new Date()));
        File arquivo = new File("noticias/globo.com/" + sdf.format(new Date()) + ".txt");

        FileWriter fw = new FileWriter(arquivo);
        BufferedWriter bw = new BufferedWriter(fw);

        ///print("\nTitles: (%d)", titles.size());
        for (Element tituloReportagem : titles) {

            if (getVerifica(tituloReportagem.text().toLowerCase(), valorProcurado)) //verifica se o titulo da reportagem contem algum dos termos procurado
            {
                if(!"samsung rouba cena com novo celular poderoso ".contains(tituloReportagem.text().toLowerCase())) ///Trata um erro de um lixo que sempre aparecia, como sendo reportagem
                {
                    bw.write("\nTitulo: "+tituloReportagem.text().toLowerCase()); ///escrita arquivo
                    bw.newLine();
                    String absHref = tituloReportagem.attr("abs:href"); //funcao que me retorna a URL completa EX: https://g1.globo.com/economia/noticia/2018/08/09/petrobras-recebe-mais-de-r-1-bi-recuperado-pela-operacao-lava-jato.ghtml
                    bw.write("\nLink: "+absHref.toLowerCase()); ///escrita arquivo
                    bw.newLine();
                    Document pagEncontrada = Jsoup.connect(absHref).get();
                    Elements pages;
                    Elements author;

                    if(absHref.contains("oglobo")) //Alguns links redirecionam para o jornal O Globo
                    {
                        pages = pagEncontrada.select(".corpo p"); //Conteudo jornal O Globo
                        author = pagEncontrada.select("span.autor"); //Data jornal O Globo
                    }
                    else 
                    {
                        pages = pagEncontrada.select("article"); ///Conteudo globo.com
                        author = pagEncontrada.select("p.content-publication-data__from"); ///Data globo.com
                    }

                    bw.write("\nAutor: "+author.text()); ///escrita arquivo
                    bw.newLine();
                    Elements date = pagEncontrada.select("time");
                    bw.write("\nData: "+date.text()); ///escrita arquivo
                    bw.newLine();

                    ///print("(%s)", link.text());
                    bw.write("\nConteudo: "+pages.text().replaceAll("\\p{Punct}", "").toLowerCase()+"\n");
                    bw.newLine();
                    bw.newLine();
                }
            }
        }

        bw.close();
        fw.close();
        //JFrame frame = new JFrame(url);
        //JOptionPane.showMessageDialog(frame, sdf.format(new Date()));
    }
    
}
