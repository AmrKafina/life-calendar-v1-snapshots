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
        
            ByteArrayOutputStream output = new ByteArrayOutputStream();
          //  output = createPDF();
           
            
            response.setHeader("Content-Type", "application/pdf");
            response.setContentLength(output.size());
            
            ServletOutputStream out = response.getOutputStream();
            output.writeTo(out);
            out.flush();
            
            
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
            
            // reads the data from input and puts it in "notebookRequest"
            ByteArrayInputStream bis = new ByteArrayInputStream(input);
            ObjectInputStream ois = new ObjectInputStream(bis);
            ArrayList<Object> notebookRequest = (ArrayList<Object>) ois.readObject();
            
            // sets the status of the response to "ok"
            response.setStatus(HttpServletResponse.SC_OK);
            
            // generates the notebook
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output = createPDF((ArrayList<String>)notebookRequest.get(0), (ArrayList<String>)notebookRequest.get(1), (ArrayList<Integer>)notebookRequest.get(2), (int[][])notebookRequest.get(3));
            
            response.setHeader("Content-Type", "application/pdf");
            response.setContentLength(output.size());
            
            
            // sends the data back to the client
            ServletOutputStream out = response.getOutputStream();
            output.writeTo(out);
            out.flush();

            
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
    
    public ByteArrayOutputStream createPDF(ArrayList<String> noteNames, ArrayList<String> noteContents, ArrayList<Integer> noteLocations, int[][] weekColors) throws IOException, COSVisitorException {
        
        PDDocument document;
        PDPage page;
        PDFont font;
        PDPageContentStream contentStream;
       // PDJpeg front;
       // PDJpeg back;
        
        InputStream inputFront;
        InputStream inputBack;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        // initialize the document
        document = new PDDocument();
        
        
        font = PDType1Font.HELVETICA_BOLD; // Or whatever font you want.
        int fontSize = 16; // Or whatever font size you want.
        int paragraphWidth = 200;
        String text = "Lorem ipsum dolor sit amet, dictas epicuri mentitum cu cum. Eos unum ferri maiorum id. An pro illum habemus eloquentiam, malorum fastidii per ad, labores invidunt ut cum. Te autem iudicabit mei. Eum ex lobortis accusamus, nobis decore omittam eu eos, modo feugiat an quo. Tale mediocrem ei ius, suas accusamus voluptaria id sea.";
        
        
        page = new PDPage(PDPage.PAGE_SIZE_A5);
        
        // Adding page to document
        document.addPage(page);

        
        // Next we start a new content stream which will "hold" the to be created content.
        contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont( font, fontSize );

        int start = 0;
        int end = 0;
        int height = 10;
        for ( int i : possibleWrapPoints(text) ) {
            float width = font.getStringWidth(text.substring(start,i)) / 1000 * fontSize;
            if ( start < end && width > paragraphWidth ) {
                // Draw partial text and increase height
                contentStream.moveTextPositionByAmount(10 , height);
                contentStream.drawString(text.substring(start,end));
                height += font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
                start = end;
            }
            end = i;
        }
        // Last piece of text
        contentStream.moveTextPositionByAmount(10 , height);
        contentStream.drawString(text.substring(start));
        
        contentStream.endText();
        
    
        // Let's close the content stream
        contentStream.close();
        
        /*
        int numberOfPages = noteNames.size();
        
        // create the pages
        for(int i = 0; i < numberOfPages; i++) {
            
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
            
            
            // Let's define the content stream
            contentStream.beginText();
            contentStream.setFont(font, 60);
            contentStream.moveTextPositionByAmount(10, 770);
            contentStream.drawString(noteContents.get(i));
            contentStream.endText();
            
            
            // Let's close the content stream       
            contentStream.close();
            
        }
        */
        // Finally Let's save the PDF
        document.save(output);
        document.close();
        
        return output;
    }
    
    
    int[] possibleWrapPoints(String text) {
        String[] split = text.split("(?<=\\W)");
        int[] ret = new int[split.length];
        ret[0] = split[0].length();
        for ( int i = 1 ; i < split.length ; i++ )
            ret[i] = ret[i-1] + split[i].length();
        return ret;
    }

    
    
}

class Paragraph {
    
    /** width of this paragraph */
    private int width;
    
    /** text to write */
    private String text;
    
    /** font to use */
    private PDFont font;
    
    /** font size to use */
    private int fontSize;
    
    public Paragraph(int width, PDFont font, int fontSize, String text) {
        this.text = text;
        this.font = font;
        this.width = width;
        this.fontSize = fontSize;
    }
    
    /**
     * Break the text in lines
     * @return
     */
    public List getLines() throws IOException {
        List result = new ArrayList();
        
        String[] split = text.split("(?<=\w)");
        int[] possibleWrapPoints = new int[split.length];
        possibleWrapPoints[0] = split[0].length();
        for ( int i = 1 ; i < split.length ; i++ ) {
            possibleWrapPoints[i] = possibleWrapPoints[i-1] + split[i].length();
        }
        
        int start = 0;
        int end = 0;
        for ( int i : possibleWrapPoints ) {
            float width = font.getStringWidth(text.substring(start,i)) / 1000 * fontSize;
            if ( start < end && width > this.width ) {
                result.add(text.substring(start,end));
                start = end;
            }
            end = i;
        }
        // Last piece of text
        result.add(text.substring(start));
        return result;
    }
    
    public float getFontHeight() {
        return font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
    }
    
    public int getWidth() {
        return width;
    }
    
    public String getText() {
        return text;
    }
    
    public PDFont getFont() {
        return font;
    }
    
    public int getFontSize() {
        return fontSize;
    }
    
}
