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
        
        try {
            doPost(request, response);
        }
        catch (Exception e) {
            response.getOutputStream().println("Error: " + e.getMessage());
        }

      //  PrintWriter out = response.getWriter();

       // response.setContentType("image/png");
        
       // String pathToWeb = getServletContext().getRealPath(File.separator);
       // File f = new File(pathToWeb + "sync_problem.png");
      //  BufferedImage bi = ImageIO.read(inputStream);
        
        //  OutputStream out = response.getOutputStream();

        
        /*
        int snapshotWidth, snapshotHeight;
        BufferedImage finalSnapshot;
        Graphics2D graphics;
        InputStream inputStream;
        Image snapshotTemplate;
        
        snapshotWidth = 2304;
        snapshotHeight = 3687;
        finalSnapshot = new BufferedImage(snapshotWidth, snapshotHeight, BufferedImage.TYPE_INT_ARGB);
        graphics = finalSnapshot.createGraphics();
        
        // loads the yearsSnapshot template
        inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/images/Weeks_Snapshot_Template.png");
        snapshotTemplate = ImageIO.read(inputStream);
        
        graphics.drawImage(snapshotTemplate, 0, 0, null);
        
        OutputStream out1 = response.getOutputStream();
        ImageIO.write(finalSnapshot, "png", out1);
        out1.close();
        
        
        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
      //  OutputStream out = response.getOutputStream();
        
        for ( int i = 0; i < fonts.length; i++ )
        {
      //      out.println(fonts[i]);
        }

        try {
        InputStream fontStream = this.getServletConfig().getServletContext().getResourceAsStream("/WEB-INF/OpenSans.ttf");
            Font customFont = Font.createFont(Font.PLAIN, fontStream);
        }
        catch (Exception e) {
      //      out.println(e.getMessage());
        }

       // out.close();
         
         */
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
            
            // reads the data from input and puts it in "snapshotRequest"
            ByteArrayInputStream bis = new ByteArrayInputStream(input);
            ObjectInputStream ois = new ObjectInputStream(bis);
            ArrayList<Object> snapshotRequest = (ArrayList<Object>) ois.readObject();
            
            // sets the status of the response to "ok"
            response.setStatus(HttpServletResponse.SC_OK);
            
            // generates the snapshot
            BufferedImage generatedSnapshot = generateSnapshot(snapshotRequest.get(0).toString(), (Integer)snapshotRequest.get(1), (int[])snapshotRequest.get(2));
            
            // sends the data back to the client
            OutputStream out = response.getOutputStream();
            ImageIO.write(generatedSnapshot, "png", out);
            out.close();
            
        }
        catch (Exception e) { // if something goes wrong, sets the status of the response to "bad request" and send back the error message
            
            try {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(e.getMessage());
                response.getWriter().close();
            }
            catch (IOException ioe) {
            }
        }
    }
    
    public BufferedImage generateSnapshot(String snapshotTitle, int snapshotType, int[] data) throws IOException, FontFormatException {

        int snapshotWidth, snapshotHeight;
        BufferedImage finalSnapshot;
        Graphics2D graphics;
        InputStream inputStream;
        Image snapshotTemplate;
        
        // creates the font used for the title
        InputStream fontStream = this.getServletConfig().getServletContext().getResourceAsStream("/WEB-INF/OpenSans.ttf");
        Font blzeeFont = Font.createFont(Font.PLAIN, fontStream);
        Font titleFont = blzeeFont.deriveFont(Font.PLAIN, 112);
        
        // this color is used for the title
        Color blackColor = new Color(51, 51, 51);

        
        if (snapshotType == 0) { // if a yearsSnapshot
            
            snapshotWidth = snapshotHeight = 2048;
            finalSnapshot = new BufferedImage(snapshotWidth, snapshotHeight, BufferedImage.TYPE_INT_ARGB);
            graphics = finalSnapshot.createGraphics();
            
            // loads the yearsSnapshot template
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/images/Years_Snapshot_Template.png");
            snapshotTemplate = ImageIO.read(inputStream);
            
        }
        else if (snapshotType == 1) { // if a yearSnapshot
            
            snapshotWidth = snapshotHeight = 2048;
            finalSnapshot = new BufferedImage(snapshotWidth, snapshotHeight, BufferedImage.TYPE_INT_ARGB);
            graphics = finalSnapshot.createGraphics();
            
            // loads the yearsSnapshot template
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/images/Year_Snapshot_Template.png");
            snapshotTemplate = ImageIO.read(inputStream);
            
            
        }
        else { // a weeksSnapshot
            
            snapshotWidth = 2304;
            snapshotHeight = 3687;
            finalSnapshot = new BufferedImage(snapshotWidth, snapshotHeight, BufferedImage.TYPE_INT_ARGB);
            graphics = finalSnapshot.createGraphics();
            
            // loads the yearsSnapshot template
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/images/Weeks_Snapshot_Template.png");
            snapshotTemplate = ImageIO.read(inputStream);

            
        }
        // sets the background color to white
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, snapshotWidth, snapshotHeight);
        
        // draws the template
        graphics.drawImage(snapshotTemplate, 0, 0, null);
        
        // sets the font for the title
        graphics.setColor(blackColor);
        graphics.setFont(titleFont);

        // draws the title, centered horizontally
        FontMetrics fm = graphics.getFontMetrics(titleFont);
        int titleWidth = fm.stringWidth(snapshotTitle);
        graphics.drawString(snapshotTitle, (snapshotWidth - titleWidth) / 2, 200);

        
        if (snapshotType == 0) { // if a yearsSnapshot
            
            // loads the images from memory
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/black/year_circle.png");
            Image yearCircleBlack = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/blue/year_circle.png");
            Image yearCircleBlue = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/green/year_circle.png");
            Image yearCircleGreen = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/grey/year_circle.png");
            Image yearCircleGrey = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/orange/year_circle.png");
            Image yearCircleOrange = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/purple/year_circle.png");
            Image yearCirclePurple = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/red/year_circle.png");
            Image yearCircleRed = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/yellow/year_circle.png");
            Image yearCircleYellow = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/pink/year_circle.png");
            Image yearCirclePink = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/white/year_circle.png");
            Image yearCircleBordered = ImageIO.read(inputStream); // the blank/transparent year circle
            
            int margin = 30;
            int rowPadding = 274;
            int topPadding = 349 + 72;
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
                
                switch (data[i + rowNumber * 10]) { // uses the appropriate image, according to the data
                        
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
                        imageToUse = yearCircleOrange;
                        break;
                    case 9:
                        imageToUse = yearCircleBlack;
                        break;
                    case 10:
                        imageToUse = yearCirclePink;
                        break;
                    default:
                        imageToUse = yearCircleBordered;
                }
                
                graphics.drawImage(imageToUse, (rowPadding + i * (circleSize + margin)), (topPadding + rowNumber * (circleSize + margin)), (rowPadding + i * (circleSize + margin) + circleSize),
                              (topPadding + rowNumber * (circleSize + margin) + circleSize), 0, 0, circleSize, circleSize, null);
                
            }
            
        }
        else if (snapshotType == 1) { // if a yearSnapshot
            
            // loads the images from memory
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/black/week_box.png");
            Image weekBoxBlack = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/blue/week_box.png");
            Image weekBoxBlue = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/green/week_box.png");
            Image weekBoxGreen = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/grey/week_box.png");
            Image weekBoxGrey = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/orange/week_box.png");
            Image weekBoxOrange = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/purple/week_box.png");
            Image weekBoxPurple = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/red/week_box.png");
            Image weekBoxRed = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/yellow/week_box.png");
            Image weekBoxYellow = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/pink/week_box.png");
            Image weekBoxPink = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/white/week_box.png");
            Image weekBoxBordered = ImageIO.read(inputStream); // the blank/transparent week box
            
            int margin = 30;
            int rowPadding = 274;
            int topPadding = 569 + 72; //220
            int boxSize = 120;
            int rowNumber = 0;
            
            for (int i = 0; rowNumber <= 5; i++) {
                
                if (i == 10) { // starts a new row every 10 boxes
                    rowNumber++;
                    i = 0;
                }
                
                
                Image imageToUse;
                
                if (rowNumber == 5 && i == 2) { // loads and draws the day box, and then exits the loop
                    switch (data[52]) {
                        case 1:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/green/day_box.png");
                            break;
                        case 2:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/blue/day_box.png");
                            break;
                        case 3:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/red/day_box.png");
                            break;
                        case 4:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/purple/day_box.png");
                            break;
                        case 5:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/white/day_box.png");
                            break;
                        case 6:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/grey/day_box.png");
                            break;
                        case 7:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/yellow/day_box.png");
                            break;
                        case 8:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/orange/day_box.png");
                            break;
                        case 9:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/black/day_box.png");
                            break;
                        case 10:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/pink/day_box.png");
                            break;
                        default:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/white/day_box.png");
                    }

                    imageToUse = ImageIO.read(inputStream);
                    
                    graphics.drawImage(imageToUse, (rowPadding + i * (boxSize + margin)), (topPadding + rowNumber * (boxSize + margin)), (rowPadding + i * (boxSize + margin) + boxSize), (topPadding + rowNumber * (boxSize + margin) + boxSize), 0, 0, boxSize, boxSize, null);
                    break;
                }
                
                switch (data[i + rowNumber * 10]) { // uses the appropriate image, according to the data
                        
                    case 1:
                        imageToUse = weekBoxGreen;
                        break;
                    case 2:
                        imageToUse = weekBoxBlue;
                        break;
                    case 3:
                        imageToUse = weekBoxRed;
                        break;
                    case 4:
                        imageToUse = weekBoxPurple;
                        break;
                    case 5:
                        imageToUse = weekBoxBordered;
                        break;
                    case 6:
                        imageToUse = weekBoxGrey;
                        break;
                    case 7:
                        imageToUse = weekBoxYellow;
                        break;
                    case 8:
                        imageToUse = weekBoxOrange;
                        break;
                    case 9:
                        imageToUse = weekBoxBlack;
                        break;
                    case 10:
                        imageToUse = weekBoxPink;
                        break;
                    default:
                        imageToUse = weekBoxBordered;
                }
                
                graphics.drawImage(imageToUse, (rowPadding + i * (boxSize + margin)), (topPadding + rowNumber * (boxSize + margin)), (rowPadding + i * (boxSize + margin) + boxSize),
                                   (topPadding + rowNumber * (boxSize + margin) + boxSize), 0, 0, boxSize, boxSize, null);
                
            }

            
        }
        else { // a weeksSnapshot
            
            // loads the images from memory
            
            // loads the images from memory
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/black/week_box_small.png");
            Image weekBoxBlack = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/blue/week_box_small.png");
            Image weekBoxBlue = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/green/week_box_small.png");
            Image weekBoxGreen = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/grey/week_box_small.png");
            Image weekBoxGrey = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/orange/week_box_small.png");
            Image weekBoxOrange = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/purple/week_box_small.png");
            Image weekBoxPurple = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/red/week_box_small.png");
            Image weekBoxRed = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/yellow/week_box_small.png");
            Image weekBoxYellow = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/pink/week_box_small.png");
            Image weekBoxPink = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/white/week_box_small.png");
            Image weekBoxBordered = ImageIO.read(inputStream); // the blank/transparent week box
             
             
             inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/black/day_box_small.png");
             Image dayBoxBlack = ImageIO.read(inputStream);
             
             inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/blue/day_box_small.png");
             Image dayBoxBlue = ImageIO.read(inputStream);
             
             inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/green/day_box_small.png");
             Image dayBoxGreen = ImageIO.read(inputStream);
             
             inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/grey/day_box_small.png");
             Image dayBoxGrey = ImageIO.read(inputStream);
             
             inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/orange/day_box_small.png");
             Image dayBoxOrange = ImageIO.read(inputStream);
             
             inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/purple/day_box_small.png");
             Image dayBoxPurple = ImageIO.read(inputStream);
             
             inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/red/day_box_small.png");
             Image dayBoxRed = ImageIO.read(inputStream);
             
             inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/yellow/day_box_small.png");
             Image dayBoxYellow = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/pink/day_box_small.png");
            Image dayBoxPink = ImageIO.read(inputStream);
             
             inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/white/day_box_small.png");
             Image dayBoxBordered = ImageIO.read(inputStream); // the blank/transparent day box

            
            // initializes some values
            int margin = 4;
            int rowPadding = 283;
            int topPadding = 411;
            int boxSize = 30;
            int rowNumber = 0;
            
            // this is where the drawing begins
            
            for (int i = 0; rowNumber < 90; i++) {
                
                if (i == 53) { // starts a new row every 53 boxes
                    rowNumber++;
                    i = 0;
                    if (rowNumber == 90)
                        break;
                }
                
                Image imageToUse;
                
                if (i == 52) { // draws the day box (at the end of each row
                    switch (data[(rowNumber * 53) + 52]) { // TODO data of rowNumber at element 52
                        case 1:
                            imageToUse = dayBoxGreen;
                            break;
                        case 2:
                            imageToUse = dayBoxBlue;
                            break;
                        case 3:
                            imageToUse = dayBoxRed;
                            break;
                        case 4:
                            imageToUse = dayBoxPurple;
                            break;
                        case 5:
                            imageToUse = dayBoxBordered;
                            break;
                        case 6:
                            imageToUse = dayBoxGrey;
                            break;
                        case 7:
                            imageToUse = dayBoxYellow;
                            break;
                        case 8:
                            imageToUse = dayBoxOrange;
                            break;
                        case 9:
                            imageToUse = dayBoxBlack;
                            break;
                        case 10:
                            imageToUse = dayBoxPink;
                            break;
                        default:
                            imageToUse = dayBoxBordered;
                    }
                 
                }
                else { // otherwise, load the appropriate week box
                    
                    switch (data[(rowNumber * 53) + i]) { // uses the appropriate image, according to the data
                            
                        case 1:
                            imageToUse = weekBoxGreen;
                            break;
                        case 2:
                            imageToUse = weekBoxBlue;
                            break;
                        case 3:
                            imageToUse = weekBoxRed;
                            break;
                        case 4:
                            imageToUse = weekBoxPurple;
                            break;
                        case 5:
                            imageToUse = weekBoxBordered;
                            break;
                        case 6:
                            imageToUse = weekBoxGrey;
                            break;
                        case 7:
                            imageToUse = weekBoxYellow;
                            break;
                        case 8:
                            imageToUse = weekBoxOrange;
                            break;
                        case 9:
                            imageToUse = weekBoxBlack;
                            break;
                        case 10:
                            imageToUse = weekBoxPink;
                            break;
                        default:
                            imageToUse = weekBoxBordered;
                    }

                }
                
                // now that the image is correctly loaded, we can finally draw it
                graphics.drawImage(imageToUse, (rowPadding + i * (boxSize + margin)), (topPadding + rowNumber * (boxSize + margin)), (rowPadding + i * (boxSize + margin) + boxSize), (topPadding + rowNumber * (boxSize + margin) + boxSize), 0, 0, boxSize, boxSize, null);
                
            }

        }
        
        return finalSnapshot;
    }
    
    
}