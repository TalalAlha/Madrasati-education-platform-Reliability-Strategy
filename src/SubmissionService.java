import java.util.Random;

public class SubmissionService {
    private final FaultInjector injector;
    private final Validator validator;
    private final Metrics metrics;
    private final Random rng;

    private static final int MAX_RETRIES = 1;

    public SubmissionService(FaultInjector injector, Validator validator, Metrics metrics, Random rng) {
        this.injector = injector;
        this.validator = validator;
        this.metrics = metrics;
        this.rng = rng;
    }

    public boolean process(Submission s) {
        long start = metrics.now();

        try {
            validator.validate(s);
        } catch (Exception e) {
            metrics.recordUserError(start, "Invalid: " + e.getMessage());
            return false;
        }

        String lastPrimaryErr = null;
        for (int attempt = 0; attempt <= MAX_RETRIES; attempt++) {
            try {
                injector.maybeThrowPrimary();
                metrics.recordSuccess(start, "primary: attempt=" + (attempt + 1));
                return true;
            } catch (Exception e) {
                lastPrimaryErr = e.getMessage();
            }
        }

        String lastBackupErr = null;
        for (int attempt = 0; attempt <= MAX_RETRIES; attempt++) {
            try {
                injector.maybeThrowBackup();
                metrics.recordSuccess(start, "backup: attempt=" + (attempt + 1));
                return true;
            } catch (Exception e) {
                lastBackupErr = e.getMessage();
            }
        }

        String reason = "primary: " + lastPrimaryErr + " | backup: " + lastBackupErr;
        metrics.recordSystemFailure(start, reason);
        return false;
    }
}
