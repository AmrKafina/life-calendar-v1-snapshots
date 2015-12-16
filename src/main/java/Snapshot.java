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

import java.util.ArrayList;

import java.io.File;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import javax.imageio.ImageIO;

import java.io.PrintWriter;

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

import java.awt.GraphicsEnvironment;
import java.awt.FontFormatException;


@WebServlet(name = "snapshot",urlPatterns = {"/snapshot/*"})
public class Snapshot extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public Snapshot() {
        super();
        
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        PrintWriter out = response.getWriter();

       // response.setContentType("image/png");
        
       // String pathToWeb = getServletContext().getRealPath(File.separator);
       // File f = new File(pathToWeb + "sync_problem.png");
      //  BufferedImage bi = ImageIO.read(inputStream);
      //  OutputStream out = response.getOutputStream();
      //  ImageIO.write(bi, "png", out);
      //  out.close();
        
        
        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
      //  OutputStream out = response.getOutputStream();
        
        for ( int i = 0; i < fonts.length; i++ )
        {
            out.println(fonts[i]);
        }

        try {
        InputStream fontStream = this.getServletConfig().getServletContext().getResourceAsStream("/WEB-INF/blzee.ttf");
            Font customFont = Font.createFont(Font.PLAIN, fontStream);
        }
        catch (Exception e) {
            out.println(e.getMessage());
        }

       // out.close();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            // constructs the data from the "request" and stores it in "input"
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
            ArrayList<Object> snapshotRequest = (ArrayList<Object>) ois.readObject();
            
            response.setStatus(HttpServletResponse.SC_OK);
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
            
            BufferedImage generatedSnapshot = generateSnapshot(snapshotRequest.get(0).toString(), (Integer)snapshotRequest.get(1), (int[])snapshotRequest.get(2));
            
            OutputStream out = response.getOutputStream();
            ImageIO.write(generatedSnapshot, "png", out);
            out.close();
            
       //     String reply = "Request recieved! Title will be " + snapshotTitle + ". The snapshot will be of type " + snapshotType + ".";
       //     writer.write(reply);
       //     writer.flush();
       //     writer.close();
            
        }
        catch (Exception e) {
            
            try{
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(e.getMessage());
                response.getWriter().close();
            } catch (IOException ioe) {
            }
   
        }
    }
    
    public BufferedImage generateSnapshot(String snapshotTitle, int snapshotType, int[] data) throws IOException, FontFormatException {

        // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
        // into integer pixels
        int width = 2048, height = 2048;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D ig2 = bi.createGraphics();
        
        // sets the color to white, and then paints the whole rectangle/canvas
        // Color backgroundColor = new Color(232,219,197);
        ig2.setColor(Color.WHITE);
        ig2.fillRect(0, 0, width, height);
        
            
                InputStream fontStream = this.getServletConfig().getServletContext().getResourceAsStream("/WEB-INF/blzee.ttf");

                //create the font to use.
            Font customFont = Font.createFont(Font.PLAIN, fontStream);
            Font titleFont = customFont.deriveFont(Font.PLAIN, 72);
                
          //      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                //register the font
          //      ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fontStream));
            
            
            ig2.setColor(Color.BLACK);
            ig2.setFont(titleFont);
          //  ig2.setFont(new Font("TimesRoman", Font.PLAIN, 72));

        FontMetrics fm = ig2.getFontMetrics(titleFont);
        int titleWidth = fm.stringWidth(snapshotTitle);
        
        ig2.drawString(snapshotTitle, (width - titleWidth) / 2, 200);

        // loads the images from memory
            InputStream inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/images/year_circle_black.png");
        Image yearCircleBlack = ImageIO.read(inputStream);
        
        inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/images/year_circle_blue.png");
        Image yearCircleBlue = ImageIO.read(inputStream);
        
        inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/images/year_circle_green.png");
        Image yearCircleGreen = ImageIO.read(inputStream);
        
        inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/images/year_circle_grey.png");
        Image yearCircleGrey = ImageIO.read(inputStream);
        
        inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/images/year_circle_pink.png");
        Image yearCirclePink = ImageIO.read(inputStream);
        
        inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/images/year_circle_purple.png");
        Image yearCirclePurple = ImageIO.read(inputStream);
        
        inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/images/year_circle_red.png");
        Image yearCircleRed = ImageIO.read(inputStream);
        
        inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/images/year_circle_yellow.png");
        Image yearCircleYellow = ImageIO.read(inputStream);
        
        inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/images/year_circle_bordered.png");
        Image yearCircleBordered = ImageIO.read(inputStream); // the blank/transparent year circle
        
        int margin = 30;
        int rowPadding = 274;
        int topPadding = 349;
        int circleSize = 120;
        int rowNumber = 0;
        
        for (int i = 0; rowNumber < 9; i++) {
            
            if (i == 10) { // starts a new row every 10 circles
                rowNumber++;
                i = 0;
                if (rowNumber == 9)
                    break;
            }
            
            Image imageToUse;
            
            switch (data[i + rowNumber * 10]) {
                    
                case 1:
                    imageToUse = yearCircleGreen;
                    break;
                case 2:
                    imageToUse = yearCircleBlue;
                    break;
                case 3:
                    imageToUse = yearCircleRed;
                    break;
                case 4:
                    imageToUse = yearCirclePurple;
                    break;
                case 5:
                    imageToUse = yearCircleBordered;
                    break;
                case 6:
                    imageToUse = yearCircleGrey;
                    break;
                case 7:
                    imageToUse = yearCircleYellow;
                    break;
                case 8:
                    imageToUse = yearCirclePink;
                    break;
                case 9:
                    imageToUse = yearCircleBlack;
                    break;
                default:
                    imageToUse = yearCircleBordered;
            }
            
            ig2.drawImage(imageToUse, (rowPadding + i * (circleSize + margin)), (topPadding + rowNumber * (circleSize + margin)), (rowPadding + i * (circleSize + margin) + circleSize),
                          (topPadding + rowNumber * (circleSize + margin) + circleSize), 0, 0, circleSize, circleSize, null);
            
        }

        return bi;
    }
    
    
}