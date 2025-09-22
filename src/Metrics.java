public class Metrics {
    private int total = 0;
    private int success = 0;

    private int userErrors = 0;      
    private int systemFailures = 0;  

    private long clockMs = 0;
    private long uptimeMs = 0;
    private long downtimeMs = 0;
    private static final int REPAIR_MS = 200; 

    public long now() {
        clockMs += 10;
        uptimeMs += 10;
        return clockMs;
    }

    public void recordSuccess(long t, String msg) {
        total++;
        success++;
    }

    public void recordUserError(long t, String msg) {
        total++;
        userErrors++;
    }

    public void recordSystemFailure(long t, String msg) {
        total++;
        systemFailures++;
        downtimeMs += REPAIR_MS;
        if (uptimeMs >= REPAIR_MS) uptimeMs -= REPAIR_MS;
    }

    public double getPOFOD() {
        return total == 0 ? 0.0 : (double) systemFailures / total;
    }

    public double getROCOF() {
        return getPOFOD();
    }

    public double getAvailability() {
        long denom = uptimeMs + downtimeMs;
        return denom == 0 ? 1.0 : (double) uptimeMs / denom;
    }

    public int getTotalRequests()   { return total; }
    public int getTotalSuccess()    { return success; }
    public int getUserErrors()      { return userErrors; }
    public int getSystemFailures()  { return systemFailures; }

    public String summary() {
        return "\n=== Metrics ===\n"
             + "Requests: " + total + "\n"
             + "Success: " + success + "\n"
             + "User Errors (invalid): " + userErrors + "\n"
             + "System Failures: " + systemFailures + "\n"
             + String.format("POFOD (system-only): %.4f%n", getPOFOD())
             + String.format("ROCOF (system-only): %.4f%n", getROCOF())
             + String.format("Availability: %.5f%%%n", getAvailability() * 100.0);
    }
}
