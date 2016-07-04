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
import java.io.ByteArrayOutputStream;

import java.awt.GraphicsEnvironment;
import java.awt.FontFormatException;
import java.nio.charset.StandardCharsets;

import org.json.*;
import org.apache.commons.codec.binary.Base64;


@WebServlet(name = "snapshotios",urlPatterns = {"/snapshotios/*"})
public class SnapshotIOS extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public SnapshotIOS() {
        super();
        
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        PrintWriter out = response.getWriter();
        out.println("I can hear you!!");
        out.close();



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
            
            // constrcuts the JSON object from the input
            String jsonString = new String(input, StandardCharsets.UTF_8);
            JSONObject jRequest  = new JSONObject(jsonString);
            
            // reads the title and type
            String snapshotTitle = jRequest.getString("title");
            int snapshotType = jRequest.getInt("type");
            
            // creates an int array and reads the colors
            JSONArray jColors = jRequest.optJSONArray("colors");
            int[] colors = new int[jColors.length()];
            for (int i = 0; i < jColors.length(); ++i)
                colors[i] = jColors.optInt(i);
        
            // generate the snapshot using the extracted data
            BufferedImage generatedSnapshot = generateSnapshot(snapshotTitle, snapshotType, colors);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(generatedSnapshot, "PNG", out);
            byte[] snapshotBytes = out.toByteArray();
            String jSnapshot = Base64.encodeBase64String(snapshotBytes);

            // sets the status of the response to "ok"
            response.setStatus(HttpServletResponse.SC_OK);

            // finally sends back the snapshot
            PrintWriter output = response.getWriter();
            output.println(jSnapshot);
            output.close();
            
            // convert the generated snapshot into a base64 string (to send it back as a JSON object)
            
           /*
            // generates the snapshot
            BufferedImage generatedSnapshot = generateSnapshot(snapshotTitle, snapshotType, colors);
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( generatedSnapshot, "png", baos );
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            
            String base64String = Base64.encodeBase64String(imageInByte);
            
            PrintWriter out = response.getWriter();
            out.println(base64String);
            out.close();

           // JSONObject requestObject = new JSONObject(jsonString);
            
           // requestObject.getJSONInteger

            */
            /*
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
            */
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
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_1/year_circle.png");
            Image yearCircleGradient1 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_2/year_circle.png");
            Image yearCircleGradient2 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_3/year_circle.png");
            Image yearCircleGradient3 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_4/year_circle.png");
            Image yearCircleGradient4 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_5/year_circle.png");
            Image yearCircleGradient5 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_6/year_circle.png");
            Image yearCircleGradient6 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_7/year_circle.png");
            Image yearCircleGradient7 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_8/year_circle.png");
            Image yearCircleGradient8 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_9/year_circle.png");
            Image yearCircleGradient9 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_10/year_circle.png");
            Image yearCircleGradient10 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_11/year_circle.png");
            Image yearCircleGradient11 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_12/year_circle.png");
            Image yearCircleGradient12 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_13/year_circle.png");
            Image yearCircleGradient13 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_14/year_circle.png");
            Image yearCircleGradient14 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_15/year_circle.png");
            Image yearCircleGradient15 = ImageIO.read(inputStream);
            
            
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
                    case 11:
                        imageToUse = yearCircleGradient1;
                        break;
                    case 12:
                        imageToUse = yearCircleGradient2;
                        break;
                    case 13:
                        imageToUse = yearCircleGradient3;
                        break;
                    case 14:
                        imageToUse = yearCircleGradient4;
                        break;
                    case 15:
                        imageToUse = yearCircleGradient5;
                        break;
                    case 16:
                        imageToUse = yearCircleGradient6;
                        break;
                    case 17:
                        imageToUse = yearCircleGradient7;
                        break;
                    case 18:
                        imageToUse = yearCircleGradient8;
                        break;
                    case 19:
                        imageToUse = yearCircleGradient9;
                        break;
                    case 20:
                        imageToUse = yearCircleGradient10;
                        break;
                    case 21:
                        imageToUse = yearCircleGradient11;
                        break;
                    case 22:
                        imageToUse = yearCircleGradient12;
                        break;
                    case 23:
                        imageToUse = yearCircleGradient13;
                        break;
                    case 24:
                        imageToUse = yearCircleGradient14;
                        break;
                    case 25:
                        imageToUse = yearCircleGradient15;
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
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_1/week_box.png");
            Image weekBoxGradient1 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_2/week_box.png");
            Image weekBoxGradient2 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_3/week_box.png");
            Image weekBoxGradient3 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_4/week_box.png");
            Image weekBoxGradient4 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_5/week_box.png");
            Image weekBoxGradient5 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_6/week_box.png");
            Image weekBoxGradient6 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_7/week_box.png");
            Image weekBoxGradient7 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_8/week_box.png");
            Image weekBoxGradient8 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_9/week_box.png");
            Image weekBoxGradient9 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_10/week_box.png");
            Image weekBoxGradient10 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_11/week_box.png");
            Image weekBoxGradient11 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_12/week_box.png");
            Image weekBoxGradient12 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_13/week_box.png");
            Image weekBoxGradient13 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_14/week_box.png");
            Image weekBoxGradient14 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_15/week_box.png");
            Image weekBoxGradient15 = ImageIO.read(inputStream);
            
