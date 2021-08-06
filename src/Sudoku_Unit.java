public class Sudoku_Unit{

    int[][] Table;

    public Sudoku_Unit() {
        //创建单个九宫格并初始化每个数值为-1
        Table = new int[Main.width][Main.width];
        for(int rows = 0;rows < Main.width;rows++){
            for(int cols= 0;cols < Main.width;cols++){
                Table[rows][cols] = -1;
            }
        }
    }

    public void DuplicateRemove(int[] ValidNumber){
        //从可用数表消除重复的数字
        for(int i = 0;i < ValidNumber.length;i++){

            if(ValidNumber[i] == -1){
                continue;
            }

            if(DuplicateCheck(ValidNumber[i])){
                ValidNumber[i] = -1;
            }
        }
    }

    public boolean DuplicateCheck(int number){
        //检查单个九宫格内是否存在相同的数字
        for(int rows = 0;rows < Main.width;rows++) {
            for (int cols = 0; cols < Main.width; cols++) {
                if(Table[rows][cols] == number){
                    return true;
                }
            }
        }
        return false;
    }

    public void setNumber(int number){
        //给予外界向九宫格内添加数字的方法；由于在数独表内数字是按顺序填充的，所以在九宫格内也默认按顺序填充
        boolean flag = false;
        for(int rows = 0;rows < Main.width;rows++){
            for(int cols = 0;cols < Main.width;cols++){
                if(Table[rows][cols] == -1){
                    Table[rows][cols] = number;
                    flag = true;
                    break;
                }
            }
            if(flag){
                break;
            }
        }
    }

    public void deleteNumber(int number){
        //给予外界删除九宫格内[指定数字]的方法
        for(int rows = 0;rows < Main.width;rows++) {
            for (int cols = 0; cols < Main.width; cols++) {
                if(Table[rows][cols] == number){
                    Table[rows][cols] = -1;
                    return;
                }
            }
        }
    }
}
