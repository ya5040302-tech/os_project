package LFU;

class DisplayManager {

    public void printHeader() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    LFU PAGE REPLACEMENT ALGORITHM SIMULATOR");
        System.out.println("=".repeat(50) + "\n");
    }

    public void printSimulationStart() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("         SIMULATION STARTED");
        System.out.println("-".repeat(50) + "\n");
    }

    public void printPageReferenceString(int[] pages) {
        System.out.print("LFU.Page Reference String: ");
        for (int i = 0; i < pages.length; i++) {
            System.out.print(pages[i]);
            if (i < pages.length - 1) System.out.print(" → ");
        }
        System.out.println("\n");
    }
}