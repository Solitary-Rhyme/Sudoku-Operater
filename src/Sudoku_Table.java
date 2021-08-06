import java.util.Arrays;
import java.util.Scanner;

//此处创建的九宫格为经典的9x9九宫格，不包含其它特殊变体
public class Sudoku_Table {

    Sudoku_Unit[][] Sudoku_Main;
    int[][] Copy_Table;
    int Solutions = 0;
    static String Sudoku_Output = "";

    //从零开始创建数独表
    public Sudoku_Table() {

        //初始化数独表
        Sudoku_Main = new Sudoku_Unit[Main.bigWidth][Main.bigWidth];
        for(int rows = 0;rows < Main.bigWidth;rows++){
            for(int cols = 0;cols < Main.bigWidth;cols++){
                Sudoku_Main[rows][cols] = new Sudoku_Unit();
            }
        }

        //初始化拷贝表，拷贝表是为了检测单行单列上数字是否重复创建的表
        Copy_Table = new int[Main.bigWidth*Main.width][Main.bigWidth*Main.width];
        for(int rows = 0;rows < Main.bigWidth*Main.width;rows++){
            for(int cols = 0;cols < Main.bigWidth*Main.width;cols++){
                Copy_Table[rows][cols] = -1;
            }
        }

        //填充数字生成完整的数独表
        this.Fill_number();
        //挖洞法生成空格
        this.DigHole();
        //输出结果
        Conservation_ItoS(Copy_Table);
//        System.out.println();
//        Show();
    }

    //获取指定的数独表
    public Sudoku_Table(int[][] Sudoku_Table){

        //初始化数独表
        Sudoku_Main = new Sudoku_Unit[Main.bigWidth][Main.bigWidth];
        for(int rows = 0;rows < Main.bigWidth;rows++){
            for(int cols = 0;cols < Main.bigWidth;cols++){
                Sudoku_Main[rows][cols] = new Sudoku_Unit();
            }
        }
        for(int rows = 0;rows < Main.bigWidth*Main.width;rows++){
            for(int cols = 0;cols < Main.bigWidth*Main.width;cols++){
                Sudoku_Main[rows/Main.width][cols/Main.width].setNumber(Sudoku_Table[rows][cols]);
            }
        }

        //初始化拷贝表，拷贝表是为了检测单行单列上数字是否重复创建的表
        Copy_Table = Sudoku_Table.clone();
    }

    //可由外部调用的,展示数独表的方法;只展示最初的唯一的数独表
    public void Show(){
        for(int i = 0;i < Main.bigWidth*Main.width;i++){
            System.out.println(Arrays.toString(Copy_Table[i]));
        }
    }

    //只能由内部调用的,展示数独表的方法;可以指定展示任意一张数独表
    private void Show(int[][] Copy_Table){
        for(int i = 0;i < Main.bigWidth*Main.width;i++){
            System.out.println(Arrays.toString(Copy_Table[i]));
        }
    }

