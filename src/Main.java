import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Main {

    //数独为经典的3x3九宫格
    public static final int bigWidth = 3;
    //每个九宫格为3x3大小
    public static final int width = 3;

    public static void main(String[] args) {

        //框体设定
        JFrame frame = new JFrame("Sudoku Operator");
        frame.setBounds(400,100,1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //添加&设定面板
        JPanel panel = new JPanel();
        panel.setFont(new Font("", Font.PLAIN, 36));
        frame.add(panel);
        Frame Frame_ = new Frame();
        Frame_.placeComponents(panel);
        //设置为可见
        frame.setVisible(true);
    }

    public static int[][] getTable(){
        //从用户输入获取指定数独表
        System.out.println("Enter your Sudoku Puzzle here(Use '0' to replace space)");
        Scanner scanner = new Scanner(System.in);
        int[][] Table = new int[bigWidth*width][bigWidth*width];

        return Table;
    }

    public static void Create_Sudoku(){
        Sudoku_Table Sudoku_Main = new Sudoku_Table();
    }

    public static void Solve_Sudoku(int[][] Table){
        Sudoku_Table Sudoku_Main = new Sudoku_Table(Table);
        if(!Sudoku_Main.Solve()){
            //若用户给予的数独不为唯一解,则发出警告
            Sudoku_Table.Sudoku_Output += "\n";
            Sudoku_Table.Sudoku_Output += "===========!!!!警告!!!!===========\n";
            Sudoku_Table.Sudoku_Output += "       此数独为非唯一解数独\n";
            Sudoku_Table.Sudoku_Output += "         本数独一共有" + Sudoku_Main.Solutions + "个解\n";
            Sudoku_Table.Sudoku_Output += "     所有可能的解都已输出在上方\n";
            Sudoku_Table.Sudoku_Output += "===========!!!!警告!!!!===========\n";

//            System.out.println(Sudoku_Table.Sudoku_Output);
        }
    }

    public static void Judge_Sudoku(int[][] Table){
        Sudoku_Table Sudoku_Main = new Sudoku_Table(Table);
        if(!Sudoku_Main.Solve_judge()){
            Sudoku_Table.Sudoku_Output += "此数独为 非唯一解 数独";
        } else{
            Sudoku_Table.Sudoku_Output += "此数独为 唯一解 数独";
        }
    }
}
