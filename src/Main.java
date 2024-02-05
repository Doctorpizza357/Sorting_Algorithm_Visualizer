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

/**
 * This class represents a sorting visualizer application using Swing.
 * @date 2/5/24
 * @author Tomas Bentolila
 */
public class Main extends JFrame {

    // Data and state variables
    private int arraySize = 10;
    private int[] array;
    private int highlightedIndex1 = -1;
    private int highlightedIndex2 = -1;

    // Buttons and UI components
    private final JButton generateButton;
    private final JButton sortButton;
    private final JButton quickSortButton;
    private final JButton compareAlgorithmsButton;
    private final JPanel barPanel;
    private final JSlider arraySizeSlider;
    private final JSlider delaySlider;
    private int delay = 400;
    private final JComboBox<String> visualizationOptions;
    private String currentVisualizationMode = "Bars"; // Default visualization mode

    // Timers for sorting algorithms
    private Timer bubbleSortTimer;
    private Timer quickSortTimer;

    /**
     * Constructor for the Main class, initializes the UI components.
     * @date 2/5/24
     * @author Tomas Bentolila
     */
    public Main() {
        array = new int[arraySize];

        // Initialize buttons and UI components
        generateButton = new JButton("Generate Bars");
        generateButton.addActionListener(e -> generateRandomBars());

        sortButton = new JButton("Sort Bars (Bubble Sort)");
        sortButton.addActionListener(e -> startBubbleSort());

        quickSortButton = new JButton("Sort Bars (Quick Sort)");
        quickSortButton.addActionListener(e -> startQuickSort());

        compareAlgorithmsButton = new JButton("Compare");
        compareAlgorithmsButton.addActionListener(e -> compareAndDisplayAlgorithms());

        String[] visualizationModes = {"Bars", "Lines", "Circles"};
        visualizationOptions = new JComboBox<>(visualizationModes);
        visualizationOptions.addActionListener(e -> changeVisualizationMode());

        arraySizeSlider = new JSlider(JSlider.HORIZONTAL, 5, 100, arraySize);
        arraySizeSlider.setMajorTickSpacing(5);
        arraySizeSlider.setPaintTicks(true);
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
        delaySlider.addChangeListener(e -> delay = delaySlider.getValue());

        barPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawVisualizations(g);
            }
        };

        // Set up the layout and add components to the frame
        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(generateButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(quickSortButton);
        buttonPanel.add(compareAlgorithmsButton);
        buttonPanel.add(visualizationOptions);

        JPanel sliderPanel = new JPanel(new FlowLayout());
        sliderPanel.add(new JLabel("Number of Bars: "));
        sliderPanel.add(arraySizeSlider);
        sliderPanel.add(new JLabel("Delay: "));
        sliderPanel.add(delaySlider);

        add(buttonPanel, BorderLayout.NORTH);
        add(sliderPanel, BorderLayout.SOUTH);
        add(barPanel, BorderLayout.CENTER);

        // Set up frame properties
        setTitle("Sorting Visualizer");
        setSize(650, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        generateRandomBars();
    }

    /**
     * Changes the current visualization mode based on the selected option in the combo box.
     * @date 2/5/24
     * @author Tomas Bentolila
     */
    private void changeVisualizationMode() {
        currentVisualizationMode = (String) visualizationOptions.getSelectedItem();
        repaint();
    }

    /**
     * Draws the visualizations based on the selected visualization mode.
     * @date 2/5/24
     * @author Tomas Bentolila
     */
    private void drawVisualizations(Graphics g) {
        if ("Bars".equals(currentVisualizationMode)) {
            drawBars(g);
        } else if ("Lines".equals(currentVisualizationMode)) {
            drawLines(g);
        } else if ("Circles".equals(currentVisualizationMode)) {
            drawCircles(g);
        }
    }

    /**
     * Generates random values for the array to be visualized as bars.
     * @date 2/5/24
     * @author Tomas Bentolila
     */
    private void generateRandomBars() {
        Random random = new Random();
        for (int i = 0; i < arraySize; i++) {
            array[i] = random.nextInt(100) + 1;
        }
        highlightedIndex1 = -1;
        highlightedIndex2 = -1;
        repaint();
    }

    /**
     * Compares and displays time complexity graphs of sorting algorithms using JFreeChart.
     * @date 2/5/24
     * @author Tomas Bentolila
     */
    private void compareAndDisplayAlgorithms() {
        SwingUtilities.invokeLater(Main::compareAlgorithms);
    }

    /**
     * Static method to create and display a time complexity graph using JFreeChart.
     * @date 2/5/24
     * @author Tomas Bentolila
     */
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
        plot.setRangeGridlinesVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ChartPanel chartPanel = new ChartPanel(chart);
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    /**
     * Draws bars based on the current state of the array and highlighted indices.
     * @date 2/5/24
     * @author Tomas Bentolila
     */
    private void drawBars(Graphics g) {
        int maxBarHeight = barPanel.getHeight();
        int barWidth = barPanel.getWidth() / arraySize;

        for (int i = 0; i < arraySize; i++) {
            int barHeight = array[i] * maxBarHeight / 100;

            Color barColor;
            if (i == highlightedIndex1 || i == highlightedIndex2) {
                barColor = new Color(255, 0, 0);  // Red for highlighted bars
            } else {
                float hue = (float) i / arraySize;
                barColor = Color.getHSBColor(hue, 1.0f, 1.0f);
            }

            g.setColor(barColor);

            if ("Bars".equals(currentVisualizationMode)) {
                g.fillRect(i * barWidth, maxBarHeight - barHeight, barWidth, barHeight);
            } else if ("Lines".equals(currentVisualizationMode)) {
                if (i < arraySize - 1) {
                    int nextBarHeight = array[i + 1] * maxBarHeight / 100;
                    g.drawLine(i * barWidth, maxBarHeight - barHeight, (i + 1) * barWidth, maxBarHeight - nextBarHeight);
                }
            } else if ("Circles".equals(currentVisualizationMode)) {
                int circleDiameter = barWidth / 2;
                int circleX = i * barWidth + circleDiameter / 2;
                int circleY = maxBarHeight - barHeight;

                g.fillOval(circleX, circleY, circleDiameter, circleDiameter);
            }
        }
    }

    /**
     * Draws lines connecting the bars based on the current state of the array and highlighted index.
     * @date 2/5/24
     * @author Tomas Bentolila
     */
    private void drawLines(Graphics g) {
        int maxBarHeight = barPanel.getHeight();
        int barWidth = barPanel.getWidth() / arraySize;

        for (int i = 0; i < arraySize - 1; i++) {
            int barHeight1 = array[i] * maxBarHeight / 100;
            int barHeight2 = array[i + 1] * maxBarHeight / 100;

            Color lineColor;
            if (i == highlightedIndex1) {
                lineColor = new Color(255, 0, 0);  // Red for highlighted lines
            } else {
                float hue = (float) i / arraySize;
                lineColor = Color.getHSBColor(hue, 1.0f, 1.0f);
            }

            g.setColor(lineColor);

            int x1 = i * barWidth + barWidth / 2;
            int y1 = maxBarHeight - barHeight1;
            int x2 = (i + 1) * barWidth + barWidth / 2;
            int y2 = maxBarHeight - barHeight2;

            g.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * Draws circles representing the bars based on the current state of the array and highlighted indices.
     * @date 2/5/24
     * @author Tomas Bentolila
     */
    private void drawCircles(Graphics g) {
        int maxBarHeight = barPanel.getHeight();
        int barWidth = barPanel.getWidth() / arraySize;

        for (int i = 0; i < arraySize; i++) {
            int barHeight = array[i] * maxBarHeight / 100;

            Color circleColor;
            if (i == highlightedIndex1 || i == highlightedIndex2) {
                circleColor = new Color(255, 0, 0);  // Red for highlighted circles
            } else {
                float hue = (float) i / arraySize;
                circleColor = Color.getHSBColor(hue, 1.0f, 1.0f);
            }

            g.setColor(circleColor);

            int circleDiameter = barWidth / 2;
            int circleX = i * barWidth + circleDiameter / 2;
            int circleY = maxBarHeight - barHeight;

            g.fillOval(circleX, circleY, circleDiameter, circleDiameter);
        }
    }

    /**
     * Initiates the Bubble Sort algorithm.
     * @date 2/5/24
     * @author Tomas Bentolila
     */
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

    /**
     * Initiates the Quick Sort algorithm.
     * @date 2/5/24
     * @author Tomas Bentolila
     */
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

    /**
     * Removes highlighting of bars, lines, or circles.
     * @date 2/5/24
     * @author Tomas Bentolila
     */
    private void removeHighlight() {
        highlightedIndex1 = -1;
        highlightedIndex2 = -1;
        repaint();
    }

    /**
     * Disables UI buttons during sorting.
     * @date 2/5/24
     * @author Tomas Bentolila
     */
    private void disableButtons() {
        sortButton.setEnabled(false);
        quickSortButton.setEnabled(false);
        generateButton.setEnabled(false);
        arraySizeSlider.setEnabled(false);
        delaySlider.setEnabled(false);
        compareAlgorithmsButton.setEnabled(false);
    }

    /**
     * Enables UI buttons after sorting completion.
     * @date 2/5/24
     * @author Tomas Bentolila
     */
    private void enableButtons() {
        sortButton.setEnabled(true);
        quickSortButton.setEnabled(true);
        generateButton.setEnabled(true);
        arraySizeSlider.setEnabled(true);
        delaySlider.setEnabled(true);
        compareAlgorithmsButton.setEnabled(true);
        removeHighlight();
    }

    /**
     * Main method to launch the application.
     * @date 2/5/24
     * @author Tomas Bentolila
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