    //向空白的数独表内按顺序依次填充符合规则的数字
    private void Fill_number(){
        //创建不可用数组,0号位弃之不用;添加失败的数字会进入不可用数组,方便新的随机数检索,减少重复判定
        boolean[] InvalidNumber = new boolean[Main.bigWidth*Main.width+1];

        //按照顺序选定填充数字的位置
        for(int rows = 0;rows < Main.bigWidth*Main.width;rows++){
            for(int cols = 0,count = 0;cols < Main.bigWidth*Main.width;cols++,count = 0){

                //循环随机生成数字并鉴定是否符合条件，若符合则填充数字并移动到下一个位置
                while(true) {
                    //不可用数组判定部分
                    int number = (int) (Math.random() * Main.width*Main.bigWidth + 1);
//                    System.out.println(number);
                    if(InvalidNumber[number]){
                        continue;
                    }

                    //基于数独规则的判定部分
                    if (Sudoku_Main[rows / Main.width][cols / Main.width].DuplicateCheck(number) || this.DuplicateCheck(Copy_Table, number, rows, cols)) {
                        //判定失败，存入不可用表并重新生成数字
                        InvalidNumber[number] = true;
                        count++;

                        //如果循环超过一定次数依旧没有跳出，说明遇到无解情况，使用reset重置整个数独表
                        if(count >= Main.bigWidth*Main.width){
                            this.reset();
                            rows = 0;
                            cols = -1;
                            break;
                        }
                    } else {
                        //判定成功，添加入拷贝表与数独表当前单元内
//                        System.out.println("-------Enter-------");
                        Copy_Table[rows][cols] = number;
                        Sudoku_Main[rows / Main.width][cols / Main.width].setNumber(number);
                        break;
                    }
                }

                //若数字成功填入或需要重置数独表，初始化不可用表
                for(int i = 0;i < 10;i++){
                    InvalidNumber[i] = false;
                }
            }
        }
    }

    private void DuplicateRemove(int[][] Copy_Table, int[] ValidNumber,int rows,int cols){
        //从可用数表消除重复的数字
        for(int i = 0;i < ValidNumber.length;i++){
            if(ValidNumber[i] == -1){
                continue;
            }

            if(DuplicateCheck(Copy_Table,ValidNumber[i],rows,cols)){
                ValidNumber[i] = -1;
            }
        }
    }

    private boolean DuplicateCheck(int[][] Copy_Table, int number, int rows, int cols){
        //检测当前表中,当前数字所处的列和行是否存在重复的数字
        for(int row = 0;row < Main.bigWidth*Main.width;row++){
            if(Copy_Table[row][cols] == number){
                return true;
            }
        }

        for(int col = 0;col < Main.bigWidth*Main.width;col++){
            if(Copy_Table[rows][col] == number){
                return true;
            }
        }

        return false;
    }

    private void reset(){
        //清空数独表和拷贝表内的所有数据
        Sudoku_Main = new Sudoku_Unit[Main.bigWidth][Main.bigWidth];
        for(int rows = 0;rows < Main.bigWidth;rows++){
            for(int cols = 0;cols < Main.bigWidth;cols++){
                Sudoku_Main[rows][cols] = new Sudoku_Unit();
            }
        }

        for(int rows = 0;rows < Main.bigWidth*Main.width;rows++){
            for(int cols = 0;cols < Main.bigWidth*Main.width;cols++){
                Copy_Table[rows][cols] = -1;
            }
        }
    }

    //对外提供的数独解算,特点是会输出所有可能的解
    public boolean Solve(){

        Solutions = 0;  //这里很重要

        //将数独表和拷贝表复制一份,solve不会直接干涉到原始数独表的值
        Sudoku_Unit[][] Sudoku_Main_cp = new Sudoku_Unit[Main.bigWidth][Main.bigWidth];
        int[][] Copy_Table_cp = new int[Main.bigWidth*Main.width][Main.bigWidth*Main.width];
        SudokuCopy(Sudoku_Main_cp,Copy_Table_cp);

        return Solve_inner(Sudoku_Main_cp,Copy_Table_cp);
    }

