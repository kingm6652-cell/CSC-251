import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** New integration: quoted UTF-8 CSV reports for payments and the items actually sold. */
public final class ReportExporter_Kilpatrick {
    private ReportExporter_Kilpatrick() { }
    private static void row(Writer writer, Object... values) throws IOException {
        for (int i=0;i<values.length;i++) {
            if (i>0) writer.write(",");
            String text=String.valueOf(values[i]);
            // Keep entered names from being interpreted as spreadsheet formulas.
            if(values[i] instanceof String && text.matches("(?s)^\\s*[=+@-].*")) text="'"+text;
            writer.write("\""+text.replace("\"","\"\"")+"\"");
        }
        writer.write("\r\n");
    }
    public static void payments(Path file, FarmData_Kilpatrick data) throws IOException {
        try(Writer writer=Files.newBufferedWriter(file,StandardCharsets.UTF_8)) {
            row(writer,"Payment ID","Type","Sale or appointment ID","Customer","Amount USD","Method","Date/time");
            for(Payment_Kilpatrick p:data.payments)
                row(writer,p.id,p.type,p.referenceId,p.customer,java.math.BigDecimal.valueOf(p.amountCents,2),p.method,p.dateTime);
        }
    }
    public static void sales(Path file, FarmData_Kilpatrick data) throws IOException {
        try(Writer writer=Files.newBufferedWriter(file,StandardCharsets.UTF_8)) {
            row(writer,"Sale ID","Type","Date/time","Customer","Method","Item ID","Item","Quantity","Unit price USD","Line total USD");
            for(StoreSale_Kilpatrick sale:data.sales.getSalesHistory()) for(SaleLineItem_Kilpatrick line:sale.getLineItems())
                row(writer,sale.getId(),sale.getType(),sale.getSaleDateTime(),sale.getCustomer(),sale.getMethod(),line.getItemId(),line.getItemName(),
                        line.getQuantitySold(),java.math.BigDecimal.valueOf(line.getUnitPriceCents(),2),java.math.BigDecimal.valueOf(line.getLineTotalCents(),2));
        }
    }
}
