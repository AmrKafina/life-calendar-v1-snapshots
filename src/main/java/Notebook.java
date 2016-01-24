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


import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.edit.*;



@WebServlet(name = "notebook",urlPatterns = {"/notebook/*"})
public class Notebook extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public Notebook() {
        super();
        
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output = createPDF();
            
            response.addHeader("Content-Type", "application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=\"yourFile.pdf\"");
            response.getOutputStream().write(output.toByteArray());
            
        } catch (Exception ex) {
        }
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
    
    public ByteArrayOutputStream createPDF() throws IOException, COSVisitorException {
        
        PDDocument document;
        PDPage page;
        PDFont font;
        PDPageContentStream contentStream;
        PDJpeg front;
        PDJpeg back;
        
        InputStream inputFront;
        InputStream inputBack;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        // Creating Document
        document = new PDDocument();
        
        // Creating Pages
        for(int i=0; i<2; i++) {
            
            page = new PDPage();
            
            // Adding page to document
            document.addPage(page);
            
            // Adding FONT to document
            font = PDType1Font.HELVETICA;
            
            // Retrieve Image to be added to the PDF
            //inputFront = new FileInputStream(new File("D:/Media/imageFront.jpg"));
            //inputBack = new FileInputStream(new File("D:/Media/imageBack.jpg"));
            
            //BufferedImage buffFront = ImageIO.read(inputFront);
            //BufferedImage resizedFront = Scalr.resize(buffFront, 460);
            
            //BufferedImage buffBack = ImageIO.read(inputBack);
            //BufferedImage resizedBack = Scalr.resize(buffBack, 460);
            
            //front = new PDJpeg(document, resizedFront);
            //back = new PDJpeg(document, resizedBack);
            
            // Next we start a new content stream which will "hold" the to be created content.
            contentStream = new PDPageContentStream(document, page);
            
            // Let's define the content stream
            contentStream.beginText();
            contentStream.setFont(font, 8);
            contentStream.moveTextPositionByAmount(10, 770);
            contentStream.drawString("Amount: $1.00");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(font, 8);
            contentStream.moveTextPositionByAmount(200, 770);
            contentStream.drawString("Sequence Number: 123456789");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(font, 8);
            contentStream.moveTextPositionByAmount(10, 760);
            contentStream.drawString("Account: 123456789");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(font, 8);
            contentStream.moveTextPositionByAmount(200, 760);
            contentStream.drawString("Captura Date: 04/25/2011");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(font, 8);
            contentStream.moveTextPositionByAmount(10, 750);
            contentStream.drawString("Bank Number: 123456789");
            contentStream.endText();
            
            contentStream.beginText();
            contentStream.setFont(font, 8);
            contentStream.moveTextPositionByAmount(200, 750);
            contentStream.drawString("Check Number: 123456789");
            contentStream.endText();            
            
            // Let's close the content stream       
            contentStream.close();
            
        }
        
        // Finally Let's save the PDF
        document.save(output);
        document.close();
        
        return output;
    }
    
    
    
}