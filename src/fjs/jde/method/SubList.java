package fjs.jde.method;

import java.util.ArrayList;
import java.util.List;

public class SubList {
    public static void main(String[] args) {
        List<String> strings = new ArrayList<String>();
        for(int i=1;i<=11;i++){
            strings.add(i+"");//��List���������
        }
        System.out.println(strings.toString());
        System.out.println(strings.size());
        int listsize = (null==strings)?0:strings.size();//List�ܼ�¼���������ǿ��жϱ����������쳣
        int perpagesize = 3;//ÿ����List��ŵļ�¼����
        int sumpagenumber = listsize/perpagesize;//�ܹ���Ҫ���ٸ���List�����������ֱ����ȥС������
        int lastListsize = listsize%perpagesize;//���һ��List��size
        if(lastListsize!=0){//������һ��List�Ĵ�С��Ϊ0���������List������Ҫ��1
            sumpagenumber++;
        }
        System.out.println(sumpagenumber);
        for(int i=0;i<sumpagenumber;i++){
            int starnum = i*perpagesize;//ÿ����List��ʼ��λ��
            if(starnum+perpagesize<listsize){
                System.out.println((starnum+1)+"-"+(starnum+perpagesize));//ÿ��List�����ļ�¼��Χ
                System.out.println(strings.subList(starnum, starnum+perpagesize));//��List
            }else{
                System.out.println((starnum+1)+"-"+(listsize));//ÿ��List�����ļ�¼��Χ
                System.out.println(strings.subList(starnum, listsize));//��List
            }
        }
    }
}