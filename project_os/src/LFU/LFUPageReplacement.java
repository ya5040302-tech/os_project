package LFU;

import utils.InputHandler;

import java.util.*;

public class LFUPageReplacement {
    public static void main(String[] args) {
        DisplayManager display = new DisplayManager();
        InputHandler input = new InputHandler();

        // Display header
        display.printHeader();

        // Get user input
        int frames = input.getFrameCount();
        int[] pages = input.getPageReferenceString();
//        int[] pages = {1,2,3,4, 7, 0,1,2,0,3,0, 4, 2, 3,0, 3,0, 3,2,
//                1, 2,0,1, 7,0, 1};
//        int[] pages = { 1, 2, 3, 4, 2, 1, 5 };
//        int[] pages = { 5, 0, 1, 3, 2, 4, 1, 0, 5 };
//        int frames = 4;
        display.printPageReferenceString(pages);

        int pagefaults = pageFaults(pages.length - 1, frames, pages);

        System.out.println("Page Faults = "
                + pagefaults);
        System.out.println("Page Hits = "
                + (pages.length - pagefaults));
        double faultRate = (pagefaults * 100.0) / pages.length;
        System.out.println("Fault Rate = " + faultRate + "%");
    }
    static int pageFaults(int n, int c, int[] pages) {
        int faults = 0;

        List<Integer> memory = new ArrayList<>();
        Map<Integer, Integer> freq = new HashMap<>();

        System.out.println("\n--- LFU Simulation Steps ---\n");

        for (int i = 0; i <= n; i++) {
            int currentPage = pages[i];
            boolean isFault = false;

            int idx = memory.indexOf(currentPage);

            // PAGE FAULT
            if (idx == -1) {
                isFault = true;

                if (memory.size() == c) {
                    memory.remove(0);
                }

                memory.add(currentPage);
                freq.put(currentPage, freq.getOrDefault(currentPage, 0) + 1);
                faults++;
            }
            // PAGE HIT
            else {
                freq.put(currentPage, freq.get(currentPage) + 1);
            }

            int k = memory.size() - 2;
            while (k >= 0 && freq.get(memory.get(k)) > freq.get(memory.get(k + 1))) {
                Collections.swap(memory, k, k + 1);
                k--;
            }

            // 🔹 PRINT STEP DETAILS
            System.out.println("Page: " + currentPage +
                    (isFault ? " → FAULT" : " → HIT"));

            System.out.println("Memory: " + memory);

            System.out.print("Frequencies: { ");
            for (int p : memory) {
                System.out.print(p + "=" + freq.get(p) + " ");
            }
            System.out.println("}");

            System.out.println("----------------------------------");
        }

        return faults;
    }
}
