/**
 * 
 */
/**
 * @author 聂适涵
 *
 */
package zszys;

import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter; 
public class Main {
	
	private static Random random = new Random();
    public static int range;
	
    private String[] createNumber(int range) {
    	
        String numberArr[]=new String[2];
    	int numerator =1+random.nextInt(range);
        int denominator = 0;
        String numberStringT="";
        String numberStringF="";
        if(random.nextBoolean()) {
            denominator = numerator;	
            numerator = 1+random.nextInt(range);
            if(denominator==numerator) 
            	denominator=0;
        }
        if(denominator>0) {      
        	int cfactor = change(numerator,denominator);
        	numerator = numerator/cfactor;
        	denominator = denominator/cfactor;
            numberStringF = ""+numerator+"/"+denominator; 
        }else {
            numberStringF = "" +numerator;
        }
        if(denominator>0) {         	
            if(numerator>denominator){
            	int cfactor = change(numerator,denominator);
            	numerator = numerator/cfactor;
            	denominator = denominator/cfactor;
            	int inte=numerator/denominator;
            	numerator=numerator%denominator;
            	if(numerator==0){
            		numberStringT="" +inte;
            	}else{
            		numberStringT=""+inte+"'"+numerator+"/"+denominator;
            	}
            }else if(numerator<denominator){
            	numberStringT = ""+numerator+"/"+denominator;
            }
        }else {
            numberStringT = "" +numerator;
        }
        numberArr[0]=numberStringF;
        numberArr[1]=numberStringT;
        return numberArr;
    }
    
    private int change(int a, int b) {
    	int c = a % b;
        return c == 0 ? b : change(b, c);
	}
    
    private String[] createExp() {
    	String expArr[]=new String[2];
    	
        char opt = '+';
        switch(random.nextInt(3))  {
            case 0 : opt = '+';    break;
            case 1 : opt = '-';    break;
            case 2 : opt = 'x';    break;
            default: break;
        };
      
        String c1[]=createNumber(range);
        String c2[]=createNumber(range);
        expArr[0]=c1[0]+opt+c2[0];
        expArr[1]=c1[1]+opt+c2[1];
         
        return expArr;
    }
    public static ArrayList transform(String prefix) {
        int i, len = prefix.length();
        prefix=prefix+ '#';
        Stack<Character> stack = new Stack<Character>();
        stack.push('#');
        ArrayList postfix = new ArrayList();
       
        for (i = 0; i < len + 1; i++) {
            if (Character.isDigit(prefix.charAt(i))) {
                if (Character.isDigit(prefix.charAt(i+1))) {
                    postfix.add(10 * (prefix.charAt(i)-'0') + (prefix.charAt(i+1)-'0'));
                    i++;
                } else {
                    postfix.add((prefix.charAt(i)-'0'));
                }
            } else {
                switch (prefix.charAt(i)) {
                case '(':
                    stack.push(prefix.charAt(i));
                    break;
                case ')':
                    while (stack.peek() != '(') {
                        postfix.add(stack.pop());
                    }
                    stack.pop();
                    break;
                default:
                    while (stack.peek() != '#'
                            && compare(stack.peek(), prefix.charAt(i))) {
                        postfix.add(stack.pop());
                    }
                    if (prefix.charAt(i) != '#') {
                        stack.push(prefix.charAt(i));
                    }
                    break;
                }
            }
        }
        return postfix;
    }

    //比较优先级
    public static boolean compare(char peek, char cur) {
        if (peek == 'x'
                && (cur == '+' || cur == '-' || cur == '÷' || cur == 'x')) {
            return true;
        } else if (peek == '/'
                && (cur == '+' || cur == '-' || cur == 'x' || cur == '/')) {
            return true;
        } else if (peek == '+' && (cur == '+' || cur == '-')) {
            return true;
        } else if (peek == '-' && (cur == '+' || cur == '-')) {
            return true;
        } else if (cur == '#') {
            return true;
        }
        return false;
    }

    public static double calculate(ArrayList postfix){
        int i,size=postfix.size();
        double res=0;
        Stack<Double> stack_num=new Stack<Double>();
        for(i=0;i<size;i++){
            if(postfix.get(i).getClass()==Integer.class){
                stack_num.push(Double.parseDouble(String.valueOf( postfix.get(i))));
            }else{
            	double a=stack_num.pop();
            	double b=stack_num.pop();
                switch((Character)postfix.get(i)){
                case '+':
                    res=b+a;
                    break;
                case '-':
                    res=b-a;
                    break;
                case 'x':
                    res=b*a;
                    break;
                case '/':
                    res=b/a;
                    break;
                }
                stack_num.push(res);
            }
        }
        res=stack_num.pop();
        return res;
    }

    public  double[] readAndRead()
	{		
    	double[] answers=new double[100];
		try
		{
			FileReader fr = new FileReader("Answers.txt");
			BufferedReader br = new BufferedReader(fr);
			String s = br.readLine();
			
			
			int i=0;
			while(s!=null)
			{	
				ArrayList postfix = transform(s);
				answers[i]=calculate(postfix);
				s= br.readLine();
				i++;
			}
					br.close();
					fr.close();
		}catch(IOException e)
			{
				System.out.println("指定文件不存在");
			}
		return answers;
	}	
	
    
    
    public static void main(String[] args) {
    	
        Main topic = new Main(); 
        
        Scanner sc= new Scanner(System.in);
   
        System.out.println("生成题目的数量：-n");
        int num=sc.nextInt();
        
        System.out.println("题目的数值范围：-r");
        range=sc.nextInt();
        topic.createNumber(range);
      
        double[] results=new double[num];
        String[] express=new String[2];
        for(int i=0;i<num;i++){
        	express=topic.createExp();
        	String s = express[0];
            ArrayList postfix = transform(s);
        	FileWriter fw = null;
             try {
 	            File f=new File("Exersies.txt");
 	            fw = new FileWriter(f, true);
             } catch (IOException e) {
             	e.printStackTrace();
             }
             
        	
             PrintWriter pw = new PrintWriter(fw);
             pw.println(i+1+"."+express[1]);
             pw.flush();
             try {
 	            fw.flush();
 	            pw.close();
 	            fw.close();
             } catch (IOException e) {
             	e.printStackTrace();
             }
             
            results[i]=calculate(postfix);
        }
        System.out.println("题目已生成，输入-c提交答案！");
        Scanner sc1=new Scanner(System.in);
        String submit=sc1.nextLine();
	    if(submit.equals("-c")){
	        double[] ans=topic.readAndRead();
	        
	        int indexC=0;
	        int indexW=0;
	        int[] coarr=new int[10];
	        int[] wrarr=new int[10];
	        for(int i=0;i<results.length;i++){
	        	if(results[i]==ans[i]){
	        		indexC+=1;
	        	}else {
	        		indexW+=1;
				}
	        }
	        System.out.println("成绩文件已生成");
	        FileWriter fw = null;
            try {
	            File f=new File("Grade.txt");
	            fw = new FileWriter(f, true);
            } catch (IOException e) {
            	e.printStackTrace();
            }
            PrintWriter pw = new PrintWriter(fw);
            pw.println("Correct:"+indexC);
            pw.println("Wrong:"+indexW);
            pw.flush();
            try {
	            fw.flush();
	            pw.close();
	            fw.close();
            } catch (IOException e) {
            	e.printStackTrace();
            }
	    }
    }
}