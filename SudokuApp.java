import javax.swing.*;
// import javax.swing.text.DefaultFormatterFactory;
// import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.text.NumberFormat;
import java.util.Random;

public class SudokuApp {

    public static final int SIZE = 9;
    private JFrame frame;
    private JTextField[][] sudokuCells = new JTextField[9][9];
    private JButton solveButton;
    private JButton clearButton;
    private JButton generateButton;

    public SudokuApp() {
        frame = new JFrame("Sudoku Solver");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(11, 9));

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField textField = new JTextField(2);
                textField.setHorizontalAlignment(JTextField.CENTER); // Center the text
                textField.setFont(new Font("Arial", Font.BOLD, 20)); // Increase font size

                if ((i / 3 + j / 3) % 2 == 0) {
                    textField.setBackground(Color.LIGHT_GRAY);
                }
                frame.add(textField);
                sudokuCells[i][j] = textField;
            }
        }

        solveButton = new JButton("Solve");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] board = new int[9][9];
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (!sudokuCells[i][j].getText().equals("")) {
                            board[i][j] = Integer.parseInt(sudokuCells[i][j].getText());
                        }
                    }
                }

                if (solveSudoku(board)) {
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            sudokuCells[i][j].setText(String.valueOf(board[i][j]));
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Cannot solve this Sudoku puzzle.");
                }
            }
        });
        frame.add(solveButton);

        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        sudokuCells[i][j].setText("");
                    }
                }
            }
        });
        frame.add(clearButton);

        generateButton = new JButton("Generate");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] generatedBoard = generatePuzzle();
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (generatedBoard[i][j] != 0) {
                            sudokuCells[i][j].setText(String.valueOf(generatedBoard[i][j]));
                        } else {
                            sudokuCells[i][j].setText("");
                        }
                    }
                }
            }
        });
        frame.add(generateButton);

        // Add placeholders for the remaining slots in the last row to maintain the 9x9
        // grid structure
        for (int i = 0; i < 6; i++) {
            frame.add(new JLabel());
        }

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new SudokuApp();
    }

    // Sudoku Solver methods
    public boolean solveSudoku(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;

                            if (solveSudoku(board)) {
                                return true;
                            } else {
                                board[row][col] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int x = 0; x < SIZE; x++) {
            if (board[row][x] == num || board[x][col] == num
                    || board[row - row % 3 + x / 3][col - col % 3 + x % 3] == num) {
                return false;
            }
        }
        return true;
    }

    // Sudoku Generator methods
    public int[][] generatePuzzle() {
        int[][] board = new int[SIZE][SIZE];
        solveSudoku(board);
        int numberOfCellsToRemove = 40;
        Random random = new Random();
        for (int i = 0; i < numberOfCellsToRemove; i++) {
            int x = random.nextInt(SIZE);
            int y = random.nextInt(SIZE);
            board[x][y] = 0;
        }
        return board;
    }
}
