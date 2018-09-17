package crawlerjsoup;

import java.io.IOException;
import java.util.List;

/**
 * Listando link de uma URL
 */
public class crawlerjsoup {

    public static void main(String[] args) throws IOException 
    {  
        globo g = new globo();
        g.busca();
        
        yahoo y = new yahoo();
        y.busca();
        
        uol u = new uol();
        u.busca();

        
        
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static boolean verifica(String tituloReportagem, List valorProcurado) {
        for (int i = 0; i < valorProcurado.size(); i++) {
            if (tituloReportagem.contains(valorProcurado.get(i).toString())) {
                return true;
            }
        }
        return false;
    }
    static boolean getVerifica(String tituloReportagem, List valorProcurado)
    {
        return verifica(tituloReportagem, valorProcurado);
    }
    
    static void getPrint(String msg, Object... args)
    {
        print(msg, args);
    }
    
    static boolean getIsPunct(String str)
    {
        if(!Character.isLetter(str.charAt(0)) && !Character.isDigit(str.charAt(0)))
            return true;
        else 
            return false;
    }

}
