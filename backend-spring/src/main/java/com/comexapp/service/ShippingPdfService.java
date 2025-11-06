package com.comexapp.service;

import com.comexapp.DTO.ShippingInstructionDTO;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class ShippingPdfService {

    public byte[] gerarPdf(ShippingInstructionDTO si) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            // OpenPDF usa Font.HELVETICA em vez de FontFamily.HELVETICA
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font normal    = new Font(Font.HELVETICA, 12, Font.NORMAL);

            Paragraph title = new Paragraph("Shipping Instruction (" + si.getTipo().toUpperCase() + ")", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            document.add(new Paragraph("Origem: " + si.getOrigem(), normal));
            document.add(new Paragraph("Destino: " + si.getDestino(), normal));
            document.add(new Paragraph("Shipper: " + si.getShipper(), normal));
            document.add(new Paragraph("Consignee: " + si.getConsignee(), normal));
            document.add(new Paragraph("Agente: " + si.getAgente(), normal));
            document.add(new Paragraph("Número House: " + si.getNumeroHouse(), normal));
            document.add(new Paragraph("Número Master: " + si.getNumeroMaster(), normal));
            document.add(new Paragraph("Cia Aérea / Armador: " + si.getCiaAerea(), normal));
            document.add(new Paragraph("Ref Cliente: " + si.getRefCliente(), normal));
            document.add(new Paragraph("Incoterm: " + si.getIncoterm(), normal));
            document.add(new Paragraph("ETD: " + si.getEtd(), normal));
            document.add(new Paragraph("ETA: " + si.getEta(), normal));

            document.add(new Paragraph("\nGerado automaticamente pelo sistema MousTrack.", normal));

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