    private boolean Solve_inner(Sudoku_Unit[][] Sudoku_Main_cp,int[][] Copy_Table_cp){

        //回溯算法解数独
        for(int rows = 0;rows < Main.bigWidth*Main.width;rows++){
            for(int cols = 0;cols < Main.bigWidth*Main.width;cols++){
                if(Copy_Table_cp[rows][cols] == -1){

                        //创建一个可用数数组,在刨除所有不可用数字后,选择数组中的元素依次尝试解
                        int[] ValidNumber = new int[Main.bigWidth*Main.width];
                        for(int num = 1;num <= Main.bigWidth*Main.width;num++){
                            ValidNumber[num-1] = num;
                        }
                        Sudoku_Main_cp[rows / Main.width][cols / Main.width].DuplicateRemove(ValidNumber);
                        DuplicateRemove(Copy_Table_cp,ValidNumber,rows,cols);

                        for(int i = 0;i < ValidNumber.length;i++) {
                            if (ValidNumber[i] != -1) {
                                //遍历拷贝表,最终满足:当前位置为空(-1)且填充数字符合数独规则条件的数字会被填入
                                Copy_Table_cp[rows][cols] = ValidNumber[i];
                                Sudoku_Main_cp[rows / Main.width][cols / Main.width].setNumber(ValidNumber[i]);
                                Solve_inner(Sudoku_Main_cp,Copy_Table_cp);
                                Copy_Table_cp[rows][cols] = -1;
                                Sudoku_Main_cp[rows / Main.width][cols / Main.width].deleteNumber(ValidNumber[i]);
                            }
                        }
//                    System.out.println("Failed!Return!");
                    if(Solutions > 1){
                        return false;   //非唯一解,返回false
                    }else{
                        return true;    //唯一解，返回true
                    }
                }
            }
        }

        //递归结束
//        System.out.println("GOT IT!");
//        System.out.println();
//        Show(Copy_Table_cp);
        Conservation_ItoS(Copy_Table_cp);
//        System.out.println(Sudoku_Output);
        Solutions++;
        if(Solutions > 1){
            return false;   //非唯一解,返回false
        }else{
            return true;    //唯一解，返回true
        }
    }

    //判断性质的数独解算,特点是不会输出解,且若发现数独表为非唯一解立刻跳出递归并返回false
    public boolean Solve_judge(){

        Solutions = 0;  //这里很重要

        //将数独表和拷贝表复制一份,solve_judge不会直接干涉到原始数独表的值
        Sudoku_Unit[][] Sudoku_Main_cp = new Sudoku_Unit[Main.bigWidth][Main.bigWidth];
        int[][] Copy_Table_cp = new int[Main.bigWidth*Main.width][Main.bigWidth*Main.width];
        SudokuCopy(Sudoku_Main_cp,Copy_Table_cp);

        try {
            Solve_judge_inner(Sudoku_Main_cp,Copy_Table_cp);
        } catch (StopMsgException e){
            return false;
        }
        return true;
    }

    private boolean Solve_judge_inner(Sudoku_Unit[][] Sudoku_Main_cp,int[][] Copy_Table_cp){

        //回溯算法解数独
        for(int rows = 0;rows < Main.bigWidth*Main.width;rows++){
            for(int cols = 0;cols < Main.bigWidth*Main.width;cols++){
                if(Copy_Table_cp[rows][cols] == -1){

                    int[] ValidNumber = new int[Main.bigWidth*Main.width];
                    for(int num = 1;num <= Main.bigWidth*Main.width;num++){
                        ValidNumber[num-1] = num;
                    }
                    Sudoku_Main_cp[rows / Main.width][cols / Main.width].DuplicateRemove(ValidNumber);
                    DuplicateRemove(Copy_Table_cp,ValidNumber,rows,cols);

                    for(int i = 0;i < ValidNumber.length;i++) {
                        if (ValidNumber[i] != -1) {
                            //遍历拷贝表,最终满足:当前位置为空(-1)且填充数字符合数独规则条件的数字会被填入
                            Copy_Table_cp[rows][cols] = ValidNumber[i];
                            Sudoku_Main_cp[rows / Main.width][cols / Main.width].setNumber(ValidNumber[i]);

                            Solve_judge_inner(Sudoku_Main_cp,Copy_Table_cp);
                            Copy_Table_cp[rows][cols] = -1;
                            Sudoku_Main_cp[rows / Main.width][cols / Main.width].deleteNumber(ValidNumber[i]);
                        }
                    }

//                    System.out.println("Failed!Return!");
                    if(Solutions > 1){
                        throw new StopMsgException();   //非唯一解,跳出递归
                    }else{
                        return true;    //唯一解，返回true
                    }
                }
            }
        }
//        System.out.println("GOT IT!");
        Solutions++;
        if(Solutions > 1){
            throw new StopMsgException();   //非唯一解,跳出递归
        }else{
            return true;    //唯一解，返回true
        }
    }