            int margin = 30;
            int rowPadding = 274;
            int topPadding = 569 + 72;
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
                        case 11:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_1/day_box.png");
                            break;
                        case 12:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_2/day_box.png");
                            break;
                        case 13:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_3/day_box.png");
                            break;
                        case 14:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_4/day_box.png");
                            break;
                        case 15:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_5/day_box.png");
                            break;
                        case 16:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_6/day_box.png");
                            break;
                        case 17:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_7/day_box.png");
                            break;
                        case 18:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_8/day_box.png");
                            break;
                        case 19:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_9/day_box.png");
                            break;
                        case 20:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_10/day_box.png");
                            break;
                        case 21:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_11/day_box.png");
                            break;
                        case 22:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_12/day_box.png");
                            break;
                        case 23:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_13/day_box.png");
                            break;
                        case 24:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_14/day_box.png");
                            break;
                        case 25:
                            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_15/day_box.png");
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
                    case 11:
                        imageToUse = weekBoxGradient1;
                        break;
                    case 12:
                        imageToUse = weekBoxGradient2;
                        break;
                    case 13:
                        imageToUse = weekBoxGradient3;
                        break;
                    case 14:
                        imageToUse = weekBoxGradient4;
                        break;
                    case 15:
                        imageToUse = weekBoxGradient5;
                        break;
                    case 16:
                        imageToUse = weekBoxGradient6;
                        break;
                    case 17:
                        imageToUse = weekBoxGradient7;
                        break;
                    case 18:
                        imageToUse = weekBoxGradient8;
                        break;
                    case 19:
                        imageToUse = weekBoxGradient9;
                        break;
                    case 20:
                        imageToUse = weekBoxGradient10;
                        break;
                    case 21:
                        imageToUse = weekBoxGradient11;
                        break;
                    case 22:
                        imageToUse = weekBoxGradient12;
                        break;
                    case 23:
                        imageToUse = weekBoxGradient13;
                        break;
                    case 24:
                        imageToUse = weekBoxGradient14;
                        break;
                    case 25:
                        imageToUse = weekBoxGradient15;
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
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_1/week_box_small.png");
            Image weekBoxGradient1 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_2/week_box_small.png");
            Image weekBoxGradient2 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_3/week_box_small.png");
            Image weekBoxGradient3 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_4/week_box_small.png");
            Image weekBoxGradient4 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_5/week_box_small.png");
            Image weekBoxGradient5 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_6/week_box_small.png");
            Image weekBoxGradient6 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_7/week_box_small.png");
            Image weekBoxGradient7 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_8/week_box_small.png");
            Image weekBoxGradient8 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_9/week_box_small.png");
            Image weekBoxGradient9 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_10/week_box_small.png");
            Image weekBoxGradient10 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_11/week_box_small.png");
            Image weekBoxGradient11 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_12/week_box_small.png");
            Image weekBoxGradient12 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_13/week_box_small.png");
            Image weekBoxGradient13 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_14/week_box_small.png");
            Image weekBoxGradient14 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_15/week_box_small.png");
            Image weekBoxGradient15 = ImageIO.read(inputStream);
            
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
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_1/day_box_small.png");
            Image dayBoxGradient1 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_2/day_box_small.png");
            Image dayBoxGradient2 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_3/day_box_small.png");
            Image dayBoxGradient3 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_4/day_box_small.png");
            Image dayBoxGradient4 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_5/day_box_small.png");
            Image dayBoxGradient5 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_6/day_box_small.png");
            Image dayBoxGradient6 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_7/day_box_small.png");
            Image dayBoxGradient7 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_8/day_box_small.png");
            Image dayBoxGradient8 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_9/day_box_small.png");
            Image dayBoxGradient9 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_10/day_box_small.png");
            Image dayBoxGradient10 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_11/day_box_small.png");
            Image dayBoxGradient11 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_12/day_box_small.png");
            Image dayBoxGradient12 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_13/day_box_small.png");
            Image dayBoxGradient13 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_14/day_box_small.png");
            Image dayBoxGradient14 = ImageIO.read(inputStream);
            
