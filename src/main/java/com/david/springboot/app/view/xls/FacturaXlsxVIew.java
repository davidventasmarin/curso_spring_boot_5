package com.david.springboot.app.view.xls;


import com.david.springboot.app.models.entity.Factura;
import com.david.springboot.app.models.entity.ItemFactura;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component("factura/ver.xlsx")
public class FacturaXlsxVIew extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachment; filename=\"factura_view.xlsx\"");
        Factura factura = (Factura) map.get("factura");
        Sheet sheet = workbook.createSheet("Factura Spring");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);

        cell.setCellValue("Datos del Cliente");
        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue(factura.getCliente().getNombre() + " " + factura.getCliente().getApellidos());

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue(factura.getCliente().getEmail());

        // Otra forma de obtener las filas. Una forma sería creando el objeto celda y fila, y otra como aquí hacerlo directamente
        sheet.createRow(4).createCell(0).setCellValue("Datos Factura");
        sheet.createRow(5).createCell(0).setCellValue("Folio: "+ factura.getId());
        sheet.createRow(6).createCell(0).setCellValue("Descripción: "+ factura.getDescripcion());
        sheet.createRow(7).createCell(0).setCellValue("Fecha: " +  factura.getCreateAt());

        CellStyle theaderStyle = workbook.createCellStyle();
        theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
        theaderStyle.setBorderTop(BorderStyle.MEDIUM);
        theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
        theaderStyle.setBorderRight(BorderStyle.MEDIUM);
        theaderStyle.setFillForegroundColor(IndexedColors.GOLD.index);
        theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle tbodyStyle = workbook.createCellStyle();
        tbodyStyle.setBorderBottom(BorderStyle.THIN);
        tbodyStyle.setBorderTop(BorderStyle.THIN);
        tbodyStyle.setBorderLeft(BorderStyle.THIN);
        tbodyStyle.setBorderRight(BorderStyle.THIN);


        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue("Producto");
        header.createCell(1).setCellValue("Precio");
        header.createCell(2).setCellValue("Cantidad");
        header.createCell(3).setCellValue("Total");

        header.getCell(0).setCellStyle(theaderStyle);
        header.getCell(1).setCellStyle(theaderStyle);
        header.getCell(2).setCellStyle(theaderStyle);
        header.getCell(3).setCellStyle(theaderStyle);

        int rownum = 10;
        for(ItemFactura item: factura.getItems()){
            Row fila = sheet.createRow(rownum ++);
            cell = fila.createCell(0);
            cell.setCellValue(item.getProducto().getNombre());
            cell.setCellStyle(tbodyStyle);

            cell = fila.createCell(1);
            fila.createCell(1).setCellValue(item.getProducto().getPrecio());
            cell.setCellStyle(tbodyStyle);

            cell = fila.createCell(2);
            fila.createCell(2).setCellValue(item.getCantidad());
            cell.setCellStyle(tbodyStyle);

            cell = fila.createCell(3);
            fila.createCell(3).setCellValue(item.calcularImporte());
            cell.setCellStyle(tbodyStyle);
        }

        Row filatotal = sheet.createRow(rownum);
        cell = filatotal.createCell(2);
        cell.setCellValue("Gran Total: ");
        cell.setCellStyle(tbodyStyle);

        cell = filatotal.createCell(3);
        cell.setCellValue(factura.getTotal());
        cell.setCellStyle(tbodyStyle);
    }
}
