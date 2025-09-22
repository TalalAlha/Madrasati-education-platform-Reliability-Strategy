import java.util.Random;

public class Submission {
    public final int id;
    public final int sizeMB;
    public final String ext;

    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 50;

    private static final String[] ALLOWED = {".pdf", ".docx", ".zip", ".jpg", ".jpeg", ".png"};
    private static final String[] BAD = {".exe", ".bat", ".rar"};

    private static double INVALID_RATE = 0.02; 

    public static void setInvalidRate(double rate) {
        INVALID_RATE = Math.max(0.0, Math.min(1.0, rate));
    }

    public Submission(int id, int sizeMB, String ext) {
        this.id = id;
        this.sizeMB = sizeMB;
        this.ext = ext;
    }

    public static Submission randomSubmission(int id, Random rng) {
        int size = MIN_SIZE + rng.nextInt(MAX_SIZE - MIN_SIZE + 1);

        boolean good = rng.nextDouble() >= INVALID_RATE; 
        String ext = good ? ALLOWED[rng.nextInt(ALLOWED.length)]
                          : BAD[rng.nextInt(BAD.length)];

        return new Submission(id, size, ext);
    }

    @Override
    public String toString() {
        return "Submission#" + id + " (" + sizeMB + "MB, " + ext + ")";
    }
}
