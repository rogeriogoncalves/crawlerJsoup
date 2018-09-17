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
import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Samsung
 */
public class yahoo 
{
    public void busca() throws IOException {
        String[] sargs = {"http://yahoo.com", "https://g1.globo.com/economia/noticia/2018/08/09/petrobras-recebe-mais-de-r-1-bi-recuperado-pela-operacao-lava-jato.ghtml", "https://oglobo.globo.com/brasil/ibope-sem-lula-bolsonaro-aparece-com-20-marina-com-12-ciro-com-9-22995661"};

        String url = sargs[0];
        getPrint("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();
        Elements titles = doc.select("a");
        ///System.out.println("Titulos: "+titles.text());

        List<String> valorProcurado = new ArrayList<String>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("entradas.txt"), Charset.forName("ISO-8859-1")));
        String line;
        while ((line = reader.readLine()) != null) {
            valorProcurado.add(line.toLowerCase());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-hh-mm");
        System.out.println(sdf.format(new Date()));
        File arquivo = new File("noticias/yahoo.com/" + sdf.format(new Date()) + ".txt");

        FileWriter fw = new FileWriter(arquivo);
        BufferedWriter bw = new BufferedWriter(fw);

        ///print("\nTitles: (%d)", titles.size());
        for (Element tituloReportagem : titles) {
            
            ///System.out.println(tituloReportagem.text().toLowerCase());

            if (getVerifica(tituloReportagem.text().toLowerCase(), valorProcurado)) //verifica se o titulo da reportagem contem algum dos termos procurado
            {
               /// System.out.println(tituloReportagem.text());
                bw.write("\nTitulo: "+tituloReportagem.text().toLowerCase()); ///escrita arquivo
                bw.newLine();
                String absHref = tituloReportagem.attr("abs:href"); //funcao que me retorna a URL completa EX: https://g1.globo.com/economia/noticia/2018/08/09/petrobras-recebe-mais-de-r-1-bi-recuperado-pela-operacao-lava-jato.ghtml
                bw.write("Link: " + absHref);
                bw.newLine();
                Document pagEncontrada = Jsoup.connect(absHref).get();
                Elements pages;
                Elements author;
               
                pages = pagEncontrada.select("p"); ///Conteudo yahoo.com
                author = pagEncontrada.select(".Provider"); ///Data yahoo.com
                ///System.out.println("Autor: "+author.text());
                
                ///System.out.println("Pages:"+pages.text());
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

        bw.close();
        fw.close();
        //JFrame frame = new JFrame(url);
        //JOptionPane.showMessageDialog(frame, sdf.format(new Date()));
    }
    
}
