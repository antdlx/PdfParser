import beans.SimpleTreeNode;
import beans.Word;
import com.aistrong.analysis.pdf.service.ReaderTextService;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.pdfbox.text.PDFLocalStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHelper {
    /**
     * 根据path读取一个pdf中的字的word,x和endx
     * @param path
     * @return [ArrayList [ArrayList Word,Word,...],
     *                  [ArrayList Word,Word,...],...]
     *         其中Word是Bean中的Word对象，包含word,x,endX属性
     */
    public ArrayList ReadFile(String path) {
        ArrayList contents = new ArrayList();

        ReaderTextService rts = new ReaderTextService();
        try {
            for(List<PDFLocalStripper.WordWithTextPositions> l : rts.readWordWithTextPositions(path)) {
                ArrayList line = new ArrayList();
                for (PDFLocalStripper.WordWithTextPositions wwtp : l) {

                    for (TextPosition tp : wwtp.getTextPositions()) {
                        Word word = new Word(tp.getUnicode().toString(),tp.getX(),tp.getEndX());
                        line.add(word);
                    }

                }
                contents.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contents;
    }


    /***
     * 将间距小于Config.LIMIT的字合并成词
     * content和return的数据结构均为：
     * ArrayList[ArrayList[Word,Word....],
     *          ArrayList[Word,Word....],
     *          ....]
     * @param content
     * @return
     */
    public ArrayList MergeWord(ArrayList content) {
        ArrayList result = new ArrayList();
        //遍历每一行
        for (int i = 0; i < content.size(); i++) {
            ArrayList line = (ArrayList) content.get(i);
            ArrayList resultLine = new ArrayList();
            //用第一行表头初始化三个指针
            Word firstWord = (Word) line.get(0);
            double left = firstWord.getX();
            double right = firstWord.getEndX();
            String word = firstWord.getWord();
            //遍历每一个字
            for (int j = 1; j < line.size(); j++) {
                Word thisWord = (Word) line.get(j);
                //注意是大的坐标减小的坐标
                double delta = thisWord.getX() - right;

                //小于间距就合并
                if (delta < Configs.LIMIT) {
                    right = thisWord.getEndX();
                    word += thisWord.getWord();
                } else {
                    //大于间距就保存，并重置三个指针
                    Word tmp = new Word(word,left,right);
                    resultLine.add(tmp);

                    left = thisWord.getX();
                    right = thisWord.getEndX();
                    word = thisWord.getWord();

                }
                if (j == line.size() - 1) {
                    Word tmp = new Word(word,left,right);
                    resultLine.add(tmp);
                }

            }
            if (line.size()==1){
                resultLine.add(firstWord);
            }
            result.add(resultLine);
        }
        return result;
    }

    /**
     * 迭代的处理每个node的宽度
     * @param rt
     * @return
     */
    public int InitWidth(SimpleTreeNode rt){
        if (rt.getChilds().size() == 0){
            rt.setWidth(1);
            return 1;
        }else {
            int sum = 0;
            for(int i = 0 ; i < rt.getChilds().size() ; i++){
                sum += InitWidth(rt.getChilds().get(i));
            }
            rt.setWidth(sum);
            return sum;
        }

    }

    /**
     * 根据输入的root根节点创建一个在path上的excel文件
     * @param root
     * @param path
     * @return
     */
    public Boolean CreateExcel(SimpleTreeNode root,String path){


        try {
            File f = new File(path);
            OutputStream out;
            out = new FileOutputStream(f);
            //创建工作薄
            WritableWorkbook workbook = null;
            workbook = Workbook.createWorkbook(out);
            //创建新的一页
            WritableSheet sheet = workbook.createSheet("First Sheet",0);

            //创建Excel
            GenerateSheet(root,sheet,0,0);

            //写文件、关闭IO
            workbook.write();
            workbook.close();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }catch (IOException e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }catch (WriteException e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    /**
     * 根据输入的node,sheet，column,row迭代的生成Excel，方式是每列每列的生成
     * @param node 根节点
     * @param sheet Excel的句柄
     * @param column 当前node的起始列数
     * @param row 当前node的起始行数
     * @throws WriteException
     */
    public void GenerateSheet(SimpleTreeNode node,WritableSheet sheet,int column,int row) throws WriteException {

        //将当前node写入句柄，if-else主要是区分是否需要合并单元格
        if (node.getWidth()==1 && node.getHeight()==1){
            sheet.addCell(new Label(column,row,node.getContent()));
        }else {
            sheet.mergeCells(column,row,column+node.getWidth()-1,row+node.getHeight()-1);
            sheet.addCell(new Label(column,row,node.getContent()));
        }

        //迭代每一个child
        int col = 0;
        for (int i = 0 ; i < node.getChilds().size() ; i++){
            SimpleTreeNode child = node.getChilds().get(i);
            SimpleTreeNode pre_child;
            if (i > 0){
                pre_child = node.getChilds().get(i-1);
                col += column+pre_child.getWidth();
                GenerateSheet(child,sheet,col,row+node.getHeight());
            }else {
                GenerateSheet(child,sheet,column,row+node.getHeight());
            }
        }

    }
}
