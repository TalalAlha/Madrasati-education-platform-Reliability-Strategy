import java.util.Random;

public class FaultInjector {
    private final Random rng;

    private static final double P_FAIL_PERM_PRIMARY = 0.002;  
    private static final double P_TIMEOUT_PRIMARY   = 0.03;   
    private static final double P_TRANSIENT_PRIMARY = 0.03;   

    private static final double P_FAIL_PERM_BACKUP  = 0.001;  
    private static final double P_TIMEOUT_BACKUP    = 0.015;  
    private static final double P_TRANSIENT_BACKUP  = 0.002;  

    private static final double P_BURST_PRIMARY = 0.01; 
    private static final int    BURST_LEN_PRIMARY = 3;  

    private static final double P_BURST_BACKUP  = 0.005;
    private static final int    BURST_LEN_BACKUP  = 2;   
    private int primaryOutageRemaining = 0;
    private int backupOutageRemaining  = 0;

    public FaultInjector(Random rng) {
        this.rng = rng;
    }

    public void maybeThrowPrimary() throws Exception {
        if (primaryOutageRemaining > 0) {
            primaryOutageRemaining--;
            throw new Exception("Primary Timeout (burst)");
        }

        if (rng.nextDouble() < P_BURST_PRIMARY) {
            primaryOutageRemaining = BURST_LEN_PRIMARY - 1; 
            throw new Exception("Primary Timeout (burst start)");
        }

        double r = rng.nextDouble();
        double tPerm = P_FAIL_PERM_PRIMARY;                      
        double tTout = tPerm + P_TIMEOUT_PRIMARY;                
        double tTran = tTout + P_TRANSIENT_PRIMARY;             

        if (r < tPerm) {
            throw new Exception("Primary Permanent Failure");
        } else if (r < tTout) {
            throw new Exception("Primary Timeout");
        } else if (r < tTran) {
            throw new Exception("Primary Transient Failure");
        }
      
    }

    public void maybeThrowBackup() throws Exception {
        if (backupOutageRemaining > 0) {
            backupOutageRemaining--;
            throw new Exception("Backup Timeout (burst)");
        }

        if (rng.nextDouble() < P_BURST_BACKUP) {
            backupOutageRemaining = BURST_LEN_BACKUP - 1;
            throw new Exception("Backup Timeout (burst start)");
        }

        double r = rng.nextDouble();
        double tPerm = P_FAIL_PERM_BACKUP;                       
        double tTout = tPerm + P_TIMEOUT_BACKUP;                
        double tTran = tTout + P_TRANSIENT_BACKUP;               

        if (r < tPerm) {
            throw new Exception("Backup Permanent Failure");
        } else if (r < tTout) {
            throw new Exception("Backup Timeout");
        } else if (r < tTran) {
            throw new Exception("Backup Transient Failure");
        }
    }
}
