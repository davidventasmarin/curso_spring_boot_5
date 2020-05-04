package com.david.springboot.app.view.pdf;


import com.david.springboot.app.models.entity.Factura;
import com.david.springboot.app.models.entity.ItemFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.tomcat.util.descriptor.LocalResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Locale;
import java.util.Map;


@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    protected void buildPdfDocument(Map<String, Object> map, Document document, PdfWriter pdfWriter,
                                    HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {

        Factura factura = (Factura) map.get("factura");

        Locale locale = localeResolver.resolveLocale(request);

        PdfPTable tablaCliente = new PdfPTable(1);
        tablaCliente.setSpacingAfter(20);
        PdfPCell cell = null;


        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.login.success", null, locale)));
        cell.setBackgroundColor(new Color(184, 218, 255));
        cell.setPadding(8f);
        tablaCliente.addCell(cell);

        tablaCliente.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellidos());
        tablaCliente.addCell(factura.getCliente().getEmail());

        PdfPTable tablaFactura = new PdfPTable(1);
        tablaFactura.setSpacingAfter(20);
        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.login.success", null, locale)));
        cell.setBackgroundColor(new Color(195, 230, 203));
        cell.setPadding(8f);
        tablaFactura.addCell(cell);
        tablaFactura.addCell("Folio: " + factura.getId());
        tablaFactura.addCell("Descripción: " + factura.getDescripcion());
        tablaFactura.addCell("Fecha " + factura.getCreateAt());

        document.add(tablaCliente);
        document.add(tablaFactura);

        PdfPTable tablaProducto = new PdfPTable(4);
        tablaProducto.setWidths(new float [] {3.5f, 1, 1, 1});
        tablaProducto.addCell("Producto " );
        tablaProducto.addCell("Precio ");
        tablaProducto.addCell("Cantidad ");
        tablaProducto.addCell("Total ");

        for(ItemFactura item: factura.getItems()){
            tablaProducto.addCell(item.getProducto().getNombre());
            tablaProducto.addCell(item.getProducto().getPrecio().toString());

            cell = new PdfPCell((new Phrase(item.getCantidad().toString())));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            tablaProducto.addCell(cell);
            tablaProducto.addCell(item.calcularImporte().toString());
        }

        cell = new PdfPCell(new Phrase("Total: "));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tablaProducto.addCell(cell);
        tablaProducto.addCell(factura.getTotal().toString());

        document.add(tablaProducto);
    }
}
