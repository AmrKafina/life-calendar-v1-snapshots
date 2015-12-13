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
import java.awt.Image;

import java.io.File;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import javax.imageio.ImageIO;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


@WebServlet(name = "snapshot",urlPatterns = {"/snapshot/*"})
public class Snapshot extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public Snapshot() {
        super();
        
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        
        
        int width = 2000, height = 2000;
        
        // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
        // into integer pixels
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        
        Graphics2D ig2 = bi.createGraphics();
        
        InputStream inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/WEB-INF/sync_disabled.png");
          Image syncImage = ImageIO.read(inputStream);
        
        Font font = new Font("TimesRoman", Font.BOLD, 20);
        ig2.setFont(font);
        String message = "www.java2s.com!";
        FontMetrics fontMetrics = ig2.getFontMetrics();
        int stringWidth = fontMetrics.stringWidth(message);
        int stringHeight = fontMetrics.getAscent();
        ig2.setPaint(Color.red);
        ig2.drawImage(syncImage,0, 0, 500, 500,
                      0, 0, 500, 500,
                      null);
       // ig2.drawString(message, (width - stringWidth) / 2, height / 2 + stringHeight / 4);
        
        
        
        
        
        
        response.setContentType("image/png");
        
       // String pathToWeb = getServletContext().getRealPath(File.separator);
       // File f = new File(pathToWeb + "sync_problem.png");
      //  BufferedImage bi = ImageIO.read(inputStream);
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