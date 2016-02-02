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
import java.util.List;


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

import java.io.Serializable;
import java.util.Date;

import java.awt.GraphicsEnvironment;
import java.awt.FontFormatException;


import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.edit.*;
import org.apache.pdfbox.exceptions.*;
import org.apache.pdfbox.pdmodel.common.*;



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
            
            
            if ((Integer)notebookRequest.get(1) == 0) { // aka if unformatted notebook
                response.setHeader("Content-Type", "text/plain");
                
                PrintWriter writer = response.getWriter();
                String resultNotebook = exportNotes((String)notebookRequest.get(0), (Integer)notebookRequest.get(2), (ArrayList<Note>)notebookRequest.get(3));
                writer.write(resultNotebook);
                writer.flush();
                
            }
            else { // aka if formatted (pdf) notebook
                
                // generates the notebook
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                output = createPDF((String)notebookRequest.get(0), (Integer)notebookRequest.get(1), (Integer)notebookRequest.get(2), (ArrayList<String>)notebookRequest.get(3), (ArrayList<String>)notebookRequest.get(4), (ArrayList<Integer>)notebookRequest.get(5), (int[][])notebookRequest.get(6)); // THIS CALL NEEDS FIXING
                
                response.setHeader("Content-Type", "application/pdf");
                response.setContentLength(output.size());
                
                // sends the data back to the client
                ServletOutputStream out = response.getOutputStream();
                output.writeTo(out);
                out.flush();
            
            }
            
            
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
    
    public ByteArrayOutputStream createPDF(String notebookTitle, int notebookType, int noteSelection, ArrayList<String> noteNames, ArrayList<String> noteContents, ArrayList<Integer> noteLocations, int[][] weekColors) throws IOException, COSVisitorException {
        
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        PDDocument doc = null;
        try
        {
            doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(doc, page);
            
            PDFont pdfFont = PDType1Font.HELVETICA;
            float fontSize = 25;
            float leading = 1.5f * fontSize;
            
            PDRectangle mediabox = page.findMediaBox();
            float margin = 72;
            float width = mediabox.getWidth() - 2*margin;
            float startX = mediabox.getLowerLeftX() + margin;
            float startY = mediabox.getUpperRightY() - margin;
            
            String text = "This is some sample text in a pdf document. Lorem ipsum!";
            List<String> lines = new ArrayList<String>();
            int lastSpace = -1;
            while (text.length() > 0)
            {
                int spaceIndex = text.indexOf(' ', lastSpace + 1);
                if (spaceIndex < 0)
                    spaceIndex = text.length();
                String subString = text.substring(0, spaceIndex);
                float size = fontSize * pdfFont.getStringWidth(subString) / 1000;
                System.out.printf("'%s' - %f of %f\n", subString, size, width);
                if (size > width)
                {
                    if (lastSpace < 0)
                        lastSpace = spaceIndex;
                    subString = text.substring(0, lastSpace);
                    lines.add(subString);
                    text = text.substring(lastSpace).trim();
                    System.out.printf("'%s' is line\n", subString);
                    lastSpace = -1;
                }
                else if (spaceIndex == text.length())
                {
                    lines.add(text);
                    System.out.printf("'%s' is line\n", text);
                    text = "";
                }
                else
                {
                    lastSpace = spaceIndex;
                }
            }
            
            contentStream.beginText();
            contentStream.setFont(pdfFont, fontSize);
            contentStream.moveTextPositionByAmount(startX, startY);            
            for (String line: lines)
            {
                contentStream.drawString(line);
                contentStream.moveTextPositionByAmount(0, -leading);
            }
            contentStream.endText(); 
            contentStream.close();
            
            doc.save(output);
        }
        finally
        {
            if (doc != null)
            {
                doc.close();
            }
        }
        
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
    

    public String exportNotes(String notebookTitle, int noteSelection, ArrayList<Note> notes) {
        
                    String result;
                    result = notebookTitle;
                    result += "\n\n";
        
                    for (Note note : notes) {
                
                        result += note.name + "\n\n" + note.content + "\n\n";
                        
                    
                    }
        
                    
        return result;
        
    }
    
}


class Note implements Serializable {
    
    public String name;
    public String content;
    public int location;
    public Date lastEdit;
    
    public Note(String name, String content, int location, Date lastEdit) {
        this.name = name;
        this.content = content;
        this.location = location;
        this.lastEdit = lastEdit;
        
    }
}
