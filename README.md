# Sorting Visualizer

This Java project provides a visual representation of sorting algorithms using different visualization modes such as Bars, Lines, and Circles. It allows users to generate random arrays, visualize the sorting process, and compare the time complexity of Bubble Sort and Quick Sort algorithms.

## Features

- **Generate Bars:** Create a random array of bars for visualization.
- **Sort Bars (Bubble Sort):** Visualize the Bubble Sort algorithm on the generated bars.
- **Sort Bars (Quick Sort):** Visualize the Quick Sort algorithm on the generated bars.
- **Compare Algorithms:** Display a time complexity graph comparing Bubble Sort (O(n^2)) and Quick Sort (O(n log n)).
- **Change Visualization Mode:** Switch between Bars, Lines, and Circles for different visualization perspectives.
- **Adjustable Array Size:** Change the number of bars in the array using the slider.
- **Adjustable Delay:** Control the speed of the visualization with the delay slider.

## Usage

1. Run the program by executing the `Main` class.
2. Use the buttons to generate random bars, start Bubble Sort, start Quick Sort, or compare algorithms.
3. Adjust the array size and delay using the sliders.
4. Change the visualization mode using the dropdown menu.

## Visualization Modes

- **Bars:** Visualize the sorting process with vertical bars.
- **Lines:** Visualize the sorting process with connecting lines between bars.
- **Circles:** Visualize the sorting process with circles representing each element.

## Algorithm Comparison

The "Compare" button opens a separate window displaying a time complexity graph comparing Bubble Sort and Quick Sort for input sizes ranging from 1 to 100.

## Dependencies

This project uses the JFreeChart library for creating the time complexity graph. Make sure to include the necessary JFreeChart library in your project.
