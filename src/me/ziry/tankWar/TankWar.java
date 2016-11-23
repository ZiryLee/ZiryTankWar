package me.ziry.tankWar;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 《练习作品-坦克大战-1.0》
 * @author Ziry
 * 时间 ：2012.10.21
 */

public class TankWar extends Frame {
	
	/**
	 * 这是个坦克大战主窗口
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 整个游戏窗口宽度
	 */
	public static final int GAME_WIDTH = 800;
	
	/**
	 * 整个游戏窗口高度
	 */
	public static final int GAME_HEIGHT = 600;
	
	//两座墙，敌军和所有子弹无法穿越
	Wall w1 = new Wall(180, 100);
	Wall w2 = new Wall(590, 300);
	
	//血块、我的坦克碰撞此血块加血
	AddPh addPh = new AddPh(this);
	
	//我的坦克，可以穿墙
	Tank myTank = new Tank(700, 500, true, this);
		
	//装载坦克、爆炸、子弹
	List<Tank> tanks = new ArrayList<Tank>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Missile> missiles = new ArrayList<Missile>();
	
	//用于双缓冲的后台图像
	Image offScreenImage = null;
	
	/**
	 * 构造游戏窗口
	 * 1.增加5辆坦克
	 * 2.设置游戏窗口大小
	 * 3.设置背景颜色
	 * 4.增加坦克按键监听
	 * 5.屏幕关闭处理
	 * 6.显示主窗口
	 * 7.重画线程
	 * 8.标题
	 */
	public TankWar() {
			
		myTank.addTank(5);								//增加5辆坦克
		this.setTitle("《Ziry-练习作品-坦克大战-1.0》");	//标题
		this.setSize(GAME_WIDTH, GAME_HEIGHT);			//设置游戏窗口大小	
		this.setResizable(false);						//不可改变窗体大小
		this.setBackground(Color.BLACK);				//设置背景颜色
//		getContentPane().setBackground(Color.BLACK);
		this.addKeyListener(new DirectionKey());		//增加坦克按键监听
		this.addWindowListener(new WindowAdapter() {	//屏幕关闭处理

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setVisible(true);							//显示主窗口
		new Thread(new repaintThread()).start();		//重画线程
		
	}
	

	//main方法
	public static void main(String[] args) {
		new TankWar();
	}
	
	
	/**
	 * 绘制窗口
	 */
	public void paint(Graphics g){
		
		super.paint(g);	
		
		//得到颜色以便还原现场
		Color c = g.getColor();
		g.setColor(Color.WHITE);
		
		//画出提示
		g.drawString("Missile count :" + missiles.size(), 10, 50);
		g.drawString("Explode count :" + explodes.size(), 10, 70);
		g.drawString("Tank    count :" + tanks.size(), 10, 90);
		g.drawString("Tank       ph :" + myTank.getPh()+"%", 10, 110);
		g.drawString("[F1]:超级炮弹、[F2]:原地复活、[F3]:增加敌军、[空格]：开火", 400, 590);
		g.setColor(c);	//还原颜色
		
		//画两座墙
		w1.drawWall(g);
		w2.drawWall(g);
		
		//画我的坦克
		myTank.drawTank(g);
		
		//我的坦克是否碰到血块
		myTank.hitPh(addPh);
		
		//画出血块
		addPh.drawAddPh(g);
					
		//循环话子弹
		for (int i = 0; i < missiles.size(); i++) {			
			
			Missile m = missiles.get(i);	//得到子弹
			m.drawMissile(g);				//画子弹
			m.hitTanks(tanks);  			//子弹是否碰坦克集合（敌军）
			m.hitTank(myTank);				//子弹是否碰到我的坦克
			m.hitWall(w1);					//子弹是否碰墙1
			m.hitWall(w2);					//子弹是否碰墙2
			
		}	
		
		//循环画爆炸
		for (int i = 0; i < explodes.size(); i++) {
			
			Explode e = explodes.get(i);
			e.drawExplode(g);
			
		}
		
		//循环画坦克
		for (int i = 0; i < tanks.size(); i++) {
			
			Tank t = tanks.get(i);		//得到坦克
			t.drawTank(g);				//画出坦克
			t.hitTanks(tanks);			//坦克碰坦克（严禁穿越）
			t.hitTank(myTank);			//坦克碰我的坦克（严禁穿越）
			t.hitWall(w1);				//坦克碰墙1
			t.hitWall(w2);				//坦克碰墙2
		}
		
	}

	
	/**
	 * 双缓冲技术处理闪屏
	 */
	public void update(Graphics g) {
		
		if( offScreenImage == null ) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreenImage = offScreenImage.getGraphics();	
		Color oldC = gOffScreenImage.getColor();
		gOffScreenImage.setColor(Color.BLACK);
		gOffScreenImage.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreenImage.setColor(oldC);
		paint(gOffScreenImage);
		g.drawImage(offScreenImage, 0, 0, null);

	}
	
	
	/**
	 * 键盘监听类
	 */
	class DirectionKey extends KeyAdapter {
		
		//当键盘被弹起
		public void keyReleased(KeyEvent e) {
			//把事件传给坦克，让它自己实现
			myTank.keyReleased(e);
		}
		
		//当键盘按下
		public void keyPressed(KeyEvent e) {
			//把事件传给坦克，让它自己实现
			myTank.keyPressed(e);
		}
		
	}

	
	/**
	 * 重画线程类
	 */
	class repaintThread implements Runnable {

		public void run() {
			
			while( true ) {
				
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		
		
	}
	

}


