package main.java;

import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;


import java.awt.image.BufferedImage;
import java.awt.Image;

import java.util.ArrayList;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import org.apache.pdfbox.exceptions.*;



@WebServlet(name = "notebook",urlPatterns = {"/notebook/*"})
public class Notebook extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public Notebook() {
        super();
        
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            
            /*
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output = createPDF();
            
            
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            
            response.setHeader("Content-Type", "application/pdf");
            
            response.setContentLength(output.size());
            
            ServletOutputStream out = response.getOutputStream();
            output.writeTo(out);
            out.flush();
             */
            
            
            String pdfFileName = "yourFile.pdf";
            String contextPath = getServletContext().getRealPath(File.separator);
            File pdfFile = new File(contextPath + pdfFileName);
            
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + pdfFileName);
            response.setContentLength((int) pdfFile.length());
            
            FileInputStream fileInputStream = new FileInputStream(pdfFile);
            OutputStream responseOutputStream = response.getOutputStream();
            int bytes;
            while ((bytes = fileInputStream.read()) != -1) {
                responseOutputStream.write(bytes);
            }
            
            
        } catch (Exception ex) {
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    
    }
    
    public ByteArrayOutputStream createPDF() throws IOException, COSVisitorException {
        
        PDDocument document;
        PDPage page;
        PDFont font;
        PDPageContentStream contentStream;
       // PDJpeg front;
       // PDJpeg back;
        
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