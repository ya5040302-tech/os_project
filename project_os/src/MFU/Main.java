import java.util.*;

// 1. Logic Class: Handles the MFU Algorithm
class MFUEngine {
    private final int capacity;
    private final List<Integer> frames;
    private final Map<Integer, Integer> frequencyMap;
    private final Map<Integer, Integer> arrivalTimeMap;
    private int pageFaults;
    private int timer;

    public MFUEngine(int capacity) {
        this.capacity = capacity;
        this.frames = new ArrayList<>();
        this.frequencyMap = new HashMap<>();
        this.arrivalTimeMap = new HashMap<>();
        this.pageFaults = 0;
        this.timer = 0;
    }

    public void runSimulation(int[] pages) {
        System.out.println("\nRef | Frames (In Memory)       | Status");
        System.out.println("---------------------------------------");

        for (int page : pages) {
            timer++;
            String status;

            if (frames.contains(page)) {
                status = "Hit";
                frequencyMap.put(page, frequencyMap.get(page) + 1);
            } else {
                status = "Fault";
                pageFaults++;

                if (frames.size() < capacity) {
                    frames.add(page);
                } else {
                    int victimIndex = findVictimIndex();
                    int removedPage = frames.get(victimIndex);

                    // Reset stats for the evicted page (Frequency resets on re-entry)
                    frequencyMap.remove(removedPage);
                    arrivalTimeMap.remove(removedPage);

                    // Replace at the specific frame index
                    frames.set(victimIndex, page);
                }
                frequencyMap.put(page, 1);
                arrivalTimeMap.put(page, timer);
            }
            displayRow(page, status);
        }
        displaySummary(pages.length);
    }

    private int findVictimIndex() {
        int victimIdx = 0;
        int maxFreq = -1;

        for (int i = 0; i < frames.size(); i++) {
            int currentPage = frames.get(i);
            int currentFreq = frequencyMap.get(currentPage);

            if (currentFreq > maxFreq) {
                maxFreq = currentFreq;
                victimIdx = i;
            }
            // FIFO Tie-breaker: If frequencies are tied, replace the oldest one
            else if (currentFreq == maxFreq) {
                if (arrivalTimeMap.get(currentPage) < arrivalTimeMap.get(frames.get(victimIdx))) {
                    victimIdx = i;
                }
            }
        }
        return victimIdx;
    }

    private void displayRow(int page, String status) {
        System.out.printf("%-3d | %-24s | %s\n", page, frames.toString(), status);
    }

    private void displaySummary(int totalPages) {
        double faultRate = (totalPages == 0) ? 0 : ((double) pageFaults / totalPages) * 100;
        System.out.println("---------------------------------------");
        System.out.println("Total Hits: " + (totalPages - pageFaults));
        System.out.println("Total Page Faults: " + pageFaults);
        System.out.printf("Fault Rate: %.2f%%\n", faultRate);
    }
}

// 2. Utility Class: Handles Robust Input Validation
class InputHandler {
    private final Scanner scanner = new Scanner(System.in);

    public int getValidInt(String prompt, boolean allowZero) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.next();
            try {
                int value = Integer.parseInt(input);
                if (value < 0) {
                    System.out.println("Error: Negative numbers are not allowed.");
                } else if (!allowZero && value == 0) {
                    System.out.println("Error: Value must be greater than 0.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: '" + input + "' is not a valid number. Please enter a whole number.");
            }
        }
    }
}

// 3. Main Class: Orchestrates the Program
public class Main {
    public static void main(String[] args) {
        InputHandler input = new InputHandler();
        System.out.println("--- Virtual Memory MFU Simulator (Class-Based) ---");

        int capacity = input.getValidInt("Enter the number of frames: ", false);
        int n = input.getValidInt("Enter the number of page references: ", false);

        int[] referenceString = new int[n];
        System.out.println("Enter page reference sequence:");
        for (int i = 0; i < n; i++) {
            referenceString[i] = input.getValidInt("Page " + (i + 1) + ": ", true);
        }

        MFUEngine engine = new MFUEngine(capacity);
        engine.runSimulation(referenceString);
    }
}