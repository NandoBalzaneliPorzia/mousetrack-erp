package com.comexapp.service;

import com.comexapp.DTO.ShippingInstructionDTO;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class ShippingWordService {

    public byte[] gerarDocx(ShippingInstructionDTO si) {
        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // === TÍTULO ===
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("Shipping Instruction (" + si.getTipo().toUpperCase() + ")");
            titleRun.setBold(true);
            titleRun.setFontSize(16);
            titleRun.addBreak();

            // === SEÇÃO DE INFORMAÇÕES ===
            XWPFTable table = document.createTable();
            table.setWidth("100%");

            addRow(table, "Origem", si.getOrigem());
            addRow(table, "Destino", si.getDestino());
            addRow(table, "Shipper", si.getShipper());
            addRow(table, "Consignee", si.getConsignee());
            addRow(table, "Agente", si.getAgente());
            addRow(table, "Número House", si.getNumeroHouse());
            addRow(table, "Número Master", si.getNumeroMaster());
            addRow(table, "Cia Aérea / Armador", si.getCiaAerea());
            addRow(table, "Ref Cliente", si.getRefCliente());
            addRow(table, "Incoterm", si.getIncoterm());
            addRow(table, "ETD", si.getEtd());
            addRow(table, "ETA", si.getEta());

            // === RODAPÉ ===
            XWPFParagraph footer = document.createParagraph();
            footer.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun footerRun = footer.createRun();
            footerRun.addBreak();
            footerRun.setText("Gerado automaticamente pelo sistema Mouss&Track.");
            footerRun.setFontSize(10);
            footerRun.setItalic(true);

            // === FINALIZA ===
            document.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addRow(XWPFTable table, String label, String value) {
        XWPFTableRow row = table.createRow();
        row.getCell(0).setText(label);
        row.getCell(1).setText(value != null ? value : "");
    }
}
