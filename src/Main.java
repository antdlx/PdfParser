import beans.SimpleTreeNode;
import parsers.SimpleTableParser;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        //实例化一些必要的工具类
        Configs configs = new Configs();
        DataHelper dataHelper = new DataHelper();
        SimpleTableParser simpleTableParser = new SimpleTableParser(Configs.LIMIT);

        //根据path读取一个pdf中的字的word,x和endx
        ArrayList contents = dataHelper.ReadFile(configs.getTEST_PDF_FILE());
        //将每一行零散的数据合并起来
        ArrayList merged_contents = dataHelper.MergeWord(contents);

        //解析数据，构建成一棵树
        SimpleTreeNode root = simpleTableParser.Parser(merged_contents);
        //生成每一个node的长度(占几格)，方便后面合并单元格
        dataHelper.InitWidth(root);

        //将树用Excel表现出来
        Boolean weather = dataHelper.CreateExcel(root,configs.getTEST_OUTPUT_FILE());

        System.out.println(weather);

    }
}
