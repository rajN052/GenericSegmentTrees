import java.util.*;

class GenericIterativeSegmentTrees
{
     static Node trees[];
    public static void main(String args[])
    {
        Scanner sc = new Scanner(System.in);

        int n=sc.nextInt();

        int arr[] = new int[n];

        for(int i=0;i<n;i++) 
        {
            arr[i]=sc.nextInt();
        }

        create (arr);

       System.out.print(query(0,3).val+" ");
    }


    public static void create(int arr[])
    {
        int n=arr.length;
        
        trees=new Node[2*n];

         for(int i=0;i<n;i++)       // leaves at indices n to 2*n -1 ;;;
         {
            trees[i+n]=new Node(arr[i],i);
         }
         for(int i=n-1;i>=0;i--)      // inititalizing non-leaf nodes
         {
            trees[i] = new Node(trees[i<<1] , trees[i<<1|1]);
         }
    }

    public static void modify(int idx, int value) 
    {  // set value at index idx
        int n=trees.length/2;
        idx+=n;

        trees[idx].modify(value);

        for (; idx > 1; idx >>= 1)
        {
             trees[idx>>1].merge(trees[idx] , trees[idx^1]);   // traversing bottom up with using bit operations instead of *2 to make it faster
        }
    }

    public static Node query(int st,int end) 
    {
        Node result = new Node();
        int n=trees.length/2;

        for(st+=n,end+=n;st<end;st>>=1,end>>=1) 
        {
            if((st&1)>0) 
            {
                result.merge(result,trees[st++]);
            }
            if((end &1)>0) 
            {
                result.merge(result,trees[--end]);
            }
        }

        return result;
    }
}





class Node 
{
    int val;
    int idx;

    Node()
    {
        this.val=0;        // Change Default Value as and when required as per operation
        this.idx=-1;
    }

    Node(int val,int idx)
    {
        this.val=val;
        this.idx=idx;
    }

    Node(Node childOne,Node childTwo) 
    {
        if(childOne==null)childOne=new Node();   // counters null pointer exceptions
        if(childTwo==null)childTwo=new Node();
        merge(childOne,childTwo);   // so no preinitialisation of nodes is required
    }

    void modify(int val) 
    {
        this.val=val;
    }

    void merge(Node childOne, Node childTwo) 
    {
        this.val= Math.max(0,childOne.val + childTwo.val);  // do add,AND,multiply,XOR,etc, ust change this function
       
        if(childOne.val>childTwo.val)this.idx=childOne.idx;
        else this.idx=childTwo.idx;
    }
}

