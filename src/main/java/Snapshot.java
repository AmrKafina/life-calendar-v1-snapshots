package main.java;

import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import javax.imageio.ImageIO;


@WebServlet(name = "snapshot",urlPatterns = {"/snapshot/*"})
public class Snapshot extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public Snapshot() {
        super();
        
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        
        InputStream inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/WEB-INF/sync_disabled.png");

    
        response.setContentType("image/png");
        
       // String pathToWeb = getServletContext().getRealPath(File.separator);
       // File f = new File(pathToWeb + "sync_problem.png");
        BufferedImage bi = ImageIO.read(inputStream);
        OutputStream out = response.getOutputStream();
        ImageIO.write(bi, "png", out);
        out.close();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            int length = request.getContentLength();
            byte[] input = new byte[length];
            ServletInputStream sin = request.getInputStream();
            int c, count = 0 ;
            while ((c = sin.read(input, count, input.length-count)) != -1) {
                count +=c;
            }
            sin.close();
            
            ByteArrayInputStream bis = new ByteArrayInputStream(input);
            ObjectInputStream ois = new ObjectInputStream(bis);
            int[] sentData = (int[])ois.readObject();
            
            response.setStatus(HttpServletResponse.SC_OK);
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
            
            String dataToNumbers = "Year colors: ";
            for (int i = 0; i < sentData.length; i++)
                dataToNumbers += "" + sentData[i];
            
            writer.write(dataToNumbers);
            writer.flush();
            writer.close();
            
            
        } catch (IOException e) {
            
            try{
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(e.getMessage());
                response.getWriter().close();
            } catch (IOException ioe) {
            }
        }
        catch (Exception e) {
            
        }
    }
    

    
}