import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame {
    public void placeComponents(JPanel panel) {

        panel.setLayout(null);

        /*
         * 创建文本域用于用户输入
         */
        JTextArea userText = new JTextArea();
        userText.setBounds(100, 70, 400, 500);
        userText.setFont(new Font("宋体",Font.BOLD,20));
        panel.add(userText);

        JTextArea OutputText = new JTextArea();
//        OutputText.setBounds(700, 70, 400, 620);
        OutputText.setFont(new Font("宋体",Font.BOLD,20));
        JScrollPane jsp = new JScrollPane(OutputText);
        jsp.setBounds(700, 70, 400, 620);
        jsp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(jsp);
        OutputText.setText("\n ======= 欢迎使用数独计算器 =======\n" +
                "\n 若要使用计算数独/求是否为唯一解功能," +
                "\n 请先在右边的框内输入完整规范的数独," +
                "\n 空格使用数字0代替。例:\n\n" +
                "         9 0 2 5 4 7 3 6 0\n" +
                "         4 6 5 0 0 3 0 2 7\n" +
                "         7 3 0 9 2 6 8 0 5\n" +
                "         0 0 6 8 1 5 4 7 9\n" +
                "         8 0 9 3 6 4 5 0 2\n" +
                "         5 1 4 0 7 0 0 8 3\n" +
                "         1 5 7 0 9 8 2 0 6\n" +
                "         2 4 0 6 0 1 0 9 0\n" +
                "         0 9 8 7 3 0 1 5 4\n\n" +
                " 注意:本计算器只接受9x9数独");
//        panel.add(OutputText);

        JButton CalculateButton = new JButton("计算结果");
        CalculateButton.setBounds(130, 600, 160, 50);
        panel.add(CalculateButton);
        CalculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int[][] IntArray = new int[Main.bigWidth*Main.width][Main.bigWidth*Main.width];
                System.out.println(userText.getText());
                if(userText.getText().equals("")){
                    System.exit(0);
                }
                Conservation_StoI(userText.getText(),IntArray);  //转化用户输入为数组形式
                Main.Solve_Sudoku(IntArray);
                OutputText.setText(Sudoku_Table.Sudoku_Output);
                Sudoku_Table.Sudoku_Output = "";
            }
        });

        JButton CreateButton = new JButton("生成数独");
        CreateButton.setBounds(310, 600, 160, 50);
        panel.add(CreateButton);
        CreateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Main.Create_Sudoku();
                OutputText.setText(Sudoku_Table.Sudoku_Output);
                Sudoku_Table.Sudoku_Output = "";
            }
        });

        JButton JudgeButton = new JButton("判断是否唯一解");
        JudgeButton.setBounds(130, 660, 160, 50);
        panel.add(JudgeButton);
        JudgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int[][] IntArray = new int[Main.bigWidth*Main.width][Main.bigWidth*Main.width];
                String s = userText.getText();
                Conservation_StoI(userText.getText(),IntArray);  //转化用户输入为数组形式
                Main.Judge_Sudoku(IntArray);
                OutputText.setText(Sudoku_Table.Sudoku_Output);
                Sudoku_Table.Sudoku_Output = "";
            }
        });

        JButton ExitButton = new JButton("退出");
        ExitButton.setBounds(310, 660, 160, 50);
        panel.add(ExitButton);
        ExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });

        JLabel InputLabel = new JLabel();
        InputLabel.setBounds(250,30,100,50);
        InputLabel.setText("输入区");
        InputLabel.setFont(new Font("宋体",Font.BOLD,20));
        panel.add(InputLabel);

        JLabel OutputLabel = new JLabel();
        OutputLabel.setBounds(870,30,100,50);
        OutputLabel.setText("输出区");
        OutputLabel.setFont(new Font("宋体",Font.BOLD,20));
        panel.add(OutputLabel);


    }

    //将用户输入的String类型转换为Int数组类型,并且将0转化为-1
    public void Conservation_StoI(String s,int[][] IntArray){

        int pos = 0;
        for(int rows = 0;rows < Main.bigWidth*Main.width;rows++){
            for(int cols = 0;cols < Main.bigWidth*Main.width;cols++){
                for(;pos < s.length();pos++){
                    if(s.charAt(pos) != ' ' && s.charAt(pos) != '\n'){
                        IntArray[rows][cols] = Integer.valueOf(s.charAt(pos)) - 48;
                        pos++;
                        break;
                    }
                }
            }
        }

        for(int rows = 0;rows < Main.bigWidth*Main.width;rows++){
            for(int cols = 0;cols < Main.bigWidth*Main.width;cols++){
                if(IntArray[rows][cols] == 0){
                    IntArray[rows][cols] = -1;
                }
            }
        }
    }
}