    private void SudokuCopy(Sudoku_Unit[][] Sudoku_Main_cp,int[][] Copy_Table_cp){
        //复制数独表
        for(int rows = 0;rows < Main.bigWidth;rows++){
            for(int cols = 0;cols < Main.bigWidth;cols++){
                Sudoku_Main_cp[rows][cols] = new Sudoku_Unit();
                for(int row = 0;row < Main.width;row++){
                    for(int col = 0;col < Main.width;col++){
                        Sudoku_Main_cp[rows][cols].setNumber(Sudoku_Main[rows][cols].Table[row][col]);
                    }
                }
            }
        }

        //复制拷贝表
        for(int rows = 0;rows < Main.bigWidth*Main.width;rows++){
            for(int cols = 0;cols < Main.bigWidth*Main.width;cols++){
                Copy_Table_cp[rows][cols] = Copy_Table[rows][cols];
            }
        }
    }

    static class StopMsgException extends RuntimeException{ }
    private boolean DigHole(){
        //挖洞法
        //随机生成44~56个洞
        int Holes = (int)(Math.random()*(56-44+1) + 44);

        try{
            DigHole_inner(Holes,0);
        } catch (StopMsgException e){
        }
        return true;
    }

    private void DigHole_inner(int Holes, int counter){

//        System.out.println(counter);

        if(counter == Holes){
            throw new StopMsgException();
        }

        for(int i = 0;i < 50;i++){      //这个是关键

            //随机生成挖洞位置
            int rows = (int)(Math.random()*Main.bigWidth*Main.width);
            int cols = (int)(Math.random()*Main.bigWidth*Main.width);

            //如果发现随机挖洞位置刚好落在空位上,则自动寻找最近的非空位置
            //这样做可能会一定程度上破坏随机性,但确实减少了大量的时间
            if(Copy_Table[rows][cols] == -1){
                label:
                for(int row = rows;row < Main.bigWidth*Main.width;row++){
                    for(int col = cols;col < Main.bigWidth*Main.width;col++){
                        if(Copy_Table[row][col] != -1){
                            rows = row;
                            cols = col;
                            break label;
                        }
                    }
                }
            }

            if(Copy_Table[rows][cols] != -1){
                //按照位置开始挖洞
                int TempNum = Copy_Table[rows][cols];
                Sudoku_Main[rows / Main.width][cols / Main.width].deleteNumber(Copy_Table[rows][cols]);
                Copy_Table[rows][cols] = -1;
                //若非唯一解,回溯
                if(!Solve_judge()){
                    Sudoku_Main[rows / Main.width][cols / Main.width].setNumber(TempNum);
                    Copy_Table[rows][cols] = TempNum;
                    continue;
                }

                DigHole_inner(Holes,counter+1);
                Sudoku_Main[rows / Main.width][cols / Main.width].setNumber(TempNum);
                Copy_Table[rows][cols] = TempNum;
            }
        }
        return;
    }

    //将数独计算结果转换为String形式并储存在Sudoku_Output中方便输出到文本框
    public void Conservation_ItoS(int[][] IntArray){
        String tempString = "\n";
        for(int rows = 0;rows < Main.bigWidth*Main.width;rows++){
            for(int cols = 0;cols < Main.bigWidth*Main.width;cols++){
                if(IntArray[rows][cols] == -1){
                    IntArray[rows][cols] = 0;
                }
                tempString += (IntArray[rows][cols] + " ");
            }
            tempString += "\n";
        }

        Sudoku_Output += tempString;
    }
}
