public class Configs {
    private String TEST_PDF_FILE = "\\src\\res\\pdfs\\table1.pdf";
    private String TEST_OUTPUT_FILE = "\\src\\res\\outputs\\table1.xls";
    public static double LIMIT = 10.1;

    public String getTEST_PDF_FILE() {
        return System.getProperty("user.dir")+TEST_PDF_FILE;
    }
    public String getTEST_OUTPUT_FILE() {
        return System.getProperty("user.dir")+TEST_OUTPUT_FILE;
    }
}