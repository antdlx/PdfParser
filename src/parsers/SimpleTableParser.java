package parsers;

import beans.SimpleTreeNode;
import beans.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimpleTableParser {

    private double LIMIT;

    public SimpleTableParser(double LIMIT){
        this.LIMIT = LIMIT;
    }

    public SimpleTreeNode Parser(ArrayList contents) {
        if (contents.size() < 1){
            System.out.println("Error: you can not parsing a empty table!");
            return null;
        }else {
            ArrayList root_list = (ArrayList) contents.get(0);
            Word root_word = (Word) root_list.get(0);
            SimpleTreeNode root = new SimpleTreeNode(root_word.getX(),root_word.getEndX(),null,root_word.getWord());
            for (int i = 1; i< contents.size() ; i++){
                root  = Growing(root,(ArrayList) contents.get(i));
            }


            return root;
        }
    }

    /**
     * 根据一个节点和输入的每一行的坐标递归的生成一棵树
     * @param rt
     * @param line_content [Word,Word,...,Word]
     * @return rt
     */
    private SimpleTreeNode Growing(SimpleTreeNode rt, ArrayList line_content){

        //弱是叶子，则给叶子添加子节点
        if (rt.getChilds().size() == 0){
            //将这一行的数据添加成子节点
            for(int i = 0; i < line_content.size() ; i++){
                Word tmp_word = (Word) line_content.get(i);
                SimpleTreeNode tmp = new SimpleTreeNode(tmp_word.getX(),tmp_word.getEndX(),rt,tmp_word.getWord());
                rt.addChilds(tmp);
            }
        }else {
            //如果不是叶子，那么用每一个child去遍历这一行的数据
            //如果当前的EndX<下一个child的X，那么认为这个数据是属于当前这个child的，加入到临时的list中，继续遍历
            //遍历结束后，将这个child对应的所有数据(即那个临时list)递归的传入当前函数，直到当前是叶子节点，再添加相应的数据节点
            int child_num = rt.getChilds().size();
            Word tmp_word = (Word) line_content.get(0);
            SimpleTreeNode tmp_node = rt.getChilds().get(0);

            //针对table1中“项目”这种节点进行的判定
            if (line_content.size()==1 && tmp_word.getEndX()-tmp_node.getLeft_border()<0.001){
                rt.addLeftChilds(new SimpleTreeNode(tmp_word.getX(),tmp_word.getEndX(),rt,tmp_word.getWord()));
            }else {

                //在最外声明list的遍历索引，可以避免每次前面部分无用的遍历
                int l = 0;
                //用每个child去遍历
                for(int i = 0; i < child_num; i++){
                    //获取当前的child和下一个child
                    SimpleTreeNode child = rt.getChilds().get(i);
                    SimpleTreeNode next_child = null;
                    if (i+1<child_num){
                        next_child = rt.getChilds().get(i+1);
                    }
                    ArrayList tmp_line = new ArrayList();
                    //遍历所有的数据
                    for (;l<line_content.size();l++){
                        Word cur_word = (Word) line_content.get(l);
                        //如果没有下一个child，即当前child是这一行最后一个child，那么剩下的数据都是这个child的
                        if (next_child == null){
                            tmp_line.add(cur_word);
                            if (l==line_content.size()-1){
                                Growing(child,tmp_line);
                            }
                        }else {
                            //如果当前的EndX<下一个child的X，那么认为这个数据是属于当前这个child的，加入到临时的list中，继续遍历
                            if (cur_word.getEndX() < next_child.getLeft_border()){
                                tmp_line.add(cur_word);
                                //迭代完成就加到root中去
                                if (l==line_content.size()-1){
                                    Growing(child,tmp_line);
                                }
                            }else {
                                //如果没有一个数据是当前child的，那么可能是一个合并的单元格，height+1，然后继续下一个child
                                //table1中的“项目”对应此种情况
                                if (tmp_line.size() == 0){
                                    child.setHeight(child.getHeight()+1);
                                    break;
                                }
                                //已经获得符合当前child的所有数据，将这些数据执行递归操作，让数据“下沉”到当前Node的child进行细分
                                Growing(child,tmp_line);
                                break;
                            }
                        }

                    }
                }

            }
        }


        return rt;
    }

}
