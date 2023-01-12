package com.app.other;

import com.app.DTO.ProductDTO;
import com.app.DTO.StorageDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.stream.Stream;

public  class CreatePDF {

    public static void createPdf(StorageDTO storageDTO, String from, String to) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(String.format("From_%s_to_%s.pdf",from,to)));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        document.open();




        PdfPTable table = new PdfPTable(7);
        addTableHeader(table);
        for (int i = 0; i < storageDTO.getProducts().size(); i++) {
            addRows(table, storageDTO.getProducts().get(i));
        }
        try {

            document.add(table);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        document.close();
    }
    private static void addTableHeader(PdfPTable table) {
        Stream.of("Note", "Name", "Price","Amount","Date of manufacture","Date of expiration","Category")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }
    private static void addRows(PdfPTable table, ProductDTO productDTO) {
        
            table.addCell(productDTO.getNote());
            table.addCell(productDTO.getName());
            table.addCell(productDTO.getPrice()+"");
            table.addCell(productDTO.getAmount()+"");
            table.addCell(productDTO.getDateOfManufacture());
            table.addCell(productDTO.getDateOfExpiration());
            table.addCell(productDTO.getCategory());


        

    }

}
