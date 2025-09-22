import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of submissions to simulate: ");
        int requests;
        try {
            requests = Integer.parseInt(scanner.nextLine());
            if (requests <= 0) {
                System.out.println("Invalid number. Using default = 300.");
                requests = 300;
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Using default = 300.");
            requests = 300;
        }

        System.out.print("Use fixed seed or random? (type 'fixed' or 'random'): ");
        String mode = scanner.nextLine().trim().toLowerCase();

        Random rng;
        Long seed = null;

        if ("fixed".equals(mode)) {
            System.out.print("Enter seed number (or press Enter for default 42): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                seed = 42L;
            } else {
                try {
                    seed = Long.parseLong(input);
                } catch (Exception e) {
                    System.out.println("Invalid seed. Using default = 42.");
                    seed = 42L;
                }
            }
            rng = new Random(seed);
        } else {
            System.out.println("Using RANDOM seed (system clock).");
            rng = new Random();
        }

        System.out.println("\n=== Madrasati Reliability Simulation ===");
        System.out.println("Requests = " + requests);
        System.out.println("Seed     = " + (seed == null ? "RANDOM" : seed));

        Validator validator = new Validator();
        FaultInjector injector = new FaultInjector(rng);
        Metrics metrics = new Metrics();
        SubmissionService service = new SubmissionService(injector, validator, metrics, rng);

        for (int i = 1; i <= requests; i++) {
            Submission s = Submission.randomSubmission(i, rng);
            service.process(s);
        }

        System.out.println(metrics.summary());

        scanner.close();
    }
}
