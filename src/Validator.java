public class Validator {
    public void validate(Submission s) throws Exception {
        if (s == null) throw new Exception("Null submission.");
        if (s.sizeMB < 1 || s.sizeMB > 50) {
            throw new Exception("Invalid size: " + s.sizeMB + "MB");
        }

        String e = s.ext == null ? "" : s.ext.trim().toLowerCase();

        boolean ok =
            e.equals(".pdf")  ||
            e.equals(".docx") ||
            e.equals(".zip")  ||
            e.equals(".jpg")  ||
            e.equals(".jpeg") ||
            e.equals(".png");

        if (!ok) throw new Exception("Invalid extension: " + s.ext);
    }
}
