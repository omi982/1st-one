package aws;

import java.util.*;
import java.util.stream.Collectors;

//class Comp implements Comparator<Integer>
//{
//	public int compare(Integer I1,Integer I2)
//	{
//		return (I1<I2)?-1:(I1>I2)?1:0;
//	}
//}

public class t
{
	public static void main(String args[])
	{
		ArrayList<Integer> l =new ArrayList<Integer>();
		l.add(20);
		l.add(10);
		l.add(25);
		l.add(5);
		l.add(30);
		l.add(0);
		System.out.println(l);
		Comparator<Integer> c = (I1,I2)->(I1<I2)?-1:(I1>I2)?1:0;
		Collections.sort(l,c);		
		System.out.println(l);
		l.stream().forEach(System.out::println); //new concept - combination of streams and method reference concept
		List<Integer> l2 = l.stream().filter(i->i%2==0).collect(Collectors.toList());
		System.out.println(l2);
	}
}
