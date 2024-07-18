 /* 8 Operations (can be extended upto 64)
    1 Max
    2 Min
    3 Sum
    4 Maxidx
    5 Minidx
    6 XOR
    7 OR
    8 AND
    
  */

import java.util.*;

public class GenericST
{
    static long trees[][];
    static  int todo[];
    public static void main(String args[]) 
    {
        System.out.println("Enter data\n Enter the number of observations");
       
        Scanner sc = new Scanner(System.in);
        int ele=sc.nextInt();
        System.out.println("Enter the elements");
        int elements[] = new int[ele];
        for(int i=0;i<ele;i++)elements[i]=sc.nextInt();



        String operations[] = new String[10];
        initialise(operations);
        boolean terminate=false;
        System.out.println("6 Operations)\n1 Max\n2 Min\n3 Sum\n4 XOR\n5 OR\n6 AND");
        System.out.println("Choose the ones you want to perform , once done choosing type -1");


        long vals=0;  // stores the operations that we perform
        int cnt=0;    // determines the number of operations required to initialise space for segment trees
        while(!terminate) 
        {
          long n=sc.nextLong();
          if(n==-1){
            terminate=true;
            continue;
          }
          n--;
          
          if((vals&1<<n) >=1)continue;
          vals|=1<<n;
          cnt++;
        }

        int ctr=0;

      

        todo = new int[cnt];
     //   System.out.println(vals);

        for(int i=0;i<=60;i++) 
        {
            if((vals & (1l<<i))>=1) 
            {
              //  System.out.println((1l<<i)+" "+i);

                todo[ctr]=i;
                ctr++;
            }
        }

         create(elements, cnt, vals);

        boolean get=true;
      
        while(get) 
        {
             System.out.println("Press 1 to update elements and 2 to get results and 3 to terminate");
            int type=sc.nextInt();

            switch(type) 
            {
                case 1:
                {
                    System.out.println("Enter index to change");
                    int index=sc.nextInt();
                    System.out.println("Enter new value");
                    int diff=sc.nextInt();

                    update(index,diff);
                    break;
                }
                case 2:
                {
                    System.out.println("Enter start range");
                    int st=sc.nextInt();
                    System.out.println("Enter end range");
                    int end=sc.nextInt();
                  long ans[]= query(st, end);

                  for(int i=0;i<ans.length;i++) 
                   {
                        System.out.println(operations[todo[i]]+" : "+ans[i]);
                    }
                   break;

                }
                default:
                {
                    get=false;
                    break;
                }
            }
        }

    

        



    }

    public static long perform(int opr,long trees[][],long val1,long val2) 
    {
        switch(opr) 
        {
            case 0:
            {
                return Math.max(val1,val2);
            }
            case 1:
            {
                return Math.min(val1,val2);
            }
            case 2:
            {
                return val1+val2;
            }
            case 3:
            {
                return val1^val2;
            }
            case 4:
            {
                return val1 | val2;
            }
            case 5:
            {
                return val1&val2;
            }
            default:
            {
                return val1;
            }

        }
    }

    public static void initialise(String operations[]) 
    {
        operations[0]="Max";
        operations[1]="Min";
        operations[2]="Sum";
        operations[3]="XOR";
        operations[4]="OR";
        operations[5]="AND";
        operations[6]="SOCHENGE";


    }
  public static void create(int arr[],int cnt,long vals)
    {
        int n=arr.length;
        
        trees=new long[2*n][cnt];
         todo = new int[cnt];
        int ctr=0;

        for(int i=0;i<=60;i++)    //// compression of operations in a set bit array todo
        {
            if((vals & (1l<<i))>=1) 
            {
                todo[ctr]=i;
                ctr++;
            }
        }

         for(int i=0;i<n;i++)       // leaves at indices n to 2*n -1 ;;;
         {
            for(int j=0;j<trees[0].length;j++) 
            {
                trees[i+n][j]=arr[i];
            }
            
         }
         for(int i=n-1;i>=0;i--)      // inititalizing non-leaf nodes
         {
              for(int j=0;j<trees[0].length;j++) 
            {
                trees[i][j]=perform(todo[j],trees,trees[2*i][j],trees[2*i+1][j]);
            }
          //  trees[i] = new Node(trees[i<<1] , trees[i<<1|1]);
         }
    }


     public static void update(int idx, int value) 
    {  // set value at index idx
        int n=trees.length/2;
        idx+=n;

        for(int j=0;j<trees[0].length;j++)
        {
            trees[idx][j]=value;
        }
        for (; idx > 1; idx >>= 1)
        {
            for(int j=0;j<trees[0].length;j++)
            {
                trees[idx>>1][j]=perform(todo[j],trees,trees[idx][j],trees[idx+1][j]);;
            }
        }
    }


 public static long[] query(int st,int end) 
    {
        long result[] = ret();
        int n=trees.length/2;
       // System.out.print(Arrays.toString(result)+" "+st+" "+end+"   ");

        for(st+=n,end+=n;st<end;st>>=1,end>>=1) 
        {
            if((st&1)>0) 
            {
               // result.merge(result,trees[st++]);
                for(int i=0;i<trees[0].length;i++)
                {
                    result[i]=perform(todo[i],trees,result[i],trees[st][i]);
                }
                st++;
            }
            if((end &1)>0) 
            {
                for(int i=0;i<trees[0].length;i++)
                {
                    result[i]=perform(todo[i],trees,result[i],trees[end-1][i]);
                }
                end--;
            }
          //  System.out.print(Arrays.toString(result)+" "+st+" "+end+"   ");
        }

        return result;
    }


 public static long retswitch(int opr) 
 {
    switch(opr) 
        {
            case 0:
            {
                return Long.MIN_VALUE;
            }
            case 1:
            {
                return Long.MAX_VALUE;
            } 
            case 5:
            {
                return Long.MAX_VALUE;
            }
            default:
            {
                return 0;
            }

        }
 }

 public static long[] ret() 
 {
    long arr[] = new long[todo.length];

    for(int i=0;i<arr.length;i++) 
    {
        arr[i]=retswitch(todo[i]);
    }

    return arr;
}
}
