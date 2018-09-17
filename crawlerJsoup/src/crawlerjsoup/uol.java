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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Samsung
 */
public class uol 
{
public void busca() throws IOException {
        String[] sargs = {"http://www.uol.com.br", "http://yahoo.com", "https://g1.globo.com/economia/noticia/2018/08/09/petrobras-recebe-mais-de-r-1-bi-recuperado-pela-operacao-lava-jato.ghtml", "https://oglobo.globo.com/brasil/ibope-sem-lula-bolsonaro-aparece-com-20-marina-com-12-ciro-com-9-22995661"};
        
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
        File arquivo = new File("noticias/uol.com.br/" + sdf.format(new Date()) + ".txt");

        FileWriter fw = new FileWriter(arquivo);
        BufferedWriter bw = new BufferedWriter(fw);

        ///getPrint("\nTitles: (%d)", titles.size());
        for (Element tituloReportagem : titles) {

            if (getVerifica(tituloReportagem.text().toLowerCase(), valorProcurado)) //verifica se o titulo da reportagem contem algum dos termos procurado
            {
                if(!" acessar a versão celular".contains(tituloReportagem.text().toLowerCase())) ///Trata um erro de um lixo que sempre aparecia, como sendo reportagem
                {
                    ///System.out.println("Titulo: "+tituloReportagem);
                bw.write("\nTitulo: "+tituloReportagem.text().toLowerCase()); ///escrita arquivo
                bw.newLine();
                
                Elements absHref = tituloReportagem.select("a"); 
                ///System.out.println("Link: " + absHref.attr("href")); //funcao que me retorna a URL completa EX: https://g1.globo.com/economia/noticia/2018/08/09/petrobras-recebe-mais-de-r-1-bi-recuperado-pela-operacao-lava-jato.ghtml
                
                Document pagEncontrada = Jsoup.connect(absHref.attr("abs:href")).get();
                bw.write("\nLink: "+absHref.attr("href")); ///escrita arquivo
                bw.newLine();
                Elements pages;
                Elements author;
                
                pages = pagEncontrada.select("article"); ///Conteudo 
                author = pagEncontrada.select(".info-header p"); ///Data 
                ///System.out.println("Autor:"+author.text());
                    if(author.isEmpty())
                    {
                        author = pagEncontrada.select(".c-signature"); ///A localização do autor varia de acordo com o layout
                    }
                bw.write("\nAutor: "+author.text()); ///escrita arquivo
                bw.newLine();
                Elements date = pagEncontrada.select("time");
                bw.write("\nData: "+date.text()); ///escrita arquivo
                bw.newLine();
                
                ///print("(%s)", link.text());
                bw.write("\nConteudo: "+pages.text().replaceAll("\\p{Punct}", "").toLowerCase()+"\n\n"); //\\W remove todos os caracteres que não são letras e substitui por espaços vazios
                bw.newLine();
                bw.newLine();
                }
   
            }
        
        
    }   
        bw.close();
        fw.close();
}
}
