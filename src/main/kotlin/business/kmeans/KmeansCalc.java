package business.kmeans;/*
 * KmeansCalc
 * 实现K-均值聚类算法
 * 默认聚类中心为3个，
 * 初始化数据对象100个
 * 收敛条件为中心点不再变化
 */
import java.lang.Math;
import java.util.Random;
import java.util.Vector;
 
public class KmeansCalc {
	static int k=3;  //聚类中心个数
	int i=0;
    int lable = 0;  //簇标签
    static int flag = 0;
    static StringBuilder data = new StringBuilder();
    static Vector<Tuple> tuples = new Vector<Tuple>();  //元组坐标集
	static Tuple means[]= new Tuple[k];  //中心点集合
	static Vector<Tuple> clusters[]=new Vector[k];  //簇集合
	Tuple meansLast[]= new Tuple[k];
	Tuple meanFrist[] = new Tuple[k];
	 
	public KmeansCalc(){
		for(int i=0;i<100;i++){
			tuples.addElement(new Tuple(0,0));
		}
		for(i=0;i<k;i++){
			means[i] = new Tuple(0,0);
			meanFrist[i] = new Tuple(0,0);
			clusters[i] = new Vector<Tuple>();
			clusters[i].addElement(means[i]);
		}
		
	}
	
	//REset Data,bt1Action
	public void ReSet(){
		data.delete(0, data.length());
		tuples.clear();
		for(i=0;i<k;i++){
			means[i] = new Tuple(0,0);
			clusters[i] = new Vector<Tuple>();
			clusters[i].clear();
			clusters[i].addElement(means[i]);
		}
		flag=0;
	}
	
	//计算欧氏距离
	public double getDistXY(Tuple t1,Tuple t2){
		
		return Math.sqrt(Math.pow((t1.x-t2.x)*(t1.x-t2.x), 2)+Math.pow((t1.y-t2.y)*(t1.y-t2.y),2));
	}
	
	//根据质心，决定当前元组属于哪个簇
	public int clusterOfTuple(Tuple means[],Tuple tuple){
		double dist=getDistXY(means[0],tuple);
		double tmp;
		int label=0;
		for(int i=1;i<k;i++){
			tmp=getDistXY(means[i],tuple);
			if(tmp<dist) {dist=tmp;label=i;}
		}
		return label;
	}
	
	//获得当前簇的质心
	public Tuple getMeans(Vector<Tuple> cluster){
		float meansX=0,meansY=0;
		Tuple t = new Tuple();
		for (int i=0;i<cluster.size();i++)
		{
			meansX+=cluster.get(i).getX();
			meansY+=cluster.get(i).getY();
		}
		t.setX(meansX/cluster.size());
		t.setY(meansY/cluster.size());
		return t;
	}
	
	//bt4 Action
	public static String getData(){
		return data.toString();
	}
	
	//init()，bt1Action
	public void KMeansInit(){
		Random ran = new Random();
		tuples.addElement(new Tuple(ran.nextInt(19),ran.nextInt(17)));
		data.append("本次数据集\n（仅作为查看复制使用，在此处修改值并不会影响程序）：\n("+String.valueOf(tuples.get(0).getX())+","+
						String.valueOf(tuples.get(0).getY())+")\n");
		for(int i=1;i<100;i++){
			Tuple tem = new Tuple(ran.nextInt(19),ran.nextInt(17));
				if(!tuples.contains(tem)){
					tuples.addElement(tem);
					data.append("("+String.valueOf(tem.getX())+","+
									String.valueOf(tem.getY())+")\n");	
				}
				else{
					i--;
				}
		}
		meanFrist[0] = tuples.get(ran.nextInt(100));
		for(i=1;i<k;i++){
			meanFrist[i] = tuples.get(ran.nextInt(100));
			for(int j=0;j<i;j++){
				if(meanFrist[i]==meanFrist[i-1]){
					i--;
				}
			}
		}
		for(i=0;i<k;i++){
			meansLast[i]= new Tuple();
			means[i].setX(meanFrist[i].getX());
			meansLast[i].x=means[i].x;
			means[i].y=meanFrist[i].getY(); 
			meansLast[i].y=means[i].y;
			
		}
		//根据默认的质心给簇赋值
		for(i=0;i!=tuples.size();++i){
			
			lable=clusterOfTuple(means,tuples.get(i));
			clusters[lable].addElement(tuples.get(i));
		}
		for(lable=0;lable<k;lable++){
			System.out.print("第"+(lable+1)+"个簇：\n");
			Vector<Tuple> t=clusters[lable];
			
			for (i=0;i<t.size();i++)
			{
				System.out.print("("+t.get(i).getX()+","+t.get(i).getY()+")"+"  ");
			}	
			System.out.print("\n");
		}
	}
	
	//Later handle,bt2&bt3 Action
	public void KMeansHandle(){
			
			for (i=0;i<k;i++)                //赋新值同时与上一次比较 
			{
				means[i]=getMeans(clusters[i]);
				if(means[i].x==meansLast[i].x&&means[i].y==meansLast[i].y)flag++;
				else { meansLast[i].x=means[i].x;  meansLast[i].y=means[i].y;}
			}
			if(flag==k)return;
			flag=0;
			for (i=0;i<k;i++) //清空每个簇
			{
				clusters[i].clear();
			}
			//根据新的质心获得新的簇
			for(i=0;i!=tuples.size();++i){
				lable=clusterOfTuple(means,tuples.get(i));
				clusters[lable].add(tuples.get(i));
			}
			for(lable=0;lable<k;lable++){
				System.out.print("第"+(lable+1)+"个簇：\n");
				System.out.print("中心点:("+means[lable].x+","+means[lable].y+")  \n");
				System.out.print("元组成员：\n");
				Vector<Tuple> t=clusters[lable];
				for (i=0;i<t.size();i++)
				{
					System.out.print("("+t.get(i).getX()+","+t.get(i).getY()+")"+"  ");
				}
				System.out.print("\n");
			}
			System.out.print("\n");
			try{
				Thread.sleep(1000);
				}catch(Exception e){
					System.out.print("Thread.sleep Exception in Kmeans.java\n");
					System.exit(0);
				}
		}
 
}
 
