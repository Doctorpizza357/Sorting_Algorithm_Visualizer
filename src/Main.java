import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Main extends JFrame {

    private int arraySize = 10;
    private int[] array;
    private int highlightedIndex1 = -1;
    private int highlightedIndex2 = -1;

    private final JButton generateButton;
    private final JButton sortButton;
    private final JButton quickSortButton;
    private final JButton compareAlgorithmsButton;
    private final JPanel barPanel;
    private final JSlider arraySizeSlider;
    private final JSlider delaySlider;
    private int delay = 400;

    private Timer bubbleSortTimer;
    private Timer quickSortTimer;

    public Main() {
        array = new int[arraySize];

        generateButton = new JButton("Generate Bars");
        generateButton.addActionListener(e -> generateRandomBars());

        sortButton = new JButton("Sort Bars (Bubble Sort)");
        sortButton.addActionListener(e -> startBubbleSort());

        quickSortButton = new JButton("Sort Bars (Quick Sort)");
        quickSortButton.addActionListener(e -> startQuickSort());

        compareAlgorithmsButton = new JButton("Compare");
        compareAlgorithmsButton.addActionListener(e -> compareAndDisplayAlgorithms());

        arraySizeSlider = new JSlider(JSlider.HORIZONTAL, 5, 100, arraySize);
        arraySizeSlider.setMajorTickSpacing(5);
        arraySizeSlider.setPaintTicks(true);
        //arraySizeSlider.setPaintLabels(true);
        arraySizeSlider.addChangeListener(e -> {
            arraySize = arraySizeSlider.getValue();
            array = new int[arraySize];
            highlightedIndex1 = -1;
            highlightedIndex2 = -1;
            generateRandomBars();
        });

        delaySlider = new JSlider(JSlider.HORIZONTAL, 1, 500, 400);
        delaySlider.setMajorTickSpacing(5);
        delaySlider.setPaintTicks(true);
        //delaySlider.setPaintLabels(true);
        delaySlider.addChangeListener(e -> delay = delaySlider.getValue());

        barPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBars(g);
            }
        };

        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(generateButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(quickSortButton);
        buttonPanel.add(compareAlgorithmsButton);

        JPanel sliderPanel = new JPanel(new FlowLayout()); // Changed layout to FlowLayout
        sliderPanel.add(new JLabel("Number of Bars: "));
        sliderPanel.add(arraySizeSlider);
        sliderPanel.add(new JLabel("Delay: "));
        sliderPanel.add(delaySlider);

        add(buttonPanel, BorderLayout.NORTH);
        add(sliderPanel, BorderLayout.SOUTH);
        add(barPanel, BorderLayout.CENTER);

        setTitle("Sorting Visualizer");
        setSize(650, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        generateRandomBars();
    }

    private void generateRandomBars() {
        Random random = new Random();
        for (int i = 0; i < arraySize; i++) {
            array[i] = random.nextInt(100) + 1;
        }
        highlightedIndex1 = -1;
        highlightedIndex2 = -1;
        repaint();
    }

    private void compareAndDisplayAlgorithms() {
        compareAlgorithms();
        SwingUtilities.invokeLater(Main::compareAlgorithms);
    }

    static void compareAlgorithms() {
        JFrame frame = new JFrame("Time Complexity Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries bubbleSortSeries = new XYSeries("Bubble Sort (O(n^2))");
        XYSeries quickSortSeries = new XYSeries("Quick Sort (O(n log n))");

        for (int n = 1; n <= 100; n++) {
            bubbleSortSeries.add(n, n * n);
            quickSortSeries.add(n, n * Math.log(n));
        }

        dataset.addSeries(bubbleSortSeries);
        dataset.addSeries(quickSortSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Time Complexity Comparison",
                "Input Size (n)",
                "Time Complexity",
                dataset,
                PlotOrientation.HORIZONTAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
        plot.setRangePannable(true);
        plot.setRangeGridlinesVisible(false);

        ChartPanel chartPanel = new ChartPanel(chart);
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void drawBars(Graphics g) {
        int maxBarHeight = barPanel.getHeight();
        int barWidth = barPanel.getWidth() / arraySize;

        for (int i = 0; i < arraySize; i++) {
            int barHeight = array[i] * maxBarHeight / 100;

            Color barColor;
            if (i == highlightedIndex1 || i == highlightedIndex2) {
                barColor = new Color(255, 0, 0);  // Red for highlighted bars
            } else {
                // Use the HSB color model to create rainbow colors
                float hue = (float) i / arraySize;
                barColor = Color.getHSBColor(hue, 1.0f, 1.0f);
            }

            g.setColor(barColor);
            g.fillRect(i * barWidth, maxBarHeight - barHeight, barWidth, barHeight);
        }
    }

    private void startBubbleSort() {
        if (bubbleSortTimer != null && bubbleSortTimer.isRunning()) {
            return;
        }

        bubbleSortTimer = new Timer(delay, new ActionListener() {
            private int i = 0;
            private int j = 0;
            private boolean swapped = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (i < arraySize - 1) {
                    if (j < arraySize - i - 1) {
                        if (!swapped) {
                            highlightedIndex1 = j;
                            highlightedIndex2 = j + 1;
                            repaint();
                        }

                        if (array[j] > array[j + 1]) {
                            int temp = array[j];
                            array[j] = array[j + 1];
                            array[j + 1] = temp;
                            swapped = true;
                            repaint();
                        } else {
                            swapped = false;
                        }

                        j++;
                    } else {
                        highlightedIndex1 = -1;
                        highlightedIndex2 = -1;
                        j = 0;
                        i++;
                    }
                } else {
                    highlightedIndex1 = -1;
                    highlightedIndex2 = -1;
                    bubbleSortTimer.stop();
                    enableButtons();
                }
            }
        });

        bubbleSortTimer.start();
        disableButtons();
    }

    private void startQuickSort() {
        if (quickSortTimer != null && quickSortTimer.isRunning()) {
            return;
        }

        quickSortTimer = new Timer(delay, new ActionListener() {
            private int[] stack;
            private int top = -1;

            private void push(int low, int high) {
                top++;
                stack[top * 2] = low;
                stack[top * 2 + 1] = high;
            }

            private void pop() {
                top--;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (stack == null) {
                    stack = new int[arraySize * 2];
                    push(0, arraySize - 1);
                    return;
                }

                if (top >= 0) {
                    int high = stack[top * 2 + 1];
                    int low = stack[top * 2];
                    pop();

                    int pivot = partition(low, high);

                    if (pivot - 1 > low) {
                        push(low, pivot - 1);
                    }

                    if (pivot + 1 < high) {
                        push(pivot + 1, high);
                    }

                    highlightedIndex1 = pivot;
                    repaint();
                } else {
                    highlightedIndex1 = -1;
                    quickSortTimer.stop();
                    enableButtons();
                }
            }

            private int partition(int low, int high) {
                int pivot = array[high];
                int i = (low - 1);

                for (int j = low; j <= high - 1; j++) {
                    if (array[j] < pivot) {
                        i++;
                        int temp = array[i];
                        array[i] = array[j];
                        array[j] = temp;
                    }
                }

                int temp = array[i + 1];
                array[i + 1] = array[high];
                array[high] = temp;

                return i + 1;
            }
        });

        quickSortTimer.start();
        disableButtons();
    }

    private void removeHighlight() {
        highlightedIndex1 = -1;
        highlightedIndex2 = -1;
        repaint();
    }

    private void disableButtons() {
        sortButton.setEnabled(false);
        quickSortButton.setEnabled(false);
        generateButton.setEnabled(false);
        arraySizeSlider.setEnabled(false);
        delaySlider.setEnabled(false);
        compareAlgorithmsButton.setEnabled(false);
    }

    private void enableButtons() {
        sortButton.setEnabled(true);
        quickSortButton.setEnabled(true);
        generateButton.setEnabled(true);
        arraySizeSlider.setEnabled(true);
        delaySlider.setEnabled(true);
        compareAlgorithmsButton.setEnabled(true);
        removeHighlight();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