            inputStream = this.getServletConfig().getServletContext().getResourceAsStream("/gradients/gradient_15/day_box_small.png");
            Image dayBoxGradient15 = ImageIO.read(inputStream);
            
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
                        case 11:
                            imageToUse = dayBoxGradient1;
                            break;
                        case 12:
                            imageToUse = dayBoxGradient2;
                            break;
                        case 13:
                            imageToUse = dayBoxGradient3;
                            break;
                        case 14:
                            imageToUse = dayBoxGradient4;
                            break;
                        case 15:
                            imageToUse = dayBoxGradient5;
                            break;
                        case 16:
                            imageToUse = dayBoxGradient6;
                            break;
                        case 17:
                            imageToUse = dayBoxGradient7;
                            break;
                        case 18:
                            imageToUse = dayBoxGradient8;
                            break;
                        case 19:
                            imageToUse = dayBoxGradient9;
                            break;
                        case 20:
                            imageToUse = dayBoxGradient10;
                            break;
                        case 21:
                            imageToUse = dayBoxGradient11;
                            break;
                        case 22:
                            imageToUse = dayBoxGradient12;
                            break;
                        case 23:
                            imageToUse = dayBoxGradient13;
                            break;
                        case 24:
                            imageToUse = dayBoxGradient14;
                            break;
                        case 25:
                            imageToUse = dayBoxGradient15;
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
                        case 11:
                            imageToUse = weekBoxGradient1;
                            break;
                        case 12:
                            imageToUse = weekBoxGradient2;
                            break;
                        case 13:
                            imageToUse = weekBoxGradient3;
                            break;
                        case 14:
                            imageToUse = weekBoxGradient4;
                            break;
                        case 15:
                            imageToUse = weekBoxGradient5;
                            break;
                        case 16:
                            imageToUse = weekBoxGradient6;
                            break;
                        case 17:
                            imageToUse = weekBoxGradient7;
                            break;
                        case 18:
                            imageToUse = weekBoxGradient8;
                            break;
                        case 19:
                            imageToUse = weekBoxGradient9;
                            break;
                        case 20:
                            imageToUse = weekBoxGradient10;
                            break;
                        case 21:
                            imageToUse = weekBoxGradient11;
                            break;
                        case 22:
                            imageToUse = weekBoxGradient12;
                            break;
                        case 23:
                            imageToUse = weekBoxGradient13;
                            break;
                        case 24:
                            imageToUse = weekBoxGradient14;
                            break;
                        case 25:
                            imageToUse = weekBoxGradient15;
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